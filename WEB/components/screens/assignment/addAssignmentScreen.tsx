import React, { useState } from 'react';
import { View, Text, TextInput, StyleSheet, TouchableOpacity, ImageBackground, Modal, ScrollView, Image } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { vi } from 'date-fns/locale';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as ImagePicker from "expo-image-picker";
import { uploadFileToS3 } from '../client/s3Client';
import * as DocumentPicker from 'expo-document-picker';
import { Audio } from 'expo-av';
import AudioPlayer from '../audio/audioPlayer';
import { AntDesign, MaterialIcons } from '@expo/vector-icons';

type AnswerOption = {
    noiDung: string;
    ketQua: boolean;
};

type Assignment = {
    questionName: string;
    correctAnswer: string;
    loiGiai: string;
    answers: { [key in `answer${'A' | 'B' | 'C' | 'D'}`]: AnswerOption };
    imageUri?: string;
    linkAnh?: string;
    audioUri?: string | null;
    linkAmThanh?: string;
};

const AddAssignmentScreen = ({ navigation, route }: { navigation: any; route: any }) => {
    const { idLopHoc, sessionId, tenLopHoc, role, } = route.params;
    const [assignmentName, setAssignmentName] = useState('');
    const [assignments, setAssignments] = useState<Assignment[]>([
        {
            questionName: '',
            correctAnswer: 'A',
            loiGiai: '',
            answers: {
                answerA: { noiDung: '', ketQua: true },
                answerB: { noiDung: '', ketQua: false },
                answerC: { noiDung: '', ketQua: false },
                answerD: { noiDung: '', ketQua: false },
            },
        },
    ]);
    const [startDate, setStartDate] = useState<Date | null>(new Date());
    const [endDate, setEndDate] = useState<Date | null>(new Date());
    const [isStartDatePickerOpen, setIsStartDatePickerOpen] = useState(false);
    const [isEndDatePickerOpen, setIsEndDatePickerOpen] = useState(false);
    const [messageModalVisible, setMessageModalVisible] = useState(false);
    const [messageText, setMessageText] = useState('');
    const [audioName, setAudioName] = useState<string | null>(null);

    const handleAddNewQuestion = () => {
        setAssignments([
            ...assignments,
            {
                questionName: '',
                correctAnswer: 'A',
                loiGiai: '',
                answers: {
                    answerA: { noiDung: '', ketQua: true },
                    answerB: { noiDung: '', ketQua: false },
                    answerC: { noiDung: '', ketQua: false },
                    answerD: { noiDung: '', ketQua: false },
                },
            },
        ]);
    };

    const validateDates = () => {
        if (startDate && endDate && startDate > endDate) {
            setMessageText('Ngày bắt đầu không được lớn hơn ngày kết thúc.');
            setMessageModalVisible(true);
            return false;
        }
        return true;
    };

    const validateInputs = () => {
        if (!assignmentName.trim()) {
            setMessageText('Tên bài tập không được để trống');
            setMessageModalVisible(true);
            return false;
        }
        for (const question of assignments) {
            if (!question.questionName.trim()) {
                setMessageText('Nội dung câu hỏi không được để trống');
                setMessageModalVisible(true);
                return false;
            }
            for (const key in question.answers) {
                if (!question.answers[key as keyof Assignment['answers']].noiDung.trim()) {
                    setMessageText('Tất cả các đáp án không được để trống');
                    setMessageModalVisible(true);
                    return false;
                }
            }
        }
        return true;
    };

    const formatDate = (date: Date) => date.toISOString();

    const handleRemoveLastQuestion = () => {
        if (assignments.length > 1) {
            setAssignments(assignments.slice(0, -1));
        }
    };

    const handleInputChange = (index: number, field: string, value: string) => {
        const newAssignments = [...assignments];
        if (field.startsWith('answer')) {
            const option = field as keyof Assignment['answers'];
            newAssignments[index].answers[option].noiDung = value;
        } else {
            newAssignments[index][field as keyof Assignment] = value as any;
        }
        setAssignments(newAssignments);
    };

    const handleCorrectAnswerChange = (index: number, correctAnswer: string) => {
        const newAssignments = [...assignments];
        newAssignments[index].correctAnswer = correctAnswer;
        Object.keys(newAssignments[index].answers).forEach((key) => {
            newAssignments[index].answers[key as keyof Assignment['answers']].ketQua = key === `answer${correctAnswer}`;
        });
        setAssignments(newAssignments);
    };

    const pickImage = async (index: number) => {
        const result = await ImagePicker.launchImageLibraryAsync({
            mediaTypes: ImagePicker.MediaTypeOptions.Images,
            quality: 1,
        });

        if (!result.canceled) {
            const newAssignments = [...assignments];
            newAssignments[index].imageUri = result.assets[0].uri;
            setAssignments(newAssignments);
        }
    };
    const handleRemoveImage = (index: number) => {
        const newAssignments = [...assignments];
        newAssignments[index].imageUri = undefined;
        setAssignments(newAssignments);
    };
    const pickAudioFile = async (index: number) => {
        try {
            const result = await DocumentPicker.getDocumentAsync({
                type: 'audio/*',
                copyToCacheDirectory: true,
            });

            if (!result.canceled && result.assets && result.assets.length > 0) {
                const fileUri = result.assets[0].uri;
                const fileName = result.assets[0].name;
                setAudioName(fileName);
                const newAssignments = [...assignments];
                newAssignments[index].audioUri = fileUri;
                setAssignments(newAssignments);
            } else {
                console.log('User canceled the picker.');
            }
        } catch (error) {
            console.error('Error picking audio file:', error);
        }
    };
    const handleClearAudio = (index: number) => {
        const newAssignments = [...assignments];
        newAssignments[index].audioUri = null;
        setAssignments(newAssignments);
        setAudioName(null)
    };
    const handleSubmitAssignments = async () => {
        if (!validateDates() || !validateInputs()) return;

        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                setMessageText('Lỗi: Không tìm thấy token xác thực. Vui lòng đăng nhập lại.');
                setMessageModalVisible(true);
                return;
            }

            const assignmentData = {
                tenBaiTap: assignmentName,
                ngayBD: startDate ? formatDate(startDate) : null,
                ngayKT: endDate ? formatDate(endDate) : null,
            };

            const response = await http.post(`baitap/createBaiTap/${sessionId}`, assignmentData, {
                headers: { Authorization: `Bearer ${token}` },
            });

            const { idBaiTap } = response.data;

            await Promise.all(
                assignments.map(async (question) => {
                    let imageUrl: string | null = null;
                    let audioUrl: string | null = null;
                    if (question.imageUri) {
                        const fileBlob = await fetch(question.imageUri).then(res => res.blob());
                        imageUrl = await uploadFileToS3(fileBlob, `imgCauHoi/question-image-assignmentId-${idBaiTap}-${Date.now()}`);
                    }
                    if (question.audioUri) {
                        const fileBlob = await fetch(question.audioUri).then(res => res.blob());
                        audioUrl = await uploadFileToS3(fileBlob, `audioCauHoi/question-audio-assignmentId-${idBaiTap}-${Date.now()}`);
                    }

                    const questionData = {
                        noiDung: question.questionName,
                        loiGiai: question.loiGiai,
                        linkAnh: imageUrl || '',
                        linkAmThanh: audioUrl || '',
                    };
                    const questionResponse = await http.post(`baitap/createCauHoi/${idBaiTap}`, questionData, {
                        headers: { Authorization: `Bearer ${token}` },
                    });
                    const { idCauHoi } = questionResponse.data;
                    await Promise.all(
                        Object.keys(question.answers).map((key) => {
                            const answer = question.answers[key as keyof Assignment['answers']];
                            const answerData = {
                                noiDung: answer.noiDung,
                                ketQua: answer.ketQua,
                            };

                            return http.post(`cauTraLoi/create/${idCauHoi}`, answerData, {
                                headers: { Authorization: `Bearer ${token}` },
                            });
                        })
                    );
                })
            );

            setMessageText('Thêm bài tập, câu hỏi và câu trả lời thành công.');
            setMessageModalVisible(true);
            setTimeout(() => {
                navigation.navigate('TeacherClassDetailScreen', { idLopHoc, sessionId, tenLopHoc, role });
            }, 1000);
        } catch (error) {
            console.error('Error while submitting assignments:', error);
            setMessageText('Lỗi: Không thể thêm bài tập. Vui lòng thử lại.');
            setMessageModalVisible(true);
        }
    };


    const handleStartDateChange = (date: Date) => {
        setStartDate(date);
        setIsStartDatePickerOpen(false);
    };

    const handleEndDateChange = (date: Date) => {
        setEndDate(date);
        setIsEndDatePickerOpen(false);
    };

    const handleCloseModal = () => {
        setMessageModalVisible(false);
    };

    return (
        <ImageBackground source={require('../../../image/bglogin.png')} style={styles.background}>
            <View style={styles.overlayContainer}>
                <TouchableOpacity style={styles.backButton} onPress={() => navigation.goBack()}>
                    <Text style={styles.backButtonText}>Quay về</Text>
                </TouchableOpacity>
                <Text style={styles.title}>Thêm Bài Tập</Text>

                <Text style={styles.label}>Tên Bài Tập</Text>
                <View style={{ width: '100%' }}>
                    <TextInput
                        style={styles.input}
                        placeholder="Tên Bài Tập"
                        value={assignmentName}
                        onChangeText={(text) => setAssignmentName(text)}
                    />
                </View>

                <View style={styles.dateContainer}>
                    <View style={styles.dateColumn}>
                        <Text style={styles.label}>Ngày Bắt Đầu</Text>
                        <TouchableOpacity style={styles.dateButton} onPress={() => setIsStartDatePickerOpen(true)}>
                            <Text style={styles.dateText}>{startDate?.toLocaleDateString('vi-VN')}  -  {startDate?.toLocaleString('vi-VN', { hour: '2-digit', minute: '2-digit' })}</Text>
                        </TouchableOpacity>
                    </View>
                    <View style={styles.dateColumn}>
                        <Text style={styles.label}>Ngày Kết Thúc</Text>
                        <TouchableOpacity style={styles.dateButton} onPress={() => setIsEndDatePickerOpen(true)}>
                            <Text style={styles.dateText}>{endDate?.toLocaleDateString('vi-VN')}  -  {endDate?.toLocaleString('vi-VN', { hour: '2-digit', minute: '2-digit' })}</Text>
                        </TouchableOpacity>
                    </View>
                </View>

                <ScrollView>
                    {assignments.map((question, index) => (
                        <View key={index} style={styles.assignmentContainer}>
                            <Text style={styles.questionLabel}>Nội dung Câu hỏi {index + 1}</Text>
                            <TextInput
                                style={styles.input}
                                placeholder="Nội dung câu hỏi"
                                value={question.questionName}
                                onChangeText={(text) => handleInputChange(index, 'questionName', text)}
                            />
                            <View style={{ flexDirection: 'row' }}>
                                <TouchableOpacity
                                    style={styles.imagePickerButton}
                                    onPress={() => pickImage(index)}
                                >
                                    <Text style={styles.imagePickerButtonText}>Chọn ảnh bài tập</Text>
                                </TouchableOpacity>
                                <TouchableOpacity
                                    style={styles.audioPickerButton}
                                    onPress={() => pickAudioFile(index)}
                                >
                                    <Text style={styles.imagePickerButtonText}>Chọn âm thanh</Text>
                                </TouchableOpacity>
                            </View>

                            {question.imageUri && (
                                <View style={styles.imageContainer}>
                                    <Image
                                        source={{ uri: question.imageUri }}
                                        style={styles.selectedImage}
                                    />
                                    <TouchableOpacity
                                        style={styles.removeImageButton}
                                        onPress={() => handleRemoveImage(index)}
                                    >
                                        <MaterialIcons name="close" size={24} color="red" />
                                    </TouchableOpacity>
                                </View>
                            )}
                            {audioName ? (
                                <Text style={styles.audioName}>{audioName}</Text>
                            ) : (
                                <Text style={styles.audioName}>Chưa chọn tệp âm thanh</Text>
                            )}

                            {question.audioUri && (
                                <View style={styles.audioContainer}>
                                    <AudioPlayer audioUri={question.audioUri} />
                                    <TouchableOpacity
                                        style={styles.deleteAudioIcon}
                                        onPress={() => handleClearAudio(index)}
                                    >
                                        <MaterialIcons name="close" size={24} color="red" />
                                        </TouchableOpacity>
                                </View>
                            )}
                            {['A', 'B', 'C', 'D'].map((option) => (
                                <View style={{ flexDirection: 'row', alignItems: 'center', marginVertical: 5, marginHorizontal: 10 }} key={option}>
                                    <Text style={styles.answerLabel}>{option}</Text>
                                    <TextInput
                                        style={[styles.input, { flex: 1 }]}
                                        placeholder={`Đáp án ${option}`}
                                        value={question.answers[`answer${option}` as keyof Assignment['answers']].noiDung}
                                        onChangeText={(text) => handleInputChange(index, `answer${option}`, text)}
                                    />
                                </View>
                            ))}
                            <View style={styles.pickerContainer}>
                                <Text style={styles.pickerLabel}>Đáp án đúng</Text>
                                <Picker
                                    selectedValue={question.correctAnswer}
                                    style={styles.picker}
                                    onValueChange={(value) => handleCorrectAnswerChange(index, value)}
                                >
                                    {['A', 'B', 'C', 'D'].map((option) => (
                                        <Picker.Item label={`Đáp án ${option}`} value={option} key={option} />
                                    ))}
                                </Picker>
                            </View>
                            <TextInput
                                style={styles.input}
                                placeholder="Lời giải"
                                value={question.loiGiai}
                                onChangeText={(text) => handleInputChange(index, 'loiGiai', text)}
                            />
                        </View>
                    ))}
                </ScrollView>

                <TouchableOpacity style={styles.addButton} onPress={handleAddNewQuestion}>
                    <Icon name="plus" size={10} color="#fff" />
                </TouchableOpacity>
                <TouchableOpacity style={styles.removeButton} onPress={handleRemoveLastQuestion}>
                    <Icon name="minus" size={10} color="#fff" />
                </TouchableOpacity>
                <TouchableOpacity style={styles.submitButton} onPress={handleSubmitAssignments}>
                    <Text style={styles.submitButtonText}>Gửi Bài Tập</Text>
                </TouchableOpacity>

                <Modal
                    visible={messageModalVisible}
                    transparent
                    animationType="fade"
                    onRequestClose={handleCloseModal}
                >
                    <View style={styles.modalOverlay}>
                        <View style={styles.modalContainer}>
                            <Text style={styles.modalMessage}>{messageText}</Text>
                            <TouchableOpacity style={styles.modalButton} onPress={handleCloseModal}>
                                <Text style={styles.modalButtonText}>Đóng</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </Modal>

                <Modal
                    visible={isStartDatePickerOpen}
                    transparent
                    animationType="fade"
                    onRequestClose={() => setIsStartDatePickerOpen(false)}
                >
                    <View style={styles.modalOverlay}>
                        <View style={styles.datePickerWrapper}>
                            <DatePicker
                                selected={startDate}
                                onChange={(date) => handleStartDateChange(date as Date)}
                                inline
                                locale={vi}
                                showTimeSelect 
                                timeFormat="HH:mm" 
                                timeIntervals={15} 
                                timeCaption="Thời gian" 
                                dateFormat="dd/MM/yyyy HH:mm"
                            />
                            <TouchableOpacity style={styles.closeButton} onPress={() => setIsStartDatePickerOpen(false)}>
                                <Text style={styles.closeButtonText}>Đóng</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </Modal>

                <Modal
                    visible={isEndDatePickerOpen}
                    transparent
                    animationType="fade"
                    onRequestClose={() => setIsEndDatePickerOpen(false)}
                >
                    <View style={styles.modalOverlay}>
                        <View style={styles.datePickerWrapper}>
                            <DatePicker
                                selected={endDate}
                                onChange={(date) => handleEndDateChange(date as Date)}
                                inline
                                locale={vi}
                                showTimeSelect // Thêm hiển thị chọn giờ
                                timeFormat="HH:mm" // Định dạng giờ
                                timeIntervals={15} // Khoảng cách giữa các giờ là 15 phút
                                timeCaption="Thời gian" // Tiêu đề cho chọn giờ
                                dateFormat="dd/MM/yyyy HH:mm" // Định dạng ngày giờ
                            />
                            <TouchableOpacity style={styles.closeButton} onPress={() => setIsEndDatePickerOpen(false)}>
                                <Text style={styles.closeButtonText}>Đóng</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </Modal>
            </View>
        </ImageBackground>
    );
};

const styles = StyleSheet.create({
    background: {
        flex: 1,
        paddingHorizontal: 400,
        height: 990,
    },
    overlayContainer: {
        flex: 1,
        padding: 20,
        backgroundColor: 'rgba(255, 255, 255, 0.9)',
        borderRadius: 15,
        marginHorizontal: 20,
    },
    backButton: {
        padding: 10,
        backgroundColor: '#00405d',
        borderRadius: 5,
        alignSelf: 'flex-start',
        marginBottom: 10,
    },
    questionLabel: {
        fontSize: 16,
        fontWeight: 'bold',
        marginBottom: 5,
        color: '#00405d',
    },
    backButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#ff6600',
        textAlign: 'center',
        marginBottom: 20,
    },
    dateContainer: {
        marginTop: 20,
        flexDirection: 'row',
        marginBottom: 20,
    },
    dateColumn: {
        flex: 1,
        alignItems: 'center',
        flexDirection: 'row'
    },
    label: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#00405d',
        marginBottom: 5,
    },
    dateButton: {
        width: 200,
        marginLeft: 20,
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderWidth: 1,
        borderRadius: 5,
        borderColor: '#ccc',
    },
    dateText: {
        color: 'black',
        fontWeight: 'bold',
        textAlign: 'center',
        fontSize: 16
    },
    assignmentContainer: {
        marginBottom: 20,
    },
    input: {
        height: 50,
        borderColor: '#ccc',
        borderWidth: 1,
        borderRadius: 5,
        paddingHorizontal: 10,
        marginVertical: 5,
    },
    answerContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        marginVertical: 5,
    },
    answerLabel: {
        fontWeight: 'bold',
        fontSize: 16,
        marginRight: 10,
    },
    pickerContainer: {
        marginVertical: 10,
    },
    pickerLabel: {
        fontSize: 16,
        fontWeight: 'bold',
        marginBottom: 10
    },
    picker: {
        height: 50,
        borderColor: '#ccc',
        borderWidth: 1,
    },
    addButton: {
        backgroundColor: '#28a745',
        borderRadius: 50,
        padding: 15,
        alignItems: 'center',
        justifyContent: 'center',
        position: 'absolute',
        bottom: 100,
        right: 20,
    },
    removeButton: {
        backgroundColor: '#dc3545',
        borderRadius: 50,
        padding: 15,
        alignItems: 'center',
        justifyContent: 'center',
        position: 'absolute',
        bottom: 100,
        right: 80,
    },
    submitButton: {
        backgroundColor: '#ff6600',
        borderRadius: 5,
        padding: 15,
        alignItems: 'center',
        marginTop: 20,
    },
    submitButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
    modalOverlay: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
    modalContainer: {
        width: 400,
        backgroundColor: '#fff',
        padding: 20,
        borderRadius: 10,
        alignItems: 'center',
        shadowColor: "#000",
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.25,
        shadowRadius: 3.84,
        elevation: 5,
    },
    modalMessage: {
        fontSize: 16,
        textAlign: 'center',
        marginBottom: 20,
    },
    modalButton: {
        backgroundColor: '#00405d',
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderRadius: 5,
        alignItems: 'center',
        justifyContent: 'center',
        width: '80%',
    },
    modalButtonText: {
        color: '#fff',
        fontWeight: 'bold',
        fontSize: 16,
    },
    closeButton: {
        marginTop: 20,
        padding: 10,
        backgroundColor: '#00405d',
        borderRadius: 5,
    },
    closeButtonText: {
        color: '#fff',
        fontWeight: 'bold',
        textAlign: 'center',
    },
    datePickerWrapper: {
        padding: 20,
        borderRadius: 10,
    },
    imagePickerButton: {
        backgroundColor: '#00bf63',
        borderRadius: 5,
        width: 150,
        padding: 10,
        alignItems: 'center',
        marginTop: 10,
    },
    audioPickerButton: {
        backgroundColor: '#00bf63',
        borderRadius: 5,
        width: 150,
        padding: 10,
        alignItems: 'center',
        marginTop: 10,
        marginLeft: 20
    },
    imagePickerButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
    imageContainer: {
        alignItems: 'center',
        marginBottom: 10,
        marginTop: 20,
    },
    selectedImage: {
        width: 800,
        height: 500,
        borderRadius: 10,
    },
    audioContainer: {
        marginVertical: 10,
        alignItems: 'center',
    },
    audioText: {
        fontSize: 16,
        color: '#333',
        marginBottom: 5,
    },
    playButton: {
        backgroundColor: '#00bf63',
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderRadius: 10,
    },
    playButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
    audioName: {
        fontSize: 14,
        color: '#333',
        marginTop: 5,
        textAlign: 'center',
    },
    removeImageButton: {
        position: 'absolute',
        top: 10,
        right: 10,
        borderRadius: 50,
        padding: 5,
        elevation: 3,
    },
    deleteAudioIcon: {
        position: 'absolute',
        top: -10,
        right: 20,
        zIndex: 1,
    },
});

export default AddAssignmentScreen;

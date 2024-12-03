import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, ScrollView, ImageBackground, ActivityIndicator, Modal, TouchableOpacity, TextInput, Image } from 'react-native';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import * as ImagePicker from 'expo-image-picker';
import { uploadFileToS3, deleteFileFromS3 } from '../client/s3Client';
import { Audio } from 'expo-av';
import AudioPlayer from '../audio/audioPlayer';
import * as DocumentPicker from 'expo-document-picker';
import { AntDesign, MaterialIcons } from '@expo/vector-icons';

interface Question {
    linkAnh: string | null;
    linkAmThanh: string | null;
    audioUri?: string;
    idCauHoi: number;
    noiDung: string;
    dapAn: Answer[] | null;
    loiGiai: string | null;
    imageUri?: string;
}

interface Answer {
    idCauTraLoi: number;
    noiDung: string;
    ketQua: boolean;
}

const AssignmentDetailScreen = ({ navigation, route }: { navigation: any, route: any }) => {
    const { assignmentId, sessionId } = route.params;
    const [questions, setQuestions] = useState<Question[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [selectedQuestion, setSelectedQuestion] = useState<Question | null>(null);
    const [modalVisible, setModalVisible] = useState(false);
    const [confirmModalVisible, setConfirmModalVisible] = useState(false);
    const [answers, setAnswers] = useState<Answer[]>([]);
    const [correctAnswerId, setCorrectAnswerId] = useState<number | null>(null);
    const [questionText, setQuestionText] = useState<string>("");
    const [explanationText, setExplanationText] = useState<string>("");
    const answerOptions = ["A", "B", "C", "D"];
    const [addQuestionModalVisible, setAddQuestionModalVisible] = useState(false);
    const [errorModalVisible, setErrorModalVisible] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [selectedImage, setSelectedImage] = useState<string | null>(null);
    const [audioName, setAudioName] = useState<string | null>(null);
    const [selectedAudio, setSelectedAudio] = useState<string | null>(null);
    const [originalImage, setOriginalImage] = useState<string | null>(null);
    const [originalAudio, setOriginalAudio] = useState<string | null>(null);

    useEffect(() => {
        fetchAssignmentDetails();
    }, []);
    useEffect(() => {
        if (addQuestionModalVisible) {
            setCorrectAnswerId(answers[0]?.idCauTraLoi); // Mặc định đáp án A là đúng
            setAnswers((prevAnswers) =>
                prevAnswers.map((answer, index) => ({
                    ...answer,
                    ketQua: index === 0, // Đáp án A mặc định đúng
                }))
            );
        }
    }, [addQuestionModalVisible]);

    const fetchAssignmentDetails = async () => {
        setIsLoading(true);
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            const response = await http.get(`baitap/getCauHoiTrue/${assignmentId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            const questionsData = response.data || [];
            const updatedQuestions = await Promise.all(
                questionsData.map(async (question: Question) => {
                    const answers = await fetchAnswersForQuestion(question.idCauHoi);
                    return { ...question, dapAn: answers, linkAnh: question.linkAnh || null }; // Gán linkAnh nếu có
                })
            );
            setQuestions(updatedQuestions);
        } catch (error) {
            console.error('Failed to fetch assignment details:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const fetchAnswersForQuestion = async (questionId: number) => {
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return [];
            }
            const response = await http.get(`baitap/getCauTraLoi/${questionId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            return Array.isArray(response.data) ? response.data : [];
        } catch (error) {
            console.error(`Failed to fetch answers for question ${questionId}:`, error);
            return [];
        }
    };

    const openEditModal = (question: Question) => {
        setSelectedQuestion(question);
        setQuestionText(question.noiDung || "");
        setExplanationText(question.loiGiai || "");
        const correctAnswer = question.dapAn?.find(answer => answer.ketQua);
        setCorrectAnswerId(correctAnswer?.idCauTraLoi || null);
        setAnswers(question.dapAn || []);
        setModalVisible(true);
        setOriginalImage(question.linkAnh);
        setOriginalAudio(question.linkAmThanh);
    };

    const openAddQuestionModal = () => {
        setQuestionText("");
        setExplanationText("");
        setAnswers([
            { idCauTraLoi: 1, noiDung: "", ketQua: false },
            { idCauTraLoi: 2, noiDung: "", ketQua: false },
            { idCauTraLoi: 3, noiDung: "", ketQua: false },
            { idCauTraLoi: 4, noiDung: "", ketQua: false },
        ]);
        setCorrectAnswerId(null);
        setAddQuestionModalVisible(true);
    };

    const confirmDeleteQuestion = (question: Question) => {
        setSelectedQuestion(question);
        setConfirmModalVisible(true);
    };
    const uploadImageToS3 = async (fileUri: string) => {
        try {
            const fileBlob = await fetch(fileUri).then(res => res.blob());
            const fileName = `imgCauHoi/question-image-assignmentId-${assignmentId}-${Date.now()}`;
            const uploadedUrl = await uploadFileToS3(fileBlob, fileName);
            return uploadedUrl;
        } catch (error) {
            console.error("Error uploading image to S3:", error);
            return null;
        }
    };
    const uploadAudioToS3 = async (fileUri: string) => {
        try {
            const fileBlob = await fetch(fileUri).then(res => res.blob());
            const fileName = `audioCauHoi/question-audio-assignmentId-${assignmentId}-${Date.now()}`;
            const uploadedUrl = await uploadFileToS3(fileBlob, fileName);
            return uploadedUrl;
        } catch (error) {
            console.error("Error uploading image to S3:", error);
            return null;
        }
    };
    const deleteQuestion = async () => {
        if (!selectedQuestion) return;
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            if (selectedQuestion.linkAnh) {
                const fileName = selectedQuestion.linkAnh.split('/').pop();
                if (fileName) {
                    await deleteFileFromS3(`imgCauHoi/${fileName}`);
                }
            }
            await http.get(`baitap/deleteCauHoi/${selectedQuestion.idCauHoi}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            setConfirmModalVisible(false);
            fetchAssignmentDetails();
        } catch (error) {
            console.error(`Failed to delete question ${selectedQuestion.idCauHoi}:`, error);
        }
    };
    const saveCorrectAnswer = async () => {
        if (!validateInputs()) return;
        if (!selectedQuestion || correctAnswerId === null) return;

        let imageUrl = null;
        let audioUrl = null;

        try {
            if (originalImage && (selectedImage || originalImage !== selectedQuestion.linkAnh)) {
                await removeOldImage(originalImage);
            }
            if (selectedImage) {
                imageUrl = await uploadImageToS3(selectedImage);
            } else {
                imageUrl = selectedQuestion.linkAnh || null;
            }
            if (originalAudio && (selectedAudio || originalAudio !== selectedQuestion.linkAmThanh)) {
                await removeOldAudio(originalAudio);
            }
            if (selectedAudio) {
                audioUrl = await uploadAudioToS3(selectedAudio);
            } else {
                audioUrl = selectedQuestion.linkAmThanh || null;
            }
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            await http.put(
                `baitap/updateCauHoi/${selectedQuestion.idCauHoi}/${assignmentId}`,
                {
                    noiDung: questionText,
                    loiGiai: explanationText,
                    linkAnh: imageUrl,
                    linkAmThanh: audioUrl,
                    trangThai: 1,
                },
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );
            await Promise.all(
                answers.map((answer) =>
                    http.put(
                        `cauTraLoi/create/${selectedQuestion.idCauHoi}/${answer.idCauTraLoi}`,
                        {
                            ...answer,
                            ketQua: answer.idCauTraLoi === correctAnswerId,
                        },
                        {
                            headers: { Authorization: `Bearer ${token}` },
                        }
                    )
                )
            );
            fetchAssignmentDetails();
        } catch (error) {
            console.error('Failed to update question, correct answer, or explanation:', error);
        } finally {
            setModalVisible(false);
            setSelectedImage(null);
            setSelectedAudio(null);
        }
    };

    const updateAnswerText = (idCauTraLoi: number, text: string) => {
        setAnswers(prevAnswers =>
            prevAnswers.map(answer =>
                answer.idCauTraLoi === idCauTraLoi ? { ...answer, noiDung: text } : answer
            )
        );
    };

    const saveNewQuestion = async () => {
        if (!validateInputs()) return;

        let imageUrl = null;
        let audioUrl = null;

        try {
            if (selectedImage) {
                imageUrl = await uploadImageToS3(selectedImage);
            }
            if (selectedAudio) {
                audioUrl = await uploadAudioToS3(selectedAudio);
            }
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }

            const response = await http.post(
                `/baitap/createCauHoi/${assignmentId}`,
                {
                    noiDung: questionText,
                    loiGiai: explanationText,
                    linkAnh: imageUrl,
                    linkAmThanh: audioUrl,
                    trangThai: 1,
                },
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );

            const newQuestionId = response.data.idCauHoi;
            await Promise.all(
                answers.map((answer, index) =>
                    http.post(
                        `cauTraLoi/create/${newQuestionId}`,
                        {
                            noiDung: answer.noiDung,
                            ketQua: answer.idCauTraLoi === correctAnswerId,
                        },
                        {
                            headers: { Authorization: `Bearer ${token}` },
                        }
                    )
                )
            );

            fetchAssignmentDetails();
        } catch (error) {
            console.error('Failed to save new question:', error);
        } finally {
            setAddQuestionModalVisible(false);
            setSelectedImage(null);
            setSelectedAudio(null);
        }
    };

    const validateInputs = () => {

        if (!questionText.trim()) {
            setErrorMessage('Nội dung câu hỏi không được để trống');
            setErrorModalVisible(true);
            return false;
        }
        if (!explanationText.trim()) {
            setErrorMessage('Lời giải không được để trống');
            setErrorModalVisible(true);
            return false;
        }
        for (const answer of answers) {
            if (!answer.noiDung.trim()) {
                setErrorMessage('Tất cả các đáp án không được để trống');
                setErrorModalVisible(true);
                return false;
            }
        }
        if (correctAnswerId === null) {
            setErrorMessage('Vui lòng chọn đáp án đúng');
            setErrorModalVisible(true);
            return false;
        }
        return true;
    };
    const pickImage = async () => {
        const result = await ImagePicker.launchImageLibraryAsync({
            mediaTypes: ImagePicker.MediaTypeOptions.Images,
            allowsEditing: true,
            base64: true,
            quality: 1,
        });

        if (!result.canceled) {
            const uri = result.assets[0].uri;
            setSelectedImage(uri);
        }
    };
    const handleRemoveImage = () => {
        if (selectedImage) {
            setSelectedImage(null);
        } else if (selectedQuestion?.linkAnh) {
            setSelectedQuestion({ ...selectedQuestion, linkAnh: null });
        }
    };
    const removeOldImage = async (oldImageLink: string | null) => {
        if (oldImageLink) {
            const oldFileName = oldImageLink.split('/').pop();
            if (oldFileName) {
                try {
                    await deleteFileFromS3(`imgCauHoi/${oldFileName}`);
                } catch (error) {
                    console.error('Error removing old image:', error);
                }
            }
        }
    };

    const pickAudioFile = async () => {
        try {
            const result = await DocumentPicker.getDocumentAsync({
                type: 'audio/*',
                copyToCacheDirectory: true,
            });

            if (!result.canceled) {
                const uri = result.assets[0].uri;
                setSelectedAudio(uri)
            }
        } catch (error) {
            console.error('Error picking audio file:', error);
        }
    }

    const handleClearAudio = () => {
        if (selectedAudio) {
            setSelectedAudio(null)
        }
        else if (selectedQuestion?.linkAmThanh) {
            setSelectedQuestion({ ...selectedQuestion, linkAmThanh: null });
            setAudioName(null)
        }
    };
    const removeOldAudio = async (oldAudioLink: string | null) => {
        if (oldAudioLink) {
            const oldFileName = oldAudioLink.split('/').pop();
            if (oldFileName) {
                try {
                    await deleteFileFromS3(`audioCauHoi/${oldFileName}`);
                } catch (error) {
                    console.error('Error removing old audio:', error);
                }
            }
        }
    };




    return (
        <ImageBackground source={require('../../../image/bglogin.png')} style={styles.background}>
            <View style={styles.overlayContainer}>
                <View style={styles.headerRow}>
                    <TouchableOpacity style={styles.backButton} onPress={() => navigation.goBack()}>
                        <Text style={styles.backButtonText}>Quay về</Text>
                    </TouchableOpacity>

                </View>

                <Text style={styles.title}>Chi tiết Bài Tập</Text>

                {isLoading ? (
                    <ActivityIndicator size="large" color="#00405d" />
                ) : (
                    <ScrollView style={styles.contentContainer}>
                        {questions.length > 0 ? (
                            questions.map((question, index) => (
                                <View key={question.idCauHoi} style={styles.questionContainer}>
                                    <View style={styles.questionHeader}>
                                        <Text style={styles.questionText}>
                                            {index + 1}. {question.noiDung}
                                        </Text>
                                        <View style={styles.iconContainer}>
                                            <TouchableOpacity onPress={() => openEditModal(question)}>
                                                <Icon name="pencil" size={20} color="#ff6600" style={styles.editIcon} />
                                            </TouchableOpacity>
                                            <TouchableOpacity onPress={() => confirmDeleteQuestion(question)}>
                                                <Icon name="delete" size={20} color="red" style={styles.deleteIcon} />
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View>
                                        {/* Hiển thị hình ảnh nếu có */}
                                        {question.linkAnh ? (
                                            <Image source={{ uri: question.linkAnh }} style={styles.questionImage} />
                                        ) : null}

                                        {/* Hiển thị audio nếu có */}
                                        {question.linkAmThanh ? (
                                            <View style={styles.audioContainer}>
                                                <AudioPlayer audioUri={question.linkAmThanh} />
                                            </View>
                                        ) : null}
                                    </View>
                                    <View style={styles.answerList}>
                                        {Array.isArray(question.dapAn) ? (
                                            question.dapAn.map((answer, idx) => (
                                                <View
                                                    key={`${question.idCauHoi}-${answer.idCauTraLoi}`}
                                                    style={styles.answerOption}
                                                >
                                                    <Text style={styles.optionLabel}>{answerOptions[idx]}.</Text>
                                                    <Text
                                                        style={[
                                                            styles.answerText,
                                                            answer.ketQua ? styles.correctAnswer : null,
                                                        ]}
                                                    >
                                                        {answer.noiDung}
                                                    </Text>
                                                    {answer.ketQua && (
                                                        <Text style={styles.correctIndicator}>✔️</Text>
                                                    )}
                                                </View>
                                            ))
                                        ) : (
                                            <Text>Loading answers...</Text>
                                        )}
                                    </View>
                                    {question.loiGiai && (
                                        <Text style={styles.explanationText}>
                                            <Text style={styles.explanationLabel}>Lời giải: </Text>
                                            {question.loiGiai}
                                        </Text>
                                    )}
                                </View>
                            ))
                        ) : (
                            <Text>Không có câu hỏi nào.</Text>
                        )}
                    </ScrollView>
                )}
                <View style={styles.addButtonContainer}>
                    <TouchableOpacity style={styles.addButton} onPress={openAddQuestionModal}>
                        <Icon name="plus" size={20} color="#fff" />
                    </TouchableOpacity>
                </View>

                {/* Modal xác nhận xóa */}
                <Modal visible={confirmModalVisible} transparent={true} animationType="slide">
                    <View style={styles.modalOverlay}>
                        <View style={styles.modalContainer2}>
                            <Text style={styles.modalText}>Bạn có chắc chắn muốn xóa câu hỏi này không?</Text>
                            <View style={styles.modalButtons2}>
                                <TouchableOpacity onPress={() => setConfirmModalVisible(false)} style={styles.modalButton2}>
                                    <Text style={styles.modalButtonText}>Hủy</Text>
                                </TouchableOpacity>
                                <TouchableOpacity onPress={deleteQuestion} style={styles.modalButton}>
                                    <Text style={styles.modalButtonText}>Đồng ý</Text>
                                </TouchableOpacity>
                            </View>
                        </View>
                    </View>
                </Modal>

                <Modal visible={modalVisible} transparent={true} animationType="slide">
                    <ScrollView>
                        <View style={styles.modalContainer}>
                            <View style={styles.modalContent}>
                                <Text style={styles.modalTitle}>Chỉnh sửa câu hỏi</Text>
                                <TextInput
                                    style={[styles.input, styles.questionInput]}
                                    value={questionText}
                                    onChangeText={setQuestionText}
                                    placeholder="Nhập nội dung câu hỏi"
                                />
                                <View>
                                    <View style={{ flexDirection: 'row' }}>
                                        <TouchableOpacity onPress={pickImage} style={styles.imagePickerButton}>
                                            <Text style={styles.imagePickerButtonText}>Chọn Ảnh</Text>
                                        </TouchableOpacity>
                                        <TouchableOpacity onPress={pickAudioFile} style={styles.imagePickerButton}>
                                            <Text style={styles.imagePickerButtonText}>Chọn âm thanh</Text>
                                        </TouchableOpacity>
                                    </View>
                                    <View style={styles.imageContainer}>
                                        {selectedImage ? (
                                            <View>
                                                <Image source={{ uri: selectedImage }} style={styles.questionImage} />
                                                <TouchableOpacity
                                                    style={styles.deleteImageIcon}
                                                    onPress={handleRemoveImage}
                                                >
                                                    <Icon name="close-circle" size={24} color="red" />
                                                </TouchableOpacity>
                                            </View>
                                        ) : (
                                            selectedQuestion?.linkAnh && (
                                                <View>
                                                    <Image source={{ uri: selectedQuestion.linkAnh }} style={styles.questionImage} />
                                                    <TouchableOpacity
                                                        style={styles.deleteImageIcon}
                                                        onPress={handleRemoveImage}
                                                    >
                                        <MaterialIcons name="close" size={24} color="red" />
                                        </TouchableOpacity>
                                                </View>
                                            )
                                        )}
                                        {
                                            selectedAudio ? (
                                                <View style={styles.audioContainer}>
                                                    <AudioPlayer audioUri={selectedAudio} />
                                                    <TouchableOpacity
                                                        style={styles.deleteAudioIcon}
                                                        onPress={() => {
                                                            setSelectedAudio(null); 
                                                        }}
                                                    >
                                        <MaterialIcons name="close" size={24} color="red" />
                                        </TouchableOpacity>
                                                </View>
                                            ) : selectedQuestion?.linkAmThanh ? (
                                                <View style={styles.audioContainer}>
                                                    <AudioPlayer audioUri={selectedQuestion.linkAmThanh} />
                                                    <TouchableOpacity
                                                        style={styles.deleteAudioIcon}
                                                        onPress={() => {
                                                            setSelectedQuestion({
                                                                ...selectedQuestion,
                                                                linkAmThanh: null, 
                                                            });
                                                        }}
                                                    >
                                        <MaterialIcons name="close" size={24} color="red" />
                                        </TouchableOpacity>
                                                </View>
                                            ) : (
                                                <Text style={styles.audioName}>Chưa chọn tệp âm thanh</Text>
                                            )
                                        }
                                    </View>


                                </View>
                                {answers.map((answer, idx) => (
                                    <View key={answer.idCauTraLoi} style={styles.modalOption}>
                                        <Text style={styles.optionLabel}>{answerOptions[idx]}.</Text>
                                        <TextInput
                                            style={styles.input}
                                            value={answer.noiDung}
                                            onChangeText={(text) => updateAnswerText(answer.idCauTraLoi, text)}
                                        />
                                    </View>
                                ))}
                                <Text style={styles.correctAnswerLabel}>Lời giải</Text>
                                <TextInput
                                    style={[styles.input, styles.explanationInput]}
                                    value={explanationText}
                                    onChangeText={setExplanationText}
                                    placeholder="Nhập lời giải"
                                />
                                <Text style={styles.correctAnswerLabel}>Chọn đáp án đúng</Text>
                                <select
                                    value={correctAnswerId ?? ""}
                                    onChange={(e) => setCorrectAnswerId(Number(e.target.value))}
                                    style={styles.picker}
                                >
                                    {answers.map((answer, idx) => (
                                        <option key={answer.idCauTraLoi} value={answer.idCauTraLoi}>
                                            {`${answerOptions[idx]}. ${answer.noiDung}`}
                                        </option>
                                    ))}
                                </select>
                                <View style={styles.modalButtons}>
                                    <TouchableOpacity onPress={() => setModalVisible(false)} style={styles.modalButton2}>
                                        <Text style={styles.modalButtonText}>Hủy</Text>
                                    </TouchableOpacity>
                                    <TouchableOpacity onPress={saveCorrectAnswer} style={styles.modalButton}>
                                        <Text style={styles.modalButtonText}>Lưu</Text>
                                    </TouchableOpacity>
                                </View>
                            </View>
                        </View>
                    </ScrollView>
                </Modal>

                <Modal visible={addQuestionModalVisible} transparent={true} animationType="slide">
                    <ScrollView>
                        <View style={styles.modalContainer}>
                            <View style={styles.modalContent}>
                                <Text style={styles.modalTitle}>Thêm câu hỏi mới</Text>
                                <TextInput
                                    style={[styles.input, styles.questionInput]}
                                    value={questionText}
                                    onChangeText={setQuestionText}
                                    placeholder="Nhập nội dung câu hỏi"
                                />
                                <View>
                                    <View style={{ flexDirection: 'row' }}>
                                        <TouchableOpacity onPress={pickImage} style={styles.imagePickerButton}>
                                            <Text style={styles.imagePickerButtonText}>Chọn Ảnh</Text>
                                        </TouchableOpacity>
                                        <TouchableOpacity onPress={pickAudioFile} style={styles.imagePickerButton}>
                                            <Text style={styles.imagePickerButtonText}>Chọn âm thanh</Text>
                                        </TouchableOpacity>
                                    </View>
                                    <View style={styles.imageContainer}>
                                        {selectedImage ? (
                                            <View>
                                                <Image source={{ uri: selectedImage }} style={styles.questionImage} />
                                                <TouchableOpacity
                                                    style={styles.deleteImageIcon}
                                                    onPress={handleRemoveImage}
                                                >
                                        <MaterialIcons name="close" size={24} color="red" />
                                        </TouchableOpacity>
                                            </View>
                                        ) : null}
                                    </View>
                                    {
                                        selectedAudio ? (
                                            <View style={styles.audioContainer}>
                                                <AudioPlayer audioUri={selectedAudio} />
                                                <TouchableOpacity
                                                    style={styles.deleteAudioIcon}
                                                    onPress={() => {
                                                        setSelectedAudio(null);
                                                    }}
                                                >
                                        <MaterialIcons name="close" size={24} color="red" />
                                        </TouchableOpacity>
                                            </View>
                                        ) : (
                                            <Text style={styles.audioName}>Chưa chọn tệp âm thanh</Text>
                                        )
                                    }
                                </View>
                                {answers.map((answer, idx) => (
                                    <View key={answer.idCauTraLoi} style={styles.modalOption}>
                                        <Text style={styles.optionLabel}>{answerOptions[idx]}.</Text>
                                        <TextInput
                                            style={styles.input}
                                            value={answer.noiDung}
                                            onChangeText={(text) => updateAnswerText(answer.idCauTraLoi, text)}
                                        />
                                    </View>
                                ))}
                                <Text style={styles.correctAnswerLabel}>Lời giải</Text>
                                <TextInput
                                    style={[styles.input, styles.explanationInput]}
                                    value={explanationText}
                                    onChangeText={setExplanationText}
                                    placeholder="Nhập lời giải"
                                />
                                <Text style={styles.correctAnswerLabel}>Chọn đáp án đúng</Text>
                                <select
                                    value={correctAnswerId ?? ""}
                                    onChange={(e) => {
                                        const selectedId = Number(e.target.value);
                                        setCorrectAnswerId(selectedId);

                                        setAnswers((prevAnswers) =>
                                            prevAnswers.map((answer) => ({
                                                ...answer,
                                                ketQua: answer.idCauTraLoi === selectedId,
                                            }))
                                        );
                                    }}
                                    style={styles.picker}
                                >
                                    {answers.map((answer, idx) => (
                                        <option key={answer.idCauTraLoi} value={answer.idCauTraLoi}>
                                            {`${answerOptions[idx]}: ${answer.noiDung}`}
                                        </option>
                                    ))}
                                </select>
                                <View style={styles.modalButtons}>
                                    <TouchableOpacity onPress={() => setAddQuestionModalVisible(false)} style={styles.modalButton2}>
                                        <Text style={styles.modalButtonText}>Hủy</Text>
                                    </TouchableOpacity>
                                    <TouchableOpacity onPress={saveNewQuestion} style={styles.modalButton}>
                                        <Text style={styles.modalButtonText}>Lưu</Text>
                                    </TouchableOpacity>
                                </View>
                            </View>
                        </View>
                    </ScrollView>
                </Modal>

                <Modal visible={errorModalVisible} transparent={true} animationType="slide">
                    <View style={styles.modalOverlay}>
                        <View style={styles.modalContainerM}>
                            <Text style={styles.modalText}>{errorMessage}</Text>
                            <TouchableOpacity
                                style={styles.modalButton2}
                                onPress={() => setErrorModalVisible(false)}
                            >
                                <Text style={styles.modalButtonText}>Đóng</Text>
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
    headerRow: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'flex-start',
        marginBottom: 10,
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#ff6600',
        textAlign: 'center',
        marginBottom: 20,
    },
    backButton: {
        padding: 10,
        backgroundColor: '#00405d',
        borderRadius: 5,
    },
    backButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
    contentContainer: {
        flex: 1,
    },
    questionContainer: {
        marginBottom: 20,
    },
    questionHeader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
    },
    questionText: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#333',
    },
    iconContainer: {
        flexDirection: 'row',
    },
    editIcon: {
        marginLeft: 10,
    },
    deleteIcon: {
        marginLeft: 10,
    },
    answerList: {
        paddingLeft: 10,
    },
    answerOption: {
        flexDirection: 'row',
        alignItems: 'center',
        marginVertical: 4,
    },
    optionLabel: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#555',
        marginRight: 5,
    },
    answerText: {
        fontSize: 16,
        color: '#333',
        flex: 1,
    },
    correctAnswer: {
        color: '#28a745',
        fontWeight: 'bold',
    },
    correctIndicator: {
        color: '#28a745',
        marginLeft: 5,
    },
    explanationText: {
        marginTop: 10,
        fontSize: 16,
        color: '#555',
        fontStyle: 'italic',
    },
    explanationLabel: {
        fontWeight: 'bold',
        color: '#333',
    },
    modalOverlay: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
    modalContainer: {
        width: 'auto',
        padding: 20,
        borderRadius: 10,
        alignItems: 'center',
        justifyContent: 'center',
        margin: 'auto',
    },
    modalContainerM: {
        width: '80%',
        maxWidth: 400,
        backgroundColor: '#fff',
        padding: 20,
        borderRadius: 10,
        alignItems: 'center',
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.25,
        shadowRadius: 4,
        elevation: 5,
    },
    modalContainer2: {
        width: '100%',
        maxWidth: 400,
        backgroundColor: '#fff',
        padding: 20,
        borderRadius: 10,
        alignItems: 'center',
    },
    modalText: {
        fontSize: 16,
        marginBottom: 20,
        textAlign: 'center',
    },
    modalButtons: {
        flexDirection: 'row',
        justifyContent: 'center',
        marginTop: 20
    },
    modalButtons2: {
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    modalButton: {
        backgroundColor: '#ff0000',
        padding: 10,
        borderRadius: 5,
        marginHorizontal: 10,
    },
    modalButton2: {
        backgroundColor: '#00405d',
        padding: 10,
        borderRadius: 5,
        marginHorizontal: 10,
    },
    modalButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
    input: {
        flex: 1,
        borderBottomWidth: 1,
        borderColor: '#ccc',
        padding: 5,
    },
    correctAnswerLabel: {
        marginTop: 10,
        fontWeight: 'bold',
        color: '#333',
        marginBottom: 10,
    },
    picker: {
        flex: 1,
        marginVertical: 10,
    },
    explanationInput: {
        marginTop: 10,
        borderColor: '#ddd',
        padding: 8,
    },
    questionInput: {
        marginBottom: 15,
        fontSize: 16,
        padding: 10,
        borderColor: '#ccc',
        borderWidth: 1,
        borderRadius: 5,
    },
    modalOption: {
        flexDirection: 'row',
        alignItems: 'center',
        marginVertical: 10,
    },
    modalContent: {
        margin: 20,
        backgroundColor: 'white',
        borderRadius: 10,
        padding: 20,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.25,
        shadowRadius: 4,
        elevation: 5,
        width: 540,
        alignSelf: 'center',
    },
    modalTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 10,
    },
    addButtonContainer: {
        justifyContent: 'center',
        alignItems: 'center',
        position: 'absolute',
        bottom: 20,
        width: '100%',
    },
    addButton: {
        backgroundColor: '#28a745',
        borderRadius: 50,
        width: 40,
        height: 40,
        alignItems: 'center',
        justifyContent: 'center',
        right: -400,
    },
    questionImage: {
        width: 500,
        height: 350,
        borderRadius: 10,
        marginTop: 10,
        marginBottom: 10,
        resizeMode: 'cover',
        alignItems: 'center'
    },
    imagePickerButton: {
        backgroundColor: '#00bf63',
        borderRadius: 5,
        width: 150,
        padding: 10,
        alignItems: 'center',
        marginTop: 10,
        marginRight: 10
    },

    imagePickerButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
    audioContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        marginTop: 10,
    },
    audioName: {
        fontSize: 14,
        color: '#333',
        marginBottom: 5,
    },
    audioRemoveButton: {
        marginLeft: 10,
    },
    deleteImageIcon: {
        position: 'absolute',
        top: 10,
        right: 10,
        borderRadius: 50,
        padding: 2,
    },
    imageContainer: {
        position: 'relative',
    },
    deleteAudioIcon: {
        position: 'absolute',
        top: -10,
        right: -10,
        zIndex: 1,
    },

});

export default AssignmentDetailScreen;

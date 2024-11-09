import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, ScrollView, ImageBackground, ActivityIndicator, Modal, TouchableOpacity, TextInput } from 'react-native';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';

interface Question {
    idCauHoi: number;
    noiDung: string;
    dapAn: Answer[] | null;
    loiGiai: string | null;
}

interface Answer {
    idCauTraLoi: number;
    noiDung: string;
    ketQua: boolean;
}

const AssignmentDetailScreen = ({ navigation, route }: { navigation: any, route: any }) => {
    const { assignmentId } = route.params;
    const [questions, setQuestions] = useState<Question[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [selectedQuestion, setSelectedQuestion] = useState<Question | null>(null);
    const [modalVisible, setModalVisible] = useState(false);
    const [answers, setAnswers] = useState<Answer[]>([]);
    const [correctAnswerId, setCorrectAnswerId] = useState<number | null>(null);
    const [questionText, setQuestionText] = useState<string>(""); // Thêm trạng thái cho câu hỏi
    const [explanationText, setExplanationText] = useState<string>("");
    const answerOptions = ["A", "B", "C", "D"];

    useEffect(() => {
        fetchAssignmentDetails();
    }, []);

    const fetchAssignmentDetails = async () => {
        setIsLoading(true);
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            const response = await http.get(`/baitap/getCauHoi/${assignmentId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            const questionsData = response.data || [];
            const updatedQuestions = await Promise.all(
                questionsData.map(async (question: Question) => {
                    const answers = await fetchAnswersForQuestion(question.idCauHoi);
                    return { ...question, dapAn: answers };
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
            const response = await http.get(`/baitap/getCauTraLoi/${questionId}`, {
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
        setQuestionText(question.noiDung || ""); // Thiết lập nội dung câu hỏi
        setExplanationText(question.loiGiai || ""); // Thiết lập lời giải
        const correctAnswer = question.dapAn?.find(answer => answer.ketQua);
        setCorrectAnswerId(correctAnswer?.idCauTraLoi || null);
        setAnswers(question.dapAn || []);
        setModalVisible(true);
    };

    const saveCorrectAnswer = async () => {
        if (!selectedQuestion || correctAnswerId === null) return;

        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            // await http.put(`/baitap/updateCauHoi/${selectedQuestion.idCauHoi}`, {
            //     noiDung: questionText,
            //     loiGiai: explanationText,
            // }, {
            //     headers: { Authorization: `Bearer ${token}` },
            // });
            await Promise.all(
                answers.map(answer => 
                    http.put(`/cauTraLoi/create/${selectedQuestion.idCauHoi}/${answer.idCauTraLoi}`, {
                        ...answer,
                        ketQua: answer.idCauTraLoi === correctAnswerId
                    }, {
                        headers: { Authorization: `Bearer ${token}` },
                    })
                )
            );
            fetchAssignmentDetails();
        } catch (error) {
            console.error('Failed to update question, correct answer or explanation:', error);
        } finally {
            setModalVisible(false);
        }
    };

    const updateAnswerText = (idCauTraLoi: number, text: string) => {
        setAnswers(prevAnswers => 
            prevAnswers.map(answer => 
                answer.idCauTraLoi === idCauTraLoi ? { ...answer, noiDung: text } : answer
            )
        );
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
                                        <TouchableOpacity onPress={() => openEditModal(question)}>
                                            <Icon name="pencil" size={20} color="#ff6600" style={styles.editIcon} />
                                        </TouchableOpacity>
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

                {/* Modal chỉnh sửa câu hỏi, đáp án đúng và lời giải */}
                <Modal visible={modalVisible} transparent={true} animationType="slide">
                    <View style={styles.modalContainer}>
                        <View style={styles.modalContent}>
                            <Text style={styles.modalTitle}>Chỉnh sửa câu hỏi</Text>
                            <TextInput
                                style={[styles.input, styles.questionInput]}
                                value={questionText}
                                onChangeText={setQuestionText}
                                placeholder="Nhập nội dung câu hỏi"
                            />
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
                                <TouchableOpacity onPress={() => setModalVisible(false)} style={styles.modalButton}>
                                    <Text style={styles.modalButtonText}>Hủy</Text>
                                </TouchableOpacity>
                                <TouchableOpacity onPress={saveCorrectAnswer} style={styles.modalButton}>
                                    <Text style={styles.modalButtonText}>Lưu</Text>
                                </TouchableOpacity>
                            </View>
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
    editIcon: {
        marginLeft: 10,
    },
    modalContainer: {
        flex: 1,
        justifyContent: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
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
        width: 500,
        alignSelf: 'center',
    },
    modalTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 10,
    },
    modalOption: {
        flexDirection: 'row',
        alignItems: 'center',
        marginVertical: 10,
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
    modalButtons: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginTop: 20,
    },
    modalButton: {
        padding: 10,
        backgroundColor: '#ff6600',
        borderRadius: 5,
    },
    modalButtonText: {
        color: '#fff',
        fontWeight: 'bold',
        textAlign: 'center',
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
});

export default AssignmentDetailScreen;

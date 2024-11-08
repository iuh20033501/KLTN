import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, ScrollView, ImageBackground, ActivityIndicator } from 'react-native';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { TouchableOpacity } from 'react-native';

interface Question {
    idCauHoi: number;
    noiDung: string;
    dapAn: Answer[] | null;
    loiGiai: string | null; // Thêm thuộc tính lời giải vào kiểu dữ liệu của câu hỏi
}

interface Answer {
    idCauTraLoi: number;
    noiDung: string;
    ketQua: boolean; // true nếu câu trả lời là đúng
}

const AssignmentDetailScreen = ({ navigation, route }: { navigation: any, route: any }) => {
    const { assignmentId } = route.params;
    const [questions, setQuestions] = useState<Question[]>([]);
    const [isLoading, setIsLoading] = useState(false);
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
                                    <Text style={styles.questionText}>
                                        {index + 1}. {question.noiDung}
                                    </Text>
                                    <View style={styles.answerList}>
                                        {Array.isArray(question.dapAn) ? (
                                            question.dapAn.map((answer, idx) => (
                                                <View 
                                                    key={`${question.idCauHoi}-${answer.idCauTraLoi ?? idx}`} 
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
                                    {/* Hiển thị lời giải phía dưới câu trả lời */}
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
    questionText: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#333',
        marginBottom: 5,
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
});

export default AssignmentDetailScreen;

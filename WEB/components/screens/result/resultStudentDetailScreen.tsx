import React, { useState, useEffect, useCallback } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, ImageBackground, ActivityIndicator, Modal } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import http from '@/utils/http';
import { useFocusEffect } from '@react-navigation/native';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface MemberInfo {
    idUser: number;
    hoTen: string;
    email: string;
}

interface Session {
    idBuoiHoc: any;
    id: number;
    chuDe: string;
}

interface Assignment {
    idBaiTap: number;
    tenBaiTap: string;
    trangThai: boolean;
}

interface Score {
    idHocVien: number;
    hoTen: string;
    score: number;
    thoiGianHoanThanh?: string;

}
interface Test {
    idTest: number;
    loaiTest: string;
    trangThai: boolean;
}

const ResultStudentDetailScreen = ({ navigation, route }: { navigation: any, route: any }) => {
    const { idLopHoc, tenLopHoc, idUser, nameUser } = route.params;
    const [activeTab, setActiveTab] = useState('ExerciseScores');
    const [members, setMembers] = useState<MemberInfo[]>([]);
    const [sessions, setSessions] = useState<Session[]>([]);
    const [assignments, setAssignments] = useState<{ [key: number]: Assignment[] }>({});
    const [expandedSession, setExpandedSession] = useState<number | null>(null);
    const [isLoadingMembers, setIsLoadingMembers] = useState(false);
    const [isLoadingSessions, setIsLoadingSessions] = useState(false);
    const [isLoadingAssignments, setIsLoadingAssignments] = useState(false);
    const [modalVisible, setModalVisible] = useState(false);
    const [scores, setScores] = useState<Score[]>([]);
    const [selectedAssignment, setSelectedAssignment] = useState<string | null>(null);
    const [tests, setTests] = useState<Test[]>([]);
    const [isLoadingTests, setIsLoadingTests] = useState(false);

    useFocusEffect(
        useCallback(() => {
            if (activeTab === 'ExerciseScores' || activeTab === 'ExamScores') {
                fetchSessions();
            }
        }, [activeTab])
    );



    const fetchSessions = async () => {
        setIsLoadingSessions(true);
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            const response = await http.get(`buoihoc/getbuoiHocByLop/${idLopHoc}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setSessions(response.data);
        } catch (error) {
            console.error('Failed to fetch sessions:', error);
        } finally {
            setIsLoadingSessions(false);
        }
    };

    const fetchAssignments = async (idBuoiHoc: number) => {
        setIsLoadingAssignments(true);
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            const response = await http.get(`baitap/getBaiTapofBuoiTrue/${idBuoiHoc}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setAssignments((prev) => ({
                ...prev,
                [idBuoiHoc]: response.data,
            }));
        } catch (error) {
            console.error(`Failed to fetch assignments for session ${idBuoiHoc}:`, error);
        } finally {
            setIsLoadingAssignments(false);
        }
    };

    const fetchScores = async (idBaiTap: number) => {
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }

            const response = await http.get(`/baitap/getTienTrinhHVBT/${idUser}/${idBaiTap}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            const scoresData = [
                {
                    idHocVien: idUser,
                    hoTen: nameUser || 'Học viên',
                    score: response.data?.cauDung ? response.data.cauDung * 10 : 0,
                },
            ];

            setScores(scoresData);
            setModalVisible(true);
        } catch (error) {
            console.error('Failed to fetch scores:', error);
        }
    };

    const fetchTests = async () => {
        setIsLoadingTests(true);
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            const response = await http.get(`baitest/getBaiTestofLopTrue/${idLopHoc}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setTests(response.data);
        } catch (error) {
            console.error('Failed to fetch tests:', error);
        } finally {
            setIsLoadingTests(false);
        }
    };

    const fetchTestScores = async (idTest: number) => {
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            const response = await http.get(`baitest/getKetQua/${idTest}/${idUser}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            const scoresData = [
                {
                    idHocVien: idUser,
                    hoTen: nameUser || 'Học viên',
                    score: response.data?.diemTest || 0,
                    thoiGianHoanThanh: response.data?.thoiGianHoanThanh || 'N/A',
                },
            ];

            setScores(scoresData);
            setModalVisible(true);
        } catch (error) {
            console.error('Failed to fetch test scores:', error);
        }
    };

    useFocusEffect(
        useCallback(() => {
            if (activeTab === 'ExamScores') {
                fetchTests();
            }
        }, [activeTab])
    );

    const toggleSession = (idBuoiHoc: number) => {
        if (expandedSession === idBuoiHoc) {
            setExpandedSession(null);
        } else {
            setExpandedSession(idBuoiHoc);
            if (!assignments[idBuoiHoc]) {
                fetchAssignments(idBuoiHoc);
            }
        }
    };
    const formatLoaiTest = (loaiTest: string): string => {
        switch (loaiTest) {
            case 'CK':
                return 'Cuối Kỳ';
            case 'GK':
                return 'Giữa Kỳ';
            default:
                return loaiTest; // Trả về giá trị gốc nếu không khớp
        }
    };

    const renderContent = () => {
        switch (activeTab) {
            case 'ExerciseScores':
                return (
                    <ScrollView style={styles.contentContainer}>
                        <Text style={styles.sectionTitle}>Điểm Bài Tập</Text>
                        {isLoadingSessions ? (
                            <ActivityIndicator size="large" color="#00405d" />
                        ) : (
                            sessions.length > 0 ? (
                                sessions.map((session) => (
                                    <View key={session.idBuoiHoc} style={styles.sessionContainer}>
                                        <TouchableOpacity
                                            onPress={() => toggleSession(session.idBuoiHoc)}
                                            style={styles.sessionHeader}
                                        >
                                            <Text style={styles.sessionText}>{session.chuDe}</Text>
                                            <Icon
                                                name={expandedSession === session.idBuoiHoc ? "chevron-up" : "chevron-down"}
                                                size={20}
                                                color="#000"
                                            />
                                        </TouchableOpacity>
                                        {expandedSession === session.idBuoiHoc && (
                                            <View style={styles.assignmentList}>
                                                {isLoadingAssignments ? (
                                                    <ActivityIndicator size="small" color="#00405d" />
                                                ) : assignments[session.idBuoiHoc] && assignments[session.idBuoiHoc].length > 0 ? (
                                                    assignments[session.idBuoiHoc].map((assignment) => (
                                                        <TouchableOpacity
                                                            key={assignment.idBaiTap}
                                                            onPress={() => {
                                                                setSelectedAssignment(assignment.tenBaiTap);
                                                                fetchScores(assignment.idBaiTap);
                                                            }}
                                                        >
                                                            <Text style={styles.assignmentText}>
                                                                {assignment.tenBaiTap}
                                                            </Text>
                                                        </TouchableOpacity>
                                                    ))
                                                ) : (
                                                    <Text style={styles.noAssignments}>Không có bài tập nào.</Text>
                                                )}
                                            </View>
                                        )}
                                    </View>
                                ))
                            ) : (
                                <Text>Không có buổi học nào.</Text>
                            )
                        )}
                    </ScrollView>
                );
            case 'ExamScores':
                return (
                    <ScrollView style={styles.contentContainer}>
                        <Text style={styles.sectionTitle}>Điểm Bài Thi</Text>
                        {isLoadingTests ? (
                            <ActivityIndicator size="large" color="#00405d" />
                        ) : tests.length > 0 ? (
                            tests.map((test) => (
                                <TouchableOpacity
                                    key={test.idTest}
                                    onPress={() => {
                                        setSelectedAssignment(test.loaiTest);
                                        fetchTestScores(test.idTest);
                                    }}
                                    style={styles.sessionContainer}
                                >
                                    <Text style={styles.assignmentText}>{formatLoaiTest(test.loaiTest)}</Text>
                                </TouchableOpacity>
                            ))
                        ) : (
                            <Text>Không có bài thi nào.</Text>
                        )}
                    </ScrollView>
                );
            default:
                return null;
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

                <Text style={styles.title}>{tenLopHoc}</Text>

                <View style={styles.tabContainer}>
                    <TouchableOpacity
                        style={[styles.tabButton, activeTab === 'ExerciseScores' && styles.activeTab]}
                        onPress={() => setActiveTab('ExerciseScores')}
                    >
                        <Text style={[styles.tabText, activeTab === 'ExerciseScores' && styles.activeTabText]}>Điểm bài tập</Text>
                    </TouchableOpacity>
                    <TouchableOpacity
                        style={[styles.tabButton, activeTab === 'ExamScores' && styles.activeTab]}
                        onPress={() => setActiveTab('ExamScores')}
                    >
                        <Text style={[styles.tabText, activeTab === 'ExamScores' && styles.activeTabText]}>Điểm bài thi</Text>
                    </TouchableOpacity>
                </View>
                {renderContent()}

                <Modal visible={modalVisible} transparent animationType="fade">
                    <View style={styles.modalOverlay}>
                        <View style={styles.modalContainer}>
                            <Text style={styles.sectionTitle}>
                                {activeTab === 'ExerciseScores' ? `Điểm bài tập: ${selectedAssignment}` : `Kết quả bài thi: ${selectedAssignment}`}
                            </Text>
                            <ScrollView style={styles.contentContainer}>
                                {scores.map((score) => (
                                    <View key={score.idHocVien} style={styles.memberCard}>
                                        <Text style={styles.memberName}>Họ tên: {score.hoTen}</Text>
                                        <Text style={styles.memberScores}>Điểm: {score.score}</Text>
                                        {activeTab === 'ExamScores' && (
                                            <Text style={styles.memberEmail}>Thời gian làm bài: {score.thoiGianHoanThanh}</Text>
                                        )}
                                    </View>
                                ))}
                            </ScrollView>
                            <TouchableOpacity style={styles.closeButton} onPress={() => setModalVisible(false)}>
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
    tabContainer: {
        flexDirection: 'row',
        marginBottom: 20,
        borderBottomWidth: 1,
        borderBottomColor: '#ddd',
    },
    tabButton: {
        flex: 1,
        paddingVertical: 10,
        alignItems: 'center',
        borderBottomWidth: 2,
        borderBottomColor: 'transparent',
    },
    activeTab: {
        borderBottomColor: '#28a745',
    },
    tabText: {
        fontSize: 16,
        color: '#666',
    },
    activeTabText: {
        color: '#28a745',
        fontWeight: 'bold',
    },
    contentContainer: {
        flex: 1,
    },
    sectionTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 10,
    },
    memberCard: {
        padding: 10,
        marginBottom: 5,
        backgroundColor: '#e0f7e9',
        borderRadius: 5,
    },
    memberName: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#333',
    },
    memberEmail: {
        fontSize: 14,
        color: '#555',
    },
    memberScores: {
        fontSize: 16,
        color: '#ff6600',
    },
    sessionContainer: {
        padding: 10,
        marginVertical: 5,
        backgroundColor: '#f0f0f0',
        borderRadius: 5,
    },
    sessionHeader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
    },
    sessionText: {
        fontSize: 16,
        fontWeight: 'bold',
    },
    assignmentList: {
        marginTop: 5,
        paddingLeft: 10,
    },
    assignmentText: {
        fontSize: 16,
        color: '#00405d',
        fontWeight: 'bold',
    },
    noAssignments: {
        fontSize: 14,
        color: '#666',
    },
    modalOverlay: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
    modalContainer: {
        width: '56%',
        backgroundColor: '#fff',
        padding: 20,
        borderRadius: 10,
    },
    modalTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 15,
    },
    modalScoreText: {
        fontSize: 16,
        marginBottom: 10,
    },
    closeButton: {
        alignSelf: 'center',
        width: 100,
        marginTop: 20,
        padding: 10,
        backgroundColor: '#28a745',
        borderRadius: 5,
    },
    closeButtonText: {
        color: '#fff',
        fontWeight: 'bold',
        textAlign: 'center'
    },
    assignmentItem: {
        padding: 10,
        marginVertical: 8,
        backgroundColor: '#f2f2f2',
        borderRadius: 8,
    },
});

export default ResultStudentDetailScreen;

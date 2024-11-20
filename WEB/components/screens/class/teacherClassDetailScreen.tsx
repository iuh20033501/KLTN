import React, { useState, useEffect,useCallback  } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, ImageBackground, ActivityIndicator, Modal } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useFocusEffect } from '@react-navigation/native';
import { deleteFileFromS3 } from '../client/s3Client'; // Ensure this function is imported

interface MemberInfo {
    idHocVien: number;
    hoTen: string;
    email: string;
}

interface ClassDetail {
    [x: string]: any;
    tenKhoaHoc: string;
    tenLopHoc: string;
    ngayBD: string;
    ngayKT: string;
    trangThai: string;
    giangVien: {
        hoTen: string;
        email: string;
        sdt: string;
    };
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
    ngayBD: string;
    ngayKT: string;
}

const TeacherClassDetailScreen = ({ navigation, route }: { navigation: any, route: any }) => {
    const { idLopHoc, tenLopHoc, role } = route.params;
    const [activeTab, setActiveTab] = useState('Assignments');
    const [members, setMembers] = useState<MemberInfo[]>([]);
    const [classDetail, setClassDetail] = useState<ClassDetail | null>(null);
    const [sessions, setSessions] = useState<Session[]>([]);
    const [assignments, setAssignments] = useState<{ [key: number]: Assignment[] }>({});
    const [isLoadingMembers, setIsLoadingMembers] = useState(false);
    const [isLoadingClassInfo, setIsLoadingClassInfo] = useState(false);
    const [isLoadingSessions, setIsLoadingSessions] = useState(false);
    const [confirmModalVisible, setConfirmModalVisible] = useState(false);
    const [selectedAssignment, setSelectedAssignment] = useState<{ id: number; name: string; sessionId: number } | null>(null);
    const [selectedQuestion, setSelectedQuestion] = useState<{ id: number; name: string } | null>(null);

    useFocusEffect(
        useCallback(() => {
            if (activeTab === 'Assignments') {
                fetchSessions();
            } else if (activeTab === 'Members') {
                fetchMembers();
            } else if (activeTab === 'ClassInfo') {
                fetchClassInfo();
            }
        }, [activeTab])
    );

    useEffect(() => {
        if (activeTab === 'Assignments' && sessions.length > 0) {
            sessions.forEach((session) => {
                if (session.idBuoiHoc) {
                    fetchAssignments(session.idBuoiHoc);
                }
            });
        }
    }, [sessions]);

    const fetchMembers = async () => {
        setIsLoadingMembers(true);
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            const response = await http.get(`lopHoc/getByLop/${idLopHoc}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            const data = Array.isArray(response.data) ? response.data : [];
            setMembers(data);
        } catch (error) {
            console.error('Failed to fetch members:', error);
        } finally {
            setIsLoadingMembers(false);
        }
    };

    const fetchClassInfo = async () => {
        setIsLoadingClassInfo(true);
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            const response = await http.get(`lopHoc/getLop/${idLopHoc}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setClassDetail(response.data);
        } catch (error) {
            console.error('Failed to fetch class details:', error);
        } finally {
            setIsLoadingClassInfo(false);
        }
    };

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
            response.data.forEach((session: Session) => {
                if (session && typeof session.idBuoiHoc === 'number') {
                    fetchAssignments(session.idBuoiHoc);
                } else {
                    console.warn(`Invalid session ID for session:`, session);
                }
            });
        } catch (error) {
            console.error('Failed to fetch sessions:', error);
        } finally {
            setIsLoadingSessions(false);
        }
    };

    const fetchAssignments = async (sessionId: number) => {
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            const response = await http.get(`baitap/getBaiTapofBuoiTrue/${sessionId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setAssignments((prev) => ({
                ...prev,
                [sessionId]: response.data,
            }));
        } catch (error) {
            console.error(`Failed to fetch assignments for session ${sessionId}:`, error);
        }
    };

    const confirmDeleteAssignment = (assignmentId: number, assignmentName: string, sessionId: number) => {
        setSelectedAssignment({ id: assignmentId, name: assignmentName, sessionId });
        setConfirmModalVisible(true);
    };

    const deleteImagesForAssignment = async (assignmentId: number) => {
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            const response = await http.get(`baitap/getCauHoiTrue/${assignmentId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            
            const questions = response.data;
    
            for (const question of questions) {
                if (question.linkAnh) {
                    const fileName = question.linkAnh.split('/').pop();
                    if (fileName) {
                        await deleteFileFromS3(`imgCauHoi/${fileName}`);
                    }
                }
            }
        } catch (error) {
            console.error(`Failed to delete images for assignment ${assignmentId}:`, error);
        }
    };
    const deleteAssignment = async () => {
        if (!selectedAssignment) return;
        const { id, sessionId } = selectedAssignment;
    
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
    
            await deleteImagesForAssignment(id);
            await http.get(`baitap/deleteBtap/${id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            fetchAssignments(sessionId); 
        } catch (error) {
            console.error(`Failed to delete assignment ${id}:`, error);
        } finally {
            setConfirmModalVisible(false);
            setSelectedAssignment(null);
        }
    };
    

    const renderContent = () => {
        switch (activeTab) {
            case 'Assignments':
                return (
                    <ScrollView style={styles.contentContainer}>
                        <Text style={styles.sectionTitle}>Bài Tập</Text>
                        {isLoadingSessions ? (
                            <ActivityIndicator size="large" color="#00405d" />
                        ) : (
                            sessions.length > 0 ? (
                                sessions.map((session) => (
                                    <View key={session.idBuoiHoc} style={styles.sessionContainer}>
                                        <Text style={styles.sessionText}>{session.chuDe}</Text>
                                        {role === 'TEACHER' && (
                                            <View style={styles.buttonRow}>
                                                <TouchableOpacity
                                                    style={styles.createButton}
                                                    onPress={() => navigation.navigate('AddAssignmentScreen', { idLopHoc, sessionId: session.idBuoiHoc, tenLopHoc, role,})}
                                                >
                                                    <Text style={styles.createButtonText}>Thêm bài tập</Text>
                                                </TouchableOpacity>
                                            </View>
                                        )}
                                        {assignments[session.idBuoiHoc] && assignments[session.idBuoiHoc].length > 0 ? (
                                            <View style={styles.assignmentList}>
                                                {assignments[session.idBuoiHoc].map((assignment) => (
                                                    <View key={assignment.idBaiTap} style={styles.assignmentItemContainer}>
                                                        <TouchableOpacity
                                                            style={styles.assignmentItem}
                                                            onPress={() => navigation.navigate('AssignmentDetailScreen', { assignmentId: assignment.idBaiTap,sessionId: session.idBuoiHoc })}
                                                        >
                                                            <Icon name="file-document" size={20} color="#ff6600" />
                                                            <Text style={styles.assignmentText}>
                                                                {assignment.tenBaiTap}
                                                            </Text>
                                                        </TouchableOpacity>
                                                        <TouchableOpacity onPress={() => confirmDeleteAssignment(assignment.idBaiTap, assignment.tenBaiTap, session.idBuoiHoc)}>
                                                            <Icon name="delete" size={20} color="red" />
                                                        </TouchableOpacity>
                                                    </View>
                                                ))}
                                            </View>
                                        ) : (
                                            <Text>Không có bài tập nào.</Text>
                                        )}
                                    </View>
                                ))
                            ) : (   
                                <Text>Không có buổi học nào.</Text> 
                            )
                        )}
                    </ScrollView>
                );
            case 'Members':
                return (
                    <ScrollView style={styles.contentContainer}>
                        <Text style={styles.sectionTitle}>Danh Sách Thành Viên</Text>
                        {isLoadingMembers ? (
                            <ActivityIndicator size="large" color="#00405d" />
                        ) : (
                            members.length > 0 ? (
                                members.map((member) => (
                                    <View key={member.idHocVien} style={styles.memberCard}>
                                        <Text style={styles.memberName}>{member.hoTen}</Text>
                                        <Text style={styles.memberEmail}>{member.email}</Text>
                                    </View>
                                ))
                            ) : (
                                <Text>Không có thành viên nào trong lớp.</Text>
                            )
                        )}
                    </ScrollView>
                );
            case 'Grades':
                return (
                    <ScrollView style={styles.contentContainer}>
                        <Text style={styles.sectionTitle}>Điểm Số</Text>
                    </ScrollView>
                );
            case 'ClassInfo':
                return (
                    <ScrollView style={styles.contentContainer}>
                        <Text style={styles.sectionTitle}>Thông Tin Lớp Học</Text>
                        {isLoadingClassInfo ? (
                            <ActivityIndicator size="large" color="#00405d" />
                        ) : (
                            classDetail ? (
                                <View>
                                    <Text style={styles.infoLabel}>Khóa học: <Text style={styles.infoText}>{classDetail.khoaHoc.tenKhoaHoc}</Text></Text>
                                    <Text style={styles.infoLabel}>Tên lớp: <Text style={styles.infoText}>{classDetail.tenLopHoc}</Text></Text>
                                    <Text style={styles.infoLabel}>Trạng thái: <Text style={styles.infoText}>{classDetail.trangThai}</Text></Text>
                                    <Text style={styles.infoLabel}>Giáo viên phụ trách: <Text style={styles.infoText}>{classDetail.giangVien.hoTen}</Text></Text>
                                    <Text style={styles.infoLabel}>Email: <Text style={styles.infoText}>{classDetail.giangVien.email}</Text></Text>
                                    <Text style={styles.infoLabel}>Số điện thoại: <Text style={styles.infoText}>{classDetail.giangVien.sdt}</Text></Text>
                                </View>
                            ) : (
                                <Text>Không có thông tin lớp học.</Text>
                            )
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
                    <TouchableOpacity style={[styles.tabButton, activeTab === 'Assignments' && styles.activeTab]} onPress={() => setActiveTab('Assignments')}>
                        <Text style={[styles.tabText, activeTab === 'Assignments' && styles.activeTabText]}>Bài tập</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={[styles.tabButton, activeTab === 'Members' && styles.activeTab]} onPress={() => setActiveTab('Members')}>
                        <Text style={[styles.tabText, activeTab === 'Members' && styles.activeTabText]}>Danh sách thành viên</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={[styles.tabButton, activeTab === 'Grades' && styles.activeTab]} onPress={() => setActiveTab('Grades')}>
                        <Text style={[styles.tabText, activeTab === 'Grades' && styles.activeTabText]}>Điểm số</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={[styles.tabButton, activeTab === 'ClassInfo' && styles.activeTab]} onPress={() => setActiveTab('ClassInfo')}>
                        <Text style={[styles.tabText, activeTab === 'ClassInfo' && styles.activeTabText]}>Thông tin lớp học</Text>
                    </TouchableOpacity>
                </View>
                {renderContent()}

                {/* Modal Xác nhận xóa */}
                <Modal
                    visible={confirmModalVisible}
                    transparent
                    animationType="slide"
                    onRequestClose={() => setConfirmModalVisible(false)}
                >
                    <View style={styles.modalOverlay}>
                        <View style={styles.modalContainer}>
                            <Text style={styles.modalTitle}>Xác nhận xóa</Text>
                            {selectedAssignment && (
                                <Text style={styles.modalMessage}>
                                    Bạn có chắc chắn muốn xóa bài tập "{selectedAssignment.name}"?
                                </Text>
                            )}
                            <View style={styles.modalButtons}>
                                <TouchableOpacity onPress={() => setConfirmModalVisible(false)} style={styles.cancelButton}>
                                    <Text style={styles.modalButtonText}>Hủy</Text>
                                </TouchableOpacity>
                                <TouchableOpacity onPress={deleteAssignment} style={styles.confirmButton}>
                                    <Text style={styles.modalButtonText}>Đồng ý</Text>
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
    infoLabel: {
        fontSize: 16,
        fontWeight: 'bold',
        marginTop: 5,
    },
    infoText: {
        fontWeight: 'normal',
    },
    sessionContainer: {
        padding: 10,
        marginVertical: 5,
        backgroundColor: '#f0f0f0',
        borderRadius: 5,
    },
    sessionText: {
        fontSize: 16,
        fontWeight: 'bold',
    },
    buttonRow: {
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    createButton: {
        padding: 8,
        backgroundColor: '#ff6600',
        borderRadius: 5,
        alignItems: 'center',
        marginTop: 5,
        width: 120,
        height: 35,
    },
    createButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
    assignmentList: {
        marginTop: 5,
    },
    assignmentItemContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        paddingVertical: 5,
    },
    assignmentItem: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    assignmentText: {
        fontSize: 16,
        color: '#333',
        paddingLeft: 10,
    },
    modalOverlay: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',

    },
    modalContainer: {
        width: '100%',
        maxWidth: 400,
        backgroundColor: '#fff',
        padding: 20,
        borderRadius: 10,
        alignItems: 'center',
    },
    modalTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 15,
    },
    modalMessage: {
        fontSize: 16,
        textAlign: 'center',
        marginBottom: 20,
    },
    modalButtons: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        width: '100%',
    },
    cancelButton: {
        flex: 1,
        backgroundColor: '#00405d',
        padding: 10,
        borderRadius: 5,
        marginRight: 10,
        alignItems: 'center',
    },
    confirmButton: {
        flex: 1,
        backgroundColor: '#ff0000',
        padding: 10,
        borderRadius: 5,
        alignItems: 'center',
    },
    modalButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
});

export default TeacherClassDetailScreen;

import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity, ImageBackground, Modal } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import http from '@/utils/http'; // Đường dẫn cần chính xác
import AsyncStorage from '@react-native-async-storage/async-storage';
type Exam = {
    idTest: number;
    bai_test: string;
    ngayBD: string;
    ngayKT: string;
};

type ExamList = {
    approvedExams: Exam[];
    pendingExams: Exam[];
};
const TeacherClassExamDetailScreen = ({ navigation, route }: { navigation: any; route: any }) => {
    const { idLopHoc, tenLopHoc, role } = route.params;
    const [activeTab, setActiveTab] = useState('Bài thi');
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [examList, setExamList] = useState<ExamList>({
        approvedExams: [],
        pendingExams: [],
    });
    useEffect(() => {
        const fetchExams = async () => {
            setIsLoading(true);
            try {
                const token = await AsyncStorage.getItem('accessToken');
                if (!token) {
                    console.error('Error: Access token not found');
                    return;
                }

                const response = await http.get(`/baitest/getBaiTestofLop/${idLopHoc}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                const data = response.data;
                const approvedExams = data.filter((exam: any) => exam.xetDuyet === true);
                const pendingExams = data.filter((exam: any) => exam.xetDuyet === false);
                console.log('Approved Exams:', approvedExams);
                console.log('Pending Exams:', pendingExams);

                setExamList({ approvedExams, pendingExams });
            } catch (error) {
                console.error('Error fetching exams:', error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchExams();
    }, [idLopHoc]);
    const [isDeleteModalVisible, setDeleteModalVisible] = useState(false);
    const [examToDelete, setExamToDelete] = useState<number | null>(null);

    const openDeleteModal = (id: number) => {
        setExamToDelete(id);
        setDeleteModalVisible(true);
    };

    const handleDeleteExam = async () => {
        if (examToDelete === null) return;

        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }

            await http.get(`baitest/deleteBaiTest/${examToDelete}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setExamList((prev) => ({
                ...prev,
                pendingExams: prev.pendingExams.filter((exam) => exam.idTest !== examToDelete),
            }));
            setDeleteModalVisible(false);
            setExamToDelete(null);
        } catch (error) {
            console.error('Failed to delete exam:', error);
        }
    };


    const renderTabContent = () => {
        switch (activeTab) {
            case 'Bài thi':
                return (
                    <ScrollView style={styles.tabContent}>
                        <TouchableOpacity
                            style={styles.addButton}
                            onPress={() => navigation.navigate('AddExamScreen', { idLopHoc })}
                        >
                            <Icon name="plus" size={20} color="#fff" />
                            <Text style={styles.addButtonText}>Thêm bài thi</Text>
                        </TouchableOpacity>

                        <Text style={styles.sectionTitle}>Bài thi đã duyệt</Text>
                        {examList.approvedExams.length > 0 ? (
                            examList.approvedExams.map((exam: any) => (
                                <TouchableOpacity
                                    key={exam.idTest}
                                    style={styles.examItem}
                                    onPress={() => navigation.navigate('ExamDetailScreen', { idTest: exam.idTest, trangThai: exam.trangThai })}
                                >
                                    <Text style={styles.examTitle}>
                                        Loại bài thi: {exam.loaiTest === "CK" ? "Bài Cuối Kì" : exam.loaiTest === "GK" ? "Bài Giữa Kì" : exam.loaiTest}
                                    </Text>
                                    <Text style={styles.examInfo}>
                                        Thời gian làm bài: {exam.thoiGianLamBai.toString()} phút
                                    </Text>
                                    <Text style={styles.examInfo}>
                                        Ngày bắt đầu: {new Date(exam.ngayBD).toLocaleDateString("vi-VN")}  -  {new Date(exam.ngayBD).toLocaleString("vi-VN", { hour: '2-digit', minute: '2-digit', second: '2-digit' })}
                                    </Text>
                                    <Text style={styles.examInfo}>
                                        Ngày kết thúc: {new Date(exam.ngayKT).toLocaleDateString("vi-VN")}  -  {new Date(exam.ngayKT).toLocaleString("vi-VN", { hour: '2-digit', minute: '2-digit', second: '2-digit' })}
                                    </Text>
                                </TouchableOpacity>
                            ))
                        ) : (
                            <Text style={styles.emptyText}>Không có bài thi đã duyệt.</Text>
                        )}

                        <Text style={styles.sectionTitle}>Bài thi chưa duyệt</Text>
                        {examList.pendingExams.length > 0 ? (
                            examList.pendingExams.map((exam: any) => (
                                <View key={exam.idTest} style={styles.examItem}>
                                    <TouchableOpacity
                                        style={styles.examDetails}
                                        onPress={() => navigation.navigate('ExamDetailScreen', { idTest: exam.idTest })}
                                    >
                                        <Text style={styles.examTitle}>
                                            Loại bài thi: {exam.loaiTest === "CK" ? "Bài Cuối Kì" : exam.loaiTest === "GK" ? "Bài Giữa Kì" : exam.loaiTest}
                                        </Text>
                                        <Text style={styles.examInfo}>
                                            Thời gian làm bài: {exam.thoiGianLamBai.toString()} phút
                                        </Text>
                                        <Text style={styles.examInfo}>
                                            Ngày bắt đầu: {new Date(exam.ngayBD).toLocaleDateString("vi-VN")}  -  {new Date(exam.ngayBD).toLocaleString("vi-VN", { hour: '2-digit', minute: '2-digit', second: '2-digit' })}
                                        </Text>
                                        <Text style={styles.examInfo}>
                                            Ngày kết thúc: {new Date(exam.ngayKT).toLocaleDateString("vi-VN")}  -  {new Date(exam.ngayKT).toLocaleString("vi-VN", { hour: '2-digit', minute: '2-digit', second: '2-digit' })}
                                        </Text>
                                    </TouchableOpacity>
                                    <TouchableOpacity style={styles.deleteIcon} onPress={() => openDeleteModal(exam.idTest)}>
                                        <Icon name="delete" size={24} color="red" />
                                    </TouchableOpacity>
                                </View>
                            ))
                        ) : (
                            <Text style={styles.emptyText}>Không có bài thi chưa duyệt.</Text>
                        )}
                    </ScrollView>
                );
            case 'Điểm số':
                return (
                    <ScrollView style={styles.tabContent}>
                        <Text style={styles.tabText}>Danh sách điểm số sẽ hiển thị ở đây.</Text>
                    </ScrollView>
                );
            case 'Thông tin lớp học':
                return (
                    <ScrollView style={styles.tabContent}>
                        <Text style={styles.tabText}>Thông tin lớp học sẽ hiển thị ở đây.</Text>
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
                <Text style={styles.title}>Chi tiết Lớp Học</Text>

                <View style={styles.tabContainer}>
                    <TouchableOpacity
                        style={[styles.tabButton, activeTab === 'Bài thi' && styles.activeTab]}
                        onPress={() => setActiveTab('Bài thi')}
                    >
                        <Text style={[styles.tabButtonText, activeTab === 'Bài thi' && styles.activeTabText]}>
                            Bài thi
                        </Text>
                    </TouchableOpacity>
                    <TouchableOpacity
                        style={[styles.tabButton, activeTab === 'Điểm số' && styles.activeTab]}
                        onPress={() => setActiveTab('Điểm số')}
                    >
                        <Text style={[styles.tabButtonText, activeTab === 'Điểm số' && styles.activeTabText]}>
                            Điểm số
                        </Text>
                    </TouchableOpacity>
                    <TouchableOpacity
                        style={[styles.tabButton, activeTab === 'Thông tin lớp học' && styles.activeTab]}
                        onPress={() => setActiveTab('Thông tin lớp học')}
                    >
                        <Text style={[styles.tabButtonText, activeTab === 'Thông tin lớp học' && styles.activeTabText]}>
                            Thông tin lớp học
                        </Text>
                    </TouchableOpacity>
                </View>

                {isLoading ? <Text>Loading...</Text> : renderTabContent()}
            </View>
            <Modal visible={isDeleteModalVisible} transparent={true} animationType="slide">
                <View style={styles.modalOverlay}>
                    <View style={styles.modalContainer}>
                        <Text style={styles.modalText}>Bạn có chắc chắn muốn xóa bài thi này không?</Text>
                        <View style={styles.modalButtons}>
                            <TouchableOpacity onPress={() => setDeleteModalVisible(false)} style={styles.modalButtonCancel}>
                                <Text style={styles.modalButtonText}>Hủy</Text>
                            </TouchableOpacity>
                            <TouchableOpacity onPress={handleDeleteExam} style={styles.modalButtonDelete}>
                                <Text style={styles.modalButtonText}>Xóa</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>
            </Modal>
            
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
        justifyContent: 'space-around',
        backgroundColor: '#fff',
        borderBottomWidth: 1,
        borderBottomColor: '#ddd',
        marginBottom: 10,
    },
    tabButton: {
        paddingVertical: 10,
        paddingHorizontal: 20,
    },
    tabButtonText: {
        fontSize: 16,
        color: '#555',
    },
    activeTab: {
        borderBottomWidth: 3,
        borderBottomColor: '#ff6600',
    },
    activeTabText: {
        color: '#ff6600',
        fontWeight: 'bold',
    },
    tabContent: {
        flex: 1,
        padding: 20,
    },
    tabText: {
        fontSize: 16,
        color: '#333',
    },
    sectionTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#00405d',
        marginVertical: 15,
    },
    addButton: {
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: '#28a745',
        padding: 10,
        borderRadius: 5,
        marginBottom: 20,
        justifyContent: 'center',
    },
    addButtonText: {
        marginLeft: 10,
        color: '#fff',
        fontWeight: 'bold',
        fontSize: 16,
    },
    examItem: {
        padding: 15,
        borderWidth: 1,
        borderColor: '#ddd',
        borderRadius: 10,
        backgroundColor: '#f9f9f9',
        marginBottom: 15,
    },
    examTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#333',
        marginBottom: 5,
    },
    examInfo: {
        fontSize: 14,
        color: '#555',
        marginBottom: 3,
    },
    emptyText: {
        fontSize: 14,
        color: '#888',
        textAlign: 'center',
        marginTop: 10,
    },
    deleteIcon: {
        position: 'absolute',
        right: 10,
        top: 10,
    },
    examDetails: {
        flex: 1, 
        padding: 10,
        justifyContent: 'center', 
    },
    modalOverlay: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
    
    modalContainer: {
        width: '20%',
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
    
    modalText: {
        fontSize: 16,
        marginBottom: 20,
        textAlign: 'center',
    },
    
    modalButtons: {
        flexDirection: 'row',
        justifyContent: 'space-around',
        width: '100%',
    },
    
    modalButtonCancel: {
        width:120,
        backgroundColor: '#00405d',
        padding: 10,
        borderRadius: 5,
    },
    
    modalButtonDelete: {
        width:120,
        backgroundColor: 'red',
        padding: 10,
        borderRadius: 5,
    },
    
    modalButtonText: {
        textAlign:'center',
        color: '#fff',
        fontWeight: 'bold',
    },
});
export default TeacherClassExamDetailScreen;

import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, ImageBackground, ActivityIndicator, Alert } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as DocumentPicker from 'expo-document-picker';
// import AWS from 'aws-sdk';
// import { AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, AWS_REGION, AWS_BUCKET_NAME } from '@env';

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
interface DocumentPickerSuccessResult {
    type: 'success';
    uri: string;
    mimeType: string | null;
    name: string;
}
interface DocumentPickerCancelResult {
    type: 'cancel';
}
type DocumentPickerResult = DocumentPickerSuccessResult | DocumentPickerCancelResult;

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

    useEffect(() => {
        if (activeTab === 'Members') {
            fetchMembers();
        } else if (activeTab === 'ClassInfo') {
            fetchClassInfo();
        } else if (activeTab === 'Assignments') {
            fetchSessions();
        }
    }, [activeTab]);

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
            const response = await http.get(`/lopHoc/getByLop/${idLopHoc}`, {
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
            const response = await http.get(`/lopHoc/getLop/${idLopHoc}`, {
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
            const response = await http.get(`/buoihoc/getbuoiHocByLop/${idLopHoc}`, {
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
            const response = await http.get(`/baitap/getBaiTapofBuoi/${sessionId}`, {
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

    // const s3 = new AWS.S3({
    //     accessKeyId: AWS_ACCESS_KEY_ID,
    //     secretAccessKey: AWS_SECRET_ACCESS_KEY,
    //     region: AWS_REGION,
    // });

    // const uploadDocument = async (sessionId: any) => {
    //     try {
    //       const result = await DocumentPicker.getDocumentAsync({});
      
    //       if (result.type === 'cancel') {
    //         console.log("User cancelled the document picker.");
    //         Alert.alert('Thông báo', 'Bạn đã hủy việc chọn tài liệu.');
    //         return;
    //       }
      
    //       const asset = result.assets ? result.assets[0] : null;
      
    //       if (asset && asset.uri) {
    //         const response = await fetch(asset.uri);
    //         const blob = await response.blob();
      
    //         const params = {
    //           Bucket: AWS_BUCKET_NAME,
    //           Key: `uploads/${asset.name}`, // Tên file sẽ được lưu trên S3
    //           Body: blob,
    //           ContentType: asset.mimeType || 'application/octet-stream',
    //         };
      
    //         // Upload file lên S3
    //         s3.upload(params, async (err: any, data: { Location: any; }) => {
    //           if (err) {
    //             console.error('Error uploading file:', err);
    //             Alert.alert('Lỗi', 'Tải tài liệu lên thất bại.');
    //             return;
    //           }
              
    //           // Lấy đường dẫn file từ S3
    //           const fileUrl = data.Location;
      
    //           // Chuẩn bị dữ liệu để gửi đến server
    //           const formData = {
    //             tenTaiLieu: asset.name || 'unknown_file.pdf',
    //             noiDung: '',
    //             linkLoad: fileUrl, // Sử dụng URL từ S3
    //             ngayMo: new Date().toISOString(),
    //             ngayDong: new Date().toISOString(),
    //           };
      
    //           // Gửi thông tin tài liệu lên server
    //           const token = await AsyncStorage.getItem('accessToken');
    //           if (!token) {
    //             console.error('No token found');
    //             Alert.alert('Lỗi', 'Không tìm thấy token xác thực. Vui lòng đăng nhập lại.');
    //             return;
    //           }
      
    //           const responseUpload = await http.post(`/taiLieu/create/${sessionId}`, formData, {
    //             headers: {
    //               Authorization: `Bearer ${token}`,
    //             },
    //           });
      
    //           console.log('Document uploaded successfully:', responseUpload.data);
    //           Alert.alert('Thông báo', 'Tải tài liệu lên thành công!');
    //         });
    //       } else {
    //         console.error('No valid asset found');
    //         Alert.alert('Lỗi', 'Không tìm thấy tài liệu hợp lệ.');
    //       }
    //     } catch (error) {
    //       console.error('Failed to upload document:', error);
    //       Alert.alert('Lỗi', 'Tải tài liệu lên thất bại. Vui lòng thử lại.');
    //     }
    //   };
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
                                                    onPress={() => navigation.navigate('CreateAssignmentScreen', { idLopHoc, sessionId: session.idBuoiHoc })}
                                                >
                                                    <Text style={styles.createButtonText}>Thêm bài tập</Text>
                                                </TouchableOpacity>
                                                {/* <TouchableOpacity
                                                    style={styles.createButton}
                                                    onPress={() => uploadDocument(session.idBuoiHoc)}
                                                >
                                                    <Text style={styles.createButtonText}>Thêm tài liệu</Text>
                                                </TouchableOpacity> */}
                                            </View>
                                        )}
                                        {assignments[session.idBuoiHoc] && assignments[session.idBuoiHoc].length > 0 ? (
                                            <View style={styles.assignmentList}>
                                                {assignments[session.idBuoiHoc].map((assignment) => (
                                                    <TouchableOpacity
                                                        key={assignment.idBaiTap}
                                                        style={styles.assignmentItem}
                                                        onPress={() => navigation.navigate('AssignmentDetailScreen', { assignmentId: assignment.idBaiTap })}
                                                    >
                                                        <Icon name="file-document" size={20} color="#ff6600" />
                                                        <Text style={styles.assignmentText}>
                                                            {assignment.tenBaiTap}
                                                        </Text>
                                                    </TouchableOpacity>
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
    assignmentItem: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingVertical: 5,
    },
    assignmentText: {
        fontSize: 16,
        color: '#333',
        paddingLeft: 10,
    },
});

export default TeacherClassDetailScreen;

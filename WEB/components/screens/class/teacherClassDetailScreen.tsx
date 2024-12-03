import React, { useState, useEffect, useCallback } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, ImageBackground, ActivityIndicator, Modal } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useFocusEffect } from '@react-navigation/native';
import { deleteFileFromS3, uploadFileToS3 } from '../client/s3Client';
import * as DocumentPicker from 'expo-document-picker';
import { Linking } from 'react-native';
import { AWSConfig } from '@/config/AWSConfig';

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
interface Document {
    idTaiLieu: number;
    tenTaiLieu: string;
    linkLoad: string;
    trangThai: boolean;
    sessionId: number;
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
    const [documents, setDocuments] = useState<Document[]>([]);
    const [confirmDocumentModalVisible, setConfirmDocumentModalVisible] = useState(false);
    const [selectedDocument, setSelectedDocument] = useState<{ fileName: string; fileUri: string } | null>(null);
    const [messageModalVisible, setMessageModalVisible] = useState(false);
    const [messageText, setMessageText] = useState('');
    const [selectedSessionId, setSelectedSessionId] = useState<number | null>(null);
    const [confirmDeleteModalVisible, setConfirmDeleteModalVisible] = useState(false);
    const [selectedDocument2, setSelectedDocument2] = useState<{ idTaiLieu: number; sessionId: number; linkLoad: string } | null>(null);

    useFocusEffect(
        useCallback(() => {
            if (activeTab === 'Assignments') {
                if (sessions.length === 0) {
                    fetchSessions();
                }
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

    useEffect(() => {
        if (activeTab === 'Documents' && sessions.length > 0) {
            const sessionsToFetch = sessions.filter(
                (session) => session.idBuoiHoc && !documents.some((doc) => doc.sessionId === session.idBuoiHoc)
            );
            if (sessionsToFetch.length > 0) {
                sessionsToFetch.forEach((session) => {
                    fetchDocuments(session.idBuoiHoc);
                });
            }
        }
    }, [sessions, activeTab]);
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
    const fetchDocuments = async (sessionId: number) => {
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            const response = await http.get(`taiLieu/getTaiLieuByBuoi/${sessionId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            const fetchedDocuments = response.data;

            setDocuments((prevDocuments) => [
                ...prevDocuments,
                ...fetchedDocuments.map((doc: Document) => ({
                    ...doc,
                    sessionId, // Thêm ID buổi học
                })),
            ]);
        } catch (error) {
            console.error(`Failed to fetch documents for session ${sessionId}:`, error);
        }
    };
    const pickDocumentFile = async (sessionId: number) => {
        try {
            const result = await DocumentPicker.getDocumentAsync({
                type: ['application/pdf'],
                copyToCacheDirectory: true,
            });

            if (!result.canceled) {
                const fileUri = result.assets[0].uri;
                const fileName = result.assets[0].name;

                setSelectedDocument({ fileName, fileUri });
                setSelectedSessionId(sessionId);
                setConfirmDocumentModalVisible(true);
            } else {
                console.log('User canceled the picker.');
            }
        } catch (error) {
            console.error('Error picking document file:', error);
        }
    };
    
    const confirmDocumentUpload = async (
        tenTaiLieu: string,
        linkLoad: string,
        trangThai: boolean,
        sessionId: number
    ) => {
        try {
            if (!tenTaiLieu || !linkLoad || sessionId === undefined || sessionId === null) {
                throw new Error("Missing required parameters for newDocument");
            }
            const newDocument = {
                tenTaiLieu,
                linkLoad,
                trangThai,
                sessionId,
                isNew: true,
            };
            await handleSubmitDocuments([newDocument], sessionId);
            setConfirmDocumentModalVisible(false); 
            setSelectedDocument(null); 
        } catch (error) {
            console.error("Error confirming document upload:", error);
            setMessageText("Lỗi: Không thể thêm tài liệu. Vui lòng thử lại.");
            setMessageModalVisible(true);
        }
    };
    
    
    const handleSubmitDocuments = async (documentsToUpload: any[], sessionId: number) => {
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                setMessageText('Lỗi: Không tìm thấy token xác thực. Vui lòng đăng nhập lại.');
                setMessageModalVisible(true);
                return;
            }
    
            // Lọc các tài liệu mới cần upload
            const newDocuments = documentsToUpload.filter((doc) => doc.isNew);
            // Xóa toàn bộ tài liệu hiện tại trong documents trước khi gọi fetch
            setDocuments([]);
    
            for (const document of newDocuments) {
                if (!document.linkLoad) continue;
    
                // Kiểm tra nếu tài liệu đã tồn tại trong documents hiện tại
                const documentExists = documents.some(
                    (doc) => doc.tenTaiLieu === document.tenTaiLieu && doc.linkLoad === document.linkLoad
                );
                if (documentExists) {
                    console.log(`Document ${document.tenTaiLieu} already exists.`);
                    continue;
                }
    
                try {
                    // Upload file lên S3
                    const fileBlob = await fetch(document.linkLoad).then((res) => res.blob());
                    const s3Key = `documents/${document.tenTaiLieu}`;
                    const uploadedLink = await uploadFileToS3(fileBlob, s3Key);
    
                    if (!uploadedLink) {
                        console.error(`Failed to upload document: ${document.tenTaiLieu}`);
                        continue;
                    }
    
                    const documentData = {
                        tenTaiLieu: document.tenTaiLieu,
                        linkLoad: uploadedLink,
                        trangThai: document.trangThai,
                    };
    
                    // Tạo tài liệu trên server
                    const response = await http.post(`taiLieu/create/${sessionId}`, documentData, {
                        headers: { Authorization: `Bearer ${token}` },
                    });
    
                    if (response.status === 200) {
                        document.isNew = false;
                        document.linkLoad = uploadedLink;
                        setMessageText('Thêm tài liệu thành công.');
                    } else {
                        console.error('Failed to create document on the server');
                    }
    
                } catch (uploadError) {
                    console.error(`Error uploading or saving document: ${document.tenTaiLieu}`, uploadError);
                }
            }
            await fetchDocuments(sessionId);
            setMessageModalVisible(true);
    
        } catch (error) {
            console.error('Error while submitting documents:', error);
            setMessageText('Lỗi: Không thể thêm tài liệu. Vui lòng thử lại.');
            setMessageModalVisible(true);
        }
    };

    const handleDeleteDocument = async () => {
        if (!selectedDocument2 || !selectedDocument2.idTaiLieu) {
            console.error('Invalid idTaiLieu:', selectedDocument2?.idTaiLieu);
            setMessageText('Lỗi: Không tìm thấy ID tài liệu. Vui lòng thử lại.');
            setMessageModalVisible(true);
            return;
        }
    
        const { idTaiLieu, sessionId, linkLoad } = selectedDocument2;
    
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                setMessageText('Lỗi: Không tìm thấy token xác thực. Vui lòng đăng nhập lại.');
                setMessageModalVisible(true);
                return;
            }
    
            if (linkLoad) {
                const fileKey = linkLoad.split('.com/')[1]; // Tách key từ linkLoad
                if (fileKey) {
                    const deleteResult = await deleteFileFromS3(fileKey); // Xóa file trên S3
                    if (!deleteResult) {
                        console.error('Failed to delete file from S3');
                        setMessageText('Lỗi: Không thể xóa tài liệu trên S3. Vui lòng thử lại.');
                        setMessageModalVisible(true);
                        return; // Dừng lại nếu xóa file trên S3 thất bại
                    }
                } else {
                    console.error('Invalid file key extracted from linkLoad:', linkLoad);
                    return;
                }
            }
    
            const response = await http.post(
                `taiLieu/update/${sessionId}/${idTaiLieu}`,
                { trangThai: false },
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );
    
            if (response.status === 200) {
                // Cập nhật trực tiếp lại state documents sau khi xóa thành công
                setDocuments((prevDocuments) =>
                    prevDocuments.filter((doc) => doc.idTaiLieu !== idTaiLieu)
                );
                setMessageText('Xóa tài liệu thành công.');
            } else {
                console.error('Failed to update document status in the database.');
            }
        } catch (error) {
            console.error('Error deleting document:', error);
            setMessageText('Lỗi: Không thể xóa tài liệu. Vui lòng thử lại.');
        } finally {
            setMessageModalVisible(true);
            setConfirmDeleteModalVisible(false);
            setSelectedDocument(null);
        }
    };
    const generateFileUrl = (key: string): string => {
        const bucketName = AWSConfig.bucketName;
        const region = AWSConfig.region;
        return `https://${bucketName}.s3.${region}.amazonaws.com/${key}`;
    };

    const handleOpenDocument = (key: string) => {
        try {
            const url = generateFileUrl(key); // Tạo URL đầy đủ từ Key
            window.open(url, '_blank'); // Mở tài liệu trong tab mới
        } catch (error) {
            console.error('Error opening document:', error);
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
                                                    onPress={() => navigation.navigate('AddAssignmentScreen', { idLopHoc, sessionId: session.idBuoiHoc, tenLopHoc, role, })}
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
                                                            onPress={() => navigation.navigate('AssignmentDetailScreen', { assignmentId: assignment.idBaiTap, sessionId: session.idBuoiHoc })}
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
            case 'Documents':
                const confirmDeleteDocument = (idTaiLieu: number, sessionId: number, linkLoad: string) => {
                    setSelectedDocument2({
                        idTaiLieu,
                        sessionId,
                        linkLoad,
                    });
                    setConfirmDeleteModalVisible(true);
                };

                return (
                    <ScrollView style={styles.contentContainer}>
                        <Text style={styles.sectionTitle}>Tài Liệu</Text>
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
                                                    onPress={() => pickDocumentFile(session.idBuoiHoc)}
                                                >
                                                    <Text style={styles.createButtonText}>Thêm tài liệu</Text>
                                                </TouchableOpacity>
                                            </View>
                                        )}
                                        {documents.filter((doc) => doc.sessionId === session.idBuoiHoc && doc.trangThai).length > 0 ? (
                                            <View style={styles.assignmentList}>
                                                {documents
                                                    .filter((doc) => doc.trangThai && doc.sessionId === session.idBuoiHoc)
                                                    .map((doc, index) => (
                                                        <View key={index} style={styles.assignmentItemContainer}>
                                                            <TouchableOpacity style={styles.assignmentItem}>
                                                                <Icon name="file-document-outline" size={20} color="#00405d" />
                                                                <Text
                                                                    style={styles.assignmentText}
                                                                    onPress={() => handleOpenDocument(doc.linkLoad)} // Gọi hàm với Key
                                                                >
                                                                    {doc.tenTaiLieu}
                                                                </Text>
                                                            </TouchableOpacity>
                                                            <TouchableOpacity
                                                                onPress={() => confirmDeleteDocument(doc.idTaiLieu, session.idBuoiHoc, doc.linkLoad)}
                                                            >
                                                                <Icon name="delete" size={20} color="red" />
                                                            </TouchableOpacity>
                                                        </View>
                                                    ))}
                                            </View>
                                        ) : (
                                            <Text>Không có tài liệu nào.</Text>
                                        )}
                                    </View>
                                ))
                            ) : (
                                <Text>Không có buổi học nào.</Text>
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
                    <TouchableOpacity style={[styles.tabButton, activeTab === 'Documents' && styles.activeTab]} onPress={() => setActiveTab('Documents')}>
                        <Text style={[styles.tabText, activeTab === 'Documents' && styles.activeTabText]}>Tài liệu</Text>
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
                <Modal
                    visible={confirmDocumentModalVisible}
                    transparent
                    animationType="slide"
                    onRequestClose={() => setConfirmDocumentModalVisible(false)}
                >
                    <View style={styles.modalOverlay}>
                        <View style={styles.modalContainer}>
                            <Text style={styles.modalTitle}>Xác nhận tải tài liệu</Text>
                            {selectedDocument && (
                                <Text style={styles.modalMessage}>
                                    Bạn có chắc chắn muốn tải tài liệu "{selectedDocument.fileName}" lên?
                                </Text>
                            )}
                            <View style={styles.modalButtons}>
                                <TouchableOpacity
                                    onPress={() => setConfirmDocumentModalVisible(false)}
                                    style={styles.cancelButton}
                                >
                                    <Text style={styles.modalButtonText}>Hủy</Text>
                                </TouchableOpacity>
                                <TouchableOpacity
                                    onPress={() => {
                                        if (selectedDocument && selectedSessionId) {
                                            confirmDocumentUpload(
                                                selectedDocument.fileName,
                                                selectedDocument.fileUri,
                                                true,
                                                selectedSessionId
                                            );
                                        } else {
                                            console.error('No document or session ID selected');
                                        }
                                    }}
                                    style={styles.confirmButton}
                                >
                                    <Text style={styles.modalButtonText}>Xác nhận</Text>
                                </TouchableOpacity>
                            </View>
                        </View>
                    </View>
                </Modal>
                <Modal
                    visible={messageModalVisible}
                    transparent
                    animationType="fade"
                    onRequestClose={() => setMessageModalVisible(false)}
                >
                    <View style={styles.modalOverlay}>
                        <View style={styles.modalContainer}>
                            <Text style={styles.modalMessage}>{messageText}</Text>
                            <TouchableOpacity
                                onPress={() => setMessageModalVisible(false)}
                                style={styles.closeButton}
                            >
                                <Text style={styles.closeButtonText}>Đóng</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </Modal>
                <Modal
                    visible={confirmDeleteModalVisible}
                    transparent
                    animationType="fade"
                    onRequestClose={() => setConfirmDeleteModalVisible(false)}
                >
                    <View style={styles.modalOverlay}>
                        <View style={styles.modalContainer}>
                            <Text style={styles.modalMessage}>Bạn có chắc chắn muốn xóa tài liệu này?</Text>
                            <View style={styles.modalButtons}>
                                <TouchableOpacity
                                    onPress={() => setConfirmDeleteModalVisible(false)}
                                    style={styles.cancelButton}
                                >
                                    <Text style={styles.cancelButtonText}>Hủy</Text>
                                </TouchableOpacity>
                                <TouchableOpacity
                                    onPress={handleDeleteDocument}
                                    style={styles.confirmButton}
                                >
                                    <Text style={styles.confirmButtonText}>Xác nhận</Text>
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
    closeButton: {
        backgroundColor: '#28a745',
        padding: 10,
        borderRadius: 5,
        alignItems: 'center',
        width: 100,
    },
    closeButtonText: {
        color: '#fff',
        fontWeight: 'bold',
        textAlign: 'center',
    },
    cancelButtonText: {
        color: "#000",
        fontWeight: "bold",
    },
    confirmButtonText: {
        color: "#fff",
        fontWeight: "bold",
    },
});

export default TeacherClassDetailScreen;

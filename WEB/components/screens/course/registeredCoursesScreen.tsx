import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, ImageBackground, TouchableOpacity, ActivityIndicator, ScrollView } from 'react-native';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface ClassInfo {
    idLopHoc: number;
    tenLopHoc: string;
    trangThai: string;
    ngayBD: string;
    ngayKT: string;
    moTa: string;
    giangVien: {
        hoTen: string;
    };
    khoaHoc: {
        tenKhoaHoc: string;
    };
}

export default function RegisteredCoursesScreen({ navigation, route }: { navigation: any; route: any }) {
    const [registeredCourses, setRegisteredCourses] = useState<ClassInfo[]>([]);
    const [isLoading, setIsLoading] = useState(true); 
    const { idUser,nameUser } = route.params;

    const fetchRegisteredCourses = async () => {
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            const response = await http.get(`/hocvien/getByHV/${idUser}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setRegisteredCourses(response.data);
        } catch (error) {
            console.error('Failed to fetch registered courses:', error);
        } finally {
            setIsLoading(false); 
        }
    };

    const formatDate = (dateString: string) => {
        const date = new Date(dateString);
        return date.toLocaleDateString('vi-VN');
    };

    const formatStatus = (status: string) => {
        switch (status) {
            case 'FULL':
                return 'Lớp đã mở';
            case 'READY':
                return 'Đang chờ mở lớp';
            default:
                return status;
        }
    };

    useEffect(() => {
        fetchRegisteredCourses();
    }, []);

    return (
        <ImageBackground
            source={require('../../../image/bglogin.png')}
            style={styles.background}
            resizeMode="cover"
        >
            <View style={styles.overlayContainer}>
                <View style={styles.headerRow}>
                    <TouchableOpacity style={styles.backButton} onPress={() => navigation.goBack()}>
                        <Text style={styles.backButtonText}>Quay về</Text>
                    </TouchableOpacity>
                    <TouchableOpacity
                        style={styles.registerButton}
                        onPress={() => navigation.navigate('CourseRegistrationScreen', { idUser, nameUser })}
                    >
                        <Text style={styles.registerButtonText}>Đăng ký khóa học</Text>
                    </TouchableOpacity>
                </View>

                <Text style={styles.title}>Khóa học đã đăng ký</Text>

                {isLoading ? (
                    <ActivityIndicator size="large" color="#00405d" />
                ) : registeredCourses.length > 0 ? (
                    <ScrollView style={styles.scrollViewContainer}>
                        <ScrollView horizontal style={styles.tableContainer}>
                            <View>
                                <View style={styles.tableHeader}>
                                    <Text style={styles.tableHeaderText}>STT</Text>
                                    <Text style={styles.tableHeaderText}>Mã lớp</Text>
                                    <Text style={styles.tableHeaderText}>Tên khóa học</Text>
                                    <Text style={styles.tableHeaderText}>Lớp học</Text>
                                    <Text style={styles.tableHeaderText}>Ngày bắt đầu</Text>
                                    <Text style={styles.tableHeaderText}>Ngày kết thúc</Text>
                                    <Text style={styles.tableHeaderText}>Lịch học</Text>
                                    <Text style={styles.tableHeaderText}>Giảng viên</Text>
                                    <Text style={styles.tableHeaderText}>Trạng thái</Text>
                                </View>
                                {registeredCourses.map((item, index) => (
                                    <View key={item.idLopHoc} style={styles.tableRow}>
                                        <Text style={styles.tableCell}>{index + 1}</Text>
                                        <Text style={styles.tableCell}>{item.idLopHoc}</Text>
                                        <Text style={styles.tableCell}>{item.khoaHoc.tenKhoaHoc}</Text>
                                        <Text style={styles.tableCell}>{item.tenLopHoc}</Text>
                                        <Text style={styles.tableCell}>{formatDate(item.ngayBD)}</Text>
                                        <Text style={styles.tableCell}>{formatDate(item.ngayKT)}</Text>
                                        <Text style={styles.tableCell}>{item.moTa}</Text>
                                        <Text style={styles.tableCell}>{item.giangVien.hoTen}</Text>
                                        <Text style={styles.tableCell}>{formatStatus(item.trangThai)}</Text>
                                    </View>
                                ))}
                            </View>
                        </ScrollView>
                    </ScrollView>
                ) : (
                    <Text style={styles.noCoursesText}>Bạn chưa đăng ký khóa học nào</Text>
                )}
            </View>
        </ImageBackground>
    );
}

const styles = StyleSheet.create({
    background: {
        flex: 1,
        paddingHorizontal: 400,
        height: 990
    },
    overlayContainer: {
        flex: 1,
        padding: 20,
        backgroundColor: 'rgba(255, 255, 255, 0.9)',
        borderRadius: 15,
        alignItems: 'center',
    },
    headerRow: {
        flexDirection: 'row',
        justifyContent: 'flex-start',
        width: '100%',
        marginBottom: 10,
    },
    backButton: {
        padding: 10,
        backgroundColor: '#00405d',
        borderRadius: 5,
        marginRight: 20,
    },
    backButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
    registerButton: {
        padding: 10,
        backgroundColor: '#28a745',
        borderRadius: 5,
    },
    registerButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#00405d',
        marginBottom: 20,
    },
    scrollViewContainer: {
        flex: 1,
        width: '100%',
    },
    tableContainer: {
        flex: 1,
        marginTop: 10,
        width: '100%',
    },
    tableHeader: {
        flexDirection: 'row',
        backgroundColor: '#00405d',
        paddingVertical: 10,
        paddingHorizontal: 5,
    },
    tableHeaderText: {
        fontWeight: 'bold',
        color: '#fff',
        width: 120,
        textAlign: 'center',
    },
    tableRow: {
        flexDirection: 'row',
        borderBottomWidth: 1,
        borderBottomColor: '#ddd',
        paddingVertical: 10,
        paddingHorizontal: 5,
    },
    tableCell: {
        width: 120,
        textAlign: 'center',
        color: '#333',
    },
    noCoursesText: {
        fontSize: 16,
        color: 'grey',
        fontStyle: 'italic',
        marginTop: 20,
    },
});

import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, FlatList, ImageBackground, TouchableOpacity, ActivityIndicator } from 'react-native';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface ClassInfo {
    idLopHoc: number;
    tenLopHoc: string;
    trangThai: string;
    ngayBD: string;
    ngayKT: string;
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
    const { idUser, nameUser } = route.params;

    const fetchRegisteredCourses = async () => {
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                console.error('No token found');
                return;
            }
            const response = await http.get(`/lopHoc/getLop/${idUser}`, {
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

    useEffect(() => {
        fetchRegisteredCourses();
    }, []);

    const renderCourseItem = ({ item }: { item: ClassInfo }) => (
        <View style={styles.courseItem}>
            <Text style={styles.courseTitle}>Lớp: {item.tenLopHoc}</Text>
            <Text style={styles.courseDetail}>Trạng thái: {item.trangThai}</Text>
            <Text style={styles.courseDetail}>Ngày bắt đầu: {item.ngayBD}</Text>
            <Text style={styles.courseDetail}>Ngày kết thúc: {item.ngayKT}</Text>
            <Text style={styles.courseDetail}>Giảng viên: {item.giangVien.hoTen}</Text>
            <Text style={styles.courseDetail}>Khóa học: {item.khoaHoc.tenKhoaHoc}</Text>
        </View>
    );

    return (
        <ImageBackground
            source={require('../../../image/bglogin.png')}
            style={styles.background}
            resizeMode="cover"
        >
            <View style={styles.overlayContainer}>
                <TouchableOpacity style={styles.backButton} onPress={() => navigation.goBack()}>
                    <Text style={styles.backButtonText}>Quay về</Text>
                </TouchableOpacity>
                <Text style={styles.title}>Khóa học đã đăng ký</Text>
                {isLoading ? (
                    // Hiển thị vòng quay tải trong khi dữ liệu đang được tải
                    <ActivityIndicator size="large" color="#00405d" />
                ) : registeredCourses.length > 0 ? (
                    <FlatList
                        data={registeredCourses}
                        renderItem={renderCourseItem}
                        keyExtractor={(item) => item.idLopHoc.toString()}
                        contentContainerStyle={styles.listContainer}
                    />
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
    backButton: {
        alignSelf: 'flex-start',
        padding: 10,
        backgroundColor: '#00405d',
        borderRadius: 5,
        marginBottom: 20,
    },
    backButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#00405d',
        marginBottom: 20,
    },
    listContainer: {
        paddingHorizontal: 20,
    },
    courseItem: {
        padding: 15,
        backgroundColor: '#f0f0f0',
        borderRadius: 8,
        marginBottom: 15,
        width: '90%',
    },
    courseTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#00405d',
    },
    courseDetail: {
        fontSize: 14,
        color: '#333',
    },
    noCoursesText: {
        fontSize: 16,
        color: 'grey',
        fontStyle: 'italic',
        marginTop: 20,
    },
});

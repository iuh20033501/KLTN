import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, Image, ImageBackground, TouchableOpacity, ActivityIndicator, FlatList } from 'react-native';
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
        image: string | undefined;
        tenKhoaHoc: string;
    };
}

export default function ResultStudentScreen({ navigation, route }: { navigation: any; route: any }) {
    const [classes, setClasses] = useState<ClassInfo[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const { idUser, role,nameUser} = route.params;

    const fetchClasses = async () => {
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
            setClasses(response.data);
            console.log(response.data);
        } catch (error) {
            console.error('Failed to fetch classes:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const getImageUri = (imageData: string | undefined) => {
        if (!imageData) return require('../../../image/efy.png'); 
        if (imageData.startsWith("data:image")) return { uri: imageData };
        
        const defaultMimeType = "image/png";
        let mimeType = defaultMimeType;

        if (/^\/9j/.test(imageData)) mimeType = "image/jpeg";
        else if (/^iVBOR/.test(imageData)) mimeType = "image/png";
        else if (/^R0lGOD/.test(imageData)) mimeType = "image/gif";
        else if (/^Qk/.test(imageData)) mimeType = "image/bmp";
        else if (/^UklGR/.test(imageData)) mimeType = "image/webp";

        return { uri: `data:${mimeType};base64,${imageData}` };
    };

    const renderClassCard = ({ item }: { item: ClassInfo }) => (
        <TouchableOpacity style={styles.card}   onPress={() => navigation.navigate('ResultStudentDetailScreen', {
            idUser, role, nameUser,
            idLopHoc: item.idLopHoc,
            tenLopHoc: item.tenLopHoc,
            tenKhoaHoc: item.khoaHoc.tenKhoaHoc,
        })}>
            <Image source={getImageUri(item.khoaHoc.image)} style={styles.classImage} />
            <View style={styles.cardHeader}>
                <Text style={styles.classTitle}>{item.tenLopHoc}</Text>
                <Text style={styles.courseName}>{item.khoaHoc.tenKhoaHoc}</Text>
            </View>
        </TouchableOpacity>
    );

    useEffect(() => {
        fetchClasses();
    }, []);

    return (
        <ImageBackground source={require('../../../image/bglogin.png')} style={styles.background}>
            <View style={styles.overlayContainer}>
                <View style={styles.headerRow}>
                    <TouchableOpacity style={styles.backButton} onPress={() => navigation.goBack()}>
                        <Text style={styles.backButtonText}>Quay về</Text>
                    </TouchableOpacity>
                </View>
                <Text style={styles.title}>Danh sách lớp học</Text>

                {isLoading ? (
                    <ActivityIndicator size="large" color="#00405d" />
                ) : (
                    <FlatList
                        data={classes}
                        renderItem={renderClassCard}
                        keyExtractor={(item) => item.idLopHoc.toString()}
                        numColumns={3}
                        contentContainerStyle={styles.cardContainer}
                    />
                )}
            </View>
        </ImageBackground>
    );
}

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
        justifyContent: 'space-between',
        marginBottom: 10,
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#00405d',
        textAlign: 'center',
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
    cardContainer: {
        marginLeft:25,
        paddingBottom: 100,
    },
    card: {
        backgroundColor: '#fff',
        borderRadius: 8,
        margin: 10,
        padding: 15,
        width: 'auto',
        shadowColor: '#000',
        shadowOpacity: 0.1,
        shadowOffset: { width: 0, height: 2 },
        shadowRadius: 4,
        elevation: 3,
    },
    classImage: {
        width: 280,
        height: 100,
        borderRadius: 8,
        marginBottom: 10,
    },
    cardHeader: {
        marginBottom: 10,
    },
    classTitle: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#333',
    },
    courseName: {
        fontSize: 14,
        color: '#777',
    },
});
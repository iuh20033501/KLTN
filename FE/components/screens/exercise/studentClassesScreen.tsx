import React, { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Image, ActivityIndicator, FlatList } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import { LinearGradient } from 'expo-linear-gradient';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface ClassInfo {
    idLopHoc: number;
    tenLopHoc: string;
    khoaHoc: {
        image: string | undefined;
        tenKhoaHoc: string;
    };
}

export default function StudentClassesScreen({ navigation, route }: { navigation: any; route: any }) {
    const [classes, setClasses] = useState<ClassInfo[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const { idUser } = route.params;

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
        } catch (error) {
            console.error('Failed to fetch classes:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const getImageUri = (imageData: string | undefined) => {
        if (!imageData) return require('../../../image/background/efy.png'); 
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

    useEffect(() => {
        fetchClasses();
    }, []);

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Icon name="arrow-back-outline" size={24} color="black" onPress={() => navigation.goBack()} />
                <Text style={styles.headerText}>Bài tập rèn luyện</Text>
                <Icon name="star-outline" size={24} color="gold" style={styles.starIcon} />
            </View>

            {isLoading ? (
                <ActivityIndicator size="large" color="#00405d" />
            ) : (
                <FlatList
                    data={classes}
                    renderItem={({ item }) => (
                        <TouchableOpacity style={styles.exerciseButton} onPress={() => navigation.navigate('LessonListScreen', { idLopHoc: item.idLopHoc })}>
                            <LinearGradient colors={['#4c669f', '#3b5998', '#192f6a']} style={styles.gradientButton}>
                                <Image source={getImageUri(item.khoaHoc.image)} style={styles.classImage} />
                                <Text style={styles.exerciseText}>{item.tenLopHoc}</Text>
                            </LinearGradient>
                        </TouchableOpacity>
                    )}
                    keyExtractor={(item) => item.idLopHoc.toString()}
                />
            )}
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        padding: 16,
    },
    header: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        paddingHorizontal: 16,
        marginBottom: 20,
    },
    headerText: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#00bf63',
        textAlign: 'center',
    },
    starIcon: {
        marginRight: 10,
    },
    exerciseButton: {
        marginBottom: 12,
        borderRadius: 8,
    },
    gradientButton: {
        flexDirection: 'row',
        alignItems: 'center',
        padding: 20,
        borderRadius: 8,
    },
    exerciseText: {
        fontSize: 18,
        color: '#fff',
        marginLeft: 10,
        fontWeight: '600',
    },
    classImage: {
        width: 50,
        height: 50,
        borderRadius: 25,
        marginRight: 10,
    },
});

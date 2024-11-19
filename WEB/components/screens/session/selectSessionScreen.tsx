import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ActivityIndicator, ScrollView, ImageBackground } from 'react-native';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';

const SelectSessionScreen = ({ navigation, route }: { navigation: any, route: any }) => {
    const { idLopHoc } = route.params;
    const [sessions, setSessions] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        fetchSessions();
    }, []);

    const fetchSessions = async () => {
        setIsLoading(true);
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
            console.log(response.data);
        } catch (error) {
            console.error('Failed to fetch sessions:', error);
        } finally {
            setIsLoading(false);
        }
    };
    
    const handleSessionSelect = (sessionId: number) => {
        navigation.navigate('CreateAssignmentScreen', { idLopHoc, sessionId });
    };

    return (
        <ImageBackground source={require('../../../image/bglogin.png')} style={styles.background}>
            <View style={styles.overlayContainer}>
                <TouchableOpacity style={styles.backButton} onPress={() => navigation.goBack()}>
                    <Text style={styles.backButtonText}>Quay lại</Text>
                </TouchableOpacity>
                
                <Text style={styles.title}>Chọn Buổi Học</Text>

                {isLoading ? (
                    <ActivityIndicator size="large" color="#00405d" />
                ) : (
                    <ScrollView>
                        {sessions.length > 0 ? (
                            sessions.map((session: any) => (
                                <TouchableOpacity
                                    key={session.id}
                                    style={styles.sessionButton}
                                    onPress={() => handleSessionSelect(session.id)}
                                >
                                    <Text style={styles.sessionText}>{session.chuDe}</Text>
                                </TouchableOpacity>
                            ))
                        ) : (
                            <Text>Không có buổi học nào.</Text>
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
    title: {
        fontSize: 20,
        fontWeight: 'bold',
        color: '#00405d',
        textAlign: 'center',
        marginBottom: 20,
    },
    backButton: {
        padding: 10,
        backgroundColor: '#00405d',
        borderRadius: 5,
        alignSelf: 'flex-start',
        marginBottom: 20,
    },
    backButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
    sessionButton: {
        padding: 15,
        backgroundColor: '#e0f7e9',
        borderRadius: 5,
        marginBottom: 10,
    },
    sessionText: {
        fontSize: 16,
        color: '#333',
    },
});

export default SelectSessionScreen;

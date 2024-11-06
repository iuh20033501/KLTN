import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, ImageBackground, ActivityIndicator } from 'react-native';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface MemberInfo {
    idHocVien: number;
    hoTen: string;
    email: string;
}

const ClassDetailScreen = ({ navigation, route }: { navigation: any, route: any }) => {
    const { idLopHoc, className } = route.params;
    const [activeTab, setActiveTab] = useState('Assignments');
    const [members, setMembers] = useState<MemberInfo[]>([]);
    const [isLoadingMembers, setIsLoadingMembers] = useState(false);

    useEffect(() => {
        if (activeTab === 'Members') {
            fetchMembers();
        }
    }, [activeTab]);

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
            console.log(response.data)
        } catch (error) {
            console.error('Failed to fetch members:', error);
        } finally {
            setIsLoadingMembers(false);
        }
    };

    const renderContent = () => {
        switch (activeTab) {
            case 'Assignments':
                return (
                    <ScrollView style={styles.contentContainer}>
                        <Text style={styles.sectionTitle}>Bài Tập</Text>
                        {/* Nội dung bài tập */}
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
                    members.map((member, index) => (
                        <View key={member.idHocVien} style={styles.memberCard}>
                            <Text style={styles.memberName}>{index + 1}. {member.hoTen}</Text>
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
                        {/* Nội dung điểm số */}
                    </ScrollView>
                );
            case 'ClassInfo':
                return (
                    <ScrollView style={styles.contentContainer}>
                        <Text style={styles.sectionTitle}>Thông Tin Lớp Học</Text>
                        {/* Nội dung thông tin lớp học */}
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
                    <Text style={styles.title}>{className}</Text>
                </View>
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
        justifyContent: 'space-between',
        marginBottom: 10,
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#ff6600',
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
});

export default ClassDetailScreen;

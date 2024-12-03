import React, { useState } from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity, ImageBackground } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';

const TeacherClassExamDetailScreen = ({ navigation }: { navigation: any }) => {
    const [activeTab, setActiveTab] = useState('Bài thi');

    const renderTabContent = () => {
        switch (activeTab) {
            case 'Bài thi':
                return (
                    <ScrollView style={styles.tabContent}>
                        <Text style={styles.tabText}>Danh sách bài thi sẽ hiển thị ở đây.</Text>
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

                {renderTabContent()}
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
});

export default TeacherClassExamDetailScreen;

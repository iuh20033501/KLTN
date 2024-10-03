import { View, Text, TouchableOpacity, StyleSheet, ScrollView, Image, Modal } from 'react-native';
import React, { useState } from 'react';
import { FontAwesome, Ionicons, MaterialIcons, Entypo,Feather,AntDesign } from '@expo/vector-icons';  

export default function UserProfileScreen({navigation}: {navigation: any}) {
    const avatarIMG = require('../../../image/avatar/1.png');

    const [selectedAvatar, setSelectedAvatar] = useState(avatarIMG);
    const [isModalVisible, setModalVisible] = useState(false);
    const avatars = [
        require('../../../image/avatar/1.png'),
        require('../../../image/avatar/2.png'),
        require('../../../image/avatar/3.png'),
        require('../../../image/avatar/4.png'),
        require('../../../image/avatar/5.png'),
        require('../../../image/avatar/6.png'),
        require('../../../image/avatar/7.png'),
        require('../../../image/avatar/8.png'),
    ];

    const handleAvatarPress = () => {
        setModalVisible(true); 
    };

    const handleAvatarSelect = (avatar: any) => {
        setSelectedAvatar(avatar); 
        setModalVisible(false); 
    };

    const renderAvatarRows = () => {
        const rows = [];
        for (let i = 0; i < avatars.length; i += 2) {
            rows.push(
                <View key={i} style={styles.avatarRow}>
                    <TouchableOpacity onPress={() => handleAvatarSelect(avatars[i])}>
                        <Image source={avatars[i]} style={styles.modalAvatar} />
                    </TouchableOpacity>
                    {avatars[i + 1] && (
                        <TouchableOpacity onPress={() => handleAvatarSelect(avatars[i + 1])}>
                            <Image source={avatars[i + 1]} style={styles.modalAvatar} />
                        </TouchableOpacity>
                    )}
                </View>
            );
        }
        return rows;
    };

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.sectionTitle}>Tùy chọn</Text>
                <TouchableOpacity style={styles.avatarContainer} onPress={handleAvatarPress}>
                    <Image source={selectedAvatar} style={styles.avatar} />
                </TouchableOpacity>
            </View>
            <View style={styles.userInfoContainer}>
                <Text style={styles.username}>Trần Quang Khải</Text>
                <Text style={styles.phone}>khaikhua2233</Text>
            </View>
            <View style={styles.optionList}>
                <TouchableOpacity style={styles.option}>
                    <View style={styles.optionRow}>
                        <AntDesign name="profile" size={24} color="green" />
                        <Text style={styles.optionText}>Xem thông tin cá nhân</Text>
                    </View>
                </TouchableOpacity>

                <TouchableOpacity style={styles.option}
                onPress={() => navigation.navigate('UpdateProfileScreen')}>
                    <View style={styles.optionRow}>
                    <MaterialIcons name="update" size={24} color="red" />
                        <Text style={styles.optionText}>Cập nhật thông tin cá nhân</Text>
                    </View>
                </TouchableOpacity>

                <TouchableOpacity style={styles.option}>
                    <View style={styles.optionRow}>
                        <AntDesign name="bells" size={24} color="gold" />
                        <Text style={styles.optionText}>Cài đặt thông báo</Text>
                    </View>
                </TouchableOpacity>

                <TouchableOpacity style={styles.option}>
                    <View style={styles.optionRow}>
                        <AntDesign name="questioncircleo" size={24} color="blue" />
                        <Text style={styles.optionText}>Câu hỏi thường gặp</Text>
                    </View>
                </TouchableOpacity>

                <TouchableOpacity style={styles.option}>
                    <View style={styles.optionRow}>
                        <AntDesign name="filetext1" size={24} color="purple" />
                        <Text style={styles.optionText}>Điều khoản sử dụng</Text>
                    </View>
                </TouchableOpacity>

                <TouchableOpacity style={styles.option}>
                    <View style={styles.optionRow}>
                        <Feather name="user" size={24} color="orange" />
                        <Text style={styles.optionText}>Đăng xuất</Text>
                    </View>
                </TouchableOpacity>
            </View>

            <Modal
                visible={isModalVisible}
                animationType="slide"
                transparent={true}
                onRequestClose={() => setModalVisible(false)}
            >
                <View style={styles.modalContainer}>
                    <View style={styles.modalContent}>
                        <Text style={styles.modalTitle}>Chọn Avatar</Text>
                        <ScrollView>
                            {renderAvatarRows()}
                        </ScrollView>
                        <TouchableOpacity onPress={() => setModalVisible(false)} style={styles.closeButton}>
                            <Text style={styles.closeButtonText}>Đóng</Text>
                        </TouchableOpacity>
                    </View>
                </View>
            </Modal>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
    },
    header: {
        flexDirection: 'column',
        alignItems: 'center',
        padding: 15,
        backgroundColor: '#fff',
        justifyContent: 'center',
    },
    avatarContainer: {
        alignSelf: 'center',
    },
    avatar: {
        marginTop: 20,
        width: 120,
        height: 120,
        borderRadius: 50,
    },
    sectionTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 8,
        color: '#00bf63',
        textAlign: 'center',
    },
    userInfoContainer: {
        alignItems: 'center',
        backgroundColor: '#fff',
    },
    username: {
        fontSize: 22,
        fontWeight: 'bold',
        color: '#00bf63',
    },
    phone: {
        fontSize: 16,
        color: '#7F7F7F',
        marginTop: 5,
    },
    optionList: {
        marginTop: 20,
    },
    option: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: 20,
        backgroundColor: '#fff',
        borderWidth: 1,
        borderColor: '#ccc',
        borderRadius: 10,
        marginTop: 10,
        width: '90%',
        alignSelf: 'center',
    },
    optionRow: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    optionText: {
        marginLeft: 10,
        fontSize: 16,
    },
    modalContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
    modalContent: {
        backgroundColor: '#fff',
        borderRadius: 10,
        padding: 20,
        alignItems: 'center',
        width: '90%',
        height: '70%',
    },
    modalTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 10,
        color: '#00bf63',
    },
    avatarRow: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginVertical: 10,
    },
    modalAvatar: {
        width: 120,
        height: 120,
        borderRadius: 40,
        marginHorizontal: 10,
    },
    closeButton: {
        marginTop: 20,
        padding: 10,
        backgroundColor: '#00bf63',
        borderRadius: 5,
    },
    closeButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
});

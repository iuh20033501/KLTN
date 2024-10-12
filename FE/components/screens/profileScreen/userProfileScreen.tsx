import { View, Text, TouchableOpacity, StyleSheet, Image } from 'react-native';
import React, { useEffect, useState } from 'react';
import { AntDesign, MaterialIcons, Feather } from '@expo/vector-icons';
import Icon from 'react-native-vector-icons/Ionicons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import http from '@/utils/http';

export default function UserProfileScreen({ navigation }: { navigation: any }) {
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

    const [user, setUser] = useState<any>(null);
    const [loading, setLoading] = useState(true);
    const [selectedAvatar, setSelectedAvatar] = useState(avatars[0]); // Avatar mặc định

    const getUserInfo = async () => {
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (token) {
                const response = await http.get('auth/profile', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                if (response.status === 200) {
                    setUser(response.data);
                    setSelectedAvatar(getAvatar(response.data?.u?.image));
                } else {
                    console.error('Lấy thông tin người dùng thất bại.');
                }
            } else {
                console.error('Token không tồn tại');
            }
        } catch (error) {
            console.error('Có lỗi xảy ra khi lấy thông tin người dùng:', error);
        } finally {
            setLoading(false);
        }
    };

    // Hàm để lấy avatar dựa trên chỉ số image
    const getAvatar = (imageIndex: string) => {
        const index = parseInt(imageIndex, 10) - 1; // Chuyển giá trị string thành số và trừ 1 để lấy đúng chỉ số
        return avatars[index] || avatars[0]; // Nếu không có avatar phù hợp thì dùng avatar mặc định
    };

    useEffect(() => {
        getUserInfo(); // Lấy thông tin người dùng khi màn hình được tải
    }, []);

    const vietHoaRole = (role: string): string => {
        switch (role) {
            case 'QUANLY':
                return 'Quản lý';
            case 'NHANVIEN':
                return 'Nhân viên';
            case 'STUDENT':
                return 'Học viên';
            default:
                return 'Không xác định';
        }
    };

    if (loading) {
        return (
            <View style={styles.loadingContainer}>
                <Text>Đang tải dữ liệu...</Text>
            </View>
        );
    }

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <TouchableOpacity onPress={() => navigation.goBack()}>
                    <Icon name="arrow-back-outline" size={24} color="black" />
                </TouchableOpacity>
                <Text style={styles.headerText}>Tùy chọn người dùng</Text>
            </View>

            {/* Hiển thị avatar */}
            <Image source={selectedAvatar} style={styles.image} />

            <View style={styles.userInfoContainer}>
                <Text style={styles.username}>{user?.u?.hoTen}</Text>
                <Text style={styles.phone}>{vietHoaRole(user?.cvEnum)}</Text>
            </View>

            <View style={styles.optionList}>
                <TouchableOpacity style={styles.option}
                    onPress={() => navigation.navigate('UserInfoScreen')}
                >
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

                <TouchableOpacity style={styles.option}
                    onPress={() => navigation.navigate('Home')}>
                    <View style={styles.optionRow}>
                        <Feather name="user" size={24} color="orange" />
                        <Text style={styles.optionText}>Đăng xuất</Text>
                    </View>
                </TouchableOpacity>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
    },
    header: {
        flexDirection: 'row',
        alignItems: 'center',
        padding: 15,
    },
    headerText: {
        fontSize: 18,
        fontWeight: 'bold',
        marginLeft: 10,
    },
    image: {
        width: 120,
        height: 120,
        borderRadius: 50,
        marginTop: 20,
        marginBottom: 10,
        alignSelf: 'center',
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
    loadingContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
});

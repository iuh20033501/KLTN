import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TextInput, Image, TouchableOpacity, Modal, ScrollView, ImageBackground } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import http from "@/utils/http";
import * as ImagePicker from "expo-image-picker";

export default function EditProfileScreen({ navigation }: { navigation: any }) {
    const [phone, setPhone] = useState('');
    const [email, setEmail] = useState('');
    const [birthday, setBirthday] = useState('');
    const [gender, setGender] = useState('');
    const [user, setUser] = useState<any>(null);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');
    const [errorModalVisible, setErrorModalVisible] = useState(false);
    const [selectedAvatar, setSelectedAvatar] = useState('');

    const validPhonePrefixes = ['032', '033', '034', '035', '036', '037', '038', '039', '081', '082', '083', '084', '085', '088', '070', '076', '077', '078', '079', '052', '056', '058', '092', '059', '099'];
    const gmailRegex = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;

    const validatePhone = (phone: string) => {
        const phonePrefix = phone.slice(0, 3);
        return validPhonePrefixes.includes(phonePrefix) && phone.length === 10;
    };

    const formatDateForServer = (dateString: string): string | null => {
        if (!dateString) return null;
        const [day, month, year] = dateString.split('/');
        return `${year}-${month}-${day}`;
    };

    const validateForm = () => {
        if (!phone.trim()) {
            setErrorMessage('Số điện thoại không được để trống');
            setErrorModalVisible(true);
            return false;
        } else if (!validatePhone(phone)) {
            setErrorMessage('Đầu số không hợp lệ');
            setErrorModalVisible(true);
            return false;
        }

        if (!email.trim()) {
            setErrorMessage('Email không được để trống');
            setErrorModalVisible(true);
            return false;
        } else if (!gmailRegex.test(email)) {
            setErrorMessage('Vui lòng nhập đúng định dạng Gmail (@gmail.com)');
            setErrorModalVisible(true);
            return false;
        }

        if (!birthday.trim()) {
            setErrorMessage('Ngày sinh không được để trống');
            setErrorModalVisible(true);
            return false;
        }

        if (!gender.trim()) {
            setErrorMessage('Giới tính không được để trống');
            setErrorModalVisible(true);
            return false;
        }

        return true;
    };

    const handleUpdate = async () => {
        if (!validateForm()) {
            return;
        }

        try {
            const genderValue = gender === 'Nam' ? true : false;
            const formattedBirthday = formatDateForServer(birthday);
            const token = await AsyncStorage.getItem('accessToken');
            const imageToUpload = selectedAvatar
            const response = await http.put(`hocvien/update/${user.u.idUser}`, {
                idUser: user.u.idUser,
                hoTen: user.u.hoTen,
                sdt: phone,
                email: email,
                ngaySinh: formattedBirthday,
                gioiTinh: genderValue,
                image: imageToUpload
            }, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.status === 200) {
                setErrorMessage('Cập nhật thông tin thành công');
                setErrorModalVisible(true);
                setTimeout(() => {
                    setErrorModalVisible(false);
                    navigation.navigate('DashboardScreen');
                }, 1250);
            } else {
                setErrorMessage('Cập nhật thông tin thất bại');
                setErrorModalVisible(true);
            }
        } catch (error) {
            console.error('Có lỗi xảy ra:', error);
            setErrorMessage('Có lỗi xảy ra khi cập nhật thông tin');
            setErrorModalVisible(true);
        }
    };

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
                    const userData = response.data;
                    setPhone(userData.u.sdt || '');
                    setEmail(userData.u.email || '');
                    setBirthday(formatDate(userData.u.ngaySinh || ''));
                    setGender(userData.u.gioiTinh === true ? 'Nam' : 'Nữ');
                    setUser(userData);
                    setSelectedAvatar(userData.u.image);
                } else {
                    setErrorMessage('Lấy thông tin người dùng thất bại.');
                    setErrorModalVisible(true);
                }
            } else {
                setErrorMessage('Token không tồn tại');
                setErrorModalVisible(true);
            }
        } catch (error) {
            console.error('Có lỗi xảy ra khi lấy thông tin người dùng:', error);
            setErrorMessage('Có lỗi xảy ra khi lấy thông tin người dùng');
            setErrorModalVisible(true);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        getUserInfo();
    }, []);

    const formatDate = (dateString: string) => {
        if (!dateString) return 'N/A';
        const dateParts = dateString.split('-');
        if (dateParts.length !== 3) return dateString;

        const [year, month, day] = dateParts;
        return `${day}/${month}/${year}`;
    };
    const pickImage = async (useLibrary: boolean) => {
        let result = null;
        if (useLibrary) {
            result = await ImagePicker.launchImageLibraryAsync({
                mediaTypes: ImagePicker.MediaTypeOptions.Images,
                base64: true,
                quality: 1,
            });
        } else {
            await ImagePicker.requestCameraPermissionsAsync();
            result = await ImagePicker.launchCameraAsync({
                mediaTypes: ImagePicker.MediaTypeOptions.Images,
                base64: true,
                quality: 1,
            });
        }

        if (result && !result.canceled) {
            const base64String: string = result.assets[0].base64 || '';
            setSelectedAvatar(base64String);

        }
    };

    return (
        <ImageBackground
            source={require('../../../image/bglogin.png')}
            style={styles.background}
            resizeMode='cover'
        >
            <ScrollView contentContainerStyle={styles.scrollContainer}>
                <View style={styles.container}>
                    <View >
                        <Text style={styles.title}>Thay đổi thông tin cá nhân</Text>
                    </View>

                    <View style={styles.avatarSection}>
                        <Image
                            source={selectedAvatar ? { uri: `data:image/png;base64,${selectedAvatar}` } : require('../../../image/efy.png')}
                            style={styles.avatar}
                        />
                        <TouchableOpacity onPress={() => pickImage(true)}>
                            <Text style={styles.editAvatarText}>Đổi ảnh đại diện</Text>
                        </TouchableOpacity>
                    </View>

                    <View style={styles.formSection}>
                        <TextInput
                            style={styles.input}
                            value={phone}
                            onChangeText={setPhone}
                            placeholder="Số điện thoại"
                        />

                        <TextInput
                            style={styles.input}
                            value={email}
                            onChangeText={setEmail}
                            placeholder="Email"
                        />

                        <TextInput
                            style={styles.input}
                            value={birthday}
                            onChangeText={setBirthday}
                            placeholder="Ngày sinh"
                        />

                        <View style={styles.inputRow}>
                            <Text style={styles.label}>Giới tính</Text>
                            <TouchableOpacity onPress={() => setGender(gender === 'Nam' ? 'Nữ' : 'Nam')}>
                                <Text style={styles.dropdown}>{gender}</Text>
                            </TouchableOpacity>
                        </View>

                        <View style={styles.buttonsContainer}>
                            <TouchableOpacity style={styles.backButton}
                                onPress={() => navigation.goBack()}>
                                <Text style={styles.backButtonText}>Quay về</Text>
                            </TouchableOpacity>

                            <TouchableOpacity style={styles.nextButton} onPress={handleUpdate}>
                                <Text style={styles.nextButtonText}>Cập nhật</Text>
                            </TouchableOpacity>
                        </View>
                    </View>

                    <Modal
                        visible={errorModalVisible}
                        transparent={true}
                        onRequestClose={() => setErrorModalVisible(false)}
                    >
                        <View style={styles.modalOverlay}>
                            <View style={styles.errorModalContainer}>
                                <Text style={styles.errorModalText}>{errorMessage}</Text>
                                <TouchableOpacity
                                    style={styles.errorCloseButton}
                                    onPress={() => setErrorModalVisible(false)}
                                >
                                    <Text style={styles.errorCloseButtonText}>OK</Text>
                                </TouchableOpacity>
                            </View>
                        </View>
                    </Modal>
                </View>
            </ScrollView>
        </ImageBackground>
    );
}

const styles = StyleSheet.create({
    background: {
        flex: 1,
    },
    scrollContainer: {
        flexGrow: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    container: {
        width: '90%',
        maxWidth: 400,
        backgroundColor: '#fff',
        borderRadius: 15,
        padding: 20,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        elevation: 3,
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        textAlign: 'center'
    },
    avatarSection: {
        marginTop: 30,
        alignItems: 'center',
        marginBottom: 20,
    },
    avatar: {
        width: 100,
        height: 100,
        borderRadius: 50,
        marginBottom: 10,
    },
    editAvatarText: {
        color: '#1DA1F2',
        fontSize: 16,
    },
    formSection: {
        marginBottom: 20,
    },
    input: {
        borderRadius: 10,
        padding: 10,
        marginBottom: 10,
        borderWidth: 1,
        borderColor: '#ccc',
        fontSize: 18,
    },
    inputRow: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: 10,
    },
    label: {
        fontSize: 16,
    },
    dropdown: {
        fontSize: 16,
        borderRadius: 10,
        padding: 10,
        borderWidth: 1,
    },
    buttonsContainer: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        width: '100%',
        top: 20
    },
    backButton: {
        backgroundColor: '#d1d1d1',
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderRadius: 5,
        width: 120,
        height: 30,
        alignItems: 'center',
        justifyContent: 'center',
        marginRight: 10
    },
    backButtonText: {
        fontSize: 16,
        color: '#333',
        textAlign: 'center',
    },
    nextButton: {
        backgroundColor: '#00405d',
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderRadius: 5,
        width: 120,
        height: 30,
        alignItems: 'center',
        justifyContent: 'center',
    },
    nextButtonText: {
        color: '#fff',
        fontSize: 16,
        textAlign: 'center',
    },
    modalOverlay: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
    errorModalContainer: {
        width: '100%',
        maxWidth: 400,
        backgroundColor: '#fff',
        padding: 20,
        borderRadius: 10,
        alignItems: 'center',
    },
    errorModalText: {
        fontSize: 16,
        marginBottom: 20,
        textAlign: 'center',
    },
    errorCloseButton: {
        backgroundColor: '#00405d',
        padding: 10,
        borderRadius: 5,
        width: '100%',
        alignItems: 'center',
    },
    errorCloseButtonText: {
        color: '#fff',
        fontSize: 16,
    },
});

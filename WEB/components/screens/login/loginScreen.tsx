import http from '@/utils/http';
import React, { useState } from 'react';
import {
    View,
    Text,
    TextInput,
    TouchableOpacity,
    StyleSheet,
    Image,
    ImageBackground,
    Dimensions,
    Modal,
} from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { FontAwesome } from '@expo/vector-icons';


export default function LoginScreen({ navigation }: { navigation: any }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [modalVisible, setModalVisible] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [isPasswordVisible, setIsPasswordVisible] = useState(false);

    const validateUsername = (name: string) => {
        const usernameRegex = /^[a-zA-Z][a-zA-Z0-9_]{5,31}$/;
        return usernameRegex.test(name);
    };

    const validatePassword = (pass: string) => {
        return pass.length >= 6 && pass.length <= 32;
    };


    const handleNext = async () => {
        if (!validateUsername(username)) {
            setErrorMessage('Tài khoản không hợp lệ. Tài khoản có độ dài từ 6 đến 32 ký tự và không bắt đầu bằng số.');
            setModalVisible(true);
            return;
        }
    
        if (!validatePassword(password)) {
            setErrorMessage('Mật khẩu không hợp lệ. Mật khẩu có độ dài từ 6 đến 32 ký tự.');
            setModalVisible(true);
            return;
        }
        setErrorMessage('');
        setModalVisible(false);
    
        try {
            const response = await http.post("auth/noauth/signin", {
                username: username,
                password: password,
            });
    
            if (response.status === 200) {
                const { accessToken } = response.data;
                await AsyncStorage.setItem('accessToken', accessToken);
    
                const profileResponse = await http.get("auth/profile", {
                    headers: { Authorization: `Bearer ${accessToken}` },
                });
    
                if (profileResponse.status === 200) {
                    const { cvEnum } = profileResponse.data;
    
                    if (cvEnum === "STUDENT" || cvEnum === "TEACHER") {
                        console.log("Đăng nhập thành công");
                        setTimeout(() => {
                            navigation.navigate('DashboardScreen');
                        }, 500);
                    } else {
                        setErrorMessage("Chỉ học viên và giáo viên mới được phép đăng nhập vào ứng dụng");
                        setModalVisible(true);
                        await AsyncStorage.removeItem('accessToken');
                    }
                } else {
                    setErrorMessage("Không thể lấy thông tin người dùng");
                    setModalVisible(true);
                }
            } else {
                setErrorMessage("Sai thông tin đăng nhập");
                setModalVisible(true);
            }
        } catch (error) {
            setErrorMessage("Đăng nhập thất bại");
            setModalVisible(true);
        }
    };
    
    return (
        <ImageBackground
            source={require('../../../image/bglogin.png')}
            style={styles.background}
            resizeMode='cover'
        >
            <Image
                source={require('../../../image/efy.png')}
                style={styles.logo}
            />
            <View style={styles.container}>

                <View style={styles.formContainer}>
                    <Text style={styles.title}>Đăng nhập</Text>

                    <TextInput
                        style={styles.input}
                        placeholder="Tài khoản"
                        placeholderTextColor="#888"
                        value={username}
                        onChangeText={setUsername}
                    />

                    <TextInput
                        style={styles.input}
                        placeholder="Mật khẩu"
                        placeholderTextColor="#888"
                        value={password}
                        onChangeText={setPassword}
                        secureTextEntry={!isPasswordVisible}
                    />
                    <TouchableOpacity onPress={() => setIsPasswordVisible(!isPasswordVisible)} style={styles.eyeIcon}>
                        <FontAwesome name={isPasswordVisible ? 'eye-slash' : 'eye'} size={26} color="gray" />
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.forgotPassword}
                        onPress={() => navigation.navigate('ResetPassword')}>
                        <Text style={styles.linkText}>Quên mật khẩu</Text>
                    </TouchableOpacity>

                    <View style={styles.buttonsContainer}>
                        <TouchableOpacity style={styles.backButton}
                            onPress={() => navigation.navigate('HomeScreen')}>
                            <Text style={styles.backButtonText}>Trang chủ</Text>
                        </TouchableOpacity>

                        <TouchableOpacity style={styles.nextButton} onPress={handleNext}>
                            <Text style={styles.nextButtonText}>Đăng nhập</Text>
                        </TouchableOpacity>
                    </View>
                    <Text style={styles.companyInfo}>TRUNG TÂM ANH NGỮ ENGLISH FOR YOU</Text>
                </View>
            </View>
            <View style={styles.container2}>
                <TouchableOpacity style={styles.signInOptions}
                    onPress={() => navigation.navigate('SignUpScreen')}>
                    <Text style={styles.linkText}>Trở thành thành viên của EFY</Text>
                </TouchableOpacity>
            </View>
            <View style={styles.footer}>
                <Text style={styles.footerText}>Terms of use </Text>
                <Text style={styles.footerText}>Privacy & cookies</Text>
            </View>
            <Modal
                animationType="slide"
                transparent={true}
                visible={modalVisible}
                onRequestClose={() => {
                    setModalVisible(false);
                }}
            >
                <View style={styles.modalOverlay}>
                    <View style={styles.modalContainer}>
                        <Text style={styles.modalText}>{errorMessage}</Text>
                        <TouchableOpacity
                            style={styles.closeButton}
                            onPress={() => setModalVisible(false)}
                        >
                            <Text style={styles.closeButtonText}>OK</Text>
                        </TouchableOpacity>
                    </View>
                </View>
            </Modal>
        </ImageBackground>
    );
};

const styles = StyleSheet.create({
    background: {
        flex: 1,
        width: 'auto',
        height: 990,
        justifyContent: 'center',
        alignItems: 'center',
    },
    container: {
        borderRadius: 15,

        width: '100%',
        maxWidth: 500,
        minWidth: 400,
        height: 450,
        backgroundColor: '#fff',
        padding: 20,
        shadowColor: '#000',
        shadowOpacity: 0.1,
        shadowOffset: { width: 0, height: 5 },
        shadowRadius: 10,
        elevation: 2,
        alignItems: 'center',
        justifyContent: 'center',

    },
    logo: {
        width: 350,
        height: 250,
        marginTop: -150
    },
    formContainer: {
        alignItems: 'center',
        width: '100%',
        justifyContent: 'center',
        top: 50
    },
    container2: {
        borderRadius: 15,

        width: '100%',
        maxWidth: 500,
        minWidth: 400,
        height: 50,
        backgroundColor: '#fff',
        padding: 20,
        shadowColor: '#000',
        shadowOpacity: 0.1,
        shadowOffset: { width: 0, height: 5 },
        shadowRadius: 10,
        elevation: 2,
        alignItems: 'center',
        justifyContent: 'center',
        marginTop: 10,
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 40,
        top: -40,
        color: '#333',
    },
    input: {
        backgroundColor: '#fff',
        width: '100%',
        padding: 15,
        marginBottom: 10,
        borderColor: '#ddd',
        borderWidth: 1,
        color: '#333',
        fontSize: 18,
        borderRadius: 15,

    },
    forgotPassword: {
        marginLeft: -345,
        marginBottom: 20,
    },
    linkText: {
        color: '#0078d4',
        fontSize: 16,
        textAlign: 'center'
    },
    buttonsContainer: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        width: '100%',
        top: 40
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
        marginTop: -3
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
        marginTop: -3
    },
    companyInfo: {
        marginTop: 130,
        fontSize: 14,
        color: '#555',
        textAlign: 'center',
        top: -55,
    },
    signInOptions: {
        justifyContent: 'center',
        alignItems: 'center',
    },
    footer: {
        flexDirection: 'column',
        justifyContent: 'space-between',
        marginTop: 20,
    },
    footerText: {
        fontSize: 12,
        color: '#666',
        textAlign: 'center',
    },
    modalOverlay: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',

    },
    modalContainer: {
        width: '100%',
        maxWidth: 400,
        backgroundColor: '#fff',
        padding: 20,
        borderRadius: 10,
        alignItems: 'center',
    },
    modalText: {
        fontSize: 16,
        marginBottom: 20,
        textAlign: 'center',
    },
    closeButton: {
        backgroundColor: '#00405d',
        padding: 10,
        borderRadius: 5,
        width: '100%',
        alignItems: 'center',
    },
    closeButtonText: {
        color: '#fff',
        fontSize: 16,
    },
    eyeIcon: {
        position: 'absolute',
        right: 15,
        marginTop: -90,
      },
});


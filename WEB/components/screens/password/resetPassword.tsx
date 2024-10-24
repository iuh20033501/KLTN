import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { useEffect, useState } from 'react';
import {
    View,
    Text,
    TextInput,
    TouchableOpacity,
    StyleSheet,
    Image,
    ImageBackground,
    Modal,
} from 'react-native';

export default function ResetPassword({ navigation }: { navigation: any }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [verifyPassword, setVerifyPassword] = useState('');
    const [phone, setPhone] = useState('');
    const [otp, setOTP] = useState('');
    let signToken = "";
    const [userName, setUserName] = useState('');
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState<any[]>([]);
    const [showUserForm, setShowUserForm] = useState(true);
    const [showInfoForm, setShowInfoForm] = useState(false);
    const [showOTPForm, setShowOTPForm] = useState(false);
    const [modalVisible, setModalVisible] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    const validateUsername = (name: string) => {
        const usernameRegex = /^[a-zA-Z][a-zA-Z0-9_]{5,31}$/;
        return usernameRegex.test(name);
    };

    const validatePassword = (pass: string) => {
        return pass.length > 5 && pass.length < 33;
    };
    // const validateOTP = (otp: string) => {
    //     const otpRegex = /^[0-9]{6}$/;
    //     return otpRegex.test(otp);
    // };
    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await http.get('auth/noauth/findAll');
                if (response.status === 200) {
                    setData(response.data);
                    console.log(response.data);
                } else {
                    setErrorMessage('Không thể lấy dữ liệu người dùng');
                }
            } catch (error) {
                console.error('Error fetching data:', error);
                setErrorMessage('Có lỗi xảy ra khi lấy dữ liệu');
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    const handleNext = async () => {
        let isValid = true;
        setErrorMessage('');

        if (showUserForm) {
            if (!validateUsername(username)) {
                setErrorMessage('Tài khoản không hợp lệ. Tài khoản có độ dài từ 6 đến 32 ký tự và không bắt đầu bằng số.');
                isValid = false;
            }
            const userAccount = data.find(item => item.tenDangNhap === username);
            if (!userAccount) {
                setErrorMessage('Tài khoản không tồn tại');
                isValid = false;
            } else if (userAccount.user && userAccount.user.sdt) {
                try {
                    const phoneNumber = userAccount.user.sdt;
                    const response = await http.post('auth/noauth/send', { phone: phoneNumber });
                    if (response.status === 200) {
                        setShowUserForm(false);
                        setShowOTPForm(true);
                        setErrorMessage('');
                        console.log(response.data)
                        console.log('OTP đã được gửi thành công đến số điện thoại:', phoneNumber);
                    } else {
                        setErrorMessage('Không thể gửi mã OTP. Vui lòng thử lại.');
                        setModalVisible(true);
                    }
                } catch (error) {
                    console.error("Lỗi khi gửi OTP:", error);
                    setErrorMessage('Không thể gửi mã OTP. Vui lòng kiểm tra kết nối mạng.');
                    setModalVisible(true);
                }
            } else {
                setErrorMessage('Không tìm thấy số điện thoại liên kết với tài khoản này.');
                isValid = false;
            }
        }
        else if (showInfoForm) {
            if (!validatePassword(password)) {
                setErrorMessage('Mật khẩu có độ dài từ 6 đến 32 ký tự');
                isValid = false;
            } else if (password !== verifyPassword) {
                setErrorMessage('Mật khẩu xác nhận không khớp.');
                isValid = false;
            }
            if (isValid) {
                try {
                    const token = await AsyncStorage.getItem('accessToken');
                    if (!token) {
                        setErrorMessage('Không tìm thấy token. Vui lòng thử lại.');
                        return;
                    }
                    console.log("Token từ AsyncStorage:", token);
                    
                    const response = await http.post(
                        "auth/account/reset",
                        { password },
                        {
                            headers: {
                                Authorization: `Bearer ${token}`,
                            },
                        }
                    );
        
                    if (response.status === 200) {
                        console.log(response.data);
                        setErrorMessage('Đã cập nhật lại mật khẩu');
                        navigation.navigate('LoginScreen');
                    } else {
                        console.log('Lỗi cập nhật mật khẩu:', response.data); 
                        throw new Error(response.data.message || "Lỗi đổi mật khẩu");
                    }
                } catch (error) {
                    console.error("Có lỗi khi reset mật khẩu:", error);
                    setErrorMessage('Có lỗi khi reset mật khẩu');
                }
            } else {
                setModalVisible(true);
            }
        }
        else if (showOTPForm) {
            if (isValid) {
                try {
                    const userAccount = data.find(item => item.tenDangNhap === username);
                    const phoneNumber = userAccount.user.sdt;
                    const response = await http.post(
                        "auth/noauth/validate",
                        { phone: phoneNumber, otp },
                        {
                            headers: {
                                "Content-Type": "application/json",
                            },
                        }
                    );
                    if (response.status === 200) {
                        const accessToken = response.data.accessToken;
                        console.log('Access Token:', accessToken);
                        await AsyncStorage.setItem('accessToken', accessToken);
                        setShowInfoForm(true);
                        setShowOTPForm(false);
                    } else {
                        throw new Error("Lỗi xác thực OTP");
                    }
                } catch (error) {
                    console.error("Có lỗi xảy ra trong quá trình xác thực OTP:", error);
                    setErrorMessage("Xác thực OTP không thành công. Vui lòng thử lại.");
                    setModalVisible(true);
                }
            } else {
                setModalVisible(true);
            }
        }
    };

    const handleBack = () => {
        if (showUserForm) {
            navigation.navigate('LoginScreen');
        } else if (showInfoForm) {
            setShowInfoForm(false);
            setShowUserForm(true);
        } else if (showOTPForm) {
            setShowOTPForm(false);
            setShowInfoForm(true);
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
                    {showUserForm && (
                        <>
                            <Text style={styles.title}>Quên mật khẩu</Text>
                            <TextInput
                                style={styles.input}
                                placeholder="Tài khoản"
                                placeholderTextColor="#888"
                                value={username}
                                onChangeText={setUsername}
                                maxLength={32}
                            />


                            <View style={styles.buttonsContainer}>
                                <TouchableOpacity style={styles.backButton}
                                    onPress={handleBack}>
                                    <Text style={styles.backButtonText}>Quay lại</Text>
                                </TouchableOpacity>
                                <TouchableOpacity style={styles.nextButton} onPress={handleNext}>
                                    <Text style={styles.nextButtonText}>Tiếp tục</Text>
                                </TouchableOpacity>
                            </View>
                        </>
                    )}
                    {showInfoForm && (
                        <>
                            <Text style={styles.subTitle}>Nhập mật khẩu mới</Text>

                            <TextInput
                                style={styles.resetPassInput}
                                placeholder="Mật khẩu"
                                placeholderTextColor="#888"
                                value={password}
                                onChangeText={setPassword}
                                secureTextEntry
                                maxLength={32}
                            />

                            <TextInput
                                style={styles.resetPassInput2}
                                placeholder="Nhập lại mật khẩu"
                                placeholderTextColor="#888"
                                value={verifyPassword}
                                onChangeText={setVerifyPassword}
                                secureTextEntry
                                maxLength={32}
                            />

                            <View style={styles.buttonsContainer2}>
                                <TouchableOpacity style={styles.backButton2} onPress={handleBack}>
                                    <Text style={styles.backButtonText2}>Quay lại</Text>
                                </TouchableOpacity>
                                <TouchableOpacity style={styles.nextButton2} onPress={handleNext}>
                                    <Text style={styles.nextButtonText2}>Tiếp tục</Text>
                                </TouchableOpacity>
                            </View>
                        </>
                    )}
                    {showOTPForm && (
                        <>
                            <Text style={styles.confirmTitle}>Xác thực mã OTP</Text>
                            <TextInput
                                style={styles.confirmInput}
                                placeholder="Nhập mã OTP"
                                placeholderTextColor="#888"
                                value={otp}
                                onChangeText={setOTP}
                                maxLength={6}
                            />
                            <View style={styles.buttonsContainer}>
                                <TouchableOpacity style={styles.backButton} onPress={handleBack}>
                                    <Text style={styles.backButtonText}>Quay lại</Text>
                                </TouchableOpacity>
                                <TouchableOpacity style={styles.nextButton} onPress={handleNext}>
                                    <Text style={styles.nextButtonText}>Xác nhận</Text>
                                </TouchableOpacity>
                            </View>
                        </>
                    )}
                    <Text style={styles.companyInfo}>TRUNG TÂM ANH NGỮ ENGLISH FOR YOU</Text>

                </View>
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
}

const styles = StyleSheet.create({
    background: {
        flex: 1,
        width: 'auto',
        height: 990,
        justifyContent: 'center',
        alignItems: 'center',
    },
    logo: {
        width: 350,
        height: 250,
        marginTop: -150
    },
    container: {
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
    formContainer: {
        alignItems: 'center',
        width: '100%',
        justifyContent: 'center',
    },

    title: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 50,
        color: '#333',

    },
    subTitle: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#333',
        top: 5
    },
    confirmTitle: {
        fontSize: 24,
        fontWeight: 'bold',
        top: -47,
        marginTop: 50,
        color: '#333',
    },
    input: {
        backgroundColor: '#fff',
        width: '100%',
        padding: 15,
        marginTop: 80,
        borderRadius: 5,
        borderColor: '#ddd',
        borderWidth: 1,
        color: '#333',
        marginBottom: 108,
        fontSize: 18
    },
    resetPassInput: {
        backgroundColor: '#fff',
        width: '100%',
        padding: 15,
        marginTop: 100,
        borderRadius: 5,
        borderColor: '#ddd',
        borderWidth: 1,
        color: '#333',
        fontSize: 18,

    },
    resetPassInput2: {
        backgroundColor: '#fff',
        width: '100%',
        padding: 15,
        marginTop: 10,
        borderRadius: 5,
        borderColor: '#ddd',
        borderWidth: 1,
        color: '#333',
        fontSize: 18,
        marginBottom: 72
    },
    confirmInput: {
        backgroundColor: '#fff',
        width: '100%',
        padding: 15,
        marginTop: 80,
        borderRadius: 5,
        borderColor: '#ddd',
        borderWidth: 1,
        color: '#333',
        marginBottom: 108,
        fontSize: 18
    },
    errorText: {
        color: 'red',
        fontSize: 14,
        marginBottom: 10,
    },
    useful: {
        marginLeft: -310,
        marginBottom: 25,
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
        top: -75
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
        textAlign: 'center'
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
    buttonsContainer2: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        width: '100%',
        top: -40
    },
    backButton2: {
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
    backButtonText2: {
        fontSize: 16,
        color: '#333',
        textAlign: 'center'
    },
    nextButton2: {
        backgroundColor: '#00405d',
        paddingVertical: 10,
        paddingHorizontal: 20,
        borderRadius: 5,
        width: 120,
        height: 30,
        alignItems: 'center',
        justifyContent: 'center',
    },
    nextButtonText2: {
        color: '#fff',
        fontSize: 16,
        textAlign: 'center'
    },

    companyInfo: {
        marginTop: 40,
        fontSize: 14,
        color: '#555',
        textAlign: 'center',
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
});

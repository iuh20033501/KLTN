import http from '@/utils/http';
<<<<<<< Updated upstream
import { Ionicons } from '@expo/vector-icons';
=======
import { FontAwesome } from '@expo/vector-icons';
>>>>>>> Stashed changes
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
    const [confirmOtp, setConfirmOTP] = useState('');
    const [userName, setUserName] = useState('');
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState<any[]>([]);
    const [showUserForm, setShowUserForm] = useState(true);
    const [showInfoForm, setShowInfoForm] = useState(false);
    const [showOTPForm, setShowOTPForm] = useState(false);
    const [modalVisible, setModalVisible] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
<<<<<<< Updated upstream
    const [otpConfirm, setOTPConfirm] = useState('');
=======
>>>>>>> Stashed changes
    const [showPassword, setShowPassword] = useState(false);
    const [showVerifyPassword, setShowVerifyPassword] = useState(false);
    const validateUsername = (name: string) => {
        const usernameRegex = /^[a-zA-Z][a-zA-Z0-9_]{5,31}$/;
        return usernameRegex.test(name);
    };

    const validatePassword = (pass: string) => {
        return pass.length > 5 && pass.length < 33;
    };
    const validateOTP = (otp: string) => {
        const otpRegex = /^[0-9]{6}$/;
        return otpRegex.test(otp);
    };
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
                setModalVisible(true);
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
<<<<<<< Updated upstream
                        setErrorMessage('Đã gửi mã OTP đến điện thoại của bạn');
                        setModalVisible(true);
                        console.log(response.data);
                        setOTPConfirm(response.data);
                        setTimeout(() => {
                            setModalVisible(false);
                            setShowUserForm(false);
                            setShowOTPForm(true);
                        }, 1000);

=======
                        console.log(response.data);
                        setConfirmOTP(response.data);
                        setShowUserForm(false);
                        setShowOTPForm(true);
                        setErrorMessage('OTP đã được gửi thành công đến số điện thoại của bạn',);
                        setModalVisible(true);
>>>>>>> Stashed changes
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
                setModalVisible(true);
                isValid = false;
            }
        }
        else if (showInfoForm) {
            if (!validatePassword(password)) {
                setErrorMessage('Mật khẩu có độ dài từ 6 đến 32 ký tự');
                setModalVisible(true);
                isValid = false;
            } else if (password !== verifyPassword) {
                setErrorMessage('Mật khẩu xác nhận không khớp.');
                setModalVisible(true);
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
<<<<<<< Updated upstream
                        setModalVisible(true)
                        setTimeout(() => {
                            setModalVisible(false);
                            navigation.navigate('LoginScreen');
                        }, 1000);
=======
                        setModalVisible(true);
                        setTimeout(() => {
                            setModalVisible(false);
                            navigation.navigate('LoginScreen');
                        }, 1250);
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
            if (String(otp.trim()) !== String(otpConfirm?.trim?.() || otpConfirm)) {
                setErrorMessage('Mã OTP không đúng');
                setModalVisible(true);
                isValid = false;
                console.log(`otp: ${otp}, otpConfirm: ${otpConfirm}`);
            }
=======
            if (!validateOTP(otp)) {
                setErrorMessage('OTP phải bao gồm 6 chữ số.');
                setModalVisible(true);
                isValid = false;
            }
            if (otp !== confirmOtp) {
                setErrorMessage('OTP không chính xác.');
                setModalVisible(true);
                isValid = false;
            }

>>>>>>> Stashed changes
            if (isValid) {
                try {
                    const userAccount = data.find(item => item.tenDangNhap === username);
                    const phoneNumber = userAccount.user.sdt;
                    const response = await http.post(
                        "auth/noauth/validate",
                        { phone: phoneNumber, otp: otp },
                        {
                            headers: {
                                "Content-Type": "application/json",
                            },
                        }
                    );
                    if (response.status === 200) {
                        const accessToken = response.data.accessToken;
                        await AsyncStorage.setItem('accessToken', accessToken);
                        setShowInfoForm(true);
                        setShowOTPForm(false);
                    } else {
<<<<<<< Updated upstream
                        setErrorMessage('Lỗi xác thực OTP');
=======
                        setErrorMessage("Xác thực OTP thất bại");
>>>>>>> Stashed changes
                        setModalVisible(true);
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
            setShowOTPForm(true);
        } else if (showOTPForm) {
            setShowOTPForm(false);
            setShowUserForm(true);
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
                                secureTextEntry={!showPassword}
                                maxLength={32}
                            />
<<<<<<< Updated upstream
                            <TouchableOpacity onPress={() => setShowPassword(!showPassword)}>
                                <Ionicons
                                    name={showPassword ? "eye-off" : "eye"}
                                    size={26}
                                    color="gray"
                                    style={styles.eyeIcon}
                                />
                            </TouchableOpacity>

=======
                            <TouchableOpacity onPress={() => setShowPassword(!showPassword)} style={styles.eyeIcon}>
                                <FontAwesome name={showPassword ? 'eye-slash' : 'eye'} size={26} color="gray" />
                            </TouchableOpacity>
>>>>>>> Stashed changes
                            <TextInput
                                style={styles.resetPassInput2}
                                placeholder="Nhập lại mật khẩu"
                                placeholderTextColor="#888"
                                value={verifyPassword}
                                onChangeText={setVerifyPassword}
                                secureTextEntry={!showVerifyPassword}
                                maxLength={32}
                            />
                            <TouchableOpacity onPress={() => setShowVerifyPassword(!showVerifyPassword)}>
                                <Ionicons
                                    name={showVerifyPassword ? "eye-off" : "eye"}
                                    size={26}
                                    color="gray"
                                    style={styles.eyeIcon2}
                                />
                            </TouchableOpacity>
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
        borderRadius: 15,

        width: '100%',
        maxWidth: 500,
        minWidth: 400,
        height: 450,
        backgroundColor: '#fff',
        padding: 20,
        borderRadius: 15,
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
        borderRadius: 15,

        backgroundColor: '#fff',
        width: '100%',
        padding: 15,
        marginTop: 80,
<<<<<<< Updated upstream
=======
        borderRadius: 15,
>>>>>>> Stashed changes
        borderColor: '#ddd',
        borderWidth: 1,
        color: '#333',
        marginBottom: 108,
        fontSize: 18
    },
    resetPassInput: {
        borderRadius: 15,

        backgroundColor: '#fff',
        width: '100%',
        padding: 15,
        marginTop: 100,
<<<<<<< Updated upstream
=======
        borderRadius: 15,
>>>>>>> Stashed changes
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
        borderRadius: 15,
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
        borderRadius: 15,
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
    eyeIcon: {
        position: 'absolute',
<<<<<<< Updated upstream
        marginTop: -42,
        right: -210
    },
    eyeIcon2: {
        position: 'absolute',
        right: -210,
        marginTop: -115,
=======
        right: 25,
        top: 165
    },
    eyeIcon2: {
        position: 'absolute',
        right: 25,
        top: 230
>>>>>>> Stashed changes
    },
});

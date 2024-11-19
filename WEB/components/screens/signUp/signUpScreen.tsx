import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { useState } from 'react';
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
import { Ionicons } from '@expo/vector-icons';

export default function SignUpScreen({ navigation }: { navigation: any }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [verifyPassword, setVerifyPassword] = useState('');
    const [email, setEmail] = useState('');
    const [name, setName] = useState('');
    const [gender, setGender] = useState('Nam');
    const [phone, setPhone] = useState('');
    const defaultAVT = "";
    const [birthday, setBirthday] = useState('');
    let signToken = "";
    const [showUserForm, setShowUserForm] = useState(true);
    const [showInfoForm, setShowInfoForm] = useState(false);
    const [showOTPForm, setShowOTPForm] = useState(false);
    const [modalVisible, setModalVisible] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [otp, setOTP] = useState('');
    const [otpConfirm, setOTPConfirm] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [showVerifyPassword, setShowVerifyPassword] = useState(false);

    const validateUsername = (name: string) => {
        const usernameRegex = /^[a-zA-Z][a-zA-Z0-9_]{5,31}$/;
        return usernameRegex.test(name);
    };

    const validatePassword = (pass: string) => {
        return pass.length >= 6 && pass.length <= 32;
    };

    const validateEmail = (email: string) => {
        const emailRegex = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;
        return emailRegex.test(email);
    };
    const validateFullName = (name: string) => {
        return name.trim() !== '';
    };

    const validPhonePrefixes = ['032', '033', '034', '035', '036', '037', '038', '039', '081', '082', '083', '084', '085', '088', '070', '076', '077', '078', '079', '052', '056', '058', '092', '059', '099'];
    const validatePhoneNumber = (phone: string) => {
        if (phone.length !== 10) return false;
        const phonePrefix = phone.substring(0, 3);
        return validPhonePrefixes.includes(phonePrefix);
    };

    const handleDateFormat = (dateStr: string) => {
        const dateParts = dateStr.split("/");
        if (dateParts.length === 3) {
            const [day, month, year] = dateParts;
            return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
        }
        return null;
    };

    const handleNext = async () => {
        let isValid = true;
        setErrorMessage('');

        if (showUserForm) {
            if (!validateUsername(username)) {
                setErrorMessage('Tài khoản không hợp lệ. Tài khoản có độ dài từ 6 đến 32 ký tự và không bắt đầu bằng số.');
                isValid = false;
            } else if (!validatePassword(password)) {
                setErrorMessage('Mật khẩu có độ dài từ 6 đến 32 ký tự');
                isValid = false;
            } else if (password !== verifyPassword) {
                setErrorMessage('Mật khẩu xác nhận không khớp.');
                isValid = false;
            }

            if (isValid) {
                setShowUserForm(false);
                setShowInfoForm(true);
            } else {
                setModalVisible(true);
            }
        }
        else if (showInfoForm) {
            const formattedBirthday = handleDateFormat(birthday);

            if (!validateFullName(name)) {
                setErrorMessage('Họ và tên không được để trống.');
                isValid = false;
            } else if (!validatePhoneNumber(phone)) {
                setErrorMessage('Số điện thoại không hợp lệ. Phải thuộc các đầu số Việt Nam.');
                isValid = false;
            } else if (!validateEmail(email)) {
                setErrorMessage('Email không hợp lệ. Email phải thuộc định dạng @gmail.com');
                isValid = false;
            } else if (!formattedBirthday) {
                setErrorMessage('Ngày sinh không hợp lệ. Vui lòng nhập theo định dạng dd/mm/yyyy.');
                isValid = false;
            }

            if (isValid) {
                try {
                    const response = await http.post('auth/noauth/send', { phone });
                    if (response.status === 200) {
                        setOTPConfirm(response.data)
                        setErrorMessage('');
                        console.log('OTP đã được gửi thành công đến số điện thoại.', response.data);
                        setShowInfoForm(false);
                        setShowOTPForm(true);
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
                setModalVisible(true);
            }
        }
        else if (showOTPForm) {
            if (String(otp.trim()) !== String(otpConfirm?.trim?.() || otpConfirm)) {
                setErrorMessage('Mã OTP không đúng');
                setModalVisible(true);
                isValid = false;
                console.log(`otp: ${otp}, otpConfirm: ${otpConfirm}`);
            }
             if (isValid) {
                try {
                    const response = await http.post(
                        "auth/noauth/validate",
                        { phone:phone, otp:otp }, 
                        {
                            headers: {
                                "Content-Type": "application/json",
                            },
                        }
                    );
                    if (response.status === 200) {
                        console.log(response.data);
                        signToken = response.data.accessToken; 
                        console.log(signToken)
                        await handleSubmit(); 
                    } else {
                        setErrorMessage('Xác thực otp thất bại');
                        setModalVisible(true);
                    }
                } catch (error) {
                    setErrorMessage("Xác thực OTP không thành công. Vui lòng thử lại.");
                    setModalVisible(true);
                }
            } else {
                setModalVisible(true);
            }
        }
    };

    const handleSubmit = async () => {
        try {
            const formattedBirthday = handleDateFormat(birthday);
            const genderValue = gender === 'Nam' ? true : false;
          const response = await http.post(
            "auth/account/signup/1",
            {
              username: username,
              name: name,
              email: email,
              password: password,
              gender: genderValue,
              phone: phone,
              birthday: formattedBirthday,
              image: defaultAVT,
            },
            {
              headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${signToken}`, 
              },
            }
          );
      
          if (response.status === 200) {
            console.log(response.data);
            await handleLogin(); 
          } else {
            setErrorMessage('Lỗi đăng ký');
            setModalVisible(true);
          }
        } catch (error) {
          console.error("Có lỗi xảy ra trong quá trình gửi thông tin:", error);
        }
      };
      const handleLogin = async () => {
        try {
          const response = await http.post("auth/noauth/signin", {
            username: username,
            password: password,
          });
    
          if (response.status === 200) {
            const { accessToken } = response.data;
            await AsyncStorage.setItem('accessToken', accessToken);
            setErrorMessage('Đăng ký thành công');
            setModalVisible(true);
            setTimeout(() => {
                setModalVisible(false);
                navigation.navigate('DashboardScreen');
            }, 1000);
          } 
        } catch (error) {
            setErrorMessage('Đăng ký thất bại');
            setModalVisible(true);
        }
      };
    
    const handleBack = () => {
        if (showUserForm) {
            navigation.navigate('HomeScreen');
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
                            <Text style={styles.title}>Đăng ký</Text>
                            <TextInput
                                style={styles.input}
                                placeholder="Tài khoản"
                                placeholderTextColor="#888"
                                value={username}
                                onChangeText={setUsername}
                                maxLength={32}
                            />

                            <TextInput
                                style={[styles.input, { flex: 1 }]}
                                placeholder="Mật khẩu"
                                placeholderTextColor="#888"
                                value={password}
                                onChangeText={setPassword}
                                secureTextEntry={!showPassword}
                                maxLength={32}
                            />
                            <TouchableOpacity onPress={() => setShowPassword(!showPassword)}>
                                <Ionicons
                                    name={showPassword ? "eye-off" : "eye"}
                                    size={26}
                                    color="gray"
                                    style={styles.eyeIcon}
                                />
                            </TouchableOpacity>

                            <TextInput
                                style={[styles.input, { flex: 1 }]}
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


                            <TouchableOpacity style={styles.useful}>
                                <Text style={styles.linkText}>Hướng dẫn đăng ký</Text>
                            </TouchableOpacity>
                            <View style={styles.buttonsContainer}>
                                <TouchableOpacity style={styles.backButton} onPress={handleBack}>
                                    <Text style={styles.backButtonText}>Trang chủ</Text>
                                </TouchableOpacity>
                                <TouchableOpacity style={styles.nextButton} onPress={handleNext}>
                                    <Text style={styles.nextButtonText}>Tiếp tục</Text>
                                </TouchableOpacity>
                            </View>
                        </>
                    )}
                    {showInfoForm && (
                        <>
                            <Text style={styles.subTitle}>Thông tin bổ sung</Text>
                            <TextInput
                                style={styles.input}
                                placeholder="Họ và tên"
                                placeholderTextColor="#888"
                                value={name}
                                onChangeText={setName}
                            />
                            <TextInput
                                style={styles.input}
                                placeholder="Số điện thoại"
                                placeholderTextColor="#888"
                                value={phone}
                                onChangeText={setPhone}
                                maxLength={10}
                            />
                            <TextInput
                                style={styles.input}
                                placeholder="Email"
                                placeholderTextColor="#888"
                                value={email}
                                onChangeText={setEmail}
                            />
                            <View style={styles.rowContainer}>
                                <TextInput
                                    style={[styles.input, { flex: 7 }]}
                                    placeholder="Ngày sinh (dd/mm/yyyy)"
                                    placeholderTextColor="#888"
                                    value={birthday}
                                    onChangeText={setBirthday}
                                />
                                <TouchableOpacity onPress={() => setGender(gender === 'Nam' ? 'Nữ' : 'Nam')} style={{ flex: 2 }}>
                                    <Text style={styles.dropdown}>{gender || 'Chọn giới tính'}</Text>
                                </TouchableOpacity>
                            </View>
                            <View style={styles.buttonsContainer}>
                                <TouchableOpacity style={styles.backButton} onPress={handleBack}>
                                    <Text style={styles.backButtonText}>Quay lại</Text>
                                </TouchableOpacity>
                                <TouchableOpacity style={styles.nextButton} onPress={handleNext}>
                                    <Text style={styles.nextButtonText}>Tiếp tục</Text>
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

            <View style={styles.container2}>
                <TouchableOpacity style={styles.signInOptions}
                    onPress={() => navigation.navigate('LoginScreen')}>
                    <Text style={styles.linkText}>Đã có tài khoản</Text>
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
        marginBottom: 50,
        color: '#333',
    },
    subTitle: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 30,
        color: '#333',
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

        marginBottom: 10,
        borderColor: '#ddd',
        borderWidth: 1,
        color: '#333',
        fontSize: 18
    },
    confirmInput: {
        borderRadius: 15,

        backgroundColor: '#fff',
        width: '100%',
        padding: 15,
        marginTop: 80,

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
    label: {
        fontSize: 16,
    },
    dropdown: {
        fontSize: 18,
        borderRadius: 10,
        padding: 10,
        borderWidth: 1,
        width:70,
        marginLeft:200,
        left:-180,
        height:50,
        textAlign:'center',
        marginTop:-10
    },
    eyeIcon: {
        position: 'absolute',
        marginTop: -50,
        right: -210
    },
    eyeIcon2: {
        position: 'absolute',
        right: -210,
        marginTop: -50,
    },
    rowContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: 10,

    },
});

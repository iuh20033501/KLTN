import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';
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



export default function ChangePassword({navigation}: {navigation: any}) {
    const [oldPassword, setOldPassword] = useState('');
    const [password, setPassword] = useState('');
    const [verifyPassword, setVerifyPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState(''); 
    const [modalVisible, setModalVisible] = useState(false);

    const handleChangePassword = async () => {
        if (oldPassword.length < 6 || oldPassword.length > 32) {
          setErrorMessage('Mật khẩu cũ phải có ít nhất 6 ký tự và tối đa 32 ký tự.');
          return;
        }
      
        if (password.length < 6 || password.length > 32) {
          setErrorMessage('Mật khẩu mới phải có ít nhất 6 ký tự và tối đa 32 ký tự.');
          return;
        }
      
        if (password !== verifyPassword) {
          setErrorMessage('Mật khẩu mới và xác nhận mật khẩu không khớp.');
          return;
        }
      
        try {
          const token = await AsyncStorage.getItem('accessToken');
          if (!token) {
            setErrorMessage('Token không tồn tại hoặc đã hết hạn. Vui lòng đăng nhập lại.');
            return;
          }
      
          const response = await http.post(
            "auth/account/changepass",
            {
              newPass: password,
              oldPass: oldPassword,
            },
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );
      
          if (response.status === 200) {
            console.log(response.data);
            setErrorMessage('Đổi mật khẩu thành công'); 
            navigation.navigate('DashboardScreen');
          } else {
            setErrorMessage('Lỗi đổi mật khẩu');
          }
        } catch (error) {
          if (axios.isAxiosError(error)) {
            if (error.response) {
              console.error("Response error:", error.response.data);
              setErrorMessage(error.response.data.message || 'Đã có lỗi xảy ra khi đổi mật khẩu.');
            } else if (error.request) {
              console.error("Request error:", error.request);
              setErrorMessage('Không có phản hồi từ máy chủ. Vui lòng kiểm tra kết nối.');
            }
          } else if (error instanceof Error) {
            console.error("General error:", error.message);
            setErrorMessage('Đã có lỗi xảy ra. Vui lòng thử lại sau.');
          }
        }
      };
      const handleBack = () => {
        navigation.goBack()
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
                    <Text style={styles.title}>Thay đổi mật khẩu</Text>
                    <TextInput
                        style={styles.input}
                        placeholder="Nhập mật khẩu cũ"
                        placeholderTextColor="#888"
                        value={oldPassword}
                        onChangeText={setOldPassword}
                        secureTextEntry
                    />
                     <TextInput
                        style={styles.input}
                        placeholder="Nhập mật khẩu mới"
                        placeholderTextColor="#888"
                        value={password}
                        onChangeText={setPassword}
                        secureTextEntry
                    />
                     <TextInput
                        style={styles.input}
                        placeholder="Xác thực mật khẩu mới"
                        placeholderTextColor="#888"
                        value={verifyPassword}
                        onChangeText={setVerifyPassword}
                        secureTextEntry
                    />
                    <View style={styles.buttonsContainer}>
                    <TouchableOpacity style={styles.backButton}
                                    onPress={handleBack}>
                                    <Text style={styles.backButtonText}>Quay lại</Text>
                                </TouchableOpacity>
                        <TouchableOpacity style={styles.nextButton} onPress={handleChangePassword}>
                            <Text style={styles.nextButtonText}>Xác nhận</Text>
                        </TouchableOpacity>
                    </View>
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
        marginTop:-150   
    },
    formContainer: {
        alignItems: 'center',
        width: '100%',
        justifyContent: 'center',
        
    },
    
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        top:-60,
        color: '#333',
    },
    input: {
        backgroundColor: '#fff',
        width: '100%',
        padding: 15,
        borderRadius: 5,
        marginBottom: 10,
        borderColor: '#ddd',
        borderWidth: 1,
        color: '#333',
        fontSize: 18,
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
        top: 30
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
        top:70,
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



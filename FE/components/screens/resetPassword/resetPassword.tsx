import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, TouchableOpacity, ScrollView, Alert, ImageBackground } from 'react-native';
import { FontAwesome } from '@expo/vector-icons'; 
import Icon from 'react-native-vector-icons/Ionicons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import http from '@/utils/http';
import { AxiosError } from 'axios'; // Import AxiosError từ axios để kiểm tra lỗi

export default function ResetPassword({ navigation }: { navigation: any }) {
  const [passWord, setPassWord] = useState('');
  const [verifyPassWord, setVerifyPassWord] = useState('');
  const backgroundImg = require("../../../image/background/bg7.png");

  const handleChangePassword = async () => {
    if (passWord.length < 5 || passWord.length > 32) {
      Alert.alert('Lỗi', 'Mật khẩu mới phải có ít nhất 6 ký tự và tối đa 32 ký tự.');
      return;
    }

    if (passWord !== verifyPassWord) {
      Alert.alert('Lỗi', 'Mật khẩu mới và xác nhận mật khẩu không khớp.');
      return;
    }

    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        Alert.alert('Lỗi', 'Token không tồn tại hoặc đã hết hạn.');
        return;
      }
      console.log('Token:', token);
      
      const response = await http.post(
        "auth/account/reset",
        {
          password: passWord,  
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      console.log({
        password: passWord,  
      });

      if (response.status === 200) {
        console.log(response.data);
        Alert.alert('Thành công', 'Đã cập nhật lại mật khẩu');
        navigation.navigate('LoginScreen');
      } else {
        throw new Error("Lỗi đổi mật khẩu");
      }
    } catch (error: unknown) {
     
      if (error instanceof AxiosError) {
        if (error.response) {
          console.error("Response error:", error.response.data); 
          Alert.alert('Thất bại', error.response.data.message || 'Đã có lỗi xảy ra khi reset mật khẩu.');
        } else if (error.request) {
          console.error("Request error:", error.request);
          Alert.alert('Thất bại', 'Không có phản hồi từ server. Vui lòng kiểm tra kết nối.');
        }
      } else {
        
        console.error("General error:", (error as Error).message);  
        Alert.alert('Thất bại', 'Đã có lỗi xảy ra khi reset mật khẩu.');
      }
    }
  };

  return (
    <ImageBackground
      source={backgroundImg}
      style={styles.backgroundImage}
      resizeMode="cover"
    >
      <TouchableOpacity onPress={() => navigation.goBack()} style={{ padding: 15, alignSelf: 'baseline' }}>
        <Icon name="arrow-back-outline" size={24} color="black" />
      </TouchableOpacity>
      <ScrollView contentContainerStyle={styles.container}>
        <Text style={styles.title}>Đổi mật khẩu</Text>
        <View style={styles.inputContainer}>
          <TextInput
            style={styles.input}
            placeholder="Nhập mật khẩu mới"
            secureTextEntry
            value={passWord}
            onChangeText={setPassWord}
          />
        </View>
        <View style={styles.inputContainer}>
          <TextInput
            style={styles.input}
            placeholder="Xác thực mật khẩu mới"
            secureTextEntry
            value={verifyPassWord}
            onChangeText={setVerifyPassWord}
          />
        </View>

        <TouchableOpacity style={styles.button} onPress={() => handleChangePassword()}>
          <Text style={styles.buttonText}>Xác nhận</Text>
        </TouchableOpacity>
      </ScrollView>
    </ImageBackground>
  );
}

  const styles = StyleSheet.create({
    backgroundImage: {
      flex: 1,
      justifyContent: 'center',
      width: "100%",
      height: "100%",
    
    },
    container: {
      flex: 1,
      alignItems: 'center',
      width: '100%',
    },
    innerContainer: {
      alignItems: 'center',
      width: '100%',
    },
    title: {
      fontSize: 25,
      fontWeight: 'bold',
      marginBottom: 50,
      marginTop: 100,
      textAlign: 'center'
    },
    input: {
      width: '100%',
      height: 50,
      borderColor: '#DADADA',
      borderWidth: 1,
      borderRadius: 10,
      paddingLeft: 15,
      marginBottom: 15,
      backgroundColor: '#FFFFFF',
      fontSize: 18,
    },
    button: {
      width: '77%',
      backgroundColor: '#00bf63',
      padding: 15,
      borderRadius: 20,
      alignItems: 'center',
      marginTop: 30,
      marginLeft: -4,
    },
    buttonText: {
      fontSize: 18,
      color: '#333333',
      fontWeight: 'bold',
    },
  
    termsText: {
      fontSize: 13,
      color: '#888',
      textAlign:'center'
    },

    inputContainer: {
      width: '85%',
      borderRadius: 10,
      marginBottom: 10,
      paddingHorizontal: 15,
    },
    eyeIcon: {
      position: 'absolute',
      right: 25,
      marginTop: 14,
    },
    link2: {
      color: '#007bff',
      textDecorationLine: 'underline',
      fontWeight: 'bold',
      marginTop: 18,
      fontSize: 14,
      alignSelf: 'center',
    },
  });

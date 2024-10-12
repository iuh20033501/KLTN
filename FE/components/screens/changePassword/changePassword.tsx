import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, TouchableOpacity, ScrollView, Alert, ImageBackground } from 'react-native';
import { FontAwesome } from '@expo/vector-icons';
import Icon from 'react-native-vector-icons/Ionicons';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default function ChangePassword({ navigation }: { navigation: any }) {
  const [oldPassowrd, setOldPassowrd] = useState('');
  const [passWord, setPassWord] = useState('');
  const [verifyPassWord, setVerifyPassWord] = useState('');
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);
  const [isPasswordVisible2, setIsPasswordVisible2] = useState(false);
  const [isPasswordVisible3, setIsPasswordVisible3] = useState(false);

  const backgroundImg = require("../../../image/background/bg7.png");
  const handleChangePassword = async () => {
    if (oldPassowrd.length < 5 || oldPassowrd.length > 32) {
      Alert.alert('Lỗi', 'Mật khẩu cũ phải có ít nhất 6 ký tự và tối đa 32 ký tự.');
      return;
    }

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
      const response = await http.post("auth/account/changepass",
        {
          newPass: passWord,
          oldPass: oldPassowrd,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        console.log('Token:', token);
        console.log({
          oldPassword: oldPassowrd,
          newPassword: passWord,
        });
      if (response.status === 200) {
        console.log(response.data);
        navigation.navigate('MainTabs');
        Alert.alert('Thành công', 'Mật khẩu đã được thay đổi thành công!');
      } else {
        throw new Error("Lỗi đổi mật khẩu");
      }
    } catch (error) {
      console.error("Có lỗi xảy ra trong quá trình đổi mật khẩu:", error);
      Alert.alert('Thất bại', 'Đã có lỗi xảy ra khi đổi mật khẩu.');
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
            placeholder="Nhập mật khẩu cũ"
            value={oldPassowrd}
            onChangeText={setOldPassowrd}
            secureTextEntry={!isPasswordVisible}
          />
          <TouchableOpacity onPress={() => setIsPasswordVisible(!isPasswordVisible)} style={styles.eyeIcon}>
              <FontAwesome name={isPasswordVisible ? 'eye-slash' : 'eye'} size={20} color="gray" />
            </TouchableOpacity>
        </View>
        <View style={styles.inputContainer}>
          <TextInput
            style={styles.input}
            placeholder="Nhập mật khẩu mới"
            value={passWord}
            onChangeText={setPassWord}
            secureTextEntry={!isPasswordVisible2}
          />
          <TouchableOpacity onPress={() => setIsPasswordVisible2(!isPasswordVisible2)} style={styles.eyeIcon}>
              <FontAwesome name={isPasswordVisible ? 'eye-slash' : 'eye'} size={20} color="gray" />
            </TouchableOpacity>
        </View>
        <View style={styles.inputContainer}>
          <TextInput
            style={styles.input}
            placeholder="Xác thực mật khẩu mới"
            value={verifyPassWord}
            onChangeText={setVerifyPassWord}
            secureTextEntry={!isPasswordVisible3}
          />
          <TouchableOpacity onPress={() => setIsPasswordVisible3(!isPasswordVisible3)} style={styles.eyeIcon}>
              <FontAwesome name={isPasswordVisible ? 'eye-slash' : 'eye'} size={20} color="gray" />
            </TouchableOpacity>
          <Text style={styles.termsText}>
            Mã OTP gồm 6 chữ số sẽ được gửi về số điện thoại đang liên kết với tài khoản học viên
          </Text>
        </View>

        <TouchableOpacity style={styles.button}
          onPress={() => {
            handleChangePassword()
          }}>
          <Text style={styles.buttonText}>Đổi mật khẩu</Text>
        </TouchableOpacity>
      </ScrollView>
    </ImageBackground>
  );
};

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
    textAlign: 'center'
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

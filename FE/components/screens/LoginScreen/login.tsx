import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, Alert, Image, KeyboardAvoidingView, Platform, ImageBackground } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { FontAwesome } from '@expo/vector-icons'; 
import { useNavigation } from '@react-navigation/native';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import Icon from 'react-native-vector-icons/Ionicons';

export default function LoginScreen({navigation}: {navigation: any}) {
  const [userName, setUserName] = useState('');
  const [passWord, setPassWord] = useState('');
  const [isPasswordVisible, setIsPasswordVisible] = useState(false);
  const backgroundImg = require("../../../image/background/bg7.png"); 
  const logoEFY = require("../../../image/logo/EFY.png");

  const handleLogin = async () => {
    if (userName.trim() === '') {
      Alert.alert("Lỗi", "Tài khoản không được để trống");
      return;
    }
    if (passWord.trim() === '') {
      Alert.alert("Lỗi", "Mật khẩu không được để trống");
      return;
    }
    if (userName.length < 6 || userName.length > 32) {
      Alert.alert("Lỗi", "Tài khoản phải có độ dài từ 6 đến 32 ký tự");
      return;
    }
    if (passWord.length < 6 || passWord.length > 32) {
      Alert.alert("Lỗi", "Mật khẩu phải có độ dài từ 6 đến 32 ký tự");
      return;
    }
  
    try {
      const response = await http.post("auth/noauth/signin", {
        username: userName,  
        password: passWord,
      });

      if (response.status === 200) {
        const { accessToken } = response.data;  
        await AsyncStorage.setItem('accessToken', accessToken);  
        console.log("Đăng nhập thành công: ", accessToken);
        navigation.navigate('MainTabs'); 
      } else {
        Alert.alert("Đăng nhập thất bại", "Sai thông tin đăng nhập");
      }
    } catch (error) {
      Alert.alert("Đăng nhập thất bại","Vui lòng kiểm tra lại tài khoản và mật khẩu của bạn");
    }
  };

  const handleInputChange = (text: string, setFunction: { (value: React.SetStateAction<string>): void; (value: React.SetStateAction<string>): void; (arg0: any): void; }) => {
  
    setFunction(text.replace(/\s+/g, ''));
  };
  return (
    <ImageBackground
      source={backgroundImg}
      style={styles.backgroundImage}
      resizeMode="cover"
    >
        <TouchableOpacity  onPress={() => navigation.goBack()} style={{padding:15, alignSelf:'baseline'}}>
        <Icon  name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
    <SafeAreaView style={{ flex: 1, marginTop: 150 }}>
      
      <View style={styles.innerContainer}>
        <Image source={logoEFY} style={styles.logo} />
        <Text style={styles.welcomeText}>Xin chào!</Text>
        <Text style={styles.titleText}>Đăng nhập để tiếp tục</Text>
        <Text style={styles.subTitleText}>Ứng dụng dành riêng cho học viên của EFY </Text>
        <View style={styles.inputContainer}>
          <TextInput
            style={styles.input}
            placeholder="Tài khoản đăng kí với EFY"
            value={userName}
            onChangeText={(text) => handleInputChange(text, setUserName)}
          />
        </View>
        <View style={styles.inputContainer}>
          <TextInput
            style={styles.input}
            placeholder="Mật khẩu của bạn"
            value={passWord}
            onChangeText={(text) => handleInputChange(text, setPassWord)}
            secureTextEntry={!isPasswordVisible}
          />
          <TouchableOpacity onPress={() => setIsPasswordVisible(!isPasswordVisible)} style={styles.eyeIcon}>
            <FontAwesome name={isPasswordVisible ? 'eye-slash' : 'eye'} size={20} color="gray" />
          </TouchableOpacity>
        </View>
        <View>
        <Text style={styles.termsText}>
          Bằng việc tiếp tục, bạn đã chấp nhận và đồng ý với những 
        </Text>
        <TouchableOpacity>
            <Text style={styles.linkText}>điều kiện và điều khoản sử dụng ứng dụng.</Text>
          </TouchableOpacity>
        </View>
        <TouchableOpacity style={styles.button} onPress={() =>handleLogin()}>
          <Text style={styles.buttonText}>Đăng nhập</Text>
        </TouchableOpacity>
        <TouchableOpacity style={{ marginTop: 30 }} onPress={() =>navigation.navigate('RequestFogetPassword')}>
          <Text style={{color: 'black', fontSize: 16, fontWeight: 'bold' }}>Quên mật khẩu</Text>
        </TouchableOpacity>

      </View>
    </SafeAreaView>
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
    backgroundColor: '#fff',
    position: 'static'
  },
  scrollContainer: {
    flexGrow: 1,
    justifyContent: 'center',
  },
  innerContainer: {
    padding: 20,
    justifyContent: 'center',
    alignItems: 'center',
  },
  logo: {
    width: 80,
    height: 80,
    marginTop: -160,
    borderRadius: 20,
  },
  welcomeText: {
    marginTop: 20,
    fontSize: 30,
    marginBottom: 10,
  },
  titleText: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  subTitleText: {
    fontSize: 14,
    marginBottom: 30,
  },

  inputContainer: {
    width: '87%',
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 10,
    marginBottom: 20,
    paddingHorizontal: 15,
  },
  input: {
    height: 50,
    fontSize: 18,
  },
  termsText: {
    fontSize: 13,
    marginBottom: 20,
    color: '#888',
    
  },
  linkText: {
    color: '#007bff',
    textDecorationLine: 'underline',
    marginLeft:40,
    marginTop:-20,
    fontSize: 13,
  },
  button: {
    backgroundColor: '#00bf63', 
    width: '87%',
    paddingVertical: 15,
    borderRadius: 30,
    alignItems: 'center',
    marginTop:30
  },
  buttonText: {
    fontSize: 16,
    fontWeight: 'bold',
    color: 'black',
  },
  eyeIcon: {
    position: 'absolute',
    right: 15,
    marginTop: 14,
  },
});

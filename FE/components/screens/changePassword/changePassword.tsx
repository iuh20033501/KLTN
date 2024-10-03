import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, TouchableOpacity, ScrollView, Alert, ImageBackground } from 'react-native';
import { FontAwesome } from '@expo/vector-icons'; 

export default function ChangePassword({navigation}: {navigation: any}) {
  const [oldPassowrd, setOldPassowrd] = useState('');
  const [passWord, setPassWord] = useState('');
  const [verifyPassWord, setVerifyPassWord] = useState('');
  const backgroundImg = require("../../../image/background/bg7.png"); 

  

  return (
    <ImageBackground
      source={backgroundImg}
      style={styles.backgroundImage}
      resizeMode="cover"
    >
      <ScrollView contentContainerStyle={styles.container}>
        <Text style={styles.title}>Đổi mật khẩu</Text>
        <View style={styles.inputContainer}>
          <TextInput
            style={styles.input}
            placeholder="Nhập mật khẩu cũ"
            value={oldPassowrd}
            onChangeText={setOldPassowrd}
          />
        </View>
        <View style={styles.inputContainer}>
          <TextInput
            style={styles.input}
            placeholder="Nhập mật khẩu mới"
            value={passWord}
            onChangeText={setPassWord}
          />
        </View>
        <View style={styles.inputContainer}>
          <TextInput
            style={styles.input}
            placeholder="Xác thực mật khẩu mới"
            value={verifyPassWord}
            onChangeText={setVerifyPassWord}
          />
           <Text style={styles.termsText}>
          Mã OTP gồm 6 chữ số sẽ được gửi về số điện thoại đang liên kết với tài khoản học viên
        </Text>
        </View>
        
        <TouchableOpacity style={styles.button}
        onPress={() => navigation.navigate('AuthenticationPass')}>
          <Text style={styles.buttonText}>Tiếp tục</Text>
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

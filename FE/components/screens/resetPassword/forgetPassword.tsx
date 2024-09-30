import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, TouchableOpacity, ScrollView, Alert, ImageBackground } from 'react-native';
import { FontAwesome } from '@expo/vector-icons'; 

export default function ForgetPassword({navigation}: {navigation: any}) {
  const [userName, setUserName] = useState('');
  const backgroundImg = require("../../../image/background/bg.png"); 

  return (
    <ImageBackground
      source={backgroundImg}
      style={styles.backgroundImage}
      resizeMode="cover"
    >
      <ScrollView contentContainerStyle={styles.container}>
        <Text style={styles.title}>Quên mật khẩu</Text>
        <View style={styles.inputContainer}>
          <TextInput
            style={styles.input}
            placeholder="Nhập tài khoản"
            value={userName}
            onChangeText={setUserName}
          />
        </View>
        
        <View style={styles.inputContainer}></View>
        <Text style={styles.terms}>
          Một đoạn mã OTP bao gồm 6 chữ số sẽ được gửi đến số điện thoại đăng ký tài khoản, vui lòng kiểm tra!
        </Text>
        <TouchableOpacity style={styles.button}
        onPress={() =>navigation.navigate('AuthenticationPass')}
       >
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
    backgroundColor: '#FFC125',
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
  terms: {
    fontSize: 12,
    textAlign: 'center',
    color: '#666666',
    width: '77%',
  },
  link: {
    color: '#007bff',
    textDecorationLine: 'underline',
    fontWeight: 'bold',
    marginTop: 1,
    textAlign: 'center',
    fontSize: 12,
  },
  footerNote: {
    marginTop: 40,
    fontSize: 11,
    color: '#666666',
    marginLeft: 5,
    width: '85%'
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

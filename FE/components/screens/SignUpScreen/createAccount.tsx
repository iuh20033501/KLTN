import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, TouchableOpacity, ScrollView, Alert, ImageBackground } from 'react-native';
import { FontAwesome } from '@expo/vector-icons';
import Icon from 'react-native-vector-icons/Ionicons';

export default function CreateAccount({ navigation, route }: { navigation: any, route: any }) {
  const [userName, setUserName] = useState('');
  const [passWord, setPassWord] = useState('');
  const [verifyPassWord, setVerifyPassWord] = useState('');

  const [isPasswordVisible, setIsPasswordVisible] = useState(false);
  const [isPasswordVisible2, setIsPasswordVisible2] = useState(false);

  const backgroundImg = require("../../../image/background/bg7.png");

  const handleContinue = () => {
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
    if (passWord !== verifyPassWord) {
        Alert.alert('Mật khẩu và mật khẩu nhập lại không khớp');
        return;
    } else {
        navigation.navigate('FillUpInformation', { userName, passWord });
      }
    };


    return (
      <ImageBackground
        source={backgroundImg}
        style={styles.backgroundImage}
        resizeMode="cover"
      >
        <ScrollView contentContainerStyle={styles.container}>
        <TouchableOpacity  onPress={() => navigation.goBack()} style={{padding:15, alignSelf:'baseline'}}>
        <Icon  name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
          <Text style={styles.title}>Bước đầu tiên để trở thành một thành viên của ngôi nhà EFY!</Text>
          <View style={styles.inputContainer}>
            <TextInput
              style={styles.input}
              placeholder="Nhập tài khoản"
              value={userName}
              onChangeText={setUserName}
            />
          </View>
          <View style={styles.inputContainer}>
            <TextInput
              style={styles.input}
              placeholder="Nhập mật khẩu"
              value={passWord}
              onChangeText={setPassWord}
              secureTextEntry={!isPasswordVisible}
            />
            <TouchableOpacity onPress={() => setIsPasswordVisible(!isPasswordVisible)} style={styles.eyeIcon}>
              <FontAwesome name={isPasswordVisible ? 'eye-slash' : 'eye'} size={20} color="gray" />
            </TouchableOpacity>
          </View>

          <View style={styles.inputContainer}>
            <TextInput
              style={styles.input}
              placeholder="Nhập lại mật khẩu"
              value={verifyPassWord}
              onChangeText={setVerifyPassWord}
              secureTextEntry={!isPasswordVisible2}
            />
            <TouchableOpacity onPress={() => setIsPasswordVisible2(!isPasswordVisible2)} style={styles.eyeIcon}>
              <FontAwesome name={isPasswordVisible2 ? 'eye-slash' : 'eye'} size={20} color="gray" />
            </TouchableOpacity>
          </View>

          <View style={styles.inputContainer}></View>
          <Text style={styles.terms}>
            Bằng việc tiếp tục, bạn đã chấp nhận và đồng ý với những
          </Text>
          <TouchableOpacity>
            <Text style={styles.link}>điều kiện và điều khoản sử dụng ứng dụng.</Text>
          </TouchableOpacity>

          <TouchableOpacity style={styles.button}
            onPress={() => handleContinue()}>
            <Text style={styles.buttonText}>Tiếp tục</Text>
          </TouchableOpacity>
          <Text style={styles.footerNote}>
            Ứng dụng dành riêng cho học viên có độ tuổi từ 8 tuổi. Nếu có nhu cầu được tư vấn về các chương trình học, quý phụ huynh vui lòng
          </Text>
          <TouchableOpacity>
            <Text style={styles.link2}>Tìm hiểu thêm</Text>
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
      marginTop: 50,
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
    terms: {
      fontSize: 12,
      textAlign: 'center',
      color: '#666666',
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

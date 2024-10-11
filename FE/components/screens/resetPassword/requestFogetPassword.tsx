import React, { useEffect, useState } from 'react';
import { StyleSheet, Text, View, TextInput, TouchableOpacity, ScrollView, Alert, ImageBackground } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import http from '@/utils/http'; 
import AsyncStorage from '@react-native-async-storage/async-storage';

export default function RequestFogetPassword({ navigation }: { navigation: any }) {
  const [userName, setUserName] = useState(''); 
  const [data, setData] = useState<any[]>([]);  
  const [loading, setLoading] = useState(true); 
  const backgroundImg = require("../../../image/background/bg7.png"); 

  
  const handleContinue = () => {
    if (!userName.trim()) {
      Alert.alert('Lỗi', 'Tài khoản không được để trống');
      return;
    }
    const userAccount = data.find((item) => item.tenDangNhap === userName);
    if (!userAccount) {
      Alert.alert('Lỗi', 'Tài khoản không tồn tại');
    } else {
      const phoneNumber = userAccount?.user?.sdt;
      if (!phoneNumber) {
        Alert.alert('Lỗi', 'Không tìm thấy số điện thoại liên kết với tài khoản này');
      } else {
        sendOTP(phoneNumber);
      }
    }
  };

  const sendOTP = async (phoneNumber: string) => {
    try {
      const response = await http.post('auth/noauth/send', { phone: phoneNumber });
      if (response.status === 200) {
        Alert.alert('Thành công', 'Mã OTP đã được gửi đến số điện thoại của bạn.');
        const { accessToken } = response.data;  
        await AsyncStorage.setItem('accessToken', accessToken);  
        navigation.navigate('AuthenticationForgetPassword', { phoneNumber });
        console.log(response.data)
      } else {
        Alert.alert('Lỗi', response.data.message || 'Có lỗi xảy ra, vui lòng thử lại.');
      }
    } catch (error) {
      console.error('Lỗi khi gửi OTP:', error);
      Alert.alert('Lỗi', 'Có lỗi xảy ra khi gửi OTP, vui lòng thử lại sau.');
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await http.get('auth/noauth/findAll');
        if (response.status === 200) {
          setData(response.data);  
          console.log(response.data);  
        } else {
          Alert.alert('Lỗi', 'Không thể lấy dữ liệu người dùng');
        }
      } catch (error) {
        console.error('Error fetching data:', error);
        Alert.alert('Lỗi', 'Có lỗi xảy ra khi lấy dữ liệu');
      } finally {
        setLoading(false);  
      }
    };

    fetchData(); 
  }, []);

  return (
    <ImageBackground source={backgroundImg} style={styles.backgroundImage} resizeMode="cover">
      <TouchableOpacity onPress={() => navigation.goBack()} style={{ padding: 15, alignSelf: 'baseline' }}>
        <Icon name="arrow-back-outline" size={24} color="black" />
      </TouchableOpacity>
      <ScrollView contentContainerStyle={styles.container}>
        <Text style={styles.title}>Quên mật khẩu</Text>
        <View style={styles.inputContainer}>
          <TextInput
            style={styles.input}
            placeholder="Nhập tài khoản đăng ký của bạn"
            value={userName}
            onChangeText={setUserName}
          />
          <Text style={styles.termsText}>
            Mã OTP gồm 6 chữ số sẽ được gửi về số điện thoại đang liên kết với tài khoản học viên
          </Text>
        </View>
        <TouchableOpacity style={styles.button} onPress={handleContinue}>
          <Text style={styles.buttonText}>Tiếp tục</Text>
        </TouchableOpacity>
      </ScrollView>
    </ImageBackground>
  );
}

const styles = StyleSheet.create({
  backgroundImage: {
    flex: 1,
    justifyContent: 'center',
    width: '100%',
    height: '100%',
  },
  container: {
    flex: 1,
    alignItems: 'center',
    width: '100%',
  },
  title: {
    fontSize: 25,
    fontWeight: 'bold',
    marginBottom: 50,
    marginTop: 100,
    textAlign: 'center',
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
    textAlign: 'center',
  },
  inputContainer: {
    width: '85%',
    borderRadius: 10,
    marginBottom: 10,
    paddingHorizontal: 15,
  },
});

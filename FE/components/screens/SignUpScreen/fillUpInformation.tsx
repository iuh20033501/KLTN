import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, TouchableOpacity, ScrollView, Alert, ImageBackground } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import LinearGradient from 'react-native-linear-gradient';
import { RadioButton } from "react-native-paper";
import http from '@/utils/http';

export default function FillUpInformation({ navigation, route }: { navigation: any, route: any }) {
  const { userName, passWord } = route.params || {};
  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [gender, setGender] = useState(true);
  const [gmail, setGmail] = useState('');
  const [birthday, setBirthday] = useState('');
  
  const backgroundImg = require("../../../image/background/bg7.png");

  const gmailRegex = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;
  const validPhonePrefixes = ['032', '033', '034', '035', '036', '037', '038', '039', '081', '082', '083',
    '084', '085', '088', '070', '076', '077', '078', '079', '052', '056', '058', '092', '059', '099'];

  const validatePhone = (phone: string) => {
    const phonePrefix = phone.slice(0, 3);
    return validPhonePrefixes.includes(phonePrefix) && phone.length === 10;
  };

  const isValidDate = (dateString: string) => {
    const parts = dateString.split('/');
    if (parts.length !== 3) return false;

    const [day, month, year] = parts.map(part => parseInt(part, 10));
    const date = new Date(year, month - 1, day); 

    return date.getDate() === day && date.getMonth() === month - 1 && date.getFullYear() === year;
  };

  const handlePost = async () => {
    try {
      const response = await http.post('auth/send', {
        phone: phone,
      });
  
      if (response.status === 200) {
        console.log(response.data);
        Alert.alert("Thành công", "Mã OTP đã được gửi đến số điện thoại của bạn.");
        navigation.navigate('Authentication', {
          userName, passWord, name, phone, gmail, birthday, gender
        });
      } else {
        switch (response.status) {
          case 400:
            Alert.alert("Lỗi", "Số điện thoại không hợp lệ.");
            break;
          case 404:
            Alert.alert("Lỗi", "Không tìm thấy thông tin.");
            break;
          case 500:
            Alert.alert("Lỗi", "Đã xảy ra lỗi máy chủ. Vui lòng thử lại sau.");
            break;
          default:
            Alert.alert("Lỗi", "Đã xảy ra lỗi không xác định. Vui lòng thử lại.");
            break;
        }
      }
    } catch (error) {
      console.log(error);
      Alert.alert("Lỗi", "Đã xảy ra lỗi khi gửi yêu cầu. Vui lòng kiểm tra kết nối mạng và thử lại.");
    }
  };
  

  const handleContinue = () => {
    if (!name.trim()) {
      Alert.alert('Họ tên không được để trống');
    } else if (!phone.trim()) {
      Alert.alert('Số điện thoại không được để trống');
    } else if (!validatePhone(phone)) {
      Alert.alert('Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại của nhà mạng Việt Nam');
    } else if (!gmail.trim()) {
      Alert.alert('Gmail không được để trống');
    } else if (!gmailRegex.test(gmail)) {
      Alert.alert('Vui lòng nhập đúng định dạng Gmail (@gmail.com)');
    } else if (!birthday.trim()) {
      Alert.alert('Ngày sinh không được để trống');
    } else if (!isValidDate(birthday)) {
      Alert.alert('Ngày sinh không hợp lệ. Vui lòng nhập theo định dạng dd/mm/yyyy');
    } else {
      handlePost();
    }
  };

  return (
    <ImageBackground
      source={backgroundImg}
      style={styles.backgroundImage}
      resizeMode="cover"
    >
      <ScrollView contentContainerStyle={styles.container}>
        <View style={styles.innerContainer}>
          <Text style={styles.title}>Nhập thông tin của bạn</Text>
          <TextInput
            style={styles.input}
            placeholder="Số điện thoại"
            placeholderTextColor="#A8A8A8"
            keyboardType="phone-pad"
            value={phone}
            onChangeText={setPhone}
          />

          <TextInput
            style={styles.input}
            placeholder="Họ tên học viên"
            placeholderTextColor="#A8A8A8"
            value={name}
            onChangeText={setName}
          />
          
          <TextInput
            style={styles.input}
            placeholder="Nhập gmail của bạn"
            placeholderTextColor="#A8A8A8"
            value={gmail}
            onChangeText={setGmail}
          />

          <TextInput
            style={styles.input}
            placeholder="Ngày sinh (dd/mm/yyyy)"
            placeholderTextColor="#A8A8A8"
            keyboardType="phone-pad"
            value={birthday}
            onChangeText={setBirthday}
          />

          <View style={styles.radioContainer}>
            <RadioButton.Group
              onValueChange={(newValue) => setGender(newValue === "true")}
              value={gender + ""}
            >
              <View style={{ flexDirection: "row", alignItems: "center", marginTop: -5 }}>
                <Text>Nam</Text>
                <RadioButton value="true" />
                <Text>Nữ</Text>
                <RadioButton value="false" />
              </View>
            </RadioButton.Group>
          </View>

          <Text style={styles.terms}>
            Bằng việc tiếp tục, bạn đã chấp nhận và đồng ý với những
          </Text>
          <TouchableOpacity>
            <Text style={styles.link}>điều kiện và điều khoản sử dụng ứng dụng.</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.button}
            onPress={handleContinue}>
            <Text style={styles.buttonText}>Tiếp tục</Text>
          </TouchableOpacity>
        </View>
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
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    height: '100%'
  },
  innerContainer: {
    alignItems: 'center',
    width: '100%',
    marginTop: -100
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 50,
  },
  input: {
    width: '77%',
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
  },
  buttonText: {
    fontSize: 18,
    color: '#333333',
    fontWeight: 'bold'
  },
  terms: {
    marginTop: 15,
    fontSize: 12,
    color: '#666666',
  },
  link: {
    color: '#007bff',
    textDecorationLine: 'underline',
    fontWeight: 'bold',
    marginTop: 1,
    fontSize: 12,
  },
  radioContainer: {
    flexDirection: 'row'
  }
});

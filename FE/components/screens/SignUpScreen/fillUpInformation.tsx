import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, TouchableOpacity, ScrollView, Alert, ImageBackground, Platform } from 'react-native';
import { RadioButton } from "react-native-paper";
import http from '@/utils/http';
import Icon from 'react-native-vector-icons/Ionicons';
import DateTimePicker from '@react-native-community/datetimepicker';

export default function FillUpInformation({ navigation, route }: { navigation: any, route: any }) {
  const { userName, passWord } = route.params || {};
  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [gender, setGender] = useState(true);
  const [gmail, setGmail] = useState('');
  const [birthday, setBirthday] = useState('');
  const image = "1";

  const [showDatePicker, setShowDatePicker] = useState(false);
  const [selectedDate, setSelectedDate] = useState(new Date());

  const backgroundImg = require("../../../image/background/bg7.png");

  const gmailRegex = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;
  const validPhonePrefixes = ['032', '033', '034', '035', '036', '037', '038', '039', '081', '082', '083', '084', '085', '088', '070', '076', '077', '078', '079', '052', '056', '058', '092', '059', '099'];

  // Format sdt
  const validatePhone = (phone: string) => {
    const phonePrefix = phone.slice(0, 3);
    return validPhonePrefixes.includes(phonePrefix) && phone.length === 10;
  };
  // Format date theo db
  const formatDate = (date: Date) => {
    const year = date.getFullYear();
    const month = (`0${date.getMonth() + 1}`).slice(-2);
    const day = (`0${date.getDate()}`).slice(-2);
    return `${year}-${month}-${day}`;
  };
  //Chọn date
  const onDateChange = (event: any, selectedDate?: Date) => {
    const currentDate = selectedDate || new Date();
    setShowDatePicker(Platform.OS === 'ios');
    setSelectedDate(currentDate);
    setBirthday(formatDate(currentDate));
  };

  // Hàm tính tuổi
  const calculateAge = (birthday: string) => {
    const today = new Date();
    const birthDate = new Date(birthday);
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDifference = today.getMonth() - birthDate.getMonth();

    if (monthDifference < 0 || (monthDifference === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }

    return age;
  };
  // Format hiển thị date
  const displayDate = (dateString: string) => {
    if (!dateString) return 'N/A';
    const [year, month, day] = dateString.split('-');
    return `${day}/${month}/${year}`;
  };
  const handlePost = async () => {
    try {
      const response = await http.post('auth/noauth/send', {
        phone: phone,
      });

      if (response.status === 200) {
        console.log(response.data);
        Alert.alert("Thành công", "Mã OTP đã được gửi đến số điện thoại của bạn.");
        navigation.navigate('Authentication', {
          userName, passWord, name, phone, gmail, birthday, gender, image
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
    if (!phone.trim()) {
      Alert.alert('Lỗi nhập', 'Số điện thoại không được để trống');
    } else if (!validatePhone(phone)) {
      Alert.alert('Lỗi nhập', 'Đầu số không hợp lệ');
    } else if (!name.trim()) {
      Alert.alert('Lỗi nhập', "Họ tên không được để trống");
    } else if (!gmail.trim()) {
      Alert.alert('Lỗi nhập', 'Gmail không được để trống');
    } else if (!gmailRegex.test(gmail)) {
      Alert.alert('Lỗi nhập', 'Vui lòng nhập đúng định dạng Gmail (@gmail.com)');
    } else if (!birthday.trim()) {
      Alert.alert('Ngày sinh không được để trống');
    } else if (calculateAge(birthday) < 8) {
      Alert.alert('Người dùng phải trên 8 tuổi');
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
        <TouchableOpacity onPress={() => navigation.goBack()} style={{ padding: 15, alignSelf: 'baseline' }}>
          <Icon name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
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

          <TouchableOpacity onPress={() => setShowDatePicker(true)} style={styles.input}>
            <Text style={{ color: birthday ? '#000' : '#A8A8A8', fontSize: 18, marginTop: 10 }}>
              {birthday ? displayDate(birthday) : 'Chọn ngày sinh của bạn'}
            </Text>
          </TouchableOpacity>
          {showDatePicker && (
            <DateTimePicker
              value={selectedDate}
              mode="date"
              display={Platform.OS === 'ios' ? 'spinner' : 'default'}
              onChange={onDateChange}
              maximumDate={new Date()}
            />
          )}

          <View style={styles.radioContainer}>
            <RadioButton.Group
              onValueChange={(newValue) => setGender(newValue === "true")}
              value={gender + ""}
            >
              <View style={{ flexDirection: "row", alignItems: "center", marginTop: -5 }}>
                <Text style={{ fontSize: 18 }}>Nam</Text>
                <RadioButton value="true" />
                <Text style={{ fontSize: 18 }}>Nữ</Text>
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
    alignItems: 'center',
    width: '100%',
    height: '100%'
  },
  innerContainer: {
    marginTop: 50,
    alignItems: 'center',
    width: '100%',
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
    flexDirection: 'row',
    alignItems: 'center',
  }
});

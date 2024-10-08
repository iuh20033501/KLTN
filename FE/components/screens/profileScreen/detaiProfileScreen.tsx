import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TextInput, Image, Switch, TouchableOpacity } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import Icon from 'react-native-vector-icons/Ionicons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import http from "@/utils/http";
export default function DetailProfileScreen({navigation}: {navigation: any}) {
  const [phone, setPhone] = useState('');
  const [email, setEmail] = useState('');
  const [birthday, setBirthday] = useState(Date);
  const [gender, setGender] = useState('');
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const avatars = [
    require('../../../image/avatar/1.png'),
    require('../../../image/avatar/2.png'),
    require('../../../image/avatar/3.png'),
    require('../../../image/avatar/4.png'),
    require('../../../image/avatar/5.png'),
    require('../../../image/avatar/6.png'),
    require('../../../image/avatar/7.png'),
    require('../../../image/avatar/8.png'),
];
const getUserInfo = async () => {
  try {
    const token = await AsyncStorage.getItem('accessToken');
    if (token) {
      const response = await http.get('auth/profile', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.status === 200) {
        const userData = response.data;
        setPhone(userData.u.sdt || ''); 
        setEmail(userData.u.email || ''); 
        setBirthday(userData.u.birthday || ''); 
        setGender(userData.u.gioiTinh === true ? 'Male' : 'Female');
        setUser(userData); 
      } else {
        console.error('Lấy thông tin người dùng thất bại.');
      }
    } else {
      console.error('Token không tồn tại');
    }
  } catch (error) {
    console.error('Có lỗi xảy ra khi lấy thông tin người dùng:', error);
  } finally {
    setLoading(false);
  }
};

useEffect(() => {
  getUserInfo();
}, []);


  return (
    <View style={styles.container}>
      <View style={{flexDirection:'row'}}>
      <TouchableOpacity  onPress={() => navigation.goBack()}>
        <Icon  name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>      
      <Text style={styles.title}>Thay đổi thông tin cá nhân</Text>
      </View>
      
      <View style={styles.avatarSection}>
        <Image source={require('../../../image/avatar/1.png')} style={styles.avatar} />
        <Text style={styles.editAvatarText}>Đổi ảnh đại diện</Text>
      </View>

      <View style={styles.formSection}>
        <TextInput
          style={styles.input}
          value={phone}
          onChangeText={setPhone}
          placeholder="Số điện thoại"
        />

        <TextInput
          style={styles.input}
          value={email}
          onChangeText={setEmail}
          placeholder="Email"
        />

        <TextInput
          style={styles.input}
          value={birthday}
          onChangeText={setBirthday}
          placeholder="Ngày sinh"
        />
        <View style={styles.inputRow}>
          <Text style={styles.label}>Gender</Text>
          <TouchableOpacity onPress={() => setGender(gender === 'Male' ? 'Female' : 'Male')}>
            <Text style={styles.dropdown}>{gender}</Text>
          </TouchableOpacity>
        </View>
        <View style={styles.buttonContainer}>
        <TouchableOpacity style={styles.button}>
            <Text style={{ color: 'black', fontSize: 26, fontWeight: 'bold' }}>Cập nhật</Text>
          </TouchableOpacity>
      </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
    marginLeft:10
  },
  avatarSection: {
    marginTop:30,
    alignItems: 'center',
    marginBottom: 20,
  },
  buttonContainer: {
    marginTop:50,
    alignItems: 'center',
  },
  button: {
    width: 300,
    height: 45,
    backgroundColor: "#00bf63",
    borderRadius: 15,
    justifyContent: 'center',
    alignItems: 'center',
  
  },
  avatar: {
    width: 100,
    height: 100,
    borderRadius: 40,
    marginBottom: 10,
  },
  editAvatarText: {
    color: '#1DA1F2',
    fontSize: 14,
  },
  formSection: {
    marginBottom: 20,
    
  },
  input: {
    borderRadius: 10,
    padding: 10,
    marginBottom: 10,
    borderWidth: 1,
    borderColor: '#ccc',
  },
  inputRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  label: {
    fontSize: 16,

  },
  dropdown: {
    fontSize: 16,
    borderRadius: 10,
    padding: 10,
    borderWidth:1,
    
  },
});

import React, { useEffect, useState } from 'react';
import { View, Text, Image, StyleSheet, TouchableOpacity } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import http from '@/utils/http';
import Icon from 'react-native-vector-icons/Ionicons';

export default function UserInfoScreen({ navigation }: { navigation: any }) {
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true); 
  const avatarIMG = require('../../../image/avatar/2.png');

  const getUserInfo = async () => {
    const token = await AsyncStorage.getItem('accessToken');

    if (token) {
      try {
        const response = await http.get('auth/profile', {
          headers: {
            Authorization: `Bearer ${token}`, 
          },
        });
        if (response.status === 200) {
          setUser(response.data); 
          console.log(response.data);
        } else {
          console.error('Lấy thông tin người dùng thất bại.'); 
        }
      } catch (error) {
        console.error('Có lỗi xảy ra khi lấy thông tin người dùng:', error); 
      }
    } else {
      console.error('Token không tồn tại'); 
    }
    setLoading(false); 
  };

  useEffect(() => {
    getUserInfo(); 
  }, []);

  if (loading) {
    return <Text>Loading...</Text>; 
  }
  if (!user) {
    return <Text>User not found.</Text>; 
  }

  return (
    <View style={styles.container}>
       <View style={styles.header}>
        <TouchableOpacity  onPress={() => navigation.goBack()}>
        <Icon  name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
        <Text style={styles.headerText}>Thông tin cá nhân</Text>
      </View>
      <View style={styles.infoContainer}>
      <Image source={avatarIMG} style={styles.image} /> 
        <Text style={styles.label}>Họ tên: <Text style={styles.value}>{user.u?.hoTen || 'N/A'}</Text></Text>
        <Text style={styles.label}>Giới tính: <Text style={styles.value}>{user.u?.gioiTinh || 'N/A'}</Text></Text>
        <Text style={styles.label}>Ngày sinh: <Text style={styles.value}>{user.u?.ngaySinh || 'N/A'}</Text></Text>
        <Text style={styles.label}>Số điện thoại: <Text style={styles.value}>{user.u?.sdt || 'N/A'}</Text></Text>
        <Text style={styles.label}>Email: <Text style={styles.value}>{user.u?.email || 'N/A'}</Text></Text>
        <Text style={styles.label}>Khóa học: <Text style={styles.value}>TAGT</Text></Text>
        <Text style={styles.label}>Lớp học: <Text style={styles.value}>TAGT-101</Text></Text>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex:1,
    padding: 15,
    backgroundColor: '#fff',
    width:'100%',
    height:'100%'
  },
  header: {
    flexDirection: 'row',
    backgroundColor: '#fff',
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 8,
    marginTop:-20,
    color: '#00bf63',
    textAlign: 'center',
    marginRight:25,
  },
  headerText: {
    fontSize: 18,
    fontWeight: 'bold',
    marginLeft: 10,
  },
  image: {
    width: 100,
    height: 100,
    borderRadius: 50,
    marginBottom: 10,
    alignSelf:'center'
  },
  linkText: {
    color: '#007BFF',
    marginBottom: 20,
  },
  infoContainer: {
    marginTop:30,
    width: '100%',
    paddingHorizontal: 20,
    backgroundColor: '#F9F9F9',
    paddingVertical: 20,
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 5,
    elevation: 3,
  },
  label: {
    fontWeight: 'bold',
    fontSize: 18,
    marginBottom: 10,
  },
  value: {
    fontWeight: 'normal',
    fontSize: 16,
  },
  backButton: {
    left:-65,
    top:-20
  },
  backButtonText: {
    fontSize: 30,
    color: 'black',
  },
});

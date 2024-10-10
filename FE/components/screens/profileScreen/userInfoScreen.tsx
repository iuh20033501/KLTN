import React, { useEffect, useState } from 'react';
import { View, Text, Image, StyleSheet, TouchableOpacity } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import http from '@/utils/http';
import Icon from 'react-native-vector-icons/Ionicons';

export default function UserInfoScreen({ navigation }: { navigation: any }) {
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
          setUser(response.data);
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
  const formatDate = (dateString: string) => {
    if (!dateString) return 'N/A';
    const dateParts = dateString.split('-');
    if (dateParts.length !== 3) return dateString;

    const [year, month, day] = dateParts;
    return `${day}/${month}/${year}`;
  };

  if (loading) {
    return <Text>Loading...</Text>; 
  }
  if (!user) {
    return <Text>User not found.</Text>; 
  }

  const getAvatar = (imageIndex: string) => {
    const index = parseInt(imageIndex, 10) - 1; 
    return avatars[index] || avatars[0]; 
  }
  
  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity  onPress={() => navigation.goBack()}>
          <Icon  name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
        <Text style={styles.headerText}>Hồ sơ học viên</Text>
      </View>
      <View style={styles.infoContainer}>
        <Image source={getAvatar(user?.u?.image)} style={styles.image} /> 
        <Text style={styles.label}>Họ tên: <Text style={styles.value}>{user.u?.hoTen || 'N/A'}</Text></Text>
        <Text style={styles.label}>
          Giới tính: <Text style={styles.value}>{user.u?.gioiTinh === true ? 'Nam' : 'Nữ'}</Text>
        </Text>
        <Text style={styles.label}>Ngày sinh: <Text style={styles.value}>{formatDate(user.u?.ngaySinh)}</Text></Text>
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
    flex: 1,
    padding: 15,
    backgroundColor: '#fff',
    width: '100%',
    height: '100%'
  },
  header: {
    flexDirection: 'row',
    backgroundColor: '#fff',
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 8,
    marginTop: -20,
    color: '#00bf63',
    textAlign: 'center',
    marginRight: 25,
  },
  headerText: {
    fontSize: 18,
    fontWeight: 'bold',
    marginLeft: 10,
  },
  image: {
    width: 150,
    height: 150,
    borderRadius: 50,
    marginBottom: 10,
    alignSelf: 'center'
  },
  linkText: {
    color: '#007BFF',
    marginBottom: 20,
  },
  infoContainer: {
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
    marginTop:100
  
  },
  label: {
    fontWeight: 'bold',
    fontSize: 20,
    marginBottom: 10,
  },
  value: {
    fontWeight: 'normal',
    fontSize: 18,
  },
  backButton: {
    left: -65,
    top: -20
  },
  backButtonText: {
    fontSize: 30,
    color: 'black',
  },
});

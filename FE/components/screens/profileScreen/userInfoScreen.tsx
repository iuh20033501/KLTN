import React, { useEffect, useState } from 'react';
import { View, Text, Image, StyleSheet, TouchableOpacity, ScrollView } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import http from '@/utils/http';
import Icon from 'react-native-vector-icons/Ionicons';
import { FontAwesome, MaterialIcons } from '@expo/vector-icons';

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
    return <Text style={styles.loadingText}>Đang tải thông tin...</Text>;
  }

  if (!user) {
    return <Text style={styles.errorText}>Không tìm thấy thông tin người dùng.</Text>;
  }

  const getAvatar = (imageIndex: string) => {
    const index = parseInt(imageIndex, 10) - 1; 
    return avatars[index] || avatars[0]; 
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Icon name="arrow-back-outline" size={28} color="#333" />
        </TouchableOpacity>
        <Text style={styles.headerText}>Thông tin cá nhân</Text>
      </View>

      <View style={styles.infoContainer}>
        <Image source={getAvatar(user?.u?.image)} style={styles.image} />
        
        <View style={styles.row}>
          <FontAwesome name="user" size={24} color="#00bf63" />
          <Text style={styles.label}>Họ tên: <Text style={styles.value}>{user.u?.hoTen || 'N/A'}</Text></Text>
        </View>

        <View style={styles.row}>
          <FontAwesome name="venus-mars" size={24} color="#00bf63" />
          <Text style={styles.label}>Giới tính: <Text style={styles.value}>{user.u?.gioiTinh === true ? 'Nam' : 'Nữ'}</Text></Text>
        </View>

        <View style={styles.row}>
          <FontAwesome name="birthday-cake" size={24} color="#00bf63" />
          <Text style={styles.label}>Ngày sinh: <Text style={styles.value}>{formatDate(user.u?.ngaySinh)}</Text></Text>
        </View>

        <View style={styles.row}>
          <FontAwesome name="phone" size={24} color="#00bf63" />
          <Text style={styles.label}>Số điện thoại: <Text style={styles.value}>{user.u?.sdt || 'N/A'}</Text></Text>
        </View>

        <View style={styles.row}>
          <FontAwesome name="envelope" size={24} color="#00bf63" />
          <Text style={styles.label}>Email: <Text style={styles.value}>{user.u?.email || 'N/A'}</Text></Text>
        </View>

        <View style={styles.row}>
          <MaterialIcons name="class" size={24} color="#00bf63" />
          <Text style={styles.label}>Khóa học: <Text style={styles.value}>TAGT</Text></Text>
        </View>

        <View style={styles.row}>
          <MaterialIcons name="school" size={24} color="#00bf63" />
          <Text style={styles.label}>Lớp học: <Text style={styles.value}>TAGT-101</Text></Text>
        </View>
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flexGrow: 1,
    backgroundColor: '#F5F5F5',
    padding: 15,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 20,
    paddingVertical: 10,
  },
  headerText: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#333',
    marginLeft: 20,
  },
  infoContainer: {
    padding: 20,
    backgroundColor: '#FFFFFF',
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 5,
    elevation: 3,
    marginBottom: 20,
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 15,
  },
  image: {
    width: 120,
    height: 120,
    borderRadius: 60,
    marginBottom: 20,
    alignSelf: 'center',
  },
  label: {
    fontSize: 18,
    fontWeight: '600',
    color: '#333',
    marginLeft: 10,
  },
  value: {
    fontSize: 18,
    fontWeight: '400',
    color: '#666',
  },
  loadingText: {
    fontSize: 18,
    textAlign: 'center',
    color: '#00bf63',
    marginTop: 20,
  },
  errorText: {
    fontSize: 18,
    textAlign: 'center',
    color: 'red',
    marginTop: 20,
  },
});

import React, { useState, useEffect } from "react";
import { View, Text, StyleSheet, TouchableOpacity, Image } from 'react-native';
import { FontAwesome } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import http from "@/utils/http";
import { useFocusEffect } from '@react-navigation/native';

export default function DashboardScreen({ navigation }: { navigation: any }) {
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [selectedAvatar, setSelectedAvatar] = useState('');
  const [classInfo, setClassInfo] = useState<any>(null);

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
          setSelectedAvatar(response.data.u.image)
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

  const getStudentClass = async (idUser: number) => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (token) {
        const response = await http.get(`/hocvien/getByHV/${idUser}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.status === 200) {
          setClassInfo(response.data[0]);
          console.log(response.data)
        } else {
          console.error("Không thể lấy thông tin lớp học.");
        }
      } else {
        console.error("Không có token, vui lòng đăng nhập lại.");
      }
    } catch (error) {
      console.error("Lỗi khi gọi API lấy thông tin lớp học:", error);
    }
  };
  useEffect(() => {
    getUserInfo();
  }, []);

  useEffect(() => {
    if (user && user.u && user.u.idUser) {
      getStudentClass(user.u.idUser);
    }
  }, [user]);
  useFocusEffect(
    React.useCallback(() => {
      getUserInfo();
    }, [])
);
const getAvatarUri = () => {
  if (!selectedAvatar) return null;
  if (selectedAvatar.startsWith("data:image")) {
    return selectedAvatar;
  }
  const defaultMimeType = "image/png";
  let mimeType = defaultMimeType;
  if (/^\/9j/.test(selectedAvatar)) {
    mimeType = "image/jpeg"; // JPEG/JPG (dựa vào header base64 của JPEG)
  } else if (/^iVBOR/.test(selectedAvatar)) {
    mimeType = "image/png"; // PNG (dựa vào header base64 của PNG)
  } else if (/^R0lGOD/.test(selectedAvatar)) {
    mimeType = "image/gif"; // GIF (dựa vào header base64 của GIF)
  } else if (/^Qk/.test(selectedAvatar)) {
    mimeType = "image/bmp"; // BMP (dựa vào header base64 của BMP)
  } else if (/^UklGR/.test(selectedAvatar)) {
    mimeType = "image/webp"; // WEBP (dựa vào header base64 của WEBP)
  }

  return `data:${mimeType};base64,${selectedAvatar}`;
};

  return (
    <View style={styles.container}>
      <Text style={styles.sectionTitle}>Trang chủ</Text>
      
      <View style={styles.header}>
        {loading ? ( 
          <Text style={styles.userName}>Loading...</Text>
        ) : (
          <>
            <TouchableOpacity
              style={styles.avatarContainer}  
              onPress={() => navigation.navigate('UserProfileScreen', {
                name: user?.u?.hoTen,
                role: user?.cvEnum,
                image : user?.u?.i
              })}
            >
              <Image source={selectedAvatar ? { uri: getAvatarUri() } : require('../../../image/avatar/1.png')} style={styles.avatar} /> 
            </TouchableOpacity>
            
            <View style={styles.userInfo}>
              <Text style={styles.userName}>{user.u.hoTen}</Text>
              <Text style={styles.courseName}>{classInfo?.tenLopHoc || 'Chưa đăng ký'}</Text>
            </View>
          </>
        )}

        <TouchableOpacity style={styles.notificationIcon}>
          <FontAwesome name="bell-o" size={25} color="black" />
        </TouchableOpacity>
      </View>

      <View style={styles.upcomingClass}>
        <Text style={styles.classTitle}>Unit 1: Hello!</Text>
        <View style={styles.classInfo}>
          <Text>Phòng học A2.03</Text>
          <Text>Mr. John</Text>
        </View>
        <Text style={styles.classTime}>Th 2, 20/09/2024 - 8:00 - 10:30</Text>
      </View>

      <View style={styles.weeklyTasks}>
        <Text style={styles.taskTitle}>Nhiệm vụ hàng tuần</Text>
        <View style={styles.taskProgress}>
          <Text>Nhiệm vụ cần hoàn thành</Text>
          <Text>0/4</Text>
        </View>
        <TouchableOpacity style={styles.actionButton}>
          <Text style={styles.actionButtonText}>Thực hiện ngay</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.practiceArea}>
        <TouchableOpacity style={styles.practiceCard}>
          <Image source={require('../../../image/logo/EFY.png')} style={styles.practiceImage} />
          <Text style={styles.practiceText}>Luyện tập lý thuyết</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.practiceCard}>
          <Image source={require('../../../image/logo/EFY.png')} style={styles.practiceImage} />
          <Text style={styles.practiceText}>Luyện nghe</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#00bf63',
    padding: 10,
    borderRadius: 10,
    justifyContent: 'space-between',
  },
  avatarContainer: {
    marginRight: 10,
  },
  avatar: {
    width: 100,
    height: 100,
    borderRadius: 50,
  },
  userInfo: {
    flex: 1,
  },
  userName: {
    color: '#000022',
    fontSize: 18,
    fontWeight: 'bold',
  },
  courseName: {
    color: '#000022',
    fontSize: 14,
  },
  notificationIcon: {
    position: 'relative',
    backgroundColor: '#fff',
    borderRadius: 50,
    width: 40,
    height: 40,
    alignItems: 'center',
    justifyContent: 'center'
  },
  upcomingClass: {
    backgroundColor: '#f0f0f0',
    padding: 15,
    borderRadius: 10,
    marginTop: 20,
  },
  classTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  classInfo: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 5,
  },
  classTime: {
    color: '#888',
  },
  weeklyTasks: {
    marginTop: 20,
    padding: 15,
    backgroundColor: '#fff',
    borderRadius: 10,
    borderWidth: 1,
    borderColor: '#ddd',
  },
  taskTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  taskProgress: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 10,
  },
  actionButton: {
    backgroundColor: '#00bf63',
    padding: 10,
    borderRadius: 5,
    alignItems: 'center',
  },
  actionButtonText: {
    color: '#000022',
    fontWeight: 'bold',
  },
  practiceArea: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 20,
  },
  practiceCard: {
    width: '48%',
    backgroundColor: '#f5f5f5',
    padding: 10,
    borderRadius: 10,
    alignItems: 'center',
  },
  practiceImage: {
    width: 60,
    height: 60,
    marginBottom: 10,
  },
  practiceText: {
    fontSize: 14,
    textAlign: 'center',
  },
  sectionTitle: {
    padding: 20,
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 8,
    color: '#00bf63',
    textAlign: 'center',
  },
});

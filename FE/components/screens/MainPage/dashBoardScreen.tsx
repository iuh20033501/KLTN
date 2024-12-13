import React, { useState, useEffect } from "react";
import { View, Text, StyleSheet, TouchableOpacity, Image } from 'react-native';
import { FontAwesome } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import http from "@/utils/http";
import { useFocusEffect } from '@react-navigation/native';
interface NextLesson {
  chuDe?: string;
  phongHoc?: string;
  tenLopHoc?: string;
  ngayHoc?: string;
  gioBatDau?: string;
  gioKetThuc?: string;
}

export default function DashboardScreen({ navigation }: { navigation: any }) {
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [selectedAvatar, setSelectedAvatar] = useState('');
  const [classInfo, setClassInfo] = useState<any>(null);
  const [nextLesson, setNextLesson] = useState<NextLesson | null>(null);

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
        const response = await http.get(`hocvien/getByHV/${idUser}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.status === 200) {
          const classes = response.data.filter((classItem: any) => classItem.trangThai === 'FULL');
          setClassInfo(classes);
          console.log(classes);
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

  const findNextSession = async (classes: any[]) => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (token) {
        let allSessions: any[] = [];
        for (const classItem of classes) {
          const response = await http.get(`buoihoc/getbuoiHocByLop/${classItem.idLopHoc}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          if (response.status === 200) {
            allSessions = [...allSessions, ...response.data];
            console.log(allSessions)
          }
        }

        const now = new Date();
        const nextSession = allSessions
          .filter((session: { ngayHoc: string }) => new Date(session.ngayHoc).getTime() > now.getTime())
          .sort((a: { ngayHoc: string }, b: { ngayHoc: string }) => new Date(a.ngayHoc).getTime() - new Date(b.ngayHoc).getTime())[0];
          if (nextSession) {
            setNextLesson({
              chuDe: nextSession.chuDe || 'Buổi học tiếp theo',
              phongHoc: nextSession.noiHoc || 'Phòng chưa xác định',
              tenLopHoc: nextSession.lopHoc.tenLopHoc || 'Chưa xác định',
              ngayHoc: nextSession.ngayHoc,
              gioBatDau: nextSession.gioHoc || '00:00',
              gioKetThuc: nextSession.gioKetThuc || '00:00',
            });
          } else {
            setNextLesson(null);
          }
      } else {
        console.error("Không có token, vui lòng đăng nhập lại.");
      }
    } catch (error) {
      console.error("Lỗi khi gọi API lấy thông tin buổi học:", error);
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
  useEffect(() => {
    if (classInfo && classInfo.length > 0) {
      findNextSession(classInfo);
    }
  }, [classInfo]);

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
                image: user?.u?.i
              })}
            >
              <Image source={selectedAvatar ? selectedAvatar : require('../../../image/avatar/1.png')} style={styles.avatar} />
            </TouchableOpacity>

            <View style={styles.userInfo}>
              <Text style={styles.userName}>{user.u.hoTen}</Text>
            </View>
          </>
        )}

        <TouchableOpacity style={styles.notificationIcon}>
          <FontAwesome name="bell-o" size={25} color="black" />
        </TouchableOpacity>
      </View>

      <View style={styles.upcomingClass}>
        {nextLesson ? (
          <>
            <Text style={styles.classTitle}>Chủ đề: {nextLesson.chuDe || ''}</Text>
            <View style={styles.classInfo}>
              <Text>{nextLesson.phongHoc || ''}</Text>
              <Text>{nextLesson.tenLopHoc || ''}</Text>
            </View>
            <Text style={styles.classTime}>
              {nextLesson.ngayHoc
                ? new Date(nextLesson.ngayHoc).toLocaleDateString('vi-VN', {
                  weekday: 'long',
                  day: '2-digit',
                  month: '2-digit',
                  year: 'numeric',
                })
                : 'Ngày chưa xác định'}{' '}
             
            </Text>
            <Text style={styles.classTime}>{nextLesson.gioBatDau || '00:00'} - {nextLesson.gioKetThuc || '00:00'}</Text>
          </>
        ) : (
          <Text style={styles.classTitle}>Không có buổi học tiếp theo</Text>
        )}
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

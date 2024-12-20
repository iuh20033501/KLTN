import http from '@/utils/http';
import { AntDesign, EvilIcons, FontAwesome6, MaterialCommunityIcons, MaterialIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useFocusEffect } from 'expo-router';
import React, { useEffect, useState } from 'react';
import { View, Text, Image, ScrollView, TouchableOpacity, StyleSheet, TextInput, ImageBackground, Modal } from 'react-native';
import StudentClassProgressScreen from '../progess/studentClassProgessScreen';
import TeacherClassProgessScreen from '../progess/teacherClassProgessScreen';
import WeeklyScheduleTotal from '../schedule/studentScheduleTotal';
import WeeklyExamSchedule from '../schedule/studentExamScheduleTotal';
import TeacherScheduleTotalScreen from '../schedule/teacherScheduleTotal';
import TeacherWeeklyExamSchedule from '../schedule/teacherExamScheduleTotal';

const DashboardScreen = ({ navigation }: { navigation: any }) => {
  const [isModalVisible, setModalVisible] = useState(false);
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [selectedAvatar, setSelectedAvatar] = useState('');
  const [logoutModalVisible, setLogoutModalVisible] = useState(false);
  const [classInfo, setClassInfo] = useState<any>(null);
  const [classLength, setClassLength] = useState<any>(null);

  const toggleModal = () => {
    setModalVisible(!isModalVisible);
  };

  const getUserInfo = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (token) {
        const response = await http.get('auth/profile', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        console.log("Response Data:", response.data);
        if (response.status === 200) {
          setUser(response.data);
          setSelectedAvatar(response.data.u.image);
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
          setClassLength(response.data.length)
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

  useFocusEffect(
    React.useCallback(() => {
      const fetchUserData = async () => {
        await getUserInfo();
      };
  
      fetchUserData();
    }, [])
  );
  
  useEffect(() => {
    const unsubscribe = navigation.addListener('focus', async () => {
      console.log('DashboardScreen focused, refetching user info...');
      await getUserInfo();
    });
  
    return unsubscribe;
  }, [navigation]);
  
  useEffect(() => {
    const fetchClassData = async () => {
      if (user && user.u && user.u.idUser) {
        console.log('Fetching class info for user:', user.u.idUser);
        await getStudentClass(user.u.idUser);
      }
    };
  
    fetchClassData();
  }, [user]); 
  
  const parseCvEnum = (cvEnum: string): string => {
    switch (cvEnum) {
      case 'STUDENT':
        return 'Học viên';
      case 'TEACHER':
        return 'Giảng viên';
      default:
        return 'Không xác định';
    }
  };
  const formatDate = (dateString: string) => {
    if (!dateString) return 'N/A';
    const dateParts = dateString.split('-');
    if (dateParts.length !== 3) return dateString;

    const [year, month, day] = dateParts;
    return `${day}/${month}/${year}`;
  };

  if (loading) {
    return <Text >Đang tải thông tin...</Text>;
  }

  if (!user || !user.u) {
    return <Text>Không tìm thấy thông tin người dùng.</Text>;
  }

  
  return (
    <ImageBackground
      source={require('../../../image/bglogin.png')}
      style={styles.background}
      resizeMode='cover'
    >
      <View style={styles.header}>
        <View style={styles.headerLeft}>
          <Image
            style={styles.logo}
            source={require('../../../image/efy.png')}
          />
        </View>
        <View style={styles.headerRight}>
          <TouchableOpacity style={styles.headerButton}>
            <AntDesign name="team" size={24} color="black" />
            <Text style={{ textAlign: 'center', marginLeft: 5, marginTop: 3 }}>Diễn đàn</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.headerButton} >
            <FontAwesome6 name="newspaper" size={24} color="black" />
            <Text style={{ textAlign: 'center', marginLeft: 5, marginTop: 3 }}>Tin tức</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.headerButton}
            onPress={toggleModal} >

            <Image
              style={styles.profileImageHeader}
              source={selectedAvatar ? selectedAvatar : require('../../../image/efy.png')}
            />
          </TouchableOpacity>
        </View>
      </View>
      <ScrollView style={styles.container}>
        <View style={styles.studentOverviewContainer}>
          <View style={styles.studentInfoCard}>
            <View style={styles.studentImageContainer}>
              <Image style={styles.studentImage} source={selectedAvatar ? selectedAvatar : require('../../../image/efy.png')} />
              <Text style={{ fontSize: 18 }}>{parseCvEnum(user.cvEnum)}</Text>
            </View>
            <View style={styles.studentDetails}>
              <Text style={styles.studentLabel}>Họ tên: <Text style={styles.studentData}>{user.u?.hoTen}</Text></Text>
              <Text style={styles.studentLabel}>Giới tính: <Text style={styles.studentData}>{user.u?.gioiTinh === true ? 'Nam' : 'Nữ'}</Text></Text>
              <Text style={styles.studentLabel}>Ngày sinh: <Text style={styles.studentData}>{formatDate(user.u?.ngaySinh)}</Text></Text>
              <Text style={styles.studentLabel}>Email: <Text style={styles.studentData}>{user.u?.email || 'N/A'}</Text></Text>
              <Text style={styles.studentLabel}>Số điện thoại: <Text style={styles.studentData}>{user.u?.sdt || 'N/A'}</Text></Text>
              {user.cvEnum === "STUDENT" && (
                <>
                  <Text style={styles.studentLabel}>Lớp học: <Text style={styles.studentData}>{classInfo?.tenLopHoc || 'Chưa đăng ký'}</Text></Text>
                  <Text style={styles.studentLabel}>Khóa học: <Text style={styles.studentData}>{classInfo?.khoaHoc?.tenKhoaHoc || 'Chưa đăng ký'}</Text></Text>
                </>
              )}
            </View>
          </View>

          <View style={styles.scheduleContainer}>
            {user.cvEnum === 'TEACHER' ? (
              <View style={styles.scheduleCard}>
                <Text style={styles.scheduleTitle}>Thông báo</Text>
                <Text style={styles.scheduleNumber}>0
                </Text>
                <TouchableOpacity>
                  <Text style={styles.linkText}>Xem chi tiết</Text>
                </TouchableOpacity>
                <View style={{ flexDirection: 'row', marginTop: 70, justifyContent: 'center' }}>
                  <View style={styles.scheduleCard2}>
                    <Text style={styles.scheduleTitle}>Lịch dạy trong tuần</Text>
                    <Text style={styles.scheduleNumber}><TeacherScheduleTotalScreen></TeacherScheduleTotalScreen></Text>
                    <TouchableOpacity>
                      <Text style={styles.linkText}>Xem chi tiết</Text>
                    </TouchableOpacity>
                  </View>
                  <View style={styles.scheduleCard2}>
                    <Text style={styles.scheduleTitle}>Lịch gác thi trong tuần</Text>
                    <Text style={styles.scheduleNumber}><TeacherWeeklyExamSchedule></TeacherWeeklyExamSchedule></Text>
                    <TouchableOpacity>
                      <Text style={styles.linkText}>Xem chi tiết</Text>
                    </TouchableOpacity>
                  </View>
                </View>
              </View>
            ) : (
              <View style={styles.scheduleCard}>
                <Text style={styles.scheduleNumber}>Đăng ký khóa học</Text>
                <Text style={styles.scheduleTitle}>
                  {classLength
                    ? `Bạn đã tham gia ${classLength} khóa học`
                    : "Bạn chưa tham gia khóa học nào"}
                </Text>
                <TouchableOpacity
                  onPress={() => {
                    navigation.navigate('CourseRegistrationScreen', { idUser: user.u.idUser, nameUser: user.u.hoTen });
                  }}
                >
                  <Text style={styles.linkText}>Đăng ký</Text>
                </TouchableOpacity>
                <View style={{ flexDirection: 'row', marginTop: 70, justifyContent: 'center' }}>
                  <View style={styles.scheduleCard2}>
                    <Text style={styles.scheduleTitle}>Lịch học trong tuần</Text>
                    <Text style={styles.scheduleNumber}><WeeklyScheduleTotal></WeeklyScheduleTotal></Text>
                    <TouchableOpacity>
                      <Text style={styles.linkText}>Xem chi tiết</Text>
                    </TouchableOpacity>
                  </View>
                  <View style={styles.scheduleCard2}>
                    <Text style={styles.scheduleTitle}>Lịch thi trong tuần</Text>
                    <Text style={styles.scheduleNumber}><WeeklyExamSchedule></WeeklyExamSchedule></Text>
                    <TouchableOpacity>
                      <Text style={styles.linkText}>Xem chi tiết</Text>
                    </TouchableOpacity>
                  </View>
                </View>
              </View>
            )}
          </View>
        </View>

        <View style={styles.featureRow}>
          {user.cvEnum === 'TEACHER' ? (
            <>
              <TouchableOpacity style={styles.featureCard}
                onPress={() => {
                  navigation.navigate('TeacherScheduleScreen', { idUser: user.u.idUser, nameUser: user.u.hoTen });
                }}>
                <AntDesign name="calendar" size={24} color="black" />
                <Text style={styles.featureText}>Lịch giảng dạy</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard}
              onPress={() => {
                navigation.navigate('ResultTeacherScreen', { idUser: user.u.idUser, nameUser: user.u.hoTen, role: user.cvEnum });
              }}>
                <AntDesign name="linechart" size={24} color="black" />
                <Text style={styles.featureText}>Quản lý điểm số</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard} 
                onPress={() => {
                  navigation.navigate('TeacherClassesScreen', { idUser: user.u.idUser, nameUser: user.u.hoTen, role: user.cvEnum });
                }}>
                <AntDesign name="book" size={24} color="black" />
                <Text style={styles.featureText}>Quản lý bài tập</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard}
               onPress={() => {
                navigation.navigate('TeacherClassesExamScreen', { idUser: user.u.idUser, nameUser: user.u.hoTen, role: user.cvEnum });
              }}>
                <MaterialCommunityIcons name="pencil-outline" size={24} color="black" />
                <Text style={styles.featureText}>Quản lý bài thi</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard}
                onPress={() => {
                  navigation.navigate('TeacherDocumentScreen', { idUser: user.u.idUser, nameUser: user.u.hoTen, role: user.cvEnum });
                }}>

                <AntDesign name="profile" size={24} color="black" />
                <Text style={styles.featureText}>Quản lý tài liệu</Text>
              </TouchableOpacity>
            </>
          ) : (
            <>
              <TouchableOpacity style={styles.featureCard}
                onPress={() => {
                  navigation.navigate('ScheduleScreen', { idUser: user.u.idUser, nameUser: user.u.hoTen });
                }}>
                <AntDesign name="calendar" size={24} color="black" />
                <Text style={styles.featureText}>Lịch học</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard}
               onPress={() => {
                navigation.navigate('ResultStudentScreen', { idUser: user.u.idUser, nameUser: user.u.hoTen, role: user.cvEnum });
              }}>
                <AntDesign name="linechart" size={24} color="black" />
                <Text style={styles.featureText}>Xem điểm</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard}
                onPress={() => {
                  navigation.navigate('StudentClassesScreen', { idUser: user.u.idUser, nameUser: user.u.hoTen, role: user.cvEnum });
                }}>
                <AntDesign name="book" size={24} color="black" />
                <Text style={styles.featureText}>Khóa học đã đăng ký</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard}
                onPress={() => {
                  navigation.navigate('BillScreen', { idUser: user.u.idUser, nameUser: user.u.hoTen });
                }}>
                <MaterialIcons name="attach-money" size={24} color="black" />
                <Text style={styles.featureText}>Tra cứu hóa đơn</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard}
                onPress={() => {
                  navigation.navigate('PaymentScreen', { idUser: user.u.idUser, nameUser: user.u.hoTen });
                }}>
                <AntDesign name="creditcard" size={24} color="black" />
                <Text style={styles.featureText}>Thanh toán khóa học</Text>
              </TouchableOpacity>
            </>
          )}
        </View>

        <View style={styles.sectionContainer}>
          {user.cvEnum === 'TEACHER' ? (
            <>
              <View style={[styles.section, { marginRight: 10 }]}>
                <Text style={styles.sectionTitle}>Quản lý bài test</Text>
                <View style={styles.chartPlaceholder}>
                  <Text>Chưa có dữ liệu hiển thị</Text>
                </View>
              </View>

              <View style={[styles.section, { marginLeft: 10 }]}>
                <Text style={styles.sectionTitle}>Chương trình giảng dạy</Text>
                <View style={styles.classList}>
               <TeacherClassProgessScreen></TeacherClassProgessScreen>
                </View>
              </View>
            </>
          ) : (
            <>
              <View style={[styles.section, { marginRight: 10 }]}>
                <Text style={styles.sectionTitle}>Kết quả học tập</Text>
                <View style={styles.chartPlaceholder}>
                  <Text>Chưa có dữ liệu hiển thị</Text>
                </View>
              </View>

              <View style={[styles.section, { marginLeft: 10 }]}>
                <Text style={styles.sectionTitle}>Tiến độ học tập</Text>
                <View style={styles.classList}>
                 <StudentClassProgressScreen></StudentClassProgressScreen>
                 {/* <StudentClassAssignmentProgressScreen></StudentClassAssignmentProgressScreen> */}
                </View>
              </View>
            </>
          )}
        </View>
      </ScrollView>
      <Modal
        visible={isModalVisible}
        transparent={true}
        animationType="fade"
        onRequestClose={toggleModal}
      >
        <TouchableOpacity style={styles.modalOverlay} onPress={toggleModal} />
        <View style={styles.modalContainer}>
          <TouchableOpacity style={styles.modalItem} onPress={() => {
            setModalVisible(false);
            navigation.navigate('EditProfileScreen');
          }}>
            <Text style={styles.modalText}>Thông tin cá nhân</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.modalItem}
            onPress={() => {
              setModalVisible(false);
              navigation.navigate('ChangePassword');
            }}>
            <Text style={styles.modalText}>Đổi mật khẩu</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={styles.modalItem}
            onPress={() => {
              setLogoutModalVisible(true);
              setTimeout(() => {
                setLogoutModalVisible(false);
                navigation.navigate('LoginScreen');
              }, 1000);
            }}
          >
            <Text style={styles.modalText}>Đăng xuất</Text>
          </TouchableOpacity>
        </View>
      </Modal>
      <Modal
        animationType="slide"
        transparent={true}
        visible={logoutModalVisible}
        onRequestClose={() => {
          setLogoutModalVisible(false);
        }}
      >
        <View style={styles.modalOverlay2}>
          <View style={styles.modalContainer2}>
            <Text style={styles.modalText2}>Đang đăng xuất...</Text>
          </View>
        </View>
      </Modal>
    </ImageBackground>
  );
};

const styles = StyleSheet.create({
  background: {
    flex: 1,
    paddingHorizontal: 400,
    height: 990
  },
  header: {
    borderRadius: 15,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: 10,
    backgroundColor: '#fff',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    elevation: 3,
  },
  headerLeft: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  logo: {
    width: 200,
    height: 50,
    borderRadius: 20,
  },
  headerRight: {
    flexDirection: 'row',
    alignItems: 'center',

  },
  headerIcon: {
    width: 25,
    height: 25,
    marginHorizontal: 10,
  },
  headerButton: {
    flexDirection: 'row',
    padding: 15,
    marginLeft: 10
  },
  profileImageHeader: {
    width: 35,
    height: 35,
    borderRadius: 20,
  },
  container: {
    flex: 1,
  },
  studentOverviewContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginVertical: 10,
  },
  studentInfoCard: {
    borderRadius: 15,
    flexDirection: 'row',
    backgroundColor: '#fff',
    padding: 15,
    flex: 0.59,
    elevation: 3,
    height: 300
  },
  studentImageContainer: {
    alignItems: 'center',
    justifyContent: 'center',
    marginRight: 15,
    width: 200
  },
  studentImage: {
    width: 150,
    height: 150,
    borderRadius: 100,
    top: -30
  },
  studentDetails: {
    flex: 1,
    marginLeft: 20,
    borderRadius: 15,

  },
  studentLabel: {
    fontSize: 18,
    color: '#00405d',
    padding: 5,
    marginTop: 5,
  },
  studentData: {
    color: '#333',
    fontWeight: 'bold',
    fontSize: 16
  },
  linkText: {
    color: '#0078d4',
    fontSize: 14,
    marginTop: 5,
  },
  scheduleContainer: {
    flexDirection: 'column',
    flex: 0.4,
    height: 140,

  },
  scheduleCard: {
    borderRadius: 15,
    backgroundColor: '#fff',
    padding: 10,
    marginBottom: 30,
    alignItems: 'center',
    elevation: 2,
    height: 150,

  },
  scheduleCard2: {
    borderRadius: 15,
    backgroundColor: '#fff',
    padding: 15,
    height: 140,
    alignItems: 'center',
    elevation: 2,
    width: 212,
    marginStart: 10,
    left: -5,

  },
  scheduleTitle: {
    fontSize: 16,
    color: '#555',
    textAlign: 'center',
  },
  scheduleNumber: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#333',
    textAlign: 'center',
  },
  featureRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginVertical: 10,

  },
  featureCard: {
    borderRadius: 15,
    width: '19%',
    backgroundColor: '#fff',
    padding: 15,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    elevation: 5,

  },
  featureText: {
    fontSize: 14,
    color: '#333',
    textAlign: 'center',
    marginTop: 5
  },
  section: {
    borderRadius: 15,
    width: '49%',
    backgroundColor: '#fff',
    height: 400,
    padding: 15,
    marginVertical: 10,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    elevation: 5,
  },
  sectionContainer: {
    flexDirection: 'row',
    width: '100%',
    justifyContent: 'space-between'
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  chartPlaceholder: {
    backgroundColor: '#e0e0e0',
    height: 330,
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 10,
  },
  progressChart: {
    width: 100,
    height: 100,
    borderRadius: 50,
    backgroundColor: '#e0f7fa',
    justifyContent: 'center',
    alignItems: 'center',
    alignSelf: 'center',
  },
  progressText: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#00796b',
  },
  classList: {
    marginTop: 10,
  },
  classItem: {
    fontSize: 18,
    color: '#333',
    marginBottom: 5,
  },
  modalOverlay: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
  },
  modalContainer: {
    position: 'absolute',
    right: 400,
    top: 70,
    backgroundColor: '#fff',
    borderRadius: 8,
    paddingVertical: 10,
    width: 150,
    elevation: 5,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,

  },
  modalItem: {
    padding: 10,
  },
  modalText: {
    fontSize: 16,
    color: '#333',
  },
  modalOverlay2: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',

  },
  modalContainer2: {
    width: '100%',
    maxWidth: 400,
    backgroundColor: '#fff',
    padding: 20,
    borderRadius: 10,
    alignItems: 'center',
  },
  modalText2: {
    fontSize: 16,
    marginBottom: 20,
    textAlign: 'center',
  },
  closeButton2: {
    backgroundColor: '#00405d',
    padding: 10,
    borderRadius: 5,
    width: '100%',
    alignItems: 'center',
  },
  closeButtonText2: {
    color: '#fff',
    fontSize: 16,
  },
});


export default DashboardScreen;

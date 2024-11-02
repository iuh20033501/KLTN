import http from '@/utils/http';
import { AntDesign, EvilIcons, FontAwesome6, MaterialIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useFocusEffect } from 'expo-router';
import React, { useEffect, useState } from 'react';
import { View, Text, Image, ScrollView, TouchableOpacity, StyleSheet, TextInput, ImageBackground, Modal } from 'react-native';

const DashboardScreen = ({ navigation }: { navigation: any }) => {
  const [isModalVisible, setModalVisible] = useState(false);
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [selectedAvatar, setSelectedAvatar] = useState('');
  const [logoutModalVisible, setLogoutModalVisible] = useState(false);
  const [classInfo, setClassInfo] = useState<any>(null);

  const toggleModal = () => {
    setModalVisible(!isModalVisible);
  };
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
              source={selectedAvatar ? { uri: getAvatarUri() } : require('../../../image/efy.png')}
            />
          </TouchableOpacity>
        </View>
      </View>
      <ScrollView style={styles.container}>
        <View style={styles.studentOverviewContainer}>
          <View style={styles.studentInfoCard}>
            <View style={styles.studentImageContainer}>
              <Image style={styles.studentImage} source={selectedAvatar ? { uri: getAvatarUri() } : require('../../../image/efy.png')} />
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
                <Text style={styles.scheduleTitle}>Thông báo nhắc nhở</Text>
                <Text style={styles.scheduleNumber}>
                  0
                </Text>
                <TouchableOpacity>
                  <Text style={styles.linkText}>Xem chi tiết</Text>
                </TouchableOpacity>
                <View style={{ flexDirection: 'row', marginTop: 70, justifyContent: 'center' }}>
                  <View style={styles.scheduleCard2}>
                    <Text style={styles.scheduleTitle}>Lịch dạy trong tuần</Text>
                    <Text style={styles.scheduleNumber}>0</Text>
                    <TouchableOpacity>
                      <Text style={styles.linkText}>Xem chi tiết</Text>
                    </TouchableOpacity>
                  </View>
                  <View style={styles.scheduleCard2}>
                    <Text style={styles.scheduleTitle}>Lịch gác thi trong tuần</Text>
                    <Text style={styles.scheduleNumber}>0</Text>
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
                  {classInfo ? "Bạn đã tham gia 1 khóa học" : "Bạn chưa tham gia khóa học nào"}
                </Text>
                <TouchableOpacity
                  onPress={() => {
                    navigation.navigate('CourseRegistrationScreen', { idUser: user.u.idUser, nameUser : user.u.hoTen });
                  }}
                >
                  <Text style={styles.linkText}>Đăng ký</Text>
                </TouchableOpacity>
                <View style={{ flexDirection: 'row', marginTop: 70, justifyContent: 'center' }}>
                  <View style={styles.scheduleCard2}>
                    <Text style={styles.scheduleTitle}>Lịch học trong tuần</Text>
                    <Text style={styles.scheduleNumber}>0</Text>
                    <TouchableOpacity>
                      <Text style={styles.linkText}>Xem chi tiết</Text>
                    </TouchableOpacity>
                  </View>
                  <View style={styles.scheduleCard2}>
                    <Text style={styles.scheduleTitle}>Lịch thi trong tuần</Text>
                    <Text style={styles.scheduleNumber}>0</Text>
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
              <TouchableOpacity style={styles.featureCard}>
                <AntDesign name="calendar" size={24} color="black" />
                <Text style={styles.featureText}>Lịch giảng dạy</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard}>
                <AntDesign name="linechart" size={24} color="black" />
                <Text style={styles.featureText}>Quản lý điểm số</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard}>
                <AntDesign name="book" size={24} color="black" />
                <Text style={styles.featureText}>Lớp học của tôi</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard}>
                <MaterialIcons name="feedback" size={24} color="black" />
                <Text style={styles.featureText}>Phản hồi từ học viên</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard}>
                <AntDesign name="profile" size={24} color="black" />
                <Text style={styles.featureText}>Quản lý tài liệu</Text>
              </TouchableOpacity>
            </>
          ) : (
            <>
              <TouchableOpacity style={styles.featureCard}
               onPress={() => {
                navigation.navigate('ScheduleScreen', { idUser: user.u.idUser, nameUser : user.u.hoTen });
              }}>
                <AntDesign name="calendar" size={24} color="black" />
                <Text style={styles.featureText}>Lịch học</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard}>
                <AntDesign name="linechart" size={24} color="black" />
                <Text style={styles.featureText}>Xem điểm bài test</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard}>
                <AntDesign name="book" size={24} color="black" />
                <Text style={styles.featureText}>Khóa học đã đăng ký</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard}>
                <MaterialIcons name="attach-money" size={24} color="black" />
                <Text style={styles.featureText}>Tra cứu công nợ</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.featureCard}
                onPress={() => {
                  navigation.navigate('PaymentScreen', { idUser: user.u.idUser, nameUser : user.u.hoTen });
                }}>
                <AntDesign name="creditcard" size={24} color="black" />
                <Text style={styles.featureText}>Thanh toán trực tuyến</Text>
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
                  <Text style={styles.classItem}>Lớp TOEIC - Unit 1: Hello!</Text>
                  <Text style={styles.classItem}>Lớp TOEIC - Unit 2: Watch movie</Text>
                  <Text style={styles.classItem}>Lớp TOEIC - Unit 3: Go to school</Text>
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
                  <Text style={styles.classItem}>Unit 1: Hello!</Text>
                  <Text style={styles.classItem}>Unit 2: Watch movie</Text>
                  <Text style={styles.classItem}>Unit 3: Go to school</Text>
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
    marginLeft: 20
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
    marginLeft: 20
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

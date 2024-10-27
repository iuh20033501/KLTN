import React, { useEffect, useState } from 'react';
import { View, Text, Image, TouchableOpacity, StyleSheet, ScrollView, Modal, ImageBackground, Alert } from 'react-native';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import CheckBox from '@react-native-community/checkbox';

type Course = {
  idKhoaHoc: number;
  tenKhoaHoc: string;
  moTa: string;
  hocPhi: number;
  giaTien: string;
  thoiGianDienRa: string;
  soBuoi: string;
  image: string;
};

type Class = {
  idLopHoc: number;
  tenLopHoc: string;
};

const CourseRegistrationScreen = ({ navigation, route }: { navigation: any, route: any }) => {
  const { idUser } = route.params;
  const [loading, setLoading] = useState(true);
  const [courses, setCourses] = useState<Course[]>([]);
  const [classes, setClasses] = useState<Class[]>([]);
  const [selectedCourse, setSelectedCourse] = useState<Course | null>(null);
  const [selectedClass, setSelectedClass] = useState<Class | null>(null);
  const [isModalVisible, setModalVisible] = useState(false);
  const [isConfirmationModalVisible, setConfirmationModalVisible] = useState(false);
  const [isPaymentOptionModalVisible, setPaymentOptionModalVisible] = useState(false);
  const [paymentOption, setPaymentOption] = useState<'center' | 'online' | null>(null);
  const [isConfirmationModalVisible2, setConfirmationModalVisible2] = useState(false);
  const [resultModalVisible, setResultModalVisible] = useState(false);
  const [resultMessage, setResultMessage] = useState('');
  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchCourses = async () => {
    try {
      const response = await http.get('auth/noauth/findAllKhoa');
      if (response.status === 200) {
        setCourses(response.data);
      }
    } catch (error) {
      console.error('Error fetching courses:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchClasses = async (courseId: number) => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('Token không tồn tại');
        return;
      }

      const response = await http.get(`/lopHoc/getByKhoa/${courseId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (response.status === 200) {
        setClasses(response.data);
        setModalVisible(true);
        console.log(response.data)
      }
    } catch (error) {
      console.error('Error fetching classes:', error);
    }
  };



  const handleCourseClick = (course: Course) => {
    setSelectedCourse(course);
    fetchClasses(course.idKhoaHoc);
  };

  const handleClassSelect = (classItem: Class) => {
    setSelectedClass(classItem);
    setModalVisible(false);
    setConfirmationModalVisible(true);
  };
  const handleProceedToPayment = () => {
    setConfirmationModalVisible(false);
    setPaymentOptionModalVisible(true);
  };
  const handleConfirmPayment = async () => {
    if (paymentOption === 'center') {
      alert("Vui lòng đến trung tâm để thực hiện thanh toán.");
      setPaymentOptionModalVisible(false);
    } else if (paymentOption === 'online') {
      setPaymentOptionModalVisible(false);
      setConfirmationModalVisible2(true);
    }
  };

  const handleConfirmPaymentVerification = async () => {
    if (selectedClass && idUser) {
      try {
        const token = await AsyncStorage.getItem('accessToken');
        if (!token) {
          setResultMessage("Không có token, vui lòng đăng nhập lại.");
          setResultModalVisible(true);
          return;
        }

        const response = await http.get(`/hocvien/dangkyLop/${selectedClass.idLopHoc}/${idUser}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });

        if (response.status === 200) {
          setResultMessage("Đăng ký lớp học thành công!");
          setResultModalVisible(true);
          setTimeout(() => {
            setResultModalVisible(false);
            navigation.navigate('DashboardScreen');
          }, 1250);
        } else {
          setResultMessage("Đăng ký không thành công, vui lòng thử lại.");
          setResultModalVisible(true);
        }
      } catch (error) {
        setResultMessage("Đăng ký không thành công, vui lòng thử lại.");
        console.error("Lỗi trong quá trình xác thực thanh toán:", error);
      } finally {
        setConfirmationModalVisible2(false);
        setResultModalVisible(true);
      }
    } else {
      setResultMessage("Không tìm thấy lớp học hoặc người dùng.");
      setResultModalVisible(true);
    }
  };

  if (loading) {
    return <Text>Đang tải khóa học...</Text>;
  }

  return (
    <ImageBackground source={require('../../../image/bglogin.png')} style={styles.background}>
      <View style={styles.titleContainer}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Text style={{ fontSize: 16, color: '#00405d', textAlign: 'center', left: -530 }}>Quay lại</Text>
        </TouchableOpacity>
        <Text style={styles.title}>Đăng ký khóa học</Text>
      </View>
      <ScrollView contentContainerStyle={styles.scrollContainer}>
        <View style={styles.coursesContainer}>
          {courses.map((course) => (
            <View key={course.idKhoaHoc} style={styles.courseCard}>
              <Image source={{ uri: `data:image/png;base64,${course.image}` }} style={styles.courseImage} />
              <View style={styles.infoContainer}>
                <Text style={styles.courseTitle}>{course.tenKhoaHoc}</Text>
                <Text style={styles.description}>{course.moTa}</Text>
                <Text style={styles.details}>Thời lượng: {course.thoiGianDienRa} tháng</Text>
                <Text style={styles.details}>Số buổi: {course.soBuoi} buổi/tuần</Text>
                <Text style={styles.details}>Học phí: <Text style={{ color: 'red' }}>{course.giaTien}đ</Text></Text>

                <TouchableOpacity
                  style={styles.registerButton}
                  onPress={() => handleCourseClick(course)}
                >
                  <Text style={styles.registerButtonText}>Chọn khóa học này</Text>
                </TouchableOpacity>
              </View>
            </View>
          ))}
        </View>
      </ScrollView>

      <Modal visible={isModalVisible} transparent={true} animationType="slide" onRequestClose={() => setModalVisible(false)}>
        <View style={styles.modalOverlay}>
          <View style={styles.modalContainer}>
            <Text style={styles.modalTitle}>Chọn lớp học</Text>
            {classes.length > 0 ? (
              classes.map((classItem) => (
                <TouchableOpacity key={classItem.idLopHoc} onPress={() => handleClassSelect(classItem)} style={styles.classOption}>
                  <Text style={styles.classText}>{classItem.tenLopHoc}</Text>
                </TouchableOpacity>
              ))
            ) : (
              <Text>Không có lớp nào cho khóa học này.</Text>
            )}
            <TouchableOpacity onPress={() => setModalVisible(false)} style={styles.closeButton}>
              <Text style={styles.closeButtonText}>Đóng</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>

      <Modal visible={isConfirmationModalVisible} transparent={true} animationType="slide" onRequestClose={() => setConfirmationModalVisible(false)}>
        <View style={styles.modalOverlay}>
          <View style={styles.modalContainer}>
            <Text style={styles.modalTitle}>Thanh toán để hoàn tất đăng ký</Text>
            <TouchableOpacity onPress={handleProceedToPayment} style={styles.confirmButton}>
              <Text style={styles.confirmButtonText}>Thanh toán</Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={() => setConfirmationModalVisible(false)} style={styles.confirmButton}>
              <Text style={styles.confirmButtonText}>Để sau</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>

      <Modal visible={isPaymentOptionModalVisible} transparent={true} animationType="slide" onRequestClose={() => setPaymentOptionModalVisible(false)}>
        <View style={styles.modalOverlay}>
          <View style={styles.modalContainer}>
            <Text style={styles.modalTitle}>Chọn phương thức thanh toán</Text>

            <TouchableOpacity onPress={() => setPaymentOption('center')} style={styles.classOption}>
              <View style={[styles.radioButtonOuter, paymentOption === 'center' && styles.radioButtonSelected]}>
                {paymentOption === 'center' && <View style={styles.radioButtonInner} />}
              </View>
              <Text style={styles.classText}>Thanh toán tại trung tâm</Text>
            </TouchableOpacity>

            <TouchableOpacity onPress={() => setPaymentOption('online')} style={styles.classOption}>
              <View style={[styles.radioButtonOuter, paymentOption === 'online' && styles.radioButtonSelected]}>
                {paymentOption === 'online' && <View style={styles.radioButtonInner} />}
              </View>
              <Text style={styles.classText}>Thanh toán online</Text>
            </TouchableOpacity>

            <TouchableOpacity onPress={handleConfirmPayment} style={styles.closeButton}>
              <Text style={styles.closeButtonText}>Xác nhận</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
      <Modal visible={isConfirmationModalVisible2} transparent={true} animationType="slide" onRequestClose={() => setConfirmationModalVisible2(false)}>
        <View style={styles.modalOverlay}>
          <View style={styles.modalContainer}>
            <Text style={styles.modalTitle}>Quét mã QR để thanh toán</Text>
            <Image source={require('../../../image/qrCode.png')} style={styles.qrImage} />
            <Text style={styles.bankInfo}>Ngân hàng: ABC Bank</Text>
            <Text style={styles.bankInfo}>Số tài khoản: 123456789</Text>
            <Text style={styles.bankInfo}>Tên tài khoản: Trung Tâm EFY</Text>
            <TouchableOpacity onPress={handleConfirmPaymentVerification} style={styles.closeButton}>
              <Text style={styles.closeButtonText}>Xác thực thanh toán</Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={() => setConfirmationModalVisible2(false)} style={styles.closeButton}>
              <Text style={styles.closeButtonText}>Hủy</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
      <Modal
        visible={resultModalVisible}
        transparent={true}
        animationType="slide"
        onRequestClose={() => setResultModalVisible(false)}
      >
        <View style={styles.modalOverlay}>
          <View style={styles.resultModalContainer}>
            <Text style={styles.resultMessageText}>{resultMessage}</Text>
            <TouchableOpacity onPress={() => setResultModalVisible(false)} style={styles.closeButton}>
              <Text style={styles.closeButtonText}>Đóng</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    </ImageBackground>
  );
};


const styles = StyleSheet.create({
  background: {
    flex: 1,
    width: 'auto',
    height: 990,
    justifyContent: 'center',
    alignItems: 'center',
  },
  titleContainer: {
    padding: 20,
    alignItems: 'center',
    flexDirection: 'row'
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#00405d',
  },
  scrollContainer: {
    flexGrow: 1,
    justifyContent: 'space-between',
  },
  coursesContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  courseCard: {
    width: '22%',
    margin: 30,
    backgroundColor: '#fff',
    borderRadius: 10,
    overflow: 'hidden',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 5,
  },
  courseImage: {
    width: '100%',
    height: 300,
  },
  infoContainer: {
    padding: 10,
  },
  courseTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  description: {
    fontSize: 14,
    color: '#666',
    marginBottom: 5,
  },
  details: {
    fontSize: 14,
    color: '#333',
    marginBottom: 5,
  },
  registerButton: {
    backgroundColor: '#00405d',
    padding: 10,
    borderRadius: 5,
    marginTop: 10,
  },
  registerButtonText: {
    color: 'white',
    textAlign: 'center',
    fontWeight: 'bold',
  },
  modalOverlay: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },
  modalContainer: {
    width: '30%',
    backgroundColor: '#fff',
    padding: 20,
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  classOption: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 10,
  },
  classText: {
    fontSize: 16,
    color: '#333',
  },
  closeButton: {
    marginTop: 10,
    padding: 10,
    backgroundColor: '#00405d',
    borderRadius: 5,
    alignItems: 'center',
    justifyContent: 'center',
    width: 200,
  },
  closeButtonText: {
    color: 'white',
    fontWeight: 'bold',
  },
  confirmButton: {
    marginTop: 10,
    padding: 10,
    backgroundColor: '#00405d',
    borderRadius: 5,
    alignItems: 'center',
    justifyContent: 'center',
    width: 200,
  },
  confirmButtonText: {
    color: 'white',
    fontWeight: 'bold',
  },
  qrContainer: {
    alignItems: 'center',
    marginVertical: 20,
  },
  bankInfo: {
    fontSize: 14,
    color: '#333',
    marginTop: 5,
  },
  radioButtonOuter: {
    width: 20,
    height: 20,
    borderRadius: 10,
    borderWidth: 2,
    borderColor: '#00405d',
    marginRight: 10,
    justifyContent: 'center',
    alignItems: 'center',
  },
  radioButtonInner: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: '#00405d',
  },
  radioButtonSelected: {
    borderColor: '#00405d',
  },
  qrImage: {
    width: 150,
    height: 150,
    marginVertical: 20,
  },
  resultModalContainer: {
    width: '80%',
    backgroundColor: '#fff',
    padding: 20,
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  resultMessageText: {
    fontSize: 16,
    color: '#333',
    textAlign: 'center',
    marginBottom: 20,
  },
});

export default CourseRegistrationScreen;

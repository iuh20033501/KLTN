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
  lichHoc: string;
  giangVien: any;
  idLopHoc: number;
  tenLopHoc: string;
  soHocVien: string;
  gmail: string
};

const CourseRegistrationScreen = ({ navigation, route }: { navigation: any, route: any }) => {
  const { idUser } = route.params;
  const [loading, setLoading] = useState(true);
  const [courses, setCourses] = useState<Course[]>([]);
  const [classes, setClasses] = useState<Class[]>([]);
  const [selectedCourse, setSelectedCourse] = useState<Course | null>(null);
  const [selectedClass, setSelectedClass] = useState<Class | null>(null);
  const [isModalVisible, setModalVisible] = useState(false);
  const [classDetails, setClassDetails] = useState<any>(null);
  const [isConfirmationModalVisible, setConfirmationModalVisible] = useState(false);
  const [isPaymentOptionModalVisible, setPaymentOptionModalVisible] = useState(false);
  const [paymentOption, setPaymentOption] = useState<'center' | 'online' | null>(null);
  const [isConfirmationModalVisible2, setConfirmationModalVisible2] = useState(false);
  const [resultModalVisible, setResultModalVisible] = useState(false);
  const [resultMessage, setResultMessage] = useState('');
  const [imageCourse, setImageCourse] = useState('');
  const [classDetailModalVisible, setClassDetailModalVisible] = useState(false);
  const [nextStepModalVisible, setNextStepModalVisible] = useState(false);
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
    setClassDetailModalVisible(true)
  };
  const handleClassRegister = () => {
    setClassDetailModalVisible(false)
    setConfirmationModalVisible(true);

  };
  const handleConfirm = () => {
    setResultModalVisible(false);
    setNextStepModalVisible(true);
  };
  const handlePayment = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token || !selectedClass) {
        setResultMessage('Token không tồn tại hoặc lớp học không được chọn');
        setResultModalVisible(true);
        return;
      }

      const response = await http.post(
        `thanhToan/create/${selectedClass.idLopHoc}/${idUser}`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      if (response.status === 200) {
        setResultMessage('Đăng ký thành công');
        setConfirmationModalVisible(false);
        setResultModalVisible(true);
        setTimeout(() => {
          handleConfirm();
      }, 1000);
      } else {
        setResultMessage('Đăng ký thất bại. Vui lòng thử lại.');
        setResultModalVisible(true);
      }
    } catch (error) {
      console.error(error);
      setResultMessage('CBạn đã đăng ký lớp học này');
      setResultModalVisible(true);
    }
  };

  if (loading) {
    return <Text>Đang tải khóa học...</Text>;
  }

  const getImageCourseUri = (imageData: string) => {
    if (!imageData) return null;
    if (imageData.startsWith("data:image")) {
      return imageData;
    }
    const defaultMimeType = "image/png";
    let mimeType = defaultMimeType;

    if (/^\/9j/.test(imageData)) {
      mimeType = "image/jpeg";
    } else if (/^iVBOR/.test(imageData)) {
      mimeType = "image/png";
    } else if (/^R0lGOD/.test(imageData)) {
      mimeType = "image/gif";
    } else if (/^Qk/.test(imageData)) {
      mimeType = "image/bmp";
    } else if (/^UklGR/.test(imageData)) {
      mimeType = "image/webp";
    }

    return `data:${mimeType};base64,${imageData}`;
  };
  return (
    <ImageBackground source={require('../../../image/bglogin.png')} style={styles.background}>
      <TouchableOpacity style={styles.backButton} onPress={() => navigation.navigate('DashboardScreen')}>
        <Text style={styles.backButtonText}>Quay về</Text>
      </TouchableOpacity>
      <View style={styles.titleContainer}>
        <Text style={styles.title}>Đăng ký khóa học</Text>
      </View>
      <ScrollView contentContainerStyle={styles.scrollContainer}>
        <View style={styles.coursesContainer}>
          {courses.map((course) => (
            <View key={course.idKhoaHoc} style={styles.courseCard}>
              <Image
                source={course.image ? { uri: getImageCourseUri(course.image) } : require('../../../image/efy.png')}
                style={styles.courseImage}
              />
              <View style={styles.infoContainer}>
                <Text style={styles.courseTitle}>{course.tenKhoaHoc}</Text>
                <Text style={styles.description}>{course.moTa}</Text>
                <Text style={styles.details}>Thời lượng: {course.thoiGianDienRa} tháng</Text>
                <Text style={styles.details}>Số buổi: {course.soBuoi} buổi</Text>
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
              <View style={styles.classOptionsContainer}>
                {classes.map((classItem) => (
                  <TouchableOpacity
                    key={classItem.idLopHoc}
                    onPress={() => handleClassSelect(classItem)}
                    style={styles.classOption}
                  >
                    <Text style={styles.classText}>{classItem.tenLopHoc}</Text>
                  </TouchableOpacity>
                ))}
              </View>
            ) : (
              <Text>Không có lớp nào cho khóa học này.</Text>
            )}
            <TouchableOpacity onPress={() => setModalVisible(false)} style={styles.closeButton}>
              <Text style={styles.closeButtonText}>Đóng</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>

      <Modal
        visible={classDetailModalVisible}
        transparent={true}
        animationType="slide"
        onRequestClose={() => setClassDetailModalVisible(false)}
      >
        <View style={styles.modalOverlay}>
          <View style={styles.modalContainer}>
            <Text style={styles.modalTitle}>Chi tiết lớp học</Text>
            {selectedClass ? (
              <>
                <Text style={styles.detailText}>Tên lớp: {selectedClass.tenLopHoc || 'Không có thông tin'}</Text>
                <Text style={styles.detailText}>Lịch học: {selectedClass.lichHoc || 'Không có thông tin'}</Text>
                <Text style={styles.detailText}>Số lượng: {selectedClass.soHocVien || 'Không có thông tin'}</Text>
                <Text style={styles.detailText}>Giảng viên: {selectedClass.giangVien?.hoTen || 'Không có thông tin'}</Text>
              </>
            ) : (
              <Text>Đang tải thông tin lớp học...</Text>
            )}
            <View style={{ flexDirection: 'row', justifyContent: 'center' }}>
              <TouchableOpacity onPress={handleClassRegister} style={styles.confirmButton}>
                <Text style={styles.confirmButtonText}>Đăng ký</Text>
              </TouchableOpacity>
              <TouchableOpacity onPress={() => setClassDetailModalVisible(false)} style={styles.closeButton}>
                <Text style={styles.closeButtonText}>Đóng</Text>
              </TouchableOpacity>
            </View>

          </View>
        </View>
      </Modal>

      <Modal visible={isConfirmationModalVisible} transparent={true} animationType="slide" onRequestClose={() => setConfirmationModalVisible(false)}>
        <View style={styles.modalOverlay}>
          <View style={styles.modalContainer}>
            <Text style={styles.modalTitle}>Hoàn tất đăng ký</Text>
            <View style={{ flexDirection: 'row', justifyContent: 'center' }}>
              <TouchableOpacity onPress={handlePayment} style={styles.confirmButton}>
                <Text style={styles.confirmButtonText}>Đăng ký</Text>
              </TouchableOpacity>
              <TouchableOpacity onPress={() => setConfirmationModalVisible(false)} style={styles.confirmButton}>
                <Text style={styles.confirmButtonText}>Để sau</Text>
              </TouchableOpacity>
            </View>
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
            <TouchableOpacity onPress={() => setResultModalVisible(false)} style={styles.confirmButton}>
              <Text style={styles.confirmButtonText}>Đóng</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>

      <Modal visible={nextStepModalVisible} transparent={true} animationType="slide"  onRequestClose={() => setNextStepModalVisible(false)}> 
        <View style={styles.modalOverlay}>
          <View style={styles.resultModalContainer}>
            <Text style={styles.resultMessageText}>Bạn muốn đi tới trang thanh toán hay quay về trang chủ</Text>
            <View style={{ flexDirection: 'row', justifyContent: 'center' }}>
              <TouchableOpacity  onPress={() => {setNextStepModalVisible(false),navigation.navigate('PaymentScreen', { idUser })}}  style={styles.confirmButton}>
                <Text style={styles.confirmButtonText}>Đi tới thanh toán</Text>
              </TouchableOpacity>
              <TouchableOpacity onPress={() => {setNextStepModalVisible(false),navigation.navigate('DashboardScreen')}} style={styles.confirmButton}>
                <Text style={styles.confirmButtonText}>Quay về trang chủ</Text>
              </TouchableOpacity>
            </View>
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
    flexDirection: 'row',
    height: 30
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#00405d',
    top: -20
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
    width: 450,
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
  classOptionsContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'center',
    marginBottom: 20,
  },
  classOption: {
    backgroundColor: '#FF0000',
    flexDirection: 'row',
    alignItems: 'center',
    padding: 10,
    margin: 5,
    borderRadius: 15

  },
  classText: {
    fontSize: 16,
    color: 'white',
    fontWeight: 'bold',
  },
  closeButton: {
    marginTop: 10,
    padding: 10,
    backgroundColor: '#00405d',
    borderRadius: 5,
    alignItems: 'center',
    justifyContent: 'center',
    width: 150,

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
    width: 150,
    marginRight: 10
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
    borderColor: 'white',
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
    borderColor: 'white',
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
  detailText: {
    fontSize: 16,
    marginVertical: 5,
    color: '#333',
  },
  backButton: {
    alignSelf: 'flex-start',
    padding: 10,
    backgroundColor: '#00405d',
    borderRadius: 5,
    top: 25,
    left: 277
  },
  backButtonText: {
    color: '#fff',
    fontWeight: 'bold',
  },
});

export default CourseRegistrationScreen;

import React from 'react';
import { View, Text, Image, TouchableOpacity, StyleSheet, ScrollView } from 'react-native';

const courses = [
  {
    id: '1',
    title: 'Khóa học COMBO',
    description: 'Tiếng Anh từ đầu tới giỏi toàn diện 4 kỹ năng nâng cao',
    duration: '12 tháng',
    schedule: '3 buổi/tuần, 1.5h',
    image: require('../../../image/combo.jpeg'),
  },
  {
    id: '2',
    title: 'Khóa học TOEIC',
    description: 'Khóa học từ đầu tới giỏi TOEIC 4 kỹ năng',
    duration: '9 tháng',
    schedule: '3 buổi/tuần, 1.5h',
    image: require('../../../image/toeic1.jpg'),
  },
  {
    id: '3',
    title: 'Khóa học IELTS',
    description: 'Khóa học IELTS nâng cao',
    duration: '6 tháng',
    schedule: '2 buổi/tuần, 1.5h',
    image: require('../../../image/ielts.jpg'),
  },
  {
    id: '4',
    title: 'Khóa học COMBO 2',
    description: 'Tiếng Anh từ đầu tới giỏi toàn diện 4 kỹ năng nâng cao',
    duration: '9 tháng',
    schedule: '3 buổi/tuần, 1.5h',
    image: require('../../../image/combo2.jpg'),
  },
  {
    id: '5',
    title: 'Khóa học COMBO 3',
    description: 'Tiếng Anh từ cơ bản tới giỏi toàn diện 4 kỹ năng',
    duration: '6 tháng',
    schedule: '3 buổi/tuần, 1.5h',
    image: require('../../../image/combo3.jpg'),
  },
  {
    id: '6',
    title: 'Khóa học TOEIC 2',
    description: 'Khóa học TOEIC nâng cao',
    duration: '6 tháng',
    schedule: '3 buổi/tuần, 1.5h',
    image:  require('../../../image/toeic2.jpg'),
  },
];

const CoursesComponent = () => {
  return (
    <View style={styles.screenContainer}>
       <View style={styles.bannerContainer}>
          <Image
            source={require('../../../image/course.png')} 
            style={styles.bannerImage}
          />
          <View style={styles.overlay}>
            <Text style={styles.bannerText}>Chọn ngay cho mình một khóa học phù hợp tại EFY</Text>
          </View>
        </View>
      <ScrollView 
        contentContainerStyle={styles.scrollContainer} 
        showsVerticalScrollIndicator={false}  
        showsHorizontalScrollIndicator={false} 
      >
        <View style={styles.coursesContainer}>
          {courses.map((item) => (
            <View key={item.id} style={styles.courseCard}>
              <Image source={item.image} style={styles.courseImage} />
              <View style={styles.infoContainer}>
                <Text style={styles.courseTitle}>{item.title}</Text>
                <Text style={styles.description}>{item.description}</Text>
                <Text style={styles.details}>Thời lượng: {item.duration}</Text>
                <Text style={styles.details}>Lịch học: {item.schedule}</Text>
                <TouchableOpacity style={styles.enrollButton}>
                  <Text style={styles.enrollButtonText}>Đăng ký khóa học này</Text>
                </TouchableOpacity>
              </View>
            </View>
          ))}
        </View>
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  screenContainer: {
    flex: 1,
    backgroundColor: '#f8f8f8',
  },
  scrollContainer: {
    flexGrow: 1, 
    justifyContent: 'space-between',
  },
  bannerContainer: {
    position: 'relative',
    height: 270, 
  },
  bannerImage: {
    width: '100%',
    height: '100%',
    resizeMode: 'cover',
  },
  overlay: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.2)', 
  },
  bannerText: {
    fontSize: 40,
    fontWeight: 'bold',
    color: '#fff',
    marginBottom: 10,
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
  enrollButton: {
    backgroundColor: '#0066cc',
    padding: 10,
    borderRadius: 5,
    marginTop: 5,
  },
  enrollButtonText: {
    color: 'white',
    textAlign: 'center',
  },
  footer: {
    backgroundColor: '#333',
    padding: 20,
    alignItems: 'center',
  },
  footerText: {
    color: '#fff',
    fontSize: 14,
  },
});

export default CoursesComponent;

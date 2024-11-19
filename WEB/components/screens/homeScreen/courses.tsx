import http from '@/utils/http';
import React, { useEffect, useState } from 'react';
import { View, Text, Image, TouchableOpacity, StyleSheet, ScrollView } from 'react-native';


type Course = {
  idKhoaHoc: number;
  tenKhoaHoc: string;
  moTa: string;
  thoiGianDienRa: string;
  soBuoi: string;
  image: string;
};
const CoursesComponent = () => {
  const [courses, setCourses] = useState<Course[]>([]);

  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchCourses = async () => {
    try {
      const response = await http.get('auth/noauth/findAllKhoa');
      setCourses(response.data);
      console.log(response.data)
    } catch (error) {
      console.error('Error fetching courses:', error);
    }
  };

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
            <View key={item.idKhoaHoc} style={styles.courseCard}>
              <Image  source={item.image ? { uri: getImageCourseUri(item.image) } : require('../../../image/efy.png')} style={styles.courseImage} />
              <View style={styles.infoContainer}>
                <Text style={styles.courseTitle}>{item.tenKhoaHoc}</Text>
                <Text style={styles.description}>{item.moTa}</Text>
                <Text style={styles.details}>Thời lượng: {item.thoiGianDienRa} tháng</Text>
                <Text style={styles.details}>Số buổi: {item.soBuoi} buổi/tuần</Text>
                <TouchableOpacity style={styles.enrollButton}>
                  <Text style={styles.enrollButtonText}>Tìm hiểu khóa học này</Text>
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

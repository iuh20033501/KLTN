import React, { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ActivityIndicator, FlatList } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import { LinearGradient } from 'expo-linear-gradient';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface ExamInfo {
    idTest: number;
    loaiTest: string;
    ngayBD: string;
    ngayKT: string;
    thoiGianLamBai: number;
    trangThai: boolean;
    xetDuyet: boolean;
    lopHoc: {
        idLopHoc: number;
        soHocVien: number;
        tenLopHoc: string;
        trangThai: string;
        ngayBD: string;
    };
    questionCount?: number; 
    hasCompletedTest?: boolean; // Thêm thuộc tính này

}
export default function StudentExamScreen({ navigation, route }: { navigation: any; route: any }) {
    const [lessons, setLessons] = useState<ExamInfo[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const { idLopHoc, idUser,tenlopHoc } = route.params;


    const fetchExams = async () => {
      try {
          const token = await AsyncStorage.getItem('accessToken');
          if (!token) {
              console.error('No token found');
              return;
          }
  
          const response = await http.get(`baitest/getBaiTestofLopTrue/${idLopHoc}`, {
              headers: { Authorization: `Bearer ${token}` },
          });
  
          const examsWithCompletionStatus = await Promise.all(
              response.data.map(async (exam: any) => {
                  const resultResponse = await http.get(`/baitest/getKetQua/${exam.idTest}/${idUser}`, {
                      headers: { Authorization: `Bearer ${token}` },
                  });
  
                  return {
                      ...exam,
                      hasCompletedTest: resultResponse.data?.idKetQua ? true : false,
                  };
              })
          );
  
          setLessons(examsWithCompletionStatus);
      } catch (error) {
          console.error('Failed to fetch exams:', error);
      } finally {
          setIsLoading(false);
      }
  };
  
    const isExamVisible = (ngayBD: string, ngayKT: string): boolean => {
        const now = new Date();
        const startDate = new Date(ngayBD);
        const endDate = new Date(ngayKT);
        return now >= startDate && now <= endDate;
    };

    const getTestType = (loaiTest: string): string => {
        if (loaiTest === "CK") return "Cuối Kì";
        if (loaiTest === "GK") return "Giữa Kì";
        return loaiTest;
      };

     
    useEffect(() => {
        fetchExams();
    }, []);

    const renderLessonCard = ({ item }: { item: ExamInfo }) => {
      const startDate = new Date(item.ngayBD);
      const endDate = new Date(item.ngayKT);
  
      return (
          <TouchableOpacity
              style={[
                  styles.lessonButton,
                  item.hasCompletedTest ? { backgroundColor: '#cccccc' } : {}, // Thêm màu xám nếu bài thi đã hoàn thành
              ]}
              onPress={() => {
                  if (!item.hasCompletedTest) {
                      navigation.navigate('ExamScreen', {
                          idTest: item.idTest,
                          idUser,
                          thoiGianLamBai: item.thoiGianLamBai,
                          loaiTest: item.loaiTest,
                          tenlopHoc,
                      });
                  }
              }}
              disabled={item.hasCompletedTest} // Không cho nhấn vào bài thi đã hoàn thành
          >
              <View style={styles.card}>
                  <Text style={styles.lessonText}>
                      Loại bài thi: <Text style={{ color: 'red' }}>{getTestType(item.loaiTest)}</Text>
                  </Text>
                  <Text style={styles.lessonDetails}>Số câu hỏi: {item.questionCount}</Text>
                  <Text style={styles.lessonDetails}>Thời gian làm bài: {item.thoiGianLamBai} phút</Text>
                  <Text style={styles.lessonDetails}>
                      Ngày bắt đầu: {startDate?.toLocaleDateString('vi-VN')} -{' '}
                      {startDate?.toLocaleString('vi-VN', { hour: '2-digit', minute: '2-digit' })}
                  </Text>
                  <Text style={styles.lessonDetails}>
                      Ngày kết thúc: {endDate?.toLocaleDateString('vi-VN')} -{' '}
                      {endDate?.toLocaleString('vi-VN', { hour: '2-digit', minute: '2-digit' })}
                  </Text>
                  <Text style={styles.lessonDetails}>
                      {item.hasCompletedTest ? 'Bài thi đã hoàn thành' : 'Bài thi chưa hoàn thành'}
                  </Text>
              </View>
          </TouchableOpacity>
      );
  };
  
    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <TouchableOpacity onPress={() => navigation.goBack()}>
                    <Icon name="arrow-back-outline" size={24} color="black" />
                </TouchableOpacity>
                <Text style={styles.headerText}>Danh sách bài thi</Text>
                <Icon name="star-outline" size={24} color="gold" style={styles.starIcon} />
            </View>

            {isLoading ? (
                <ActivityIndicator size="large" color="#00405d" />
            ) : (
              <FlatList
              data={lessons.filter((lesson) => isExamVisible(lesson.ngayBD, lesson.ngayKT))}
              renderItem={({ item }) => renderLessonCard({ item })}
              keyExtractor={(item) => item.idTest.toString()}
              contentContainerStyle={styles.lessonList}
          />
            )}
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        padding: 16,
    },
    header: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        paddingHorizontal: 16,
        marginBottom: 20,
    },
    headerText: {
        fontSize: 22,
        fontWeight: 'bold',
        color: '#00bf63',
        marginLeft: 10,
    },
    lessonList: {
        paddingBottom: 20,
    },
    buttonGradient: {
        paddingVertical: 20,
        borderRadius: 8,
        justifyContent: 'center',
        paddingHorizontal: 15,
    },
    lessonButton: {
        marginBottom: 16,
        borderRadius: 10,
        overflow: 'hidden',
      },
    card: {
        backgroundColor: '#d3d3d3', 
        padding: 15,
        borderRadius: 10,
      },
      lessonText: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#333',
        marginBottom: 5,
        textAlign: 'left',
      },
      lessonDetails: {
        fontSize: 14,
        color: '#black',
        marginBottom: 3,
        textAlign: 'left',
      },
    starIcon: {
        marginRight: 10,
    },
});

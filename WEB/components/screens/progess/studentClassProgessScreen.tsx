import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, ActivityIndicator, TouchableOpacity } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useFocusEffect } from '@react-navigation/native'; // Import useFocusEffect
import http from '@/utils/http';
import Icon from 'react-native-vector-icons/Ionicons';

interface ProgressData {
  className: string;
  progress: number;
  completed: number;
  total: number;
}

export default function StudentClassProgressScreen() {
  const [progressData, setProgressData] = useState<{
    classProgress: ProgressData[];
    assignmentProgress: ProgressData[];
  }>({ classProgress: [], assignmentProgress: [] });
  const [currentClassIndex, setCurrentClassIndex] = useState(0);
  const [loading, setLoading] = useState(true);

  const fetchProgressData = async () => {
    try {
      setLoading(true);
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) throw new Error('Token không tồn tại');
  
      const profileResponse = await http.get('/auth/profile', {
        headers: { Authorization: `Bearer ${token}` },
      });
      const userId = profileResponse.data.u?.idUser;
      if (!userId) throw new Error('Không tìm thấy idUser');
  
      const classesResponse = await http.get(`/hocvien/getByHV/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
  
      const classList = (classesResponse.data || []).filter(
        (lop: { trangThai: string }) => lop.trangThai === "FULL"
      );
  
      const classProgress: ProgressData[] = [];
      const assignmentProgress: ProgressData[] = [];
  
      for (const lop of classList) {
        try {
          const classTotalResponse = await http.get(`/buoihoc/getbuoiHocByLop/${lop.idLopHoc}`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          const classCompletedResponse = await http.get(`/buoihoc/getBuoiDaHoc/${lop.idLopHoc}`, {
            headers: { Authorization: `Bearer ${token}` },
          });
  
          const assignmentTotalResponse = await http.get(`/baitap/getBaiTapofLop/${lop.idLopHoc}`, {
            headers: { Authorization: `Bearer ${token}` },
          });
  
          let assignmentCompleted = 0;
          try {
            const assignmentCompletedResponse = await http.get(
              `/baitap/getTienTrinh/${userId}/${lop.idLopHoc}`,
              { headers: { Authorization: `Bearer ${token}` } }
            );
            assignmentCompleted = assignmentCompletedResponse.data.length || 0;
          } catch (apiError) {
            console.warn(`Không tìm thấy tiến trình cho lớp học: ${lop.tenLopHoc}.`, apiError);
            assignmentCompleted = 0;
          }
  
          const classTotal = classTotalResponse.data.length || 0;
          const classCompleted = classCompletedResponse.data.length || 0;
          const assignmentTotal = assignmentTotalResponse.data.length || 0;
  
          classProgress.push({
            className: lop.tenLopHoc || 'Không rõ',
            progress: classTotal > 0 ? Math.round((classCompleted / classTotal) * 100) : 0,
            completed: classCompleted,
            total: classTotal,
          });
  
          assignmentProgress.push({
            className: lop.tenLopHoc || 'Không rõ',
            progress: assignmentTotal > 0 ? Math.round((assignmentCompleted / assignmentTotal) * 100) : 0,
            completed: assignmentCompleted,
            total: assignmentTotal,
          });
        } catch (innerError) {
          console.error(`Lỗi khi tải dữ liệu cho lớp học: ${lop.tenLopHoc}`, innerError);
        }
      }
  
      setProgressData({ classProgress, assignmentProgress });
    } catch (error) {
      console.error('Error fetching progress data:', error);
      setProgressData({ classProgress: [], assignmentProgress: [] });
    } finally {
      setLoading(false);
    }
  };
  
  useFocusEffect(
    React.useCallback(() => {
      fetchProgressData();
    }, [])
  );

  const handleClassNext = () => {
    setCurrentClassIndex((prev) => (prev + 1) % progressData.classProgress.length);
  };

  const handleClassPrevious = () => {
    setCurrentClassIndex((prev) =>
      prev === 0 ? progressData.classProgress.length - 1 : prev - 1
    );
  };

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color="#00405d" />
        <Text>Đang tải dữ liệu...</Text>
      </View>
    );
  }

  const currentClass = progressData.classProgress[currentClassIndex];
  const currentAssignment = progressData.assignmentProgress[currentClassIndex];

  return (
    <View style={styles.container}>
      <TouchableOpacity style={styles.iconButton} onPress={handleClassPrevious}>
        <Icon name="chevron-back-outline" size={30} color="black" />
      </TouchableOpacity>

      <View style={styles.contentContainer}>
        <Text style={styles.title}>{currentClass.className}</Text>
        <View style={styles.card}>
          <Text style={styles.sectionTitle}>Tiến độ buổi học</Text>
          <Text style={styles.progressText}>{currentClass.progress}%</Text>
          <Text style={styles.detailText}>
            {currentClass.completed}/{currentClass.total} buổi học đã hoàn thành
          </Text>
        </View>

        <View style={styles.card}>
          <Text style={styles.sectionTitle}>Tiến độ bài tập</Text>
          <Text style={styles.progressText}>{currentAssignment.progress}%</Text>
          <Text style={styles.detailText}>
            {currentAssignment.completed}/{currentAssignment.total} bài tập đã hoàn thành
          </Text>
        </View>
      </View>

      <TouchableOpacity style={styles.iconButton} onPress={handleClassNext}>
        <Icon name="chevron-forward-outline" size={30} color="black" />
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  contentContainer: {
    flex: 1,
    alignItems: 'center',
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    color: 'black',
    marginBottom: 10,
    alignSelf: 'flex-start',
  },
  card: {
    backgroundColor: '#fff',
    padding: 20,
    borderRadius: 10,
    alignItems: 'center',
    marginBottom: 20,
    width: 400,
    elevation: 2,
    shadowColor: '#000',
    shadowOpacity: 0.2,
    shadowRadius: 4,
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
    color: 'black',
    marginBottom: 10,
  },
  progressText: {
    fontSize: 32,
    fontWeight: 'bold',
    color: '#3498db',
  },
  detailText: {
    fontSize: 16,
    color: '#777',
  },
  iconButton: {
    justifyContent: 'center',
    alignItems: 'center',
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, Dimensions, ActivityIndicator, TouchableOpacity } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import http from '@/utils/http';
import { useFocusEffect } from '@react-navigation/native';
import Icon from 'react-native-vector-icons/Ionicons'; 

interface ProgressData {
  className: string;
  progress: number;
  completed: number;
  total: number;
}

export default function TeacherClassProgessScreen() {
  const [classProgress, setClassProgress] = useState<ProgressData[]>([]);
  const [currentIndex, setCurrentIndex] = useState(0); 
  const [loading, setLoading] = useState(true);

  const fetchProgressData = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) return;
  
      const profileResponse = await http.get('/auth/profile', {
        headers: { Authorization: `Bearer ${token}` },
      });
      const userId = profileResponse.data.u.idUser;
  
      const classesResponse = await http.get(`/lopHoc/getByGv/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const classList = classesResponse.data.filter(
        (lop: { trangThai: string }) => lop.trangThai === "FULL"
      );
      const progressData: ProgressData[] = [];
      for (const lop of classList) {
        const totalResponse = await http.get(`/buoihoc/getbuoiHocByLop/${lop.idLopHoc}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const completedResponse = await http.get(`/buoihoc/getBuoiDaHoc/${lop.idLopHoc}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const total = totalResponse.data.length;
        const completed = completedResponse.data.length;
        const progress = total > 0 ? Math.round((completed / total) * 100) : 0;
        progressData.push({
          className: lop.tenLopHoc || 'Không rõ',
          progress,
          completed,
          total,
        });
      }
      setClassProgress(progressData);
    } catch (error) {
      console.error('Error fetching progress data:', error);
    } finally {
      setLoading(false);
    }
  };
  useFocusEffect(
    React.useCallback(() => {
      fetchProgressData();
    }, [])
  );

  const handleNext = () => {
    setCurrentIndex((prevIndex) => (prevIndex + 1) % classProgress.length);
  };

  const handlePrevious = () => {
    setCurrentIndex((prevIndex) =>
      prevIndex === 0 ? classProgress.length - 1 : prevIndex - 1
    );
  };

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color="#00405d" />
        <Text>Loading progress data...</Text>
      </View>
    );
  }

  if (classProgress.length === 0) {
    return (
      <View style={styles.noDataContainer}>
        <Text style={styles.noDataText}>Không có dữ liệu lớp học</Text>
      </View>
    );
  }

  const currentClass = classProgress[currentIndex];

  return (
    <View style={styles.container}>
      <TouchableOpacity style={styles.iconButton} onPress={handlePrevious}>
        <Icon name="chevron-back-outline" size={30} color="black" />
      </TouchableOpacity>

      <View style={styles.contentContainer}>
        <Text style={styles.title}>{currentClass.className}</Text>
        <View style={styles.progressContainer}>
          <Text style={styles.progressText}>{currentClass.progress}%</Text>
          <Text style={styles.detailText}>
            {currentClass.completed}/{currentClass.total} buổi học đã hoàn thành
          </Text>
        </View>
      </View>

      <TouchableOpacity style={styles.iconButton} onPress={handleNext}>
        <Icon name="chevron-forward-outline" size={30} color="black" />
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row', 
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
  },
  contentContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
    color: 'black',
    marginBottom: 20,
  },
  progressContainer: {
    alignItems: 'center',
    backgroundColor: '#fff',
    padding: 20,
    borderRadius: 10,
    elevation: 2,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.2,
    shadowRadius: 4,
    width: 400,
  },
  progressText: {
    fontSize: 32,
    fontWeight: 'bold',
    color: '#3498db',
    marginBottom: 10,
  },
  detailText: {
    fontSize: 16,
    color: '#777',
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  noDataContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  noDataText: {
    fontSize: 16,
    color: '#777',
  },
  iconButton: {
    justifyContent: 'center',
    alignItems: 'center',
  
    marginTop:20,
  },
});

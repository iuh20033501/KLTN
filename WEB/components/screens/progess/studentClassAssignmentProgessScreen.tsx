import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, ActivityIndicator } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import http from '@/utils/http';
import { useFocusEffect } from '@react-navigation/native';

export default function StudentClassAssignmentProgressScreen() {
  const [progress, setProgress] = useState(0);
  const [completedAssignments, setCompletedAssignments] = useState(0);
  const [totalAssignments, setTotalAssignments] = useState(0);
  const [loading, setLoading] = useState(true);

  const fetchProgressData = async () => {
    try {
      // Lấy token
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('Access token not found');
        return;
      }

      // Lấy thông tin học viên
      console.log('Fetching user profile...');
      const profileResponse = await http.get('/auth/profile', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log('User profile:', profileResponse.data);
      const studentId = profileResponse.data.u.idUser;

      // Lấy danh sách tiến trình bài tập
      console.log(`Fetching assignment progress for student ID: ${studentId}`);
      const progressResponse = await http.get(`/baitap/getTienTrinhofHV/${studentId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log('Progress data:', progressResponse.data);
      const completed = progressResponse.data.length; // Tổng số bài tập đã hoàn thành

      // Lấy danh sách lớp học của học viên
      console.log(`Fetching total assignments for student ID: ${studentId}`);
      const classesResponse = await http.get(`/hocvien/getByHV/${studentId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log('Class list:', classesResponse.data);
      const classList = classesResponse.data;

      let total = 0;

      // Tính tổng số bài tập từ tất cả các lớp
      for (const lop of classList) {
        console.log(`Fetching assignments for class ID: ${lop.idLopHoc}`);
        const assignmentsResponse = await http.get(`/baitap/getBaiTapofLop/${lop.idLopHoc}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        console.log(`Assignments for class ${lop.idLopHoc}:`, assignmentsResponse.data);

        total += assignmentsResponse.data.length;
      }

      // Cập nhật state
      setTotalAssignments(total);
      setCompletedAssignments(completed);
      setProgress(total > 0 ? Math.round((completed / total) * 100) : 0);
      console.log('Final progress:', { total, completed, progress: Math.round((completed / total) * 100) });
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

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color="#00405d" />
        <Text>Loading progress data...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Tiến trình bài tập</Text>
      <View style={styles.progressContainer}>
        <Text style={styles.progressText}>{progress}%</Text>
        <Text style={styles.detailText}>
          {completedAssignments}/{totalAssignments} bài tập đã hoàn thành
        </Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  title: {
    fontSize: 14,
    fontWeight: 'bold',
    color: '#00405d',
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
});

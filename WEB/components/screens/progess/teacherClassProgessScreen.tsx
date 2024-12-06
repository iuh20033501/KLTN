import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, ActivityIndicator } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import http from '@/utils/http'; 

export default function TeacherClassProgessScreen() {
  const [progress, setProgress] = useState(0);
  const [completedClasses, setCompletedClasses] = useState(0);
  const [totalClasses, setTotalClasses] = useState(0);
  const [loading, setLoading] = useState(true);

  const fetchProgressData = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        return;
      }
      const profileResponse = await http.get('/auth/profile', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const userId = profileResponse.data.u.idUser;
      const classesResponse = await http.get(`/lopHoc/getByGv/${userId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const classList = classesResponse.data;
      let total = 0;
      let completed = 0;

      for (const lop of classList) {
        const totalResponse = await http.get(`/buoihoc/getbuoiHocByLop/${lop.idLopHoc}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        total += totalResponse.data.length;
        const completedResponse = await http.get(`/buoihoc/getBuoiDaHoc/${lop.idLopHoc}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        completed += completedResponse.data.length;
      }

      setTotalClasses(total);
      setCompletedClasses(completed);
      setProgress(Math.round((completed / total) * 100));
    } catch (error) {
      console.error('Error fetching progress data:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProgressData();
  }, []);

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
      <Text style={styles.title}>Tiến độ buổi dạy</Text>
      <View style={styles.progressContainer}>
        <Text style={styles.progressText}>{progress}%</Text>
        <Text style={styles.detailText}>
          {completedClasses}/{totalClasses} buổi học đã hoàn thành
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

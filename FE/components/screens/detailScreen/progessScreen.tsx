import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity, ActivityIndicator } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import CircularProgress from 'react-native-circular-progress-indicator';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface ProgressInfo {
  tenLopHoc: string;
  chuDe: string;
  tienTrinh: number;
  tongTienTrinh: number;
  idLopHoc: number;
  baiTapDaHoanThanh: number;
  tongBaiTap: number;
  buoiDaHoc: number;
  tongBuoiHoc: number;
}

export default function ProgressScreen({ navigation, route }: { navigation: any; route: any }) {
  const { idUser } = route.params;
  const [progressData, setProgressData] = useState<ProgressInfo[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    fetchProgressData();
  }, []);

  const fetchProgressData = async () => {
    setIsLoading(true);
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('No token found');
        return;
      }

      const classesResponse = await http.get(`hocvien/getByHV/${idUser}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      const classes = classesResponse.data.filter((classItem: any) => classItem.trangThai === 'FULL');

      const progressPromises = classes.map(async (classItem: any) => {
        try {
          const classProgressResponse = await http.get(`buoihoc/getBuoiDaHoc/${classItem.idLopHoc}`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          const classTotalResponse = await http.get(`buoihoc/getbuoiHocByLop/${classItem.idLopHoc}`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          const assignmentCompletedResponse = await http.get(`baitap/getTienTrinh/${idUser}/${classItem.idLopHoc}`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          const assignmentTotalResponse = await http.get(`baitap/getBaiTapofLop/${classItem.idLopHoc}`, {
            headers: { Authorization: `Bearer ${token}` },
          });

          return {
            tenLopHoc: classItem.tenLopHoc,
            chuDe: classProgressResponse.data[0]?.chuDe || 'Không rõ',
            tienTrinh: (classProgressResponse.data.length / classTotalResponse.data.length) * 100 || 0,
            tongTienTrinh: 100,
            idLopHoc: classItem.idLopHoc,
            baiTapDaHoanThanh: assignmentCompletedResponse.data.length || 0,
            tongBaiTap: assignmentTotalResponse.data.length || 0,
            buoiDaHoc: classProgressResponse.data.length || 0,
            tongBuoiHoc: classTotalResponse.data.length || 0,
          };
        } catch (error) {
          console.warn(`Lỗi khi lấy dữ liệu cho lớp ${classItem.tenLopHoc}:`, error);
          return {
            tenLopHoc: classItem.tenLopHoc,
            chuDe: 'Không rõ',
            tienTrinh: 0,
            tongTienTrinh: 100,
            idLopHoc: classItem.idLopHoc,
            baiTapDaHoanThanh: 0,
            tongBaiTap: 0,
            buoiDaHoc: 0,
            tongBuoiHoc: 0,
          };
        }
      });

      const progressData = await Promise.all(progressPromises);
      setProgressData(progressData);
    } catch (error) {
      console.error('Failed to fetch progress data:', error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Icon name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
        <Text style={styles.title}>Tiến Trình</Text>
      </View>

      {isLoading ? (
        <ActivityIndicator size="large" color="#00405d" />
      ) : (
        progressData.map((progress) => (
          <View key={progress.idLopHoc} style={styles.progressItem}>
              <Text style={styles.titleClass}>{progress.tenLopHoc}</Text>
            <View style={styles.progressCircleContainer}>
              <CircularProgress
                value={progress.tienTrinh} 
                radius={50}
                duration={1000}
                progressValueColor={'#000'}
                maxValue={progress.tongTienTrinh}
                title={`${progress.buoiDaHoc}/${progress.tongBuoiHoc}`}
                titleColor={'#000'}
                titleStyle={{ fontWeight: 'bold' }}
                activeStrokeColor={'#99FF00'}
                activeStrokeSecondaryColor={'#00A762'}
              />
              <View style={styles.progressInfoContainer}>
                <Text style={styles.sessionLabel}>Buổi học đã học</Text>
              </View>
            </View>

            <View style={styles.progressCircleContainer}>
              <CircularProgress
                value={(progress.baiTapDaHoanThanh / progress.tongBaiTap) * 100 || 0}
                radius={50}
                duration={1000}
                progressValueColor={'#000'}
                maxValue={100}
                title={`${progress.baiTapDaHoanThanh}/${progress.tongBaiTap}`}
                titleColor={'#000'}
                titleStyle={{ fontWeight: 'bold' }}
                activeStrokeColor={'#FF5733'}
                activeStrokeSecondaryColor={'#C70039'}
              />
              <View style={styles.progressInfoContainer}>
                <Text style={styles.sessionLabel}>Bài tập đã làm</Text>
              </View>
            </View>
          </View>
        ))
      )}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    padding: 16,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 20,
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    marginLeft: 10,
  },
  titleClass: {
    fontSize: 16,
    fontWeight: 'bold',
    marginLeft: 10,
    marginBottom:10,
  },
  progressItem: {
    marginBottom: 20,
    borderRadius: 10,
    backgroundColor: '#F8F8F8',
    padding: 10,
  },
  progressCircleContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 10,
  },
  progressInfoContainer: {
    marginLeft: 20,
    flex: 1,
  },
  sessionLabel: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#555',
  },
});

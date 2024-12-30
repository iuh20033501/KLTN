import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, FlatList, TouchableOpacity } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import http from '@/utils/http';

export default function LeaderboardScreen({ navigation, route }: { navigation: any; route: any }) {
  const { idUser } = route.params;
  const [classes, setClasses] = useState<any[]>([]);
  const [currentClassIndex, setCurrentClassIndex] = useState(0);
  const [leaderboard, setLeaderboard] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  const getClassList = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      const response = await http.get(`/hocvien/getByHV/${idUser}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (response.status === 200 && response.data?.length > 0) {
        setClasses(response.data);
        return response.data;
      } else {
        console.warn('Không có lớp học nào.');
        setClasses([]);
      }
    } catch (error) {
      console.error('Lỗi khi gọi API lấy danh sách lớp học:', error);
    }
    return [];
  };

 const getLeaderboardData = async (classId: number) => {
  try {
    const token = await AsyncStorage.getItem('accessToken');
    const response = await http.get(`lopHoc/getByLop/${classId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (response.status === 200 && response.data) {
      const students = response.data;

      const studentScores = await Promise.all(
        students.map(async (student: any) => {
          try {
            const progressResponse = await http.get(
              `baitap/getTienTrinh/${student.idUser}/${classId}`,
              { headers: { Authorization: `Bearer ${token}` } }
            );

            if (progressResponse.status === 200 && progressResponse.data) {
              const progressData = progressResponse.data;
              const totalCorrectAnswers = progressData.reduce(
                (total: number, item: any) => total + item.cauDung,
                0
              );
              const totalScore = totalCorrectAnswers * 10;

              return { name: student.hoTen, score: totalScore };
            }
          } catch (error) {
          }
          return { name: student.hoTen, score: 0 };
        })
      );

      return studentScores;
    }
  } catch (error) {
    console.error('Lỗi khi lấy danh sách học viên:', error);
  }
  return [];
};

  const fetchLeaderboard = async (classIndex: number) => {
    if (classes[classIndex]) {
      setLoading(true);
      const leaderboardData = await getLeaderboardData(classes[classIndex].idLopHoc);
      setLeaderboard(leaderboardData.sort((a, b) => b.score - a.score)); 
      setLoading(false);
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      const classList = await getClassList();
      if (classList.length > 0) {
        await fetchLeaderboard(0);
      } else {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const handlePrevClass = () => {
    if (currentClassIndex > 0) {
      const newIndex = currentClassIndex - 1;
      setCurrentClassIndex(newIndex);
      fetchLeaderboard(newIndex);
    }
  };

  const handleNextClass = () => {
    if (currentClassIndex < classes.length - 1) {
      const newIndex = currentClassIndex + 1;
      setCurrentClassIndex(newIndex);
      fetchLeaderboard(newIndex);
    }
  };

  const renderItem = ({ item, index }: { item: any; index: number }) => (
    <View style={[styles.itemContainer, index < 3 && styles.topThree]}>
      <Text style={styles.rankText}>{index + 1}</Text>
      <Text style={styles.nameText}>{item.name}</Text>
      <Text style={styles.pointsText}>{item.score} điểm</Text>
    </View>
  );

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Icon name="arrow-back-outline" size={24} color="#333" />
        </TouchableOpacity>
        <Text style={styles.title}>Bảng Xếp Hạng</Text>
      </View>

      {classes.length > 0 ? (
        <>
          <View style={styles.classNavigation}>
            <TouchableOpacity onPress={handlePrevClass} disabled={currentClassIndex === 0}>
              <Text style={[styles.navButton, currentClassIndex === 0 && styles.disabledButton]}>{'<'}</Text>
            </TouchableOpacity>
            <Text style={styles.classTitle}>{classes[currentClassIndex]?.tenLopHoc || 'Lớp học'}</Text>
            <TouchableOpacity onPress={handleNextClass} disabled={currentClassIndex === classes.length - 1}>
              <Text style={[styles.navButton, currentClassIndex === classes.length - 1 && styles.disabledButton]}>{'>'}</Text>
            </TouchableOpacity>
          </View>

          {loading ? (
            <Text style={styles.loadingText}>Đang tải dữ liệu...</Text>
          ) : leaderboard.length > 0 ? (
            <FlatList
              data={leaderboard}
              keyExtractor={(item, index) => index.toString()}
              renderItem={renderItem}
              contentContainerStyle={styles.list}
            />
          ) : (
            <Text style={styles.errorText}>Không có dữ liệu bảng xếp hạng.</Text>
          )}
        </>
      ) : (
        <Text style={styles.errorText}>Không có lớp học nào để hiển thị.</Text>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#f9f9f9', padding: 16 },
  header: { flexDirection: 'row', alignItems: 'center', marginBottom: 16 },
  title: { fontSize: 20, fontWeight: 'bold', marginLeft: 10 },
  classNavigation: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 },
  classTitle: { fontSize: 18, fontWeight: 'bold', color: '#333' },
  navButton: {  fontSize:40 , fontWeight: 'bold', color: '#00bf63' },
  disabledButton: { color: '#ccc'},
  loadingText: { fontSize: 18, textAlign: 'center', marginTop: 20, color: '#00bf63' },
  errorText: { fontSize: 18, textAlign: 'center', marginTop: 20, color: 'red' },
  list: { paddingBottom: 16 },
  itemContainer: { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', backgroundColor: '#fff', padding: 16, borderRadius: 8, marginBottom: 8, shadowColor: '#000', shadowOffset: { width: 0, height: 2 }, shadowOpacity: 0.1, shadowRadius: 3, elevation: 2 },
  topThree: { backgroundColor: '#ffd700' },
  rankText: { fontSize: 18, fontWeight: 'bold', color: '#333' },
  nameText: { fontSize: 16, color: '#555', flex: 1, marginLeft: 8 },
  pointsText: { fontSize: 16, fontWeight: 'bold', color: '#00bf63' },
});

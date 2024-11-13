import React, { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ActivityIndicator, FlatList } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import Icon from 'react-native-vector-icons/Ionicons';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface ExerciseInfo {
  idBaiTap: number;
  tenBaiTap: string;
  moTa: string;
  ngayBD: string;
  ngayKT: string;
}

export default function ExerciseListScreen({ navigation, route }: { navigation: any; route: any }) {
  const [exercises, setExercises] = useState<ExerciseInfo[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const { idBuoiHoc,idUser } = route.params;

  const fetchExercises = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('No token found');
        return;
      }

      const response = await http.get(`baitap/getBaiTapofBuoiTrue/${idBuoiHoc}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      const currentDate = new Date();
      const filteredExercises = response.data.filter((exercise: ExerciseInfo) => {
        const ngayBD = new Date(exercise.ngayBD);
        const ngayKT = new Date(exercise.ngayKT);

        // Kiểm tra nếu ngày hiện tại nằm trong khoảng ngày bắt đầu và ngày kết thúc
        return currentDate >= ngayBD && currentDate <= ngayKT;
      });

      setExercises(filteredExercises);
    } catch (error) {
      console.error('Failed to fetch exercises:', error);
    } finally {
      setIsLoading(false);
    }
  };
  
  useEffect(() => {
    fetchExercises();
  }, []);

  const renderExerciseCard = ({ item }: { item: ExerciseInfo }) => (
    <TouchableOpacity onPress={() => navigation.navigate('ExerciseScreen', { idBaiTap: item.idBaiTap, tenBaiTap: item.tenBaiTap,idUser })}>
      <LinearGradient colors={['#4c669f', '#3b5998', '#192f6a']} style={styles.buttonGradient}>
        <Text style={styles.exerciseText}>{item.tenBaiTap}</Text>
      </LinearGradient>
    </TouchableOpacity>
  );

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Icon name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
        <Text style={styles.headerText}>Danh sách bài tập</Text>
      </View>

      {isLoading ? (
        <ActivityIndicator size="large" color="#00405d" />
      ) : exercises.length > 0 ? (
        <FlatList
          data={exercises}
          renderItem={renderExerciseCard}
          keyExtractor={(item) => item.idBaiTap.toString()}
          contentContainerStyle={styles.exerciseList}
        />
      ) : (
        <View style={styles.noExerciseContainer}>
          <Text style={styles.noExerciseText}>Không có bài tập nào</Text>
        </View>
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
    marginBottom: 20,
  },
  headerText: {
    fontSize: 22,
    fontWeight: 'bold',
    color: '#00bf63',
    textAlign: 'center',
    flex: 1,
    marginRight: 24, 
  },
  exerciseList: {
    paddingBottom: 20,
  },
  buttonGradient: {
    borderRadius: 8,
    paddingVertical: 20,
    paddingHorizontal: 15,
    marginBottom: 10,
    alignItems: 'center',
  },
  exerciseText: {
    fontSize: 18,
    color: '#ffffff',
    fontWeight: 'bold',
    textAlign: 'center',
  },
  noExerciseContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  noExerciseText: {
    fontSize: 18,
    color: '#333',
    fontWeight: 'bold',
  },
});

import React, { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ActivityIndicator, FlatList } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import { LinearGradient } from 'expo-linear-gradient';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface LessonInfo {
  chuDe: string;
  idBuoiHoc: number;
  tenBuoiHoc: string;
  ngayHoc: string;
  thoiGianBatDau: string;
  thoiGianKetThuc: string;
}

export default function LessonListScreen({ navigation, route }: { navigation: any; route: any }) {
  const [lessons, setLessons] = useState<LessonInfo[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const { idLopHoc,idUser } = route.params;


  const fetchLessons = async () => {
      try {
          const token = await AsyncStorage.getItem('accessToken');
          if (!token) {
              console.error('No token found');
              return;
          }
          const response = await http.get(`buoihoc/getbuoiHocByLop/${idLopHoc}`, {
              headers: {
                  Authorization: `Bearer ${token}`,
              },
          });
          setLessons(response.data);
      } catch (error) {
          console.error('Failed to fetch lessons:', error);
      } finally {
          setIsLoading(false);
      }
  };
  
  useEffect(() => {
      fetchLessons();
  }, []);
  const renderLessonCard = ({ item }: { item: LessonInfo }) => (
    <TouchableOpacity style={styles.lessonButton} onPress={() => navigation.navigate('ExerciseListScreen', { idBuoiHoc: item.idBuoiHoc,idUser })}>
      <LinearGradient colors={['#4c669f', '#3b5998', '#192f6a']} style={styles.buttonGradient}>
        <Text style={styles.lessonText}>{item.chuDe}</Text>
       
      </LinearGradient>
    </TouchableOpacity>
  );

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Icon name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
        <Text style={styles.headerText}>Bài tập </Text>
        <Icon name="star-outline" size={24} color="gold" style={styles.starIcon} />
      </View>

      {isLoading ? (
        <ActivityIndicator size="large" color="#00405d" />
      ) : (
        <FlatList
          data={lessons}
          renderItem={renderLessonCard}
          keyExtractor={(item) => item.idBuoiHoc.toString()}
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
  lessonButton: {
    marginBottom: 16,
    borderRadius: 10,
    overflow: 'hidden',
  },
  buttonGradient: {
    paddingVertical: 20,
    borderRadius: 8,
    justifyContent: 'center',
    paddingHorizontal: 15,
  },
  lessonText: {
    fontSize: 20,
    color: '#ffffff',
    textAlign: 'center',
    fontWeight: 'bold',
    marginBottom: 5,
  },
  lessonDetails: {
    fontSize: 16,
    color: '#ffffff',
    textAlign: 'center',
  },
  starIcon: {
    marginRight: 10,
  },
});

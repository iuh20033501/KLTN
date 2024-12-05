import React, { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ActivityIndicator, FlatList, Alert } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import Icon from 'react-native-vector-icons/Ionicons';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Linking } from 'react-native';
import { useFocusEffect } from '@react-navigation/native';

interface ExerciseInfo {
  idBaiTap: number;
  tenBaiTap: string;
  moTa: string;
  ngayBD: string;
  ngayKT: string;
  cauDaLam?: number;
  totalQuestions?: number;
  diem?: number;
}

interface DocumentInfo {
  idTaiLieu: number;
  tenTaiLieu: string;
  linkLoad: string;
}

export default function ExerciseListScreen({ navigation, route }: { navigation: any; route: any }) {
  const [activeTab, setActiveTab] = useState<'Exercises' | 'Documents'>('Exercises');
  const [exercises, setExercises] = useState<ExerciseInfo[]>([]);
  const [documents, setDocuments] = useState<DocumentInfo[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const { idBuoiHoc, idUser } = route.params;

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
      const filteredExercises = await Promise.all(
        response.data.map(async (exercise: ExerciseInfo) => {
          const ngayBD = new Date(exercise.ngayBD);
          const ngayKT = new Date(exercise.ngayKT);

          if (currentDate >= ngayBD && currentDate <= ngayKT) {
            const questionsResponse = await http.get(`baitap/getCauHoiTrue/${exercise.idBaiTap}`, {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            });
            const progressResponse = await http.get(`baitap/getTienTrinhHVBT/${idUser}/${exercise.idBaiTap}`, {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            });

            return {
              ...exercise,
              cauDaLam: progressResponse.data.cauDaLam || 0,
              totalQuestions: questionsResponse.data.length,
              diem: progressResponse.data.cauDung * 10 || 0,
            };
          }
          return null;
        })
      );

      setExercises(filteredExercises.filter(Boolean));
    } catch (error) {
      console.error('Failed to fetch exercises:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const fetchDocuments = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('No token found');
        return;
      }

      const response = await http.get(`taiLieu/getTaiLieuByBuoi/${idBuoiHoc}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      const filteredDocuments = response.data.filter((document: any) => document.trangThai === true);
      setDocuments(filteredDocuments);
    } catch (error) {
      console.error('Failed to fetch documents:', error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (activeTab === 'Exercises') {
      setIsLoading(true);
      fetchExercises();
    } else {
      setIsLoading(true);
      fetchDocuments();
    }
  }, [activeTab]);

  useFocusEffect(
    React.useCallback(() => {
      fetchExercises(); 
    }, [])
  );
  const renderExerciseCard = ({ item }: { item: ExerciseInfo }) => (
    <TouchableOpacity
      onPress={() =>
        navigation.navigate('ExerciseScreen', { 
          idBaiTap: item.idBaiTap, 
          tenBaiTap: item.tenBaiTap, 
          idUser,
        })
      }
    >
      <LinearGradient colors={['#4c669f', '#3b5998', '#192f6a']} style={styles.buttonGradient}>
        <Text style={styles.exerciseText}>{item.tenBaiTap}</Text>
        <Text style={styles.progressText}>
          {item.cauDaLam}/{item.totalQuestions} câu hỏi
        </Text>
        <Text style={styles.scoreText}>Điểm: {item.diem  || 0}</Text>
      </LinearGradient>
    </TouchableOpacity>
  );

  const renderDocumentCard = ({ item }: { item: DocumentInfo }) => (
    <TouchableOpacity onPress={() => handleOpenDocument(item.linkLoad)}>
      <View style={styles.documentCard}>
        <Text style={styles.documentTitle}>{item.tenTaiLieu}</Text>
      </View>
    </TouchableOpacity>
  );

  const handleOpenDocument = async (linkLoad: string) => {
    try {
      const supported = await Linking.canOpenURL(linkLoad);

      if (supported) {
        await Linking.openURL(linkLoad);
      } else {
        Alert.alert('Không thể mở tài liệu này:', linkLoad);
      }
    } catch (error) {
      console.error('Error opening document:', error);
      Alert.alert('Lỗi khi mở tài liệu');
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Icon name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
        <Text style={styles.headerText}>Danh sách</Text>
      </View>

      <View style={styles.tabContainer}>
        <TouchableOpacity
          style={[styles.tabButton, activeTab === 'Exercises' && styles.activeTab]}
          onPress={() => setActiveTab('Exercises')}
        >
          <Text style={[styles.tabText, activeTab === 'Exercises' && styles.activeTabText]}>Bài Tập</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[styles.tabButton, activeTab === 'Documents' && styles.activeTab]}
          onPress={() => setActiveTab('Documents')}
        >
          <Text style={[styles.tabText, activeTab === 'Documents' && styles.activeTabText]}>Tài Liệu</Text>
        </TouchableOpacity>
      </View>

      {isLoading ? (
        <ActivityIndicator size="large" color="#00405d" />
      ) : activeTab === 'Exercises' ? (
        <FlatList
          data={exercises}
          renderItem={renderExerciseCard}
          keyExtractor={(item) => item.idBaiTap.toString()}
          contentContainerStyle={styles.exerciseList}
        />
      ) : (
        <FlatList
          data={documents}
          renderItem={renderDocumentCard}
          keyExtractor={(item) => item.idTaiLieu.toString()}
          contentContainerStyle={styles.exerciseList}
        />
      )}
    </View>
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
  headerText: {
    fontSize: 22,
    fontWeight: 'bold',
    color: '#00bf63',
    textAlign: 'center',
    flex: 1,
    marginRight: 24,
  },
  tabContainer: {
    flexDirection: 'row',
    marginBottom: 16,
  },
  tabButton: {
    flex: 1,
    padding: 10,
    backgroundColor: '#f5f5f5',
    alignItems: 'center',
    borderRadius: 8,
    marginHorizontal: 5,
  },
  activeTab: {
    backgroundColor: '#00bf63',
  },
  tabText: {
    fontSize: 16,
    color: '#333',
  },
  activeTabText: {
    color: '#fff',
    fontWeight: 'bold',
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
  progressText: {
    fontSize: 14,
    color: '#ffffff',
    textAlign: 'center',
    marginTop: 5,
  },
  scoreText: {
    fontSize: 14,
    color: '#ffff00',
    textAlign: 'center',
    marginTop: 5,
  },
  documentCard: {
    padding: 16,
    marginBottom: 10,
    backgroundColor: '#f9f9f9',
    borderRadius: 8,
  },
  documentTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 8,
  },
});

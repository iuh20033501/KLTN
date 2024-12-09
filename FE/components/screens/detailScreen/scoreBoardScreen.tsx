  import React, { useState, useEffect } from 'react';
  import { View, Text, StyleSheet, ScrollView, TouchableOpacity, ActivityIndicator } from 'react-native';
  import Icon from 'react-native-vector-icons/Ionicons';
  import CircularProgress from 'react-native-circular-progress-indicator';
  import http from '@/utils/http';
  import AsyncStorage from '@react-native-async-storage/async-storage';

  interface Class {
    idLopHoc: number;
    tenLopHoc: string;
  }

  interface Assignment {
    tenLopHoc: string | undefined;
    chuDe: string | undefined;
    totalQ: number | undefined;
    idBaiTap: number;
    tenBaiTap: string;
    diem: number;
  }

  interface Test {
    tenLopHoc: string | undefined;
    idTest: number;
    loaiTest: string;
    diemTest: number;
  }

  export default function ScoreBoardScreen({ navigation, route }: { navigation: any; route: any }) {
    const { idUser } = route.params;
    const [activeTab, setActiveTab] = useState('ExerciseScores');
    const [classes, setClasses] = useState<Class[]>([]);
    const [assignments, setAssignments] = useState<Assignment[]>([]);
    const [tests, setTests] = useState<Test[]>([]);
    const [isLoading, setIsLoading] = useState(false);

    const [totalQ, setTotalQ] = useState(0);

    useEffect(() => {
      fetchClasses();
    }, []);

    useEffect(() => {
      if (activeTab === 'ExerciseScores' && classes.length > 0) {
        fetchAssignments(classes[0].idLopHoc); 
      } else if (activeTab === 'ExamScores' && classes.length > 0) {
        fetchTests(classes[0].idLopHoc); 
      }
    }, [activeTab, classes]);

    const fetchClasses = async () => {
      setIsLoading(true);
      try {
        const token = await AsyncStorage.getItem('accessToken');
        if (!token) {
          console.error('No token found');
          return;
        }

        const response = await http.get(`/hocvien/getByHV/${idUser}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        setClasses(response.data);
      } catch (error) {
        console.error('Failed to fetch classes:', error);
      } finally {
        setIsLoading(false);
      }
    };
    const fetchAssignments = async (idLopHoc: number) => {
      setIsLoading(true);
      try {
          const token = await AsyncStorage.getItem('accessToken');
          if (!token) {
              console.error('No token found');
              return;
          }
  
          const response = await http.get(`baitap/getBaiTapofBuoiTrue/${idLopHoc}`, {
              headers: { Authorization: `Bearer ${token}` },
          });
  
          const assignmentData = await Promise.all(
              response.data.map(async (assignment: any) => {
                  const totalQuestionsResponse = await http.get(`/baitap/getCauHoiTrue/${assignment.idBaiTap}`, {
                      headers: { Authorization: `Bearer ${token}` },
                  });
  
                  const totalQuestions = totalQuestionsResponse.data.length;
                  const totalQ = totalQuestions * 10;
  
                  const scoreResponse = await http.get(`/baitap/getTienTrinhHVBT/${idUser}/${assignment.idBaiTap}`, {
                      headers: { Authorization: `Bearer ${token}` },
                  });
  
                  const correctAnswers = scoreResponse.data?.cauDung || 0;
                  const diem = correctAnswers * 10;
  
                  return {
                      idBaiTap: assignment.idBaiTap,
                      tenBaiTap: assignment.tenBaiTap,
                      diem,
                      totalQ,
                      chuDe: assignment.buoiHoc?.chuDe || 'Không rõ',
                      tenLopHoc:assignment.buoiHoc?.lopHoc?.tenLopHoc,
                  };
              })
          );
  
          setAssignments(assignmentData);
      } catch (error) {
          console.error('Failed to fetch assignments:', error);
      } finally {
          setIsLoading(false);
      }
  };
  
    

  const fetchTests = async (idLopHoc: number) => {
    setIsLoading(true);
    try {
        const token = await AsyncStorage.getItem('accessToken');
        if (!token) {
            console.error('No token found');
            return;
        }
        const response = await http.get(`baitest/getBaiTestofLopTrue/${idLopHoc}`, {
            headers: { Authorization: `Bearer ${token}` },
        });
        console.log(response.data)
        const testData = await Promise.all(
            response.data.map(async (test: any) => {
                const scoreResponse = await http.get(`/baitest/getKetQua/${test.idTest}/${idUser}`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                return {
                    idTest: test.idTest,
                    loaiTest: test.loaiTest,
                    diemTest: scoreResponse.data?.diemTest || 0, 
                    tenLopHoc: test.lopHoc?.tenLopHoc || "Không xác định", 
                };
            })
        );

        setTests(testData);
    } catch (error) {
        console.error('Failed to fetch tests:', error);
    } finally {
        setIsLoading(false);
    }
};


    const renderAssignments = () => (
      <ScrollView style={styles.scoresContainer}>
          {isLoading ? (
              <ActivityIndicator size="large" color="#00405d" />
          ) : (
              assignments.map((assignment) => (
                  <View key={assignment.idBaiTap} style={styles.scoreItem}>
                      <View style={styles.scoreDetail}>
                          <Text style={styles.scoreLabel}>{assignment.tenBaiTap}</Text>
                          <CircularProgress
                              value={assignment.diem}
                              radius={50}
                              duration={1000}
                              progressValueColor={'#000'}
                              maxValue={assignment.totalQ}
                              title={`/${assignment.totalQ}`}
                              titleColor={'#000'}
                              titleStyle={{ fontWeight: 'bold' }}
                              activeStrokeColor={'#99FF00'}
                              activeStrokeSecondaryColor={'#00A762'}
                          />
                      </View>
                      <View style={styles.sessionContainer}>
                          <Text style={styles.sessionLabel}>{assignment.chuDe}</Text>
                          <Text style={styles.sessionLabel}>{assignment.tenLopHoc}</Text>

                      </View>
                  </View>
              ))
          )}
      </ScrollView>
  );
  const renderTests = () => (
    <ScrollView style={styles.scoresContainer}>
        {isLoading ? (
            <ActivityIndicator size="large" color="#00405d" />
        ) : (
            tests.map((test) => (
                <View key={test.idTest} style={styles.scoreItem}>
                    <View style={styles.scoreDetail}>
                        <Text style={styles.scoreLabel}>
                            {test.loaiTest === 'CK' ? 'Cuối Kỳ' : 'Giữa Kỳ'}
                        </Text>
                        <CircularProgress
                            value={test.diemTest}
                            radius={50}
                            duration={1000}
                            progressValueColor={'#000'}
                            maxValue={10}
                            title={'/10'}
                            titleColor={'#000'}
                            titleStyle={{ fontWeight: 'bold' }}
                            activeStrokeColor={'#99FF00'}
                            activeStrokeSecondaryColor={'#00A762'}
                        />
                    </View>
                    <View style={styles.sessionContainer}>
                        <Text style={styles.sessionLabel}>{test.tenLopHoc}</Text>
                    </View>
                </View>
            ))
        )}
    </ScrollView>
);

    return (
      <ScrollView style={styles.container}>
        <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
        <Icon name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
          <Text style={styles.title}>Bảng điểm</Text>
        </View>

        <View style={styles.tabContainer}>
          <TouchableOpacity
            style={[styles.tabButton, activeTab === 'ExerciseScores' && styles.activeTab]}
            onPress={() => setActiveTab('ExerciseScores')}
          >
            <Text style={[styles.tabText, activeTab === 'ExerciseScores' && styles.activeTabText]}>Điểm bài tập</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.tabButton, activeTab === 'ExamScores' && styles.activeTab]}
            onPress={() => setActiveTab('ExamScores')}
          >
            <Text style={[styles.tabText, activeTab === 'ExamScores' && styles.activeTabText]}>Điểm bài test</Text>
          </TouchableOpacity>
        </View>

        {activeTab === 'ExerciseScores' ? renderAssignments() : renderTests()}
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
    tabContainer: {
      flexDirection: 'row',
      marginBottom: 20,
    },
    tabButton: {
      flex: 1,
      paddingVertical: 10,
      alignItems: 'center',
      borderBottomWidth: 2,
      borderBottomColor: 'transparent',
    },
    activeTab: {
      borderBottomColor: '#28a745',
    },
    tabText: {
      fontSize: 16,
      color: '#666',
    },
    activeTabText: {
      color: '#28a745',
      fontWeight: 'bold',
    },
    scoresContainer: {
      marginTop: 20,
    },
    scoreItem: {
      padding: 16,
      backgroundColor: '#F8F8F8',
      borderRadius: 10,
      marginBottom: 10,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    scoreDetail: {
      flexDirection: 'column',
    },
    scoreLabel: {
      fontSize: 16,
      fontWeight: 'bold',
      marginBottom: 10,
    },
    sessionContainer: {
      justifyContent: 'center', // Căn giữa theo chiều dọc
      alignItems: 'flex-end', // Căn phải
      flex: 1, // Để đảm bảo khoảng cách hợp lý
  },
  sessionLabel: {
      fontSize: 14,
      fontWeight: 'bold',
      color: '#555',
  },
  });

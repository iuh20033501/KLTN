import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ActivityIndicator, Image, ScrollView, Modal } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import http from '@/utils/http';
import AudioPlayer from '../audio/audioPlayer';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface QuestionInfo {
  idCauHoi: number;
  noiDung: string;
  linkAnh: string | null;
  linkAmThanh: string | null;
}

interface AnswerInfo {
  idCauTraLoi: number;
  noiDung: string;
  ketQua: boolean;
}

export default function ExerciseScreen({ navigation, route }: { navigation: any; route: any }) {
  const [questions, setQuestions] = useState<QuestionInfo[]>([]);
  const { idTest, loaiTest, idUser, thoiGianLamBai } = route.params;

  const [answers, setAnswers] = useState<AnswerInfo[]>([]);
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [selectedAnswers, setSelectedAnswers] = useState<{ [key: number]: AnswerInfo | null }>({});
  const [isLoading, setIsLoading] = useState(true);
  const [reviewModalVisible, setReviewModalVisible] = useState(false);
  const [viewResults, setViewResults] = useState(false);
  const [finalScore, setFinalScore] = useState<number | null>(null);
  const [timeLeft, setTimeLeft] = useState(() => thoiGianLamBai * 60);
  const [startTime, setStartTime] = useState<number | null>(null);

  const fetchQuestions = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      const response = await http.get(`baitest/getCauHoiTrue/${idTest}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setQuestions(response.data);
    } catch (error) {
      console.error('Failed to fetch questions:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const fetchAnswers = async (idCauHoi: number) => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      const response = await http.get(`baitest/getCauTraLoi/${idCauHoi}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setAnswers(response.data);
    } catch (error) {
      console.error('Failed to fetch answers:', error);
    }
  };

  useEffect(() => {
    fetchQuestions();
    setStartTime(Date.now()); // Lưu thời gian hiện tại khi mở bài thi

  }, []);

  const calculateElapsedTime = (): string => {
    if (!startTime) return '00:00';
    const elapsedTime = Math.floor((Date.now() - startTime) / 1000); // Thời gian đã qua tính bằng giây
    const minutes = Math.floor(elapsedTime / 60);
    const seconds = elapsedTime % 60;
    return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
  };

  useEffect(() => {
    if (timeLeft > 0) {
      const timer = setInterval(() => {
        setTimeLeft((prevTime) => Math.max(prevTime - 1, 0));
      }, 1000);
      return () => clearInterval(timer); 
    }
    if (timeLeft === 0 && !viewResults && !reviewModalVisible) {
      submitAnswers();
    }
  }, [timeLeft, viewResults, reviewModalVisible]);

  useEffect(() => {
    if (questions[currentQuestion]) {
      fetchAnswers(questions[currentQuestion].idCauHoi);
    }
  }, [currentQuestion, questions]);

  const handleOptionPress = (option: AnswerInfo) => {
    setSelectedAnswers((prev) => ({
      ...prev,
      [questions[currentQuestion].idCauHoi]: option,
    }));
  };

  const handleNext = () => {
    if (currentQuestion < questions.length - 1) {
      setCurrentQuestion((prev) => prev + 1);
    }
  };

  const handlePrevious = () => {
    if (currentQuestion > 0) {
      setCurrentQuestion((prev) => prev - 1);
    }
  };

  const handleSubmit = () => {
    setReviewModalVisible(true);
  };

  const submitAnswers = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) return;
  
      const totalQuestions = questions.length;
      const correctAnswers = questions.reduce((count, question) => {
        const selectedAnswer = selectedAnswers[question.idCauHoi];
        return selectedAnswer?.ketQua ? count + 1 : count;
      }, 0);
      const diemTest = parseFloat(((correctAnswers / totalQuestions) * 10).toFixed(2));
      setFinalScore(diemTest);
  
      const elapsedTime = calculateElapsedTime(); 
      const elapsedTimeString = elapsedTime.toString(); 
      const response = await http.post(
        `baitest/lamBai/createKetQua/${idTest}/${idUser}`,
        { diemTest,thoiGianHoanThanh: elapsedTimeString },
        { headers: { Authorization: `Bearer ${token}` } }
      );
  
      if (response.status === 200) {
        setViewResults(true);
        setTimeout(() => {
          navigation.goBack();
        }, 2000);
      }
    } catch (error) {
      console.error('Lỗi khi nộp bài:', error);
    }
  };

  useEffect(() => {
    if (timeLeft <= 0 && !viewResults) {
      submitAnswers();
    }
  }, [timeLeft, viewResults]);

  const formatTime = (seconds: number): string => {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  const getTestType = (loaiTest: string): string => {
    if (loaiTest === 'CK') return 'Cuối Kì';
    if (loaiTest === 'GK') return 'Giữa Kì';
    return loaiTest;
  };

  if (isLoading) {
    return <ActivityIndicator size="large" color="#00405d" />;
  }

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Icon name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
        <Text style={styles.headerText}>{getTestType(loaiTest)}</Text>
      </View>

      <View style={styles.topBar}>
        <Text style={styles.progressText}>
          Câu hỏi {currentQuestion + 1}/{questions.length}
        </Text>
        <Text style={[styles.timerText, { color: timeLeft <= 60 ? 'red' : 'black' }]}>
          Thời gian còn lại: {formatTime(timeLeft)}
        </Text>
      </View>

      <ScrollView showsVerticalScrollIndicator={false}>
        <View style={styles.questionContainer}>
          <Text style={styles.questionText}>{questions[currentQuestion]?.noiDung}</Text>

          {questions[currentQuestion]?.linkAnh && (
            <Image source={{ uri: questions[currentQuestion].linkAnh }} style={styles.questionImage} />
          )}

          {questions[currentQuestion]?.linkAmThanh && (
            <AudioPlayer audioUri={questions[currentQuestion].linkAmThanh} />
          )}
        </View>

        {answers.map((option) => (
          <TouchableOpacity
            key={option.idCauTraLoi}
            style={[
              styles.optionButton,
              selectedAnswers[questions[currentQuestion].idCauHoi]?.idCauTraLoi === option.idCauTraLoi
                ? styles.selectedOption
                : styles.defaultOption,
            ]}
            onPress={() => handleOptionPress(option)}
          >
            <Text style={styles.optionText}>{option.noiDung}</Text>
          </TouchableOpacity>
        ))}

        <View style={styles.navigationButtons}>
          <TouchableOpacity
            onPress={handlePrevious}
            style={[styles.navButton, { opacity: currentQuestion === 0 ? 0.5 : 1 }]}
            disabled={currentQuestion === 0}
          >
            <Text style={styles.navButtonText}>Câu trước</Text>
          </TouchableOpacity>
          <TouchableOpacity
            onPress={handleNext}
            style={[styles.navButton, { opacity: currentQuestion === questions.length - 1 ? 0.5 : 1 }]}
            disabled={currentQuestion === questions.length - 1}
          >
            <Text style={styles.navButtonText}>Câu tiếp theo</Text>
          </TouchableOpacity>
        </View>

        <TouchableOpacity onPress={handleSubmit} style={styles.submitButton}>
          <Text style={styles.submitButtonText}>Nộp bài</Text>
        </TouchableOpacity>
      </ScrollView>

      {/* Modal xem lại bài */}
      <Modal visible={reviewModalVisible} transparent={true} animationType="slide">
        <View style={styles.modalContainer}>
          <ScrollView contentContainerStyle={styles.modalContent}>
            <Text style={[styles.headerText, { textAlign: 'center', marginBottom: 10 }]}>Xem lại bài trước khi nộp:</Text>
            {questions.map((question, index) => (
              <View key={question.idCauHoi} style={styles.reviewContainer}>
                <Text style={styles.questionText}>
                  {index + 1}. {question.noiDung}
                </Text>
                <Text style={styles.answerText}>
                  Đáp án đã chọn: {selectedAnswers[question.idCauHoi]?.noiDung || 'Chưa chọn'}
                </Text>
              </View>
            ))}

            <View style={styles.modalButtons}>
              <TouchableOpacity
                onPress={() => setReviewModalVisible(false)}
                style={[styles.modalButton, { backgroundColor: '#f44336' }]}
              >
                <Text style={styles.modalButtonText}>Quay lại</Text>
              </TouchableOpacity>
              <TouchableOpacity
                onPress={() => {
                  setReviewModalVisible(false);
                  submitAnswers();
                }}
                style={[styles.modalButton, { backgroundColor: '#4caf50' }]}
              >
                <Text style={styles.modalButtonText}>Nộp bài</Text>
              </TouchableOpacity>
            </View>
          </ScrollView>
        </View>
      </Modal>

      <Modal visible={viewResults} transparent={true} animationType="slide">
        <View style={styles.modalContainer}>
          <View style={styles.modalContent}>
            <Text style={styles.modalText}>Điểm của bạn: {finalScore}</Text>
            <TouchableOpacity
              onPress={() => navigation.goBack()}
              style={[styles.modalButton, { backgroundColor: '#00405d' }]}
            >
              <Text style={styles.modalButtonText}>Quay lại</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 10,
    backgroundColor: '#ffffff',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 20,
  },
  headerText: {
    fontSize: 18,
    fontWeight: 'bold',
    marginLeft: 10,
  },
  topBar: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 10,
  },
  progressText: {
    fontWeight: 'bold',
    fontSize: 16,
    color: '#00405d',
  },
  questionContainer: {
    marginBottom: 20,
  },
  questionText: {
    fontSize: 16,
    color: '#333333',
  },
  questionImage: {
    width: '100%',
    height: 200,
    resizeMode: 'contain',
    marginTop: 10,
  },
  optionButton: {
    padding: 10,
    borderRadius: 5,
    marginBottom: 10,
    borderWidth: 1,
    borderColor: '#ddd',
  },
  defaultOption: {
    backgroundColor: '#ffffff',
  },
  selectedOption: {
    backgroundColor: '#4caf50',
  },
  optionText: {
    fontSize: 16,
    color: '#333333',
  },
  navigationButtons: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 20,
  },
  navButton: {
    padding: 10,
    backgroundColor: '#00405d',
    borderRadius: 5,
    alignItems: 'center',
    flex: 1,
    marginHorizontal: 5,
  },
  navButtonText: {
    fontSize: 16,
    color: '#ffffff',
  },
  submitButton: {
    marginTop: 20,
    padding: 15,
    backgroundColor: '#00405d',
    borderRadius: 5,
    alignItems: 'center',
  },
  submitButtonText: {
    fontSize: 16,
    color: '#ffffff',
    fontWeight: 'bold',
  },
  modalContainer: {
    width: '100%',
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0,0,0,0.5)',
  },
  modalContent: {
    width: 350,
    backgroundColor: '#ffffff',
    padding: 20,
    borderRadius: 10,
  },
  reviewContainer: {
    marginBottom: 10,
    borderBottomWidth: 1,
    borderBottomColor: '#ddd',
    paddingBottom: 10,

  },
  modalText: {
    fontSize: 16,
    color: '#333333',
    textAlign: 'center',
  },
  modalButton: {
    padding: 10,
    borderRadius: 5,
    alignItems: 'center',
    marginTop: 10,
  },
  modalButtonText: {
    fontSize: 16,
    color: '#ffffff',
    textAlign: 'center',
  },
  modalButtons: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignSelf: 'center',
    width: '60%'
  },
  answerText: {
    fontSize: 14,
    color: '#555',
  },
  timerText: {
    fontSize: 18,
    fontWeight: 'bold',
    color: 'red',
  },
});

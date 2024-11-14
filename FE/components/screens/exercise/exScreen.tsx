import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Modal, ActivityIndicator } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface QuestionInfo {
  loiGiai: string;
  idCauHoi: number;
  noiDung: string;
}

interface AnswerInfo {
  idCauTraLoi: number;
  noiDung: string;
  ketQua: boolean;
}

export default function ExerciseScreen({ navigation, route }: { navigation: any; route: any }) {
  const [questions, setQuestions] = useState<QuestionInfo[]>([]);
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [answers, setAnswers] = useState<AnswerInfo[]>([]);
  const [selectedOption, setSelectedOption] = useState<AnswerInfo | null>(null);
  const [isCorrect, setIsCorrect] = useState<boolean | null>(null);
  const [score, setScore] = useState(0);
  const [showExplanationModal, setShowExplanationModal] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [correctAnswersCount, setCorrectAnswersCount] = useState(0);
  const [showResultModal, setShowResultModal] = useState(false);
  const [showExitModal, setShowExitModal] = useState(false); // Modal xác nhận thoát

  const { idBaiTap, tenBaiTap, idUser } = route.params;

  const fetchQuestions = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('No token found');
        return;
      }
      const response = await http.get(`baitap/getCauHoiTrue/${idBaiTap}`, {
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
      if (!token) {
        console.error('No token found');
        return;
      }
      const response = await http.get(`baitap/getCauTraLoi/${idCauHoi}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setAnswers(response.data);
    } catch (error) {
      console.error('Failed to fetch answers:', error);
    }
  };

  const createExerciseProgress = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('No token found');
        return;
      }
      const response = await http.get(`baitap/createTienTrinh/${idUser}/${idBaiTap}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log('Progress created:', response.data);
    } catch (error) {
      console.error('Failed to create exercise progress:', error);
    }
  };

  const submitExercise = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('No token found');
        return;
      }
      await http.get(`baitap/updateTienTrinh/${idUser}/${idBaiTap}/${correctAnswersCount}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setShowResultModal(true); // Hiển thị modal kết quả
    } catch (error) {
      console.error('Failed to update exercise progress:', error);
    }
  };

  useEffect(() => {
    createExerciseProgress();
    fetchQuestions();
  }, []);

  useEffect(() => {
    if (questions[currentQuestion]) {
      fetchAnswers(questions[currentQuestion].idCauHoi);
    }
  }, [currentQuestion, questions]);

  const handleOptionPress = (option: AnswerInfo) => {
    setSelectedOption(option);
    if (option.ketQua) {
      setIsCorrect(true);
      setScore(score + 10);
      setCorrectAnswersCount(correctAnswersCount + 1);
    } else {
      setIsCorrect(false);
      setShowExplanationModal(true);
    }
  };

  const handleNextOrSubmit = () => {
    if (currentQuestion < questions.length - 1) {
      setSelectedOption(null);
      setIsCorrect(null);
      setShowExplanationModal(false);
      setCurrentQuestion(currentQuestion + 1);
    } else {
      submitExercise();
    }
  };

  const handleExit = () => {
    setShowExitModal(true); // Hiển thị modal xác nhận khi người dùng muốn thoát
  };

  const handleExitConfirm = async () => {
    await submitExercise(); // Cập nhật tiến trình trước khi thoát
    setShowExitModal(false);
    setShowResultModal(true); // Hiển thị modal kết quả
  };

  const handleExitCancel = () => {
    setShowExitModal(false); // Tiếp tục làm bài
  };

  if (isLoading) {
    return <ActivityIndicator size="large" color="#00405d" />;
  }

  if (!questions[currentQuestion]) {
    return (
      <View style={styles.container}>
        <Text style={styles.noQuestionsText}>Không có câu hỏi nào cho bài tập này.</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={handleExit}>
          <Icon name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
        <Text style={styles.headerText}>{tenBaiTap}</Text>
      </View>

      <View style={styles.topBar}>
        <Text style={styles.progressText}>{currentQuestion + 1}/{questions.length}</Text>
        <Text style={styles.scoreText}>Điểm: {score}</Text>
      </View>

      <Text style={styles.questionHeader}>Chọn câu trả lời đúng</Text>
      <View style={styles.questionContainer}>
        <Text style={styles.questionText}>
          {questions[currentQuestion].noiDung}
        </Text>
      </View>

      {answers.map((option) => (
        <TouchableOpacity
          key={option.idCauTraLoi}
          style={[
            styles.optionButton,
            selectedOption === option && isCorrect === true
              ? styles.correctOption
              : selectedOption === option && isCorrect === false
                ? styles.wrongOption
                : styles.defaultOption,
          ]}
          onPress={() => handleOptionPress(option)}
          disabled={selectedOption !== null}
        >
          <Text style={styles.optionText}>{option.noiDung}</Text>
        </TouchableOpacity>
      ))}

      {selectedOption !== null && (
        <TouchableOpacity onPress={handleNextOrSubmit} style={styles.checkButton}>
          <Text style={styles.checkButtonText}>
            {currentQuestion === questions.length - 1 ? 'Nộp bài' : 'Tiếp tục'}
          </Text>
        </TouchableOpacity>
      )}

      <Modal
        visible={showExplanationModal}
        transparent={true}
        animationType="slide"
      >
        <View style={styles.modalContainer}>
          <View style={styles.modalContent}>
            <Text style={styles.modalTitle}>Oops</Text>
            <Text style={styles.correctAnswer}>
              Đáp án: {answers.find((answer) => answer.ketQua)?.noiDung}
            </Text>
            <Text style={styles.explanationText}>
              Giải thích: {questions[currentQuestion]?.loiGiai}
            </Text>
            <TouchableOpacity
              onPress={() => setShowExplanationModal(false)}
              style={styles.continueButton}
            >
              <Text style={styles.continueButtonText}>Tiếp tục</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>

      {/* Result Modal */}
      <Modal
        visible={showResultModal}
        transparent={true}
        animationType="slide"
      >
        <View style={styles.modalContainer}>
          <View style={styles.modalContent}>
            <Text style={styles.modalTitle}>Kết quả</Text>
            <Text style={styles.resultText}>Điểm đạt được: {score} / {questions.length * 10}</Text>
            <TouchableOpacity
              onPress={() => {
                setShowResultModal(false);
                navigation.goBack();
              }}
              style={styles.continueButton}
            >
              <Text style={styles.continueButtonText}>Quay lại</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>

      {/* Exit Confirmation Modal */}
      <Modal
        visible={showExitModal}
        transparent={true}
        animationType="slide"
      >
        <View style={styles.modalContainer}>
          <View style={styles.modalContent}>
            <Text style={styles.modalTitle}>Bạn có muốn kết thúc bài tập giữa chừng?</Text>
            <View style={styles.exitButtonsContainer}>
              <TouchableOpacity onPress={handleExitConfirm} style={styles.exitButton}>
                <Text style={styles.exitButtonText}>Đồng ý</Text>
              </TouchableOpacity>
              <TouchableOpacity onPress={handleExitCancel} style={styles.exitButton}>
                <Text style={styles.exitButtonText}>Hủy</Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: '#fff',
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
  topBar: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  progressText: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#6b6b6b',
  },
  scoreText: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#00bf63',
  },
  questionHeader: {
    fontSize: 18,
    fontWeight: 'bold',
    textAlign: 'center',
    marginVertical: 10,
  },
  questionContainer: {
    backgroundColor: '#f0f0f0',
    padding: 20,
    borderRadius: 10,
    marginBottom: 20,
  },
  questionText: {
    fontSize: 18,
    color: '#333',
  },
  optionButton: {
    backgroundColor: '#f9f9f9',
    borderRadius: 25,
    paddingVertical: 15,
    paddingHorizontal: 20,
    marginVertical: 10,
    justifyContent: 'center',
  },
  optionText: {
    fontSize: 16,
    color: '#333',
  },
  correctOption: {
    backgroundColor: '#d4f8e8',
    borderColor: '#00bf63',
  },
  wrongOption: {
    backgroundColor: '#fddcdc',
    borderColor: '#ff4f4f',
  },
  defaultOption: {
    borderWidth: 1,
    borderColor: '#ddd',
  },
  checkButton: {
    backgroundColor: '#00bf63',
    borderRadius: 25,
    paddingVertical: 15,
    paddingHorizontal: 20,
    marginTop: 30,
    justifyContent: 'center',
    alignItems: 'center',
  },
  checkButtonText: {
    color: '#fff',
    fontSize: 18,
    fontWeight: 'bold',
  },
  modalContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },
  modalContent: {
    backgroundColor: '#fff',
    padding: 20,
    borderRadius: 10,
    alignItems: 'center',
    width: '90%',
  },
  modalTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    color: 'red',
    marginBottom: 20,
  },
  correctAnswer: {
    fontSize: 20,
    color: '#00bf63',
    marginBottom: 10,
  },
  continueButton: {
    backgroundColor: '#ff7676',
    padding: 10,
    borderRadius: 5,
  },
  continueButtonText: {
    color: 'white',
    fontWeight: 'bold',
  },
  resultText: {
    fontSize: 18,
    color: '#333',
    marginVertical: 5,
    textAlign: 'center',
    fontWeight: 'bold',
  },
  exitButtonsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    width: '100%',
    marginTop: 20,
  },
  exitButton: {
    backgroundColor: '#00bf63',
    padding: 10,
    borderRadius: 5,
    width: '40%',
    alignItems: 'center',
  },
  exitButtonText: {
    color: '#fff',
    fontWeight: 'bold',
  },
  noQuestionsText: {
    fontSize: 18,
    textAlign: 'center',
    color: '#333',
  },
  explanationText: {
    fontSize: 16,
    color: '#333',
    marginVertical: 10,
    textAlign: 'center',
  },
});

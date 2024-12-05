import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Modal, ActivityIndicator, Image, ScrollView, Alert } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import http from '@/utils/http';
import AudioPlayer from '../audio/audioPlayer';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface QuestionInfo {
  loiGiai: string;
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
  const { idBaiTap, tenBaiTap, idUser, startFromQuestion } = route.params;
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [answers, setAnswers] = useState<AnswerInfo[]>([]);
  const [selectedOption, setSelectedOption] = useState<AnswerInfo | null>(null);
  const [isCorrect, setIsCorrect] = useState<boolean | null>(null);
  const [score, setScore] = useState(0);
  const [showExplanationModal, setShowExplanationModal] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [correctAnswersCount, setCorrectAnswersCount] = useState(0);
  const [showResultModal, setShowResultModal] = useState(false);
  const [showExitModal, setShowExitModal] = useState(false);
  const [showCompletedModal, setShowCompletedModal] = useState(false);


  const fetchQuestions = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
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

  const getExerciseProgress = async (idUser: string, idBaiTap: string) => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('Token not found');
        return null;
      }
  
      const response = await http.get(`baitap/getTienTrinhHVBT/${idUser}/${idBaiTap}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
  
      if (response.data?.idTienTrinh) {
        const progressData = response.data;
        console.log('Exercise progress found:', progressData);
        const correctCount = progressData.cauDung || 0;
        const calculatedScore = correctCount * 10;
        setCurrentQuestion(progressData.cauDaLam || 0);
        setCorrectAnswersCount(correctCount);
        setScore(calculatedScore);
        return progressData; 
      } else {
        console.log('No progress found for this exercise');
        return null;
      }
    } catch (error) {
      console.error('Error while fetching exercise progress:', error);
      return null;
    }
  };

  const resetProgress = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('Token not found');
        return;
      }
  
      await http.get(
        `baitap/updateTienTrinh/${idUser}/${idBaiTap}/0/0`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setCurrentQuestion(0);
      setCorrectAnswersCount(0);
      setScore(0);
      setShowCompletedModal(false);
      Alert.alert('Bài tập đã được làm mới. Bạn có thể bắt đầu lại!');
    } catch (error) {
      console.error('Failed to reset exercise progress:', error);
      Alert.alert('Không thể làm mới bài tập.');
    }
  };

  const createExerciseProgress = async (idUser: string, idBaiTap: string) => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('Token not found');
        return false;
      }
      const response = await http.get(`baitap/createTienTrinh/${idUser}/${idBaiTap}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.data?.idTienTrinh) {
        console.log('New exercise progress created successfully:', response.data);
        return true;
      } else {
        console.error('Unexpected response from API:', response.data);
        return false;
      }
    } catch (error) {
      console.error('Error while creating exercise progress:', error);
      return false;
    }
  };


  const handleExerciseProgress = async () => {
    try {
      const existingProgress = await getExerciseProgress(idUser, idBaiTap);
      if (!existingProgress) {
        const success = await createExerciseProgress(idUser, idBaiTap);
        if (success) {
          console.log('New exercise progress created');
        } else {
          console.error('Failed to create new exercise progress');
        }
      } else {
        console.log('Existing exercise progress found');
      }
    } catch (error) {
      console.error('Error in handleExerciseProgress:', error);
    }
  };

  const submitExercise = async (correctAnswers: number, questionsAttempted: number) => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('Token not found');
        return;
      }
      await http.get(
        `baitap/updateTienTrinh/${idUser}/${idBaiTap}/${correctAnswers}/${questionsAttempted}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setShowResultModal(true); 
    } catch (error) {
      console.error('Failed to update exercise progress:', error);
    }
  };


  useEffect(() => {
    const fetchData = async () => {
      await fetchQuestions(); 
      await handleExerciseProgress(); 
    };
    fetchData();
  }, []);

  useEffect(() => {
    if (questions.length > 0) {
      const checkCompletion = async () => {
        const progress = await getExerciseProgress(idUser, idBaiTap);
        if (progress && progress.cauDaLam === questions.length) {
          setShowCompletedModal(true);
        }
      };
      checkCompletion();
    }
  }, [questions]);

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
  const handleNextOrSubmit = async () => {
    if (currentQuestion < questions.length - 1) {
      setSelectedOption(null);
      setIsCorrect(null);
      setShowExplanationModal(false);
      setCurrentQuestion((prev) => prev + 1);
    }
    else {
      if (!selectedOption) {
        Alert.alert('Hãy chọn đáp án trước khi nộp bài!');
        return;
      }
      const totalQuestionsAttempted = questions.length; 
      const finalCorrectAnswers = selectedOption.ketQua
        ? correctAnswersCount + 1 
        : correctAnswersCount;

      await submitExercise(finalCorrectAnswers, totalQuestionsAttempted);
    }
  };
  const handleExit = () => {
    setShowExitModal(true);
  };

  const handleExitConfirm = async () => {
    const totalQuestionsAttempted = currentQuestion;
    await submitExercise(correctAnswersCount, totalQuestionsAttempted);
    setShowExitModal(false);
    setShowResultModal(true);
  };

  const handleExitCancel = () => {
    setShowExitModal(false);
  };

  if (isLoading) {
    return <ActivityIndicator size="large" color="#00405d" />;
  }

 if (showCompletedModal) {
  return (
    <Modal visible={showCompletedModal} transparent={true} animationType="slide">
      <View style={styles.modalContainer}>
        <View style={styles.modalContent}>
          <Text style={styles.modalTitle}>Bạn đã hoàn thành bài tập này!</Text>
          <Text style={styles.resultText}>Bạn có muốn làm mới bài tập không?</Text>
          <View style={styles.exitButtonsContainer}>
            <TouchableOpacity
              onPress={() => resetProgress()}
              style={[styles.exitButton, { backgroundColor: '#4caf50' }]}
            >
              <Text style={styles.exitButtonText}>Có</Text>
            </TouchableOpacity>
            <TouchableOpacity
              onPress={() => {
                setShowCompletedModal(false);
                navigation.goBack();
              }}
              style={[styles.exitButton, { backgroundColor: '#f44336' }]}
            >
              <Text style={styles.exitButtonText}>Không</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );
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
        <Text style={styles.progressText}>
          {currentQuestion + 1}/{questions.length}
        </Text>
        <Text style={styles.scoreText}>Điểm: {score}</Text>
      </View>

      <Text style={styles.questionHeader}>Chọn câu trả lời đúng</Text>
      <ScrollView showsVerticalScrollIndicator={false} showsHorizontalScrollIndicator={false}>
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

        <TouchableOpacity
          style={styles.checkButton}
          onPress={handleNextOrSubmit}
        >
          <Text style={styles.checkButtonText}>
            {currentQuestion === questions.length - 1 ? 'Nộp bài' : 'Câu tiếp theo'}
          </Text>
        </TouchableOpacity>
      </ScrollView>

      <Modal visible={showExplanationModal} transparent={true} animationType="slide">
        <View style={styles.modalContainer}>
          <View style={styles.modalContent}>
            <Text style={styles.modalTitle}>Giải thích</Text>
            <Text style={styles.explanationText}>{questions[currentQuestion]?.loiGiai}</Text>
            <TouchableOpacity
              onPress={() => setShowExplanationModal(false)}
              style={styles.continueButton}
            >
              <Text style={styles.continueButtonText}>Tiếp tục</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>

      <Modal visible={showResultModal} transparent={true} animationType="slide">
        <View style={styles.modalContainer}>
          <View style={styles.modalContent}>
            <Text style={styles.modalTitle}>Kết quả</Text>
            <Text style={styles.resultText}>Bạn đã trả lời đúng {correctAnswersCount} câu</Text>
            <TouchableOpacity
              onPress={() => navigation.goBack()}
              style={styles.continueButton}
            >
              <Text style={styles.continueButtonText}>Trở lại</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>

      <Modal visible={showExitModal} transparent={true} animationType="slide">
        <View style={styles.modalContainer}>
          <View style={styles.modalContent}>
            <Text style={styles.modalTitle}>Bạn có chắc chắn muốn thoát?</Text>
            <View style={styles.exitButtonsContainer}>
              <TouchableOpacity
                onPress={handleExitConfirm}
                style={[styles.exitButton, { backgroundColor: '#f44336' }]}
              >
                <Text style={styles.exitButtonText}>Có</Text>
              </TouchableOpacity>
              <TouchableOpacity
                onPress={handleExitCancel}
                style={[styles.exitButton, { backgroundColor: '#4caf50' }]}
              >
                <Text style={styles.exitButtonText}>Không</Text>
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
    fontSize: 16,
    color: '#00405d',
  },
  scoreText: {
    fontSize: 16,
    color: '#00405d',
  },
  questionHeader: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 10,
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
  correctOption: {
    backgroundColor: '#4caf50',
  },
  wrongOption: {
    backgroundColor: '#f44336',
  },
  optionText: {
    fontSize: 16,
    color: '#333333',
  },
  checkButton: {
    padding: 10,
    backgroundColor: '#00405d',
    borderRadius: 5,
    alignItems: 'center',
  },
  checkButtonText: {
    fontSize: 16,
    color: '#ffffff',
  },
  modalContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0,0,0,0.5)',
  },
  modalContent: {
    width: '80%',
    padding: 20,
    backgroundColor: '#ffffff',
    borderRadius: 10,
    alignItems: 'center',
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  explanationText: {
    fontSize: 16,
    color: '#333333',
    marginBottom: 20,
  },
  continueButton: {
    padding: 10,
    backgroundColor: '#00405d',
    borderRadius: 5,
    alignItems: 'center',
  },
  continueButtonText: {
    fontSize: 16,
    color: '#ffffff',
  },
  resultText: {
    fontSize: 16,
    color: '#333333',
    marginBottom: 20,
  },
  exitButtonsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    width:170,
  },
  exitButton: {
    padding: 10,
    borderRadius: 5,
    width: '40%',
    alignItems: 'center',
  },
  exitButtonText: {
    fontSize: 16,
    color: '#ffffff',
  },
  noQuestionsText: {
    fontSize: 16,
    color: '#00405d',
    textAlign: 'center',
    marginTop: 20,
  },
});

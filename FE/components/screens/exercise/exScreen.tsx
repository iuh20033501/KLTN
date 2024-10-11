import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Modal, Pressable, Image } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';  // Sử dụng gradient nếu cần

export default function ExerciseScreen() {
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [selectedOption, setSelectedOption] = useState<string | null>(null);
  const [isCorrect, setIsCorrect] = useState<boolean | null>(null);
  const [score, setScore] = useState(0);
  const [showExplanationModal, setShowExplanationModal] = useState(false);

  const questions = [
    {
      question: "There ___ a huge tree in our backyard.",
      options: ["didn't use to be", "used to", "didn't use to", "used to be"],
      correctOption: "used to be",
      explanation: "Explanation about the correct usage of 'used to be'.",
    },
  ];

  const handleOptionPress = (option: string) => {
    setSelectedOption(option);
    if (option === questions[currentQuestion].correctOption) {
      setIsCorrect(true);
      setScore(score + 10);
    } else {
      setIsCorrect(false);
      setShowExplanationModal(true); // Hiển thị modal giải thích khi sai đáp án
    }
  };

  const handleNextQuestion = () => {
    setSelectedOption(null);
    setIsCorrect(null);
    setShowExplanationModal(false); // Đóng modal khi chuyển câu hỏi
    if (currentQuestion < questions.length - 1) {
      setCurrentQuestion(currentQuestion + 1);
    } else {
      alert("Bạn đã hoàn thành bài tập này");
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.topBar}>
        <Text style={styles.progressText}>{currentQuestion + 1}/{questions.length}</Text>
        <View style={styles.progressBar}>
          <View style={[styles.progressFill, { width: `${((currentQuestion + 1) / questions.length) * 100}%` }]} />
        </View>
      </View>

      <Text style={styles.questionHeader}>Choose the correct answer.</Text>
      <View style={styles.questionContainer}>
        <Text style={styles.questionText}>
          {questions[currentQuestion].question}
        </Text>
      </View>

      {questions[currentQuestion].options.map((option, index) => (
        <TouchableOpacity
          key={index}
          style={[
            styles.optionButton,
            selectedOption === option && isCorrect === true
              ? styles.correctOption
              : selectedOption === option && isCorrect === false
                ? styles.wrongOption
                : styles.defaultOption,
          ]}
          onPress={() => handleOptionPress(option)}
          disabled={selectedOption !== null} // Vô hiệu hóa khi đã chọn đáp án
        >
          <Text style={styles.optionText}>{String.fromCharCode(65 + index)}. {option}</Text>
        </TouchableOpacity>
      ))}

      {selectedOption !== null && (
        <TouchableOpacity onPress={handleNextQuestion} style={styles.checkButton}>
          <Text style={styles.checkButtonText}>Next</Text>
        </TouchableOpacity>
      )}

      {/* Modal giải thích khi chọn sai đáp án */}
      <Modal
        visible={showExplanationModal}
        transparent={true}
        animationType="slide"
      >
        <View style={styles.modalContainer}>
          <View style={styles.modalContent}>
            <Text style={styles.modalTitle}>Oops, sorry!</Text>
            <Text style={styles.correctAnswer}>
              Answer: {questions[currentQuestion].correctOption}
            </Text>
            <Text style={styles.explanationText}>
              {questions[currentQuestion].explanation}
            </Text>
            <TouchableOpacity
              onPress={() => setShowExplanationModal(false)}
              style={styles.continueButton}
            >
              <Text style={styles.continueButtonText}>Continue</Text>
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
    padding: 20,
    backgroundColor: '#fff',
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
  progressBar: {
    flex: 1,
    height: 10,
    borderRadius: 5,
    backgroundColor: '#e0e0e0',
    marginLeft: 10,
  },
  progressFill: {
    height: '100%',
    backgroundColor: '#00bf63',
    borderRadius: 5,
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
  explanationText: {
    fontSize: 16,
    textAlign: 'center',
    marginBottom: 20,
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
});

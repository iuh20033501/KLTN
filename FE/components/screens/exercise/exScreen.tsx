import React, { useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Modal, Pressable, Image } from 'react-native';
import { FontAwesome } from '@expo/vector-icons'; // Thêm icon (Yêu cầu cài đặt expo-vector-icons)

export default function ExerciseScreen() {
  // Câu hỏi hiện tại và lựa chọn
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [selectedOption, setSelectedOption] = useState(null);
  const [isCorrect, setIsCorrect] = useState(null);
  const [showHint, setShowHint] = useState(false); // Điều khiển hiển thị modal hint
  const [score, setScore] = useState(0); // Khởi tạo điểm số bắt đầu từ 100
  const hintImg = require('../../../image/hint/hint.png')
  // Danh sách các câu hỏi với giải thích
  const questions = [
    {
      question: "Marie is proficient ___ English.",
      options: ["in", "on", "with"],
      correctOption: "in",
      explanation: `Khi nói về khả năng thành thạo trong một ngôn ngữ, chúng ta sử dụng giới từ "in". 
Cụm từ "proficient in" có nghĩa là "thành thạo về".

Ví dụ:
Marie is proficient in English. (Marie thành thạo tiếng Anh.)
She is proficient in playing the piano. (Cô ấy thành thạo chơi piano.)

Các giới từ khác:
- "on": được sử dụng để chỉ sự tập trung vào một chủ đề, ví dụ: "She is focused on her studies." (Cô ấy tập trung vào việc học.)
- "with": chỉ việc sử dụng một công cụ, ví dụ: "He is skilled with a knife." (Anh ấy khéo léo với con dao.)`,
    },
    
  ];
  const handleOptionPress = (option) => {
    setSelectedOption(option);
    if (option === questions[currentQuestion].correctOption) {
      setIsCorrect(true); 
      setScore(score + 10); 
    } else {
      setIsCorrect(false); 
    }
  };


  const handleNextQuestion = () => {
    setSelectedOption(null); 
    setIsCorrect(null); 
    if (currentQuestion < questions.length - 1) {
      setCurrentQuestion(currentQuestion + 1); 
      alert("Bạn đã hoàn thành bài tập này"); 
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Exercise</Text>
      <Text style={styles.score}>Score: {score}</Text>
      <Text style={styles.questionCounter}>
        {currentQuestion + 1}/{questions.length}
      </Text>
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
            selectedOption === option && isCorrect === true && option === questions[currentQuestion].correctOption
              ? styles.correctOption
              : selectedOption === option && isCorrect === false
                ? styles.wrongOption
                : null,
          ]}
          onPress={() => handleOptionPress(option)}
          disabled={selectedOption !== null} 
        >
          <Text style={styles.optionText}>{option}</Text>
        </TouchableOpacity>
      ))}

      {selectedOption !== null && (
        <>
          <TouchableOpacity onPress={handleNextQuestion} style={styles.nextButton}>
            <Text style={styles.nextButtonText}>Câu tiếp theo</Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={() => setShowHint(true)} style={styles.hintIcon}>
            <Image source={hintImg} style={{ width: 100, height: 100, marginLeft:200,marginTop:80 }}>
            </Image>
          </TouchableOpacity>
        </>
      )}
      <Modal visible={showHint} transparent={true} animationType="slide">
        <View style={styles.modalContainer}>
          <View style={styles.modalContent}>
            <Text style={styles.hintText}>
              {questions[currentQuestion].explanation}
            </Text>
            <Pressable
              onPress={() => setShowHint(false)}
              style={styles.closeButton}
            >
              <Text style={styles.closeButtonText}>Đóng</Text>
            </Pressable>
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
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    textAlign: 'center',
    marginVertical: 20,
  },
  score: {
    fontSize: 18,
    textAlign: 'center',
    marginBottom: 10,
    color: 'green',
  },
  questionCounter: {
    fontSize: 18,
    textAlign: 'center',
    marginBottom: 10,
  },
  questionContainer: {
    backgroundColor: '#FFC125',
    padding: 20,
    borderRadius: 10,
    marginBottom: 20,
  },
  questionText: {
    color: 'black',
    fontSize: 18,
  },
  optionButton: {
    backgroundColor: '#fff',
    borderColor: '#000',
    borderWidth: 1,
    padding: 15,
    borderRadius: 10,
    marginVertical: 10,
    alignItems: 'center',
  },
  optionText: {
    fontSize: 16,
  },
  correctOption: {
    backgroundColor: 'green',
    borderColor: 'green',
  },
  wrongOption: {
    backgroundColor: 'red',
    borderColor: 'red',
  },
  hintIcon: {
    alignSelf: 'center',
    marginTop: 20,
  },
  nextButton: {
    alignSelf: 'center',
    marginTop: 20,
    backgroundColor: '#1E88E5',
    padding: 10,
    borderRadius: 5,
  },
  nextButtonText: {
    color: 'white',
    fontWeight: 'bold',
  },
  modalContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },
  modalContent: {
    backgroundColor: 'white',
    padding: 20,
    borderRadius: 10,
    alignItems: 'center',
    width: 300,
  },
  hintText: {
    fontSize: 14,
    marginBottom: 20,
    textAlign: 'center',
  },
  closeButton: {
    backgroundColor: '#1E88E5',
    padding: 10,
    borderRadius: 5,
  },
  closeButtonText: {
    color: 'white',
    fontWeight: 'bold',
    textAlign: 'center',
  },
});

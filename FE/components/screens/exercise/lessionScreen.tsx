import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

export default function LessonListScreen({navigation}: {navigation: any}) {
  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Icon name="arrow-back-outline" size={24} color="black" />
        <Text style={styles.headerText}>Danh sách bài học</Text>
      </View>

      <TouchableOpacity style={styles.lessonButton}
       onPress={() => navigation.navigate('ExerciseScreen')}>
        <Text style={styles.lessonText}>Bài 01</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.lessonButton}>
        <Text style={styles.lessonText}>Bài 02</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.lessonButton}>
        <Text style={styles.lessonText}>Bài 03</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.lessonButton}>
        <Text style={styles.lessonText}>Bài 04</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.lessonButton}>
        <Text style={styles.lessonText}>Bài 05</Text>
      </TouchableOpacity>
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
  },
  headerText: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#00bf63',
    marginBottom: 20,
    height: 40,
    marginTop: 30,
    marginLeft: 10,
  },
  lessonButton: {
    backgroundColor: '#f0f0f0',
    padding: 20,
    borderRadius: 8,
    marginBottom: 12,
  },
  lessonText: {
    fontSize: 18,
    color: '#00bf63',
    textAlign: 'center',
  },
});

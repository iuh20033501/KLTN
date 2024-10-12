import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import { LinearGradient } from 'expo-linear-gradient';

export default function LessonListScreen({ navigation }: { navigation: any }) {
  return (
    <View style={styles.container}>
      <View style={styles.header}>
      <TouchableOpacity onPress={() => navigation.goBack()}>
          <Icon name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
        <Text style={styles.headerText}>Danh sách bài học</Text>   
        <Icon name="star-outline" size={24} color="gold" style={styles.starIcon} />
  
      </View>

      <TouchableOpacity style={styles.lessonButton}
        onPress={() => navigation.navigate('ExerciseScreen')}>
        <LinearGradient
          colors={['#00c6ff', '#0072ff']}
          style={styles.buttonGradient}
        >
          <Text style={styles.lessonText}>Bài 01</Text>
        </LinearGradient>
      </TouchableOpacity>

      <TouchableOpacity style={styles.lessonButton}>
        <LinearGradient
          colors={['#f7971e', '#ffd200']}
          style={styles.buttonGradient}
        >
          <Text style={styles.lessonText}>Bài 02</Text>
        </LinearGradient>
      </TouchableOpacity>

      <TouchableOpacity style={styles.lessonButton}>
        <LinearGradient
          colors={['#f54ea2', '#ff7676']}
          style={styles.buttonGradient}
        >
          <Text style={styles.lessonText}>Bài 03</Text>
        </LinearGradient>
      </TouchableOpacity>

      <TouchableOpacity style={styles.lessonButton}>
        <LinearGradient
          colors={['#42e695', '#3bb2b8']}
          style={styles.buttonGradient}
        >
          <Text style={styles.lessonText}>Bài 04</Text>
        </LinearGradient>
      </TouchableOpacity>

      <TouchableOpacity style={styles.lessonButton}>
        <LinearGradient
          colors={['#e1eec3', '#f05053']}
          style={styles.buttonGradient}
        >
          <Text style={styles.lessonText}>Bài 05</Text>
        </LinearGradient>
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
  lessonButton: {
    marginBottom: 16,
    borderRadius: 10,
    overflow: 'hidden',
  },
  buttonGradient: {
    paddingVertical: 20,
    borderRadius: 8,
    justifyContent: 'center',
  },
  lessonText: {
    fontSize: 20,
    color: '#ffffff',
    textAlign: 'center',
    fontWeight: 'bold',
  },
  starIcon: {
    marginRight: 10,
  },
});

import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import { LinearGradient } from 'expo-linear-gradient';

export default function ListEXScreen({ navigation }: { navigation: any }) {
  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Icon name="arrow-back-outline" size={24} color="black" onPress={() => navigation.goBack()} />
        <Text style={styles.headerText}>Bài tập rèn luyện</Text>
        <Icon name="star-outline" size={24} color="gold" style={styles.starIcon} />
      </View>

      <TouchableOpacity style={styles.exerciseButton} onPress={() => navigation.navigate('LessionListScreen')}>
        <LinearGradient
          colors={['#4c669f', '#3b5998', '#192f6a']}
          style={styles.gradientButton}
        >
          <Icon name="book-outline" size={24} color="white" />
          <Text style={styles.exerciseText}>Tính từ</Text>
        </LinearGradient>
      </TouchableOpacity>

      <TouchableOpacity style={styles.exerciseButton}>
        <LinearGradient
          colors={['#f12711', '#f5af19']}
          style={styles.gradientButton}
        >
          <Icon name="rocket-outline" size={24} color="white" />
          <Text style={styles.exerciseText}>Trạng từ</Text>
        </LinearGradient>
      </TouchableOpacity>

      <TouchableOpacity style={styles.exerciseButton}>
        <LinearGradient
          colors={['#1f4037', '#99f2c8']}
          style={styles.gradientButton}
        >
          <Icon name="document-text-outline" size={24} color="white" />
          <Text style={styles.exerciseText}>Mạo từ</Text>
        </LinearGradient>
      </TouchableOpacity>

      <TouchableOpacity style={styles.exerciseButton}>
        <LinearGradient
          colors={['#fc4a1a', '#f7b733']}
          style={styles.gradientButton}
        >
          <Icon name="library-outline" size={24} color="white" />
          <Text style={styles.exerciseText}>Danh từ</Text>
        </LinearGradient>
      </TouchableOpacity>

      <TouchableOpacity style={styles.exerciseButton}>
        <LinearGradient
          colors={['#36d1dc', '#5b86e5']}
          style={styles.gradientButton}
        >
          <Icon name="people-outline" size={24} color="white" />
          <Text style={styles.exerciseText}>Chủ ngữ / Tân ngữ</Text>
        </LinearGradient>
      </TouchableOpacity>

      <TouchableOpacity style={styles.exerciseButton}>
        <LinearGradient
          colors={['#ff512f', '#f09819']}
          style={styles.gradientButton}
        >
          <Icon name="create-outline" size={24} color="white" />
          <Text style={styles.exerciseText}>Động từ</Text>
        </LinearGradient>
      </TouchableOpacity>
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
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    marginBottom: 20,
  },
  headerText: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#00bf63',
    textAlign: 'center',
  },
  starIcon: {
    marginRight: 10,
  },
  exerciseButton: {
    marginBottom: 12,
    borderRadius: 8,
  },
  gradientButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
    borderRadius: 8,
  },
  exerciseText: {
    fontSize: 18,
    color: '#fff',
    marginLeft: 10,
    textAlign: 'center',
    fontWeight: '600',
  },
});

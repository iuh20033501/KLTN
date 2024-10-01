import React from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

export default function ListEXScreen({navigation}: {navigation: any}) {
  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Icon name="arrow-back-outline" size={24} color="black" />
        <Text style={styles.headerText}>Bài tập rèn luyện</Text>
      </View>
      
      <TouchableOpacity style={styles.exerciseButton}
      onPress={() => navigation.navigate('LessionListScreen')}>
        <Text style={styles.exerciseText}>Tính từ</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.exerciseButton}>
        <Text style={styles.exerciseText}>Trạng từ</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.exerciseButton}>
        <Text style={styles.exerciseText}>Mạo từ</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.exerciseButton}>
        <Text style={styles.exerciseText}>Danh từ</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.exerciseButton}>
        <Text style={styles.exerciseText}>Chủ ngữ / Tân ngữ</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.exerciseButton}>
        <Text style={styles.exerciseText}>Động từ</Text>
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
    color: '#388E3C',
    marginBottom: 20,
    height: 40,
    marginTop: 30,
    marginLeft: 10,
  },
  exerciseButton: {
    backgroundColor: '#f0f0f0',
    padding: 20,
    borderRadius: 8,
    marginBottom: 12,
  },
  exerciseText: {
    fontSize: 18,
    color: '#388E3C',
    textAlign: "center",
  },
});

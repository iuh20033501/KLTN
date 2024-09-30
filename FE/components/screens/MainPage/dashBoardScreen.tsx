import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Image } from 'react-native';
import { FontAwesome } from '@expo/vector-icons'; // Sử dụng icon

export default function DashboardScreen() {
  return (
    <View style={styles.container}>
      {/* Header */}
      <View style={styles.header}>
        <Text style={styles.userName}>TRẦN QUANG KHẢI</Text>
        <Text style={styles.courseName}>Tiếng Anh giao tiếp - L153304</Text>
        <TouchableOpacity style={styles.notificationIcon}>
          <FontAwesome name="bell" size={24} color="#000022" />
          <View style={styles.notificationBadge}>
            <Text style={styles.notificationBadgeText}>6</Text>
          </View>
        </TouchableOpacity>
      </View>

      {/* Buổi học sắp tới */}
      <View style={styles.upcomingClass}>
        <Text style={styles.classTitle}>Unit 1: Hello!</Text>
        <View style={styles.classInfo}>
          <Text>Phòng học A2.03</Text>
          <Text>Mr. John</Text>
        </View>
        <Text style={styles.classTime}>Th 2, 20/09/2024 - 8:00 - 10:30</Text>
      </View>

      {/* Nhiệm vụ hàng tuần */}
      <View style={styles.weeklyTasks}>
        <Text style={styles.taskTitle}>Nhiệm vụ hàng tuần</Text>
        <View style={styles.taskProgress}>
          <Text>Nhiệm vụ cần hoàn thành</Text>
          <Text>0/4</Text>
        </View>
        <TouchableOpacity style={styles.actionButton}>
          <Text style={styles.actionButtonText}>Thực hiện ngay</Text>
        </TouchableOpacity>
      </View>

      {/* Khu vực luyện tập */}
      <View style={styles.practiceArea}>
        <TouchableOpacity style={styles.practiceCard}>
          <Image source={require('../../../image/logo/EFYLogo.png')} style={styles.practiceImage} />
          <Text style={styles.practiceText}>Luyện tập lý thuyết</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.practiceCard}>
          <Image source={require('../../../image/logo/EFYLogo.png')} style={styles.practiceImage} />
          <Text style={styles.practiceText}>Luyện nghe</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  header: {
    backgroundColor: '#FFC125',
    padding: 10,
    borderRadius: 10,
    justifyContent: 'space-between',
    height:100
  },
  userName: {
    color: '#000022',
    fontSize: 18,
    fontWeight: 'bold',
  },
  courseName: {
    color: '#000022',
    fontSize: 14,
  },
  notificationIcon: {
    position: 'relative',
  },
  notificationBadge: {
    position: 'absolute',
    top: -5,
    right: 280,
    backgroundColor: 'red',
    borderRadius: 10,
    width: 20,
    height: 20,
    justifyContent: 'center',
    alignItems: 'center',
  },
  notificationBadgeText: {
    color: '#fff',
    fontSize: 12,
  },
  upcomingClass: {
    backgroundColor: '#f0f0f0',
    padding: 15,
    borderRadius: 10,
    marginTop: 20,
    width:"auto",
    height:120
  },
  classTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  classInfo: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 5,
  },
  classTime: {
    color: '#888',
  },
  weeklyTasks: {
    marginTop: 20,
    padding: 15,
    backgroundColor: '#fff',
    borderRadius: 10,
    borderWidth: 1,
    borderColor: '#ddd',
    height:140
  },
  taskTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  taskProgress: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 10,
  },
  actionButton: {
    backgroundColor: '#FFC125',
    padding: 10,
    borderRadius: 5,
    alignItems: 'center',
    
  },
  actionButtonText: {
    color: '#000022',
    fontWeight: 'bold',
  },
  practiceArea: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 20,
    height:120

  },
  practiceCard: {
    width: '48%',
    backgroundColor: '#f5f5f5',
    padding: 10,
    borderRadius: 10,
    alignItems: 'center',
  },
  practiceImage: {
    width: 60,
    height: 60,
    marginBottom: 10,
  },
  practiceText: {
    fontSize: 14,
    textAlign: 'center',
  },
  navigationBar: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingVertical: 15,
    borderTopWidth: 1,
    borderTopColor: '#ddd',
    marginTop: 20,
  },
  navItem: {
    alignItems: 'center',
  },
});

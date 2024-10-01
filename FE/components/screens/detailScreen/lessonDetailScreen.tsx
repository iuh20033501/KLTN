import React from 'react';
import { View, Text, StyleSheet, ScrollView } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

export default function LessonDetailScreen() {
  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <Icon name="arrow-back-outline" size={24} color="black" />
        <Text style={styles.headerText}>Lịch học</Text>
      </View>

      <View style={styles.lessonInfo}>
        <Text style={styles.lessonDate}>Thứ Ba, 01/10</Text>
        <Text style={styles.lessonCode}>[mã lớp]</Text>
        <Text style={styles.lessonTitle}>Unit 3: Go to school</Text>

        <View style={styles.lessonDetail}>
          <View style={styles.timeContainer}>
            <Icon name="time-outline" size={16} color="gray" />
            <Text style={styles.timeText}>19:10 - 21:10</Text>
          </View>
          <View style={styles.roomContainer}>
            <Icon name="location-outline" size={16} color="gray" />
            <Text style={styles.roomText}>Phòng học A2.03</Text>
          </View>
        </View>

        {/* Giáo viên */}
        <View style={styles.teacherContainer}>
          <Icon name="person-outline" size={16} color="gray" />
          <Text style={styles.teacherText}>Giáo viên nước ngoài</Text>
        </View>
      </View>

      {/* Chú giải */}
      <View style={styles.legend}>
        <Text style={styles.legendTitle}>Chú giải</Text>
        <View style={styles.legendContainer}>
          <View style={styles.legendItem}>
            <View style={[styles.circle, { backgroundColor: 'green' }]} />
            <Text>Buổi học</Text>
          </View>
          <View style={styles.legendItem}>
            <View style={[styles.circle, { backgroundColor: 'orange' }]} />
            <Text>Buổi thi</Text>
          </View>
        </View>
      </View>
    </ScrollView>
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
  },
  headerText: {
    fontSize: 18,
    fontWeight: 'bold',
    marginLeft: 10,
  },
  lessonInfo: {
    marginTop: 20,
    padding: 16,
    borderRadius: 10,
    backgroundColor: '#F8F8F8',
  },
  lessonDate: {
    fontSize: 14,
    color: 'gray',
  },
  lessonCode: {
    fontSize: 16,
    color: '#00A762',
    fontWeight: 'bold',
    marginTop: 5,
  },
  lessonTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginTop: 5,
  },
  lessonDetail: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 10,
  },
  timeContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  timeText: {
    marginLeft: 5,
    color: 'gray',
  },
  roomContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  roomText: {
    marginLeft: 5,
    color: 'gray',
  },
  teacherContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 10,
  },
  teacherText: {
    marginLeft: 5,
    color: 'gray',
  },
  legend: {
    marginTop: 20,
  },
  legendTitle: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  legendContainer: {
    flexDirection: 'row',
    marginTop: 10,
  },
  legendItem: {
    flexDirection: 'row',
    alignItems: 'center',
    marginRight: 20,
  },
  circle: {
    width: 16,
    height: 16,
    borderRadius: 8,
    marginRight: 8,
  },
});

  import React from 'react';
  import { View, Text, StyleSheet, ScrollView, TouchableOpacity } from 'react-native';
  import Icon from 'react-native-vector-icons/Ionicons';
  import CircularProgress from 'react-native-circular-progress-indicator';

  export default function ScoreBoardScreen() {
    return (
      <ScrollView style={styles.container}>
        <View style={styles.header}>
          <Icon name="arrow-back-outline" size={24} color="black" />
          <Text style={styles.title}>Bảng điểm</Text>
        </View>

        <View style={styles.courseInfo}>
          <Text style={styles.courseName}>Tiếng Anh giao tiếp cho người mới</Text>
          <View style={styles.statusContainer}>
            <Text style={styles.statusText}>Đang học</Text>
          </View>
        </View>

        <View style={styles.scoresContainer}>
          <View style={styles.scoreItem}>
            <View style={styles.scoreDetail}>
              <Text style={styles.scoreLabel}>Bài kiểm tra giữa khóa</Text>
              <CircularProgress
                value={100} 
                radius={50}
                duration={1000}
                progressValueColor={'#000'}
                maxValue={100}
                title={'/100'}
                titleColor={'#000'}
                titleStyle={{fontWeight: 'bold'}}
                activeStrokeColor={'#99FF00'}
                activeStrokeSecondaryColor={'#00A762'}
              />
            </View>
            <Text style={styles.date}>Jul 19, 2024</Text>
          </View>

          <View style={styles.scoreItem}>
            <View style={styles.scoreDetail}>
              <Text style={styles.scoreLabel}>Bài thi viết</Text>
              <CircularProgress
                value={20} 
                radius={50}
                duration={1000}
                progressValueColor={'#000'}
                maxValue={100}
                title={'/100'}
                titleColor={'#000'}
                titleStyle={{fontWeight: 'bold'}}
                activeStrokeColor={'#99FF00'}
                activeStrokeSecondaryColor={'#00A762'}
              />
            </View>
            <Text style={styles.date}>Jul 19, 2024</Text>
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
      marginBottom: 20,
    },
    title: {
      fontSize: 20,
      fontWeight: 'bold',
      marginLeft: 10,
    },
    courseInfo: {
      padding: 16,
      borderRadius: 10,
      backgroundColor: '#F8F8F8',
    },
    courseName: {
      fontSize: 18,
      fontWeight: 'bold',
      color: '#00A762',
    },
    statusContainer: {
      marginTop: 8,
      backgroundColor: '#E8F5E9',
      padding: 8,
      borderRadius: 5,
      alignItems: 'center',
    },
    statusText: {
      fontSize: 14,
      color: '#388E3C',
    },
    scoresContainer: {
      marginTop: 20,
    },
    scoreItem: {
      padding: 16,
      backgroundColor: '#F8F8F8',
      borderRadius: 10,
      marginBottom: 10,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    scoreDetail: {
      flexDirection: 'column',
    },
    scoreLabel: {
      fontSize: 16,
      fontWeight: 'bold',
      marginBottom: 10,
    },
    date: {
      fontSize: 14,
      color: 'gray',
    },
  });

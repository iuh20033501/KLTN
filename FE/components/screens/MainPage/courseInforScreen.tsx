import React from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity } from 'react-native';
import { FontAwesome, Ionicons, MaterialIcons, Entypo,Feather } from '@expo/vector-icons';  // Install expo icons package

export default function CourseInfoScreen({navigation}: {navigation: any}) {
  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
      <Text style ={{fontSize:20,fontWeight:'bold', textAlign:"center",padding:20, color: '#388E3C',}}>Thông tin</Text>

        <Text style={styles.courseTitle}>Tiếng Anh giao tiếp - [mã lớp]</Text>
        <View style={styles.infoBox}>
          <View style={styles.infoRow}>
            <FontAwesome name="calendar" size={24} color="black" />
            <Text style={styles.infoText}>1/9/2024 - 30/12/2024</Text>
          </View>
          <View style={styles.infoRow}>
            <Ionicons name="school" size={24} color="black" />
            <Text style={styles.infoText}>Phòng học A3.04</Text>
          </View>
          <View style={styles.infoRow}>
            <Entypo name="location-pin" size={24} color="black" />
            <Text style={styles.infoText}>
            12 Nguyễn Văn Bảo, Phường 4, Gò Vấp, Hồ Chí Minh
            </Text>
          </View>
        </View>
      </View>

      <View style={styles.optionList}>
        <TouchableOpacity style={styles.option}
         onPress={() => navigation.navigate('LessonDetailScreen')}>
          <View style={styles.optionRow}>
            <MaterialIcons name="schedule" size={24} color="orange" />
            <Text style={styles.optionText}>Lịch học</Text>
          </View>
          <Text style={styles.optionValue}>16/24</Text>
        </TouchableOpacity>

        <TouchableOpacity style={styles.option}
        onPress={() => navigation.navigate('ScoreBoardScreen')}>
          <View style={styles.optionRow}>
            <FontAwesome name="list-alt" size={24} color="green" />
            <Text style={styles.optionText}>Bảng điểm</Text>
          </View>
        </TouchableOpacity>

        <TouchableOpacity style={styles.option}>
          <View style={styles.optionRow}>
            <FontAwesome name="graduation-cap" size={24} color="red" />
            <Text style={styles.optionText}>Chứng nhận</Text>
          </View>
        </TouchableOpacity>

        <TouchableOpacity style={styles.option}>
          <View style={styles.optionRow}>
            <FontAwesome name="trophy" size={24} color="gold" />
            <Text style={styles.optionText}>Bảng xếp hạng</Text>
          </View>
        </TouchableOpacity>

        <TouchableOpacity style={styles.option}>
          <View style={styles.optionRow}>
            <MaterialIcons name="volume-up" size={24} color="blue" />
            <Text style={styles.optionText}>Kết quả phát âm</Text>
          </View>
        </TouchableOpacity>
        <TouchableOpacity style={styles.option}>
          <View style={styles.optionRow}>
          <Feather name="file-text" size={24} color="purple" />
            <Text style={styles.optionText}>Nội quy</Text>
          </View>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width:'100%',
    backgroundColor: '#f8f8f8',
  },
  header: {
    padding: 20,
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#ccc',
  },
  courseTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  infoBox: {
    backgroundColor: '#f0f0f0',
    padding: 15,
    borderRadius: 8,
  },
  infoRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
  },
  infoText: {
    marginLeft: 10,
    fontSize: 16,
  },
  optionList: {
    marginTop: 20,
  },
  option: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 20,
    backgroundColor: '#fff',
    borderBottomWidth: 1,
    borderBottomColor: '#ccc',
  },
  optionRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  optionText: {
    marginLeft: 10,
    fontSize: 16,
  },
  optionValue: {
    fontSize: 16,
    color: 'orange',
  },
});

import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, ScrollView, ActivityIndicator, TouchableOpacity } from 'react-native';
import DateTimePicker from '@react-native-community/datetimepicker';
import Icon from 'react-native-vector-icons/Ionicons';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface LessonInfo {
  chuDe: string;
  idBuoiHoc: number;
  ngayHoc: string;
  gioHoc: string;
  gioKetThuc: string;
  lopHoc: {
    idLopHoc: number;
    tenLopHoc: string;
  };
  noiHoc: string;
  hocOnl: boolean;
}

export default function LessonDetailScreen({ navigation, route }: { navigation: any; route: any }) {
  const { idUser } = route.params;
  const [lessons, setLessons] = useState<LessonInfo[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [isDatePickerVisible, setIsDatePickerVisible] = useState(false);

  const calculateStartOfWeek = (date: Date) => {
    const selectedDay = new Date(date);
    const day = selectedDay.getDay();
    const diff = selectedDay.getDate() - day + (day === 0 ? -6 : 1);
    return new Date(selectedDay.setDate(diff));
  };

  const fetchWeeklyLessons = async (startDate: Date) => {
    try {
      setLoading(true);
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('Token không tồn tại');
        return;
      }

      const response = await http.get(`/buoihoc/getByHocVien/${idUser}`, {
        params: { startDate: startDate.toISOString().split('T')[0] },
        headers: { Authorization: `Bearer ${token}` },
      });

      if (response.status === 200) {
        setLessons(response.data);
      } else {
        console.error('Không tìm thấy dữ liệu buổi học');
        setLessons([]);
      }
    } catch (error) {
      console.error('Lỗi khi lấy dữ liệu buổi học:', error);
      setLessons([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const startOfWeek = calculateStartOfWeek(new Date());
    setSelectedDate(startOfWeek);
    fetchWeeklyLessons(startOfWeek);
  }, [idUser]);

  const handleDateChange = (event: any, date?: Date) => {
    if (date) {
      const startOfWeek = calculateStartOfWeek(date);
      setSelectedDate(startOfWeek);
      fetchWeeklyLessons(startOfWeek);
    }
    setIsDatePickerVisible(false);
  };

  const handlePreviousWeek = () => {
    const newDate = new Date(selectedDate);
    newDate.setDate(newDate.getDate() - 7);
    setSelectedDate(newDate);
    fetchWeeklyLessons(newDate);
  };

  const handleNextWeek = () => {
    const newDate = new Date(selectedDate);
    newDate.setDate(newDate.getDate() + 7);
    setSelectedDate(newDate);
    fetchWeeklyLessons(newDate);
  };

  const handleCurrentWeek = () => {
    const currentWeekStart = calculateStartOfWeek(new Date());
    setSelectedDate(currentWeekStart);
    fetchWeeklyLessons(currentWeekStart);
  };

  // Get the lessons only for the selected week
  const startOfWeek = calculateStartOfWeek(selectedDate);
  const endOfWeek = new Date(startOfWeek);
  endOfWeek.setDate(endOfWeek.getDate() + 6);

  const filteredLessons = lessons.filter((lesson) => {
    const lessonDate = new Date(lesson.ngayHoc);
    return lessonDate >= startOfWeek && lessonDate <= endOfWeek;
  });

  if (loading) {
    return <ActivityIndicator size="large" color="#00405d" />;
  }

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
        <Icon name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
        
        <Text style={styles.headerText}>Lịch học</Text>
      </View>

      <View style={styles.datePickerContainer}>
        <TouchableOpacity style={styles.dateButton} onPress={() => setIsDatePickerVisible(true)}>
          <Text style={styles.buttonText}>{selectedDate.toLocaleDateString('vi-VN')}</Text>
        </TouchableOpacity>
      </View>
      <View style={styles.datePickerContainer}>
      
        <TouchableOpacity style={styles.navigationButton} onPress={handlePreviousWeek}>
          <Text style={styles.navigationButtonText}>{"<"}</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.navigationButton} onPress={handleCurrentWeek}>
          <Text style={styles.navigationButtonText}>Tuần hiện tại</Text>
        </TouchableOpacity>
       
        <TouchableOpacity style={styles.navigationButton} onPress={handleNextWeek}>
          <Text style={styles.navigationButtonText}>{">"}</Text>
        </TouchableOpacity>
      </View>

      {isDatePickerVisible && (
        <DateTimePicker
          value={selectedDate}
          mode="date"
          display="default"
          onChange={handleDateChange}
        />
      )}

      {filteredLessons.length === 0 ? (
        <View style={styles.noLessonsContainer}>
          <Text style={styles.noLessonsText}>Không có lịch học trong tuần này</Text>
        </View>
      ) : (
        filteredLessons.map((lesson) => (
          <View
            key={lesson.idBuoiHoc}
            style={[
              styles.lessonInfo,
              { backgroundColor: lesson.hocOnl ? '#d0ebff' : '#f0f0f0' },
            ]}
          >
            <Text style={styles.lessonDate}>
              {new Date(lesson.ngayHoc).toLocaleDateString('vi-VN', { weekday: 'long', day: 'numeric', month: 'numeric' })}
            </Text>
            <Text style={styles.lessonCode}>{lesson.lopHoc.tenLopHoc}</Text>
            <Text style={styles.lessonTitle}>{lesson.chuDe}</Text>

            <View style={styles.lessonDetail}>
              <View style={styles.timeContainer}>
                <Icon name="time-outline" size={16} color="gray" />
                <Text style={styles.timeText}>
                  {lesson.gioHoc} - {lesson.gioKetThuc}
                </Text>
              </View>
              <View style={styles.roomContainer}>
                <Icon name="location-outline" size={16} color="gray" />
                <Text style={styles.roomText}>{lesson.noiHoc}</Text>
              </View>
            </View>

            <View style={styles.teacherContainer}>
              <Icon name="person-outline" size={16} color="gray" />
              <Text style={styles.teacherText}>{lesson.hocOnl ? 'Giáo viên trực tuyến' : 'Giáo viên trực tiếp'}</Text>
            </View>
          </View>
        ))
      )}

      <View style={styles.legend}>
        <Text style={styles.legendTitle}>Chú giải</Text>
        <View style={styles.legendContainer}>
          <View style={styles.legendItem}>
            <View style={[styles.circle, { backgroundColor: '#d0ebff' }]} />
            <Text>Học trực tuyến</Text>
          </View>
          <View style={styles.legendItem}>
            <View style={[styles.circle, { backgroundColor: '#f0f0f0' }]} />
            <Text>Học trực tiếp</Text>
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
  },
  lessonDate: {
    fontSize: 14,
    color: 'gray',
  },
  lessonCode: {
    fontSize: 16,
    color: '#00bf63',
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
  datePickerContainer: {
    marginTop:20,
    flexDirection: 'row',
    justifyContent:'center'
  },
  dateButton: {
    width:310,
    padding: 10,
    backgroundColor: '#00bf63',
    borderRadius: 5,
  },
  buttonText: {
    textAlign:'center',
    color: '#fff',
    fontWeight: 'bold',
    fontSize:20
  },
  navigationButton: {
    padding: 10,
    borderRadius: 5,
    backgroundColor: '#00bf63',
    marginHorizontal: 5,
  },
  navigationButtonText: {
    color: '#fff',
    fontWeight: 'bold',
  },
  modalContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },
  closeButton: {
    marginTop: 20,
    padding: 10,
    backgroundColor: '#00405d',
    borderRadius: 5,
  },
  closeButtonText: {
    color: '#fff',
    fontWeight: 'bold',
  },
  noLessonsContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 20,
  },
  noLessonsText: {
    fontSize: 16,
    fontWeight: 'bold',
    color: 'gray',
  },
});

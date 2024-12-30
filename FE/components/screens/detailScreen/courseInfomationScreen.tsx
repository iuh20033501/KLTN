import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, FlatList } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import http from '@/utils/http';
import Icon from 'react-native-vector-icons/Ionicons';
import { FontAwesome, MaterialIcons } from '@expo/vector-icons';

export default function CourseInfomationScreen({ navigation, route }: { navigation: any; route: any }) {
  const [classes, setClasses] = useState<any[]>([]);
  const { idUser } = route.params;
  const [loading, setLoading] = useState(true);

 
  const getStudentClasses = async (idUser: number) => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (token) {
        const response = await http.get(`/hocvien/getByHV/${idUser}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.status === 200) {
          setClasses(response.data); 
        } else {
          console.error('Không thể lấy thông tin lớp học.');
        }
      } else {
        console.error('Không có token, vui lòng đăng nhập lại.');
      }
    } catch (error) {
      console.error('Lỗi khi gọi API lấy thông tin lớp học:', error);
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      if (idUser) {
        await getStudentClasses(idUser);
      }
      setLoading(false); 
    };

    fetchData();
  }, []);

  if (loading) {
    return <Text style={styles.loadingText}>Đang tải thông tin...</Text>;
  }

  if (!classes || classes.length === 0) {
    return <Text style={styles.errorText}>Không có thông tin lớp học.</Text>;
  }

  const formatDate = (dateString: string) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Icon name="arrow-back-outline" size={28} color="#333" />
        </TouchableOpacity>
        <Text style={styles.headerText}>Thông tin các khóa học</Text>
      </View>

      <FlatList
        data={classes}
        keyExtractor={(item, index) => index.toString()}
        renderItem={({ item }) => (
          <View style={styles.infoContainer}>
            <View style={styles.row}>
              <MaterialIcons name="class" size={24} color="#00bf63" />
              <Text style={styles.label}>
                Lớp học: <Text style={styles.value}>{item.tenLopHoc || 'N/A'}</Text>
              </Text>
            </View>

            <View style={styles.row}>
              <FontAwesome name="book" size={24} color="#00bf63" />
              <Text style={styles.label}>
                Khóa học: <Text style={styles.value}>{item.khoaHoc?.tenKhoaHoc || 'N/A'}</Text>
              </Text>
            </View>

            <View style={styles.row}>
              <FontAwesome name="calendar" size={24} color="#00bf63" />
              <Text style={styles.label}>
                Ngày bắt đầu: <Text style={styles.value}>{formatDate(item.ngayBD)}</Text>
              </Text>
            </View>

            <View style={styles.row}>
              <FontAwesome name="calendar-check-o" size={24} color="#00bf63" />
              <Text style={styles.label}>
                Ngày kết thúc: <Text style={styles.value}>{formatDate(item.ngayKT)}</Text>
              </Text>
            </View>

            <View style={styles.row}>
              <FontAwesome name="book" size={24} color="#00bf63" />
              <Text style={styles.label}>
                Lịch: <Text style={styles.value}>{item.moTa || 'N/A'}</Text>
              </Text>
            </View>

            <View style={styles.row}>
              <MaterialIcons name="person" size={24} color="#00bf63" />
              <Text style={styles.label}>
                Giảng viên: <Text style={styles.value}>{item.giangVien?.hoTen || 'N/A'}</Text>
              </Text>
            </View>

            <View style={styles.row}>
              <MaterialIcons name="record-voice-over" size={24} color="#00bf63" />
              <Text style={styles.label}>
                Liên hệ giảng viên: <Text style={styles.value}>{item.giangVien?.email || 'N/A'}</Text>
              </Text>
            </View>
          </View>
        )}
      />
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flexGrow: 1,
    backgroundColor: '#F5F5F5',
    padding: 15,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 20,
    paddingVertical: 10,
  },
  headerText: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#333',
    marginLeft: 20,
  },
  infoContainer: {
    padding: 20,
    backgroundColor: '#FFFFFF',
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 5,
    elevation: 3,
    marginBottom: 20,
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 15,
  },
  label: {
    fontSize: 18,
    fontWeight: '600',
    color: '#333',
    marginLeft: 10,
  },
  value: {
    fontSize: 18,
    fontWeight: '400',
    color: '#666',
  },
  loadingText: {
    fontSize: 18,
    textAlign: 'center',
    color: '#00bf63',
    marginTop: 20,
  },
  errorText: {
    fontSize: 18,
    textAlign: 'center',
    color: 'red',
    marginTop: 20,
  },
});

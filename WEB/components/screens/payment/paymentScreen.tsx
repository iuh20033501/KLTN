import React, { useEffect, useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ScrollView, Alert } from 'react-native';
import CheckBox from '@react-native-community/checkbox';
import http from '@/utils/http'; 
import AsyncStorage from '@react-native-async-storage/async-storage';

type PendingClass = {
  idLopHoc: number;
  tenLopHoc: string;
  trangThai: string; //
};

const PaymentScreen = ({ navigation }: { navigation: any }) => {
  const [pendingClasses, setPendingClasses] = useState<PendingClass[]>([]);
  const [selectedClasses, setSelectedClasses] = useState<number[]>([]);

  useEffect(() => {
    fetchPendingClasses();
  }, []);

  const fetchPendingClasses = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('Token không tồn tại');
        return;
      }

      const response = await http.get('/lopHoc/getPendingClasses', {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (response.status === 200) {
        setPendingClasses(response.data);
      }
    } catch (error) {
      console.error('Error fetching pending classes:', error);
    }
  };

  const handleClassToggle = (classId: number) => {
    setSelectedClasses((prevSelected) =>
      prevSelected.includes(classId)
        ? prevSelected.filter((id) => id !== classId)
        : [...prevSelected, classId]
    );
  };

  const handlePayment = async () => {
    if (selectedClasses.length === 0) {
      Alert.alert("Thông báo", "Vui lòng chọn ít nhất một lớp để thanh toán.");
      return;
    }

    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('Token không tồn tại');
        return;
      }

      const response = await http.post('/lopHoc/confirmPayment', {
        classIds: selectedClasses
      }, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (response.status === 200) {
        Alert.alert("Thanh toán thành công", "Đã hoàn tất thanh toán các lớp học đã chọn.");
        fetchPendingClasses(); 
        setSelectedClasses([]); 
      } else {
        Alert.alert("Lỗi", "Thanh toán không thành công, vui lòng thử lại.");
      }
    } catch (error) {
      console.error('Error confirming payment:', error);
      Alert.alert("Lỗi", "Thanh toán không thành công, vui lòng thử lại.");
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Thanh toán học phí</Text>
      <ScrollView contentContainerStyle={styles.scrollContainer}>
        {pendingClasses.length > 0 ? (
          pendingClasses.map((classItem) => (
            <View key={classItem.idLopHoc} style={styles.classItem}>
              <CheckBox
                value={selectedClasses.includes(classItem.idLopHoc)}
                onValueChange={() => handleClassToggle(classItem.idLopHoc)}
              />
              <Text style={styles.classText}>{classItem.tenLopHoc}</Text>
              <Text style={styles.statusText}>{classItem.trangThai === 'Pending' ? 'Chưa thanh toán' : 'Hoàn tất'}</Text>
            </View>
          ))
        ) : (
          <Text>Không có lớp nào cần thanh toán.</Text>
        )}
      </ScrollView>
      {pendingClasses.length > 0 && (
        <TouchableOpacity style={styles.paymentButton} onPress={handlePayment}>
          <Text style={styles.paymentButtonText}>Thanh toán</Text>
        </TouchableOpacity>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: '#f8f8f8',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#00405d',
    textAlign: 'center',
    marginBottom: 20,
  },
  scrollContainer: {
    flexGrow: 1,
  },
  classItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 15,
    borderBottomWidth: 1,
    borderBottomColor: '#ddd',
  },
  classText: {
    fontSize: 16,
    flex: 1,
  },
  statusText: {
    fontSize: 14,
    color: '#ff8c00',
  },
  paymentButton: {
    backgroundColor: '#0066cc',
    padding: 15,
    borderRadius: 8,
    alignItems: 'center',
    marginTop: 20,
  },
  paymentButtonText: {
    color: 'white',
    fontSize: 18,
    fontWeight: 'bold',
  },
});

export default PaymentScreen;

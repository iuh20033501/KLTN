import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, FlatList, ActivityIndicator, TouchableOpacity, Modal, ImageBackground, ScrollView, CheckBox } from 'react-native';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';

type Payment = {
  [x: string]: any;
  idTT: number;
  noiDungThu: string;
  giaTien: number;
  trangThai: string;
};

const PaymentScreen = ({ navigation, route }: { navigation: any; route: any }) => {
  const { idUser } = route.params;
  const [payments, setPayments] = useState<Payment[]>([]);
  const [loading, setLoading] = useState(true);
  const [messageModalVisible, setMessageModalVisible] = useState(false);
  const [messageText, setMessageText] = useState('');
  const [selectedPayments, setSelectedPayments] = useState<Set<number>>(new Set());
  const [isSelectAll, setIsSelectAll] = useState(false);
  const [totalAmount, setTotalAmount] = useState(0);

  const fetchPayments = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        setMessageText('Lỗi: Token không tồn tại');
        setMessageModalVisible(true);
        return;
      }

      const response = await http.get(`thanhToan/findByIdHV/${idUser}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (response.status === 200) {
        setPayments(response.data);
      } else {
        setMessageText('Lỗi: Không thể lấy dữ liệu thanh toán');
        setMessageModalVisible(true);
      }
    } catch (error) {
      console.error('Error fetching payments:', error);
      setMessageText('Lỗi: Đã xảy ra lỗi khi lấy dữ liệu thanh toán');
      setMessageModalVisible(true);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPayments();
  }, []);

  const handleSelectAllPayments = () => {
    const waitPayments = payments.filter(payment => payment.trangThai === 'WAIT');
    if (isSelectAll) {
      setSelectedPayments(new Set());
      setTotalAmount(0);
    } else {
      const allPaymentIds = waitPayments.map(payment => payment.idTT);
      setSelectedPayments(new Set(allPaymentIds));
      setTotalAmount(waitPayments.reduce((sum, payment) => sum + payment.giaTien, 0));
    }
    setIsSelectAll(!isSelectAll);
  };

  const handleSelectPayment = (id: number, amount: number) => {
    setSelectedPayments((prevSelected) => {
      const updatedSelected = new Set(prevSelected);
      if (updatedSelected.has(id)) {
        updatedSelected.delete(id);
        setTotalAmount((prevTotal) => prevTotal - amount);
      } else {
        updatedSelected.add(id);
        setTotalAmount((prevTotal) => prevTotal + amount);
      }
      return updatedSelected;
    });
  };

  const getPaymentStatus = (status: string): string => {
    switch (status) {
      case 'WAIT':
        return 'Chờ thanh toán';
      case 'DONE':
        return 'Đã thanh toán';
      case 'CANCEL':
        return 'Đã hủy';
      default:
        return 'Trạng thái không xác định';
    }
  };

  const renderPendingPaymentItem = ({ item, index }: { item: Payment; index: number }) => (
    <View style={styles.paymentItem}>
      <CheckBox
        style={styles.checkbox}
        value={selectedPayments.has(item.idTT)}
        onValueChange={() => handleSelectPayment(item.idTT, item.lopHoc.khoaHoc.giaTien || 0)}
      />
      <Text style={styles.paymentText}>{index + 1}</Text>
      <Text style={styles.paymentText}>{item.idTT}</Text>
      <Text style={styles.paymentText}>{item.lopHoc?.khoaHoc?.tenKhoaHoc || ''}</Text>
      <Text style={styles.paymentText}>{item.lopHoc?.tenLopHoc || ''}</Text>
      <Text style={styles.paymentText}>{(item.lopHoc.khoaHoc.giaTien || 0).toLocaleString()} VND</Text>
      <Text style={styles.paymentText}>{getPaymentStatus(item.trangThai)}</Text>
    </View>
  );

  const renderCompletedPaymentItem = ({ item, index }: { item: Payment; index: number }) => (
    <View style={styles.paymentItem}>
      <Text style={styles.paymentText}>{index + 1}</Text>
      <Text style={styles.paymentText}>{item.idTT}</Text>
      <Text style={styles.paymentText}>{item.lopHoc?.khoaHoc?.tenKhoaHoc || ''}</Text>
      <Text style={styles.paymentText}>{item.lopHoc?.tenLopHoc || ''}</Text>
      <Text style={styles.paymentText}>{(item.lopHoc.khoaHoc.giaTien || 0).toLocaleString()} VND</Text>
      <Text style={styles.paymentText}>{getPaymentStatus(item.trangThai)}</Text>
    </View>
  );

  if (loading) {
    return <ActivityIndicator size="large" color="#00405d" />;
  }

  return (
    <ImageBackground source={require('../../../image/bglogin.png')} style={styles.background}>
      <View style={styles.container}>
        <TouchableOpacity style={styles.backButton} onPress={() => navigation.navigate('DashboardScreen')}>
          <Text style={styles.backButtonText}>Quay về</Text>
        </TouchableOpacity>

        <Text style={styles.title}>Thanh toán trực tuyến</Text>

        <Text style={styles.sectionTitle}>Đã thanh toán hoặc đã hủy</Text>
        <ScrollView style={styles.scrollContainer}>
          <View style={styles.tableHeader}>
            <Text style={styles.tableHeaderText}>STT</Text>
            <Text style={styles.tableHeaderText}>Mã thanh toán</Text>
            <Text style={styles.tableHeaderText}>Khóa học</Text>
            <Text style={styles.tableHeaderText}>Lớp học</Text>
            <Text style={styles.tableHeaderText}>Số tiền (VND)</Text>
            <Text style={styles.tableHeaderText}>Trạng thái</Text>
          </View>
          <FlatList
            data={payments.filter((payment) => payment.trangThai !== 'WAIT')}
            keyExtractor={(item) => item.idTT.toString()}
            renderItem={renderCompletedPaymentItem}
            ListEmptyComponent={<Text style={styles.noPaymentsText}>Không có khoản thanh toán đã thực hiện.</Text>}
          />
        </ScrollView>

        <Text style={styles.sectionTitle}>Chờ thanh toán</Text>
        <ScrollView style={styles.scrollContainer}>
          <View style={styles.tableHeader}>
            <CheckBox
              style={styles.checkbox}
              value={isSelectAll}
              onValueChange={handleSelectAllPayments}
            />
            <Text style={styles.tableHeaderText}>STT</Text>
            <Text style={styles.tableHeaderText}>Mã thanh toán</Text>
            <Text style={styles.tableHeaderText}>Khóa học</Text>
            <Text style={styles.tableHeaderText}>Lớp học</Text>
            <Text style={styles.tableHeaderText}>Số tiền (VND)</Text>
            <Text style={styles.tableHeaderText}>Trạng thái</Text>
          </View>
          <FlatList
            data={payments.filter((payment) => payment.trangThai === 'WAIT')}
            keyExtractor={(item) => item.idTT.toString()}
            renderItem={renderPendingPaymentItem}
            ListEmptyComponent={<Text style={styles.noPaymentsText}>Không có khoản thanh toán chờ.</Text>}
          />
        </ScrollView>

        <Text style={styles.totalText}>Tổng thanh toán: {totalAmount.toLocaleString()} VND</Text>

        <TouchableOpacity style={styles.payButton}>
          <Text style={styles.buttonText}>THANH TOÁN</Text>
        </TouchableOpacity>

        <Modal
          visible={messageModalVisible}
          transparent={true}
          animationType="slide"
          onRequestClose={() => setMessageModalVisible(false)}
        >
          <View style={styles.modalOverlay}>
            <View style={styles.modalContainer}>
              <Text style={styles.modalMessage}>{messageText}</Text>
              <TouchableOpacity onPress={() => setMessageModalVisible(false)} style={styles.modalButton}>
                <Text style={styles.modalButtonText}>Đóng</Text>
              </TouchableOpacity>
            </View>
          </View>
        </Modal>
      </View>
    </ImageBackground>
  );
};

const styles = StyleSheet.create({
  background: {
    flex: 1,
    paddingHorizontal: 400,
    height: 990
  },
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: 'rgba(255, 255, 255, 0.8)',
  },
  backButton: {
    alignSelf: 'flex-start',
    padding: 10,
    backgroundColor: '#00405d',
    borderRadius: 5,
    marginBottom: 10,
  },
  backButtonText: {
    color: '#fff',
    fontWeight: 'bold',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#00405d',
    textAlign: 'center',
    marginBottom: 20,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#00405d',
    marginTop: 20,
    marginBottom: 10,
  },
  scrollContainer: {
    maxHeight: 300,  // Adjust as needed for table scroll area height
  },
  tableHeader: {
    flexDirection: 'row',
    paddingVertical: 10,
    backgroundColor: '#e8e8e8',
    borderRadius: 5,
  },
  tableHeaderText: {
    flex: 1,
    fontWeight: 'bold',
    color: '#333',
    textAlign: 'center',
  },
  paymentItem: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 10,
    borderBottomWidth: 1,
    borderBottomColor: '#ddd',
  },
  paymentText: {
    flex: 1,
    textAlign: 'center',
    fontSize: 14,
    color: '#333',
  },
  checkbox: {
    marginHorizontal: 8,
  },
  noPaymentsText: {
    fontSize: 16,
    color: '#333',
    textAlign: 'center',
    marginVertical: 20,
  },
  totalText: {
    fontSize: 18,
    fontWeight: 'bold',
    color: 'red',
    textAlign: 'right',
    marginTop: 10,
  },
  payButton: {
    backgroundColor: '#f44336',
    padding: 15,
    borderRadius: 5,
    marginTop: 20,
    alignItems: 'center',
  },
  buttonText: {
    color: '#fff',
    fontWeight: 'bold',
  },
  modalOverlay: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },
  modalContainer: {
    width: 300,
    backgroundColor: '#fff',
    padding: 20,
    borderRadius: 10,
    alignItems: 'center',
  },
  modalMessage: {
    fontSize: 16,
    color: '#333',
    marginBottom: 20,
    textAlign: 'center',
  },
  modalButton: {
    backgroundColor: '#00405d',
    padding: 10,
    borderRadius: 5,
    width: '100%',
    alignItems: 'center',
  },
  modalButtonText: {
    color: '#fff',
    fontWeight: 'bold',
  },
});

export default PaymentScreen;

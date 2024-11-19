import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, FlatList, ActivityIndicator, TouchableOpacity, Modal, ImageBackground, ScrollView, Alert, CheckBox, Image } from 'react-native';
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
  const { idUser, nameUser } = route.params;
  const [payments, setPayments] = useState<Payment[]>([]);
  const [loading, setLoading] = useState(true);
  const [messageModalVisible, setMessageModalVisible] = useState(false);
  const [messageText, setMessageText] = useState('');
  const [selectedPayments, setSelectedPayments] = useState<Set<number>>(new Set());
  const [isSelectAll, setIsSelectAll] = useState(false);
  const [totalAmount, setTotalAmount] = useState(0);
  const [isPaymentModalVisible, setIsPaymentModalVisible] = useState(false);
  const [isQrPaymentModalVisible, setIsQrPaymentModalVisible] = useState(false);
  const [totalPaid, setTotalPaid] = useState(0); 

  useEffect(() => {
    fetchPayments();
  }, []);
  
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
        console.log(response.data)
        calculateTotalPaid(response.data); 

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

  const handleCancelSelectedPayments = async () => {
    if (selectedPayments.size === 0) {
      setMessageText('Bạn chưa chọn thanh toán nào để hủy');
      setMessageModalVisible(true);
      return;
    }
  
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        setMessageText('Token không tồn tại');
        setMessageModalVisible(true);
        return;
      }
  
      for (const idTT of selectedPayments) {
        const response = await http.get(`/thanhToan/delete/${idTT}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
  
        if (response.status !== 200) {
          setMessageText(`Không thể hủy thanh toán có ID: ${idTT}`);
          setMessageModalVisible(true);
          return;
        }
      }
  
      setMessageText('Các thanh toán đã được hủy thành công');
      setMessageModalVisible(true);
      setSelectedPayments(new Set()); 
      fetchPayments(); 
    } catch (error) {
      console.error('Error canceling payments:', error);
      setMessageText('Đã xảy ra lỗi khi hủy thanh toán');
      setMessageModalVisible(true);
    }
  };

 

  const handleSelectAllPayments = () => {
    const waitPayments = payments.filter(payment => payment.trangThai === 'WAIT');
    if (isSelectAll) {
      setSelectedPayments(new Set());
      setTotalAmount(0);
    } else {
      const allPaymentIds = waitPayments.map(payment => payment.idTT);
      setSelectedPayments(new Set(allPaymentIds));
      setTotalAmount(waitPayments.reduce((sum, payment) => sum + payment.lopHoc.khoaHoc.giaTien, 0));
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

  const handlePayment = () => {
    console.log("Selected payments size:", selectedPayments.size);

    if (selectedPayments.size === 0) {
      setMessageText('Bạn chưa chọn thanh toán nào');
      setMessageModalVisible(true);
    } else {
      setIsPaymentModalVisible(true);
    }
  };

  const handleQrPayment = () => {
    if (selectedPayments.size === 0) {
      setMessageText('Bạn chưa chọn thanh toán nào');
      setMessageModalVisible(true);
    } else {
      setIsQrPaymentModalVisible(true);
    }
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

   const calculateTotalPaid = (payments: Payment[]) => {
      const total = payments
        .filter((payment) => payment.trangThai === 'DONE')
        .reduce((sum, payment) => sum + (payment?.lopHoc?.khoaHoc?.giaTien || 0), 0);
      setTotalPaid(total);
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
        <Text style={styles.sectionTitle}>Tổng số tiền đã đóng: <Text style={{color:'red'}}>{totalPaid.toLocaleString()} VND</Text></Text>

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
        <View style={{ flexDirection: 'row' }}>
          <TouchableOpacity onPress={handlePayment} style={styles.payButton}>
            <Text style={styles.buttonText}>THANH TOÁN</Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={handleQrPayment} style={styles.payButtonQr}>
            <Text style={styles.buttonText}>THANH TOÁN QR</Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={handleCancelSelectedPayments} style={styles.cancelButton}>
        <Text style={styles.buttonText}>HỦY THANH TOÁN</Text>
      </TouchableOpacity>

        </View>

        <Modal
          visible={isPaymentModalVisible}
          transparent={true}
          animationType="slide"
          onRequestClose={() => setIsPaymentModalVisible(false)}
        >
          <View style={styles.modalOverlay}>
            <View style={styles.modalContainer}>
              <Image source={require('../../../image/efy.png') as any} style={styles.logo} />
              <Text style={styles.modalBankInfo}>Ngân hàng:<Text style={{ color: 'red', fontWeight: 'bold' }}> BIDV</Text></Text>
              <Text style={styles.modalBankInfo}>Số tài khoản: <Text style={{ color: 'red', fontWeight: 'bold' }}>92991133</Text> </Text>
              <Text style={styles.modalBankInfo}>Chủ tài khoản: <Text style={{ color: 'red', fontWeight: 'bold' }}>Trung tâm anh ngữ English For You</Text></Text>
              <Text style={styles.modalMessage}>Nội dung thanh toán:</Text>
              <Text style={styles.modalMessage}> <Text style={{ fontWeight: 'bold' }}>
                {Array.from(selectedPayments).map(id => {
                  const payment = payments.find(p => p.idTT === id);
                  return `${payment?.idTT}-${payment?.lopHoc?.khoaHoc?.tenKhoaHoc}-${payment?.lopHoc?.tenLopHoc}`;
                }).join(', ')}-{nameUser}
              </Text></Text>
              <Text style={styles.modalMessage}>Số tiền: <Text style={{ color: 'red', fontWeight: 'bold' }}>{totalAmount.toLocaleString()} VND</Text></Text>
              <TouchableOpacity onPress={() => setIsPaymentModalVisible(false)} style={styles.modalButton}>
                <Text style={styles.modalButtonText}>Đóng</Text>
              </TouchableOpacity>
            </View>
          </View>
        </Modal>

        <Modal
          visible={messageModalVisible}
          transparent={true}
          animationType="slide"
          onRequestClose={() => setMessageModalVisible(false)}
        >
          <View style={styles.modalOverlay}>
            <View style={styles.modalContainer}>
              <Text style={styles.modalMessage2}>{messageText}</Text>
              <TouchableOpacity onPress={() => setMessageModalVisible(false)} style={styles.modalButton}>
                <Text style={styles.modalButtonText}>Đóng</Text>
              </TouchableOpacity>
            </View>
          </View>
        </Modal>
        <Modal
          visible={isQrPaymentModalVisible}
          transparent={true}
          animationType="slide"
          onRequestClose={() => setIsQrPaymentModalVisible(false)}
        >
          <View style={styles.modalOverlay}>
            <View style={styles.modalContainer}>
              <Image source={require('../../../image/efy.png') as any} style={styles.logo} />
              <Text style={styles.modalMessage}>Nội dung thanh toán:</Text>
              <Text style={styles.modalMessage}><Text style={{ fontWeight: 'bold' }}>
                {Array.from(selectedPayments).map(id => {
                  const payment = payments.find(p => p.idTT === id);
                  return `${payment?.idTT}-${payment?.lopHoc?.khoaHoc?.tenKhoaHoc}-${payment?.lopHoc?.tenLopHoc}`;
                }).join(', ')}-{nameUser}
              </Text>
              </Text>
              <Text style={styles.modalMessage}>Số tiền: <Text style={{ color: 'red', fontWeight: 'bold' }}>{totalAmount.toLocaleString()} VND</Text></Text>
              <Image source={require('../../../image/qrCode.png') as any} style={styles.qrCodeImage} />
              <TouchableOpacity onPress={() => setIsQrPaymentModalVisible(false)} style={styles.modalButton}>
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
    borderRadius: 15,

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
    marginTop: 10,
    marginBottom: 10,
  },
  scrollContainer: {
    maxHeight: 300,
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
    backgroundColor: '#00405d',
    padding: 15,
    borderRadius: 5,
    marginTop: 20,
    alignItems: 'center',
    marginLeft: 20,
    width: 200
  },
  payButtonQr: {
    backgroundColor: 'green',
    padding: 15,
    borderRadius: 5,
    marginTop: 20,
    alignItems: 'center',
    marginLeft: 20,
    width: 200
  },
  cancelButton: {
    backgroundColor: '#f44336',
    padding: 15,
    borderRadius: 5,
    marginTop: 20,
    alignItems: 'center',
    marginLeft: 20,
    width: 200
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
    width: 450,
    backgroundColor: '#fff',
    padding: 20,
    borderRadius: 10,
  },
  modalMessage: {
    fontSize: 16,
    color: '#00405d',
    marginBottom: 20,
  },
  modalMessage2: {
    fontSize: 16,
    color: '#00405d',
    marginBottom: 20,
    textAlign: 'center'
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
  modalBankInfo: {
    fontSize: 16,
    color: '#333',
    marginBottom: 10,
  },
  logo: {
    width: 150,
    height: 65,
    marginBottom: 10,
    alignSelf: 'center'
  },
  qrCodeImage: {
    width: 150,
    height: 150,
    marginVertical: 20,
    alignSelf: 'center'

  },
});

export default PaymentScreen;

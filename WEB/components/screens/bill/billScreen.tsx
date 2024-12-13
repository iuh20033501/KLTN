import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, FlatList, ActivityIndicator, TouchableOpacity, ImageBackground, Alert, Modal, ScrollView } from 'react-native';
import http from '@/utils/http';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Entypo, AntDesign } from '@expo/vector-icons';
import ElectroBill from './electroBill';

type Bill = {
    idHoaDon: number;
    thanhTien: number;
    trangThai: boolean;
    ngayLap: string;
    nguoiLap: { hoTen: string };
};

type PaymentDetail = {
    idTT: number;
    tenKhoaHoc: string;
    tenLopHoc: string;
    soTien: number;
};

const BillScreen = ({ navigation, route }: { navigation: any; route: any }) => {
    const { idUser } = route.params;
    const [bills, setBills] = useState<Bill[]>([]);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');
    const [paymentDetails, setPaymentDetails] = useState<PaymentDetail[]>([]);
    const [modalVisible, setModalVisible] = useState(false);
    const [selectedBill, setSelectedBill] = useState<Bill | null>(null);
    const [electroBillModalVisible, setElectroBillModalVisible] = useState(false);
    const [selectedBillData, setSelectedBillData] = useState<any>({});
    useEffect(() => {
        fetchBills();
    }, []);

    const fetchBills = async () => {
        setLoading(true);
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                setErrorMessage('Token không tồn tại.');
                return;
            }

            const response = await http.get(`hoaDon/getByIdHocVien/${idUser}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            if (response.status === 200) {
                const processedData = response.data.map((item: any) => ({
                    idHoaDon: item.idHoaDon,
                    thanhTien: item.thanhTien,
                    trangThai: item.trangThai,
                    ngayLap: item.ngayLap,
                    nguoiLap: item.nguoiLap,
                }));
                setBills(processedData);
            } else {
                setErrorMessage('Lỗi khi lấy dữ liệu hóa đơn.');
            }
        } catch (error) {
            console.error('Error fetching bills:', error);
            setErrorMessage('Đã xảy ra lỗi khi lấy dữ liệu hóa đơn.');
        } finally {
            setLoading(false);
        }
    };

    const fetchPaymentDetails = async (idHoaDon: number) => {
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                setErrorMessage('Token không tồn tại.');
                return;
            }

            const response = await http.get(`thanhToan/findByIdHD/${idHoaDon}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            if (response.status === 200) {
                const sanitizedData = response.data.map((item: any) => ({
                    idTT: item.idTT || 0,
                    tenKhoaHoc: item.lopHoc?.khoaHoc?.tenKhoaHoc || 'Không xác định',
                    tenLopHoc: item.lopHoc?.tenLopHoc || 'Không xác định',
                    soTien: item.lopHoc?.khoaHoc?.giaTien || 0,
                }));
                setPaymentDetails(sanitizedData);
                setModalVisible(true);
            } else {
                Alert.alert('Lỗi', 'Không thể lấy dữ liệu chi tiết thanh toán.');
            }
        } catch (error) {
            console.error('Error fetching payment details:', error);
            Alert.alert('Lỗi', 'Đã xảy ra lỗi khi lấy dữ liệu chi tiết thanh toán.');
        }
    };



    const getBillStatus = (status: boolean): string => (status ? 'Đã thanh toán' : 'Chưa thanh toán');

    const handleViewHDDT = async (bill: Bill) => {
        try {
            const token = await AsyncStorage.getItem('accessToken');
            if (!token) {
                Alert.alert('Lỗi', 'Token không tồn tại.');
                return;
            }

            const response = await http.get(`thanhToan/findByIdHD/${bill.idHoaDon}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            if (response.status === 200) {
                const paymentDetails = response.data.map((item: any) => ({
                    idTT: item.idTT || 0,
                    tenKhoaHoc: item.lopHoc?.khoaHoc?.tenKhoaHoc || 'Không xác định',
                    tenLopHoc: item.lopHoc?.tenLopHoc || 'Không xác định',
                    soTien: item.lopHoc?.khoaHoc?.giaTien || 0,
                    nguoiThanhToan: item.nguoiThanhToan,
                }));

                setSelectedBillData({ bill, paymentDetails });
                setElectroBillModalVisible(true);
            } else {
                Alert.alert('Lỗi', 'Không thể lấy dữ liệu thanh toán.');
            }
        } catch (error) {
            console.error('Error fetching payment details:', error);
            Alert.alert('Lỗi', 'Đã xảy ra lỗi khi lấy dữ liệu thanh toán.');
        }
    };


    if (loading) {
        return <ActivityIndicator size="large" color="#00405d" style={styles.loading} />;
    }

    if (errorMessage) {
        return (
            <View style={styles.errorContainer}>
                <Text style={styles.errorMessage}>{errorMessage}</Text>
                <TouchableOpacity onPress={fetchBills} style={styles.retryButton}>
                    <Text style={styles.retryButtonText}>Thử lại</Text>
                </TouchableOpacity>
            </View>
        );
    }

    return (
        <ImageBackground source={require('../../../image/bglogin.png')} style={styles.background}>
            <View style={styles.container}>
                <TouchableOpacity style={styles.backButton} onPress={() => navigation.navigate('DashboardScreen')}>
                    <Text style={styles.backButtonText}>Quay về</Text>
                </TouchableOpacity>

                <Text style={styles.title}>Danh sách hóa đơn</Text>
                <View style={styles.tableHeader}>
                    <Text style={[styles.tableHeaderText, styles.columnSTT]}>STT</Text>
                    <Text style={[styles.tableHeaderText, styles.columnMaHD]}>Mã hóa đơn</Text>
                    <Text style={[styles.tableHeaderText, styles.columnSoTien]}>Số tiền</Text>
                    <Text style={[styles.tableHeaderText, styles.columnNgayLap]}>Ngày lập</Text>
                    <Text style={[styles.tableHeaderText, styles.columnDonViThu]}>Đơn vị thu</Text>
                    <Text style={[styles.tableHeaderText, styles.columnHDDT]}>Chi tiết</Text>
                </View>
                <FlatList
                    data={bills}
                    keyExtractor={(item) => item.idHoaDon.toString()}
                    renderItem={({ item, index }) => (
                        <View style={styles.billItem}>
                            <Text style={[styles.billText, styles.columnSTT]}>{index + 1}</Text>
                            <Text style={[styles.billText, styles.columnMaHD]}>{item.idHoaDon}</Text>
                            <Text style={[styles.billText, styles.columnSoTien]}>{item.thanhTien.toLocaleString()} VND</Text>
                            <Text style={[styles.billText, styles.columnNgayLap]}>{new Date(item.ngayLap).toLocaleDateString()}</Text>
                            <Text style={[styles.billText, styles.columnDonViThu]}>{item.nguoiLap.hoTen}</Text>
                            <TouchableOpacity
                                style={[styles.actionButton, styles.columnHDDT]}
                                onPress={() => handleViewHDDT(item)}
                            >
                                <Entypo name="magnifying-glass" size={20} color="black" />
                            </TouchableOpacity>
                        </View>
                    )}
                    ListEmptyComponent={null}  // Chỉ cần bỏ qua ListEmptyComponent
                />

                {selectedBill && (
                    <Modal animationType="slide" transparent={true} visible={modalVisible} onRequestClose={() => setModalVisible(false)}>
                        <View style={styles.modalOverlay}>
                            <View style={styles.modalContainer}>
                                <Text style={styles.modalTitle}>Chi tiết hóa đơn</Text>
                                <Text style={styles.modalSubTitle}>Hóa đơn: {selectedBill.idHoaDon}</Text>
                                <ScrollView style={styles.modalScroll}>
                                    <View style={styles.tableHeader}>
                                        <Text style={[styles.tableHeaderText, styles.columnSTT]}>STT</Text>
                                        <Text style={[styles.tableHeaderText, styles.columnMaHD]}>Khóa học</Text>
                                        <Text style={[styles.tableHeaderText, styles.columnSoTien]}>Lớp học</Text>
                                        <Text style={[styles.tableHeaderText, styles.columnSoTien]}>Số tiền</Text>
                                    </View>
                                    {paymentDetails.map((payment, index) => (
                                        <View style={styles.billItem} key={payment.idTT}>
                                            <Text style={[styles.billText, styles.columnSTT]}>{index + 1}</Text>
                                            <Text style={[styles.billText, styles.columnMaHD]}>{payment.tenKhoaHoc}</Text>
                                            <Text style={[styles.billText, styles.columnSoTien]}>{payment.tenLopHoc}</Text>
                                            <Text style={[styles.billText, styles.columnSoTien]}>{payment.soTien.toLocaleString()} VND</Text>
                                        </View>
                                    ))}
                                </ScrollView>
                                <TouchableOpacity style={styles.modalCloseButton} onPress={() => setModalVisible(false)}>
                                    <Text style={styles.modalCloseButtonText}>Đóng</Text>
                                </TouchableOpacity>
                            </View>
                        </View>
                    </Modal>
                )}
                <Modal
                    visible={electroBillModalVisible}
                    animationType="slide"
                    onRequestClose={() => setElectroBillModalVisible(false)}
                >
                    <ScrollView>
                        {selectedBillData.bill && selectedBillData.paymentDetails ? (
                            <ElectroBill
                                bill={selectedBillData.bill}
                                paymentDetails={selectedBillData.paymentDetails}
                            />
                        ) : (
                            <ActivityIndicator size="large" color="#00405d" style={styles.loading} />
                        )}
                        <TouchableOpacity
                            style={styles.closeButton}
                            onPress={() => setElectroBillModalVisible(false)}
                        >
                            <Text style={styles.closeButtonText}>Đóng</Text>
                        </TouchableOpacity>
                    </ScrollView>
                </Modal>
            </View>
        </ImageBackground>
    );
};

const styles = StyleSheet.create({
    background: {
        flex: 1,
        paddingHorizontal: 400,
        height: 990,
    },
    container: {
        flex: 1,
        padding: 10,
        backgroundColor: 'rgba(255, 255, 255, 0.9)',
        borderRadius: 10,
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
        fontSize: 22,
        fontWeight: 'bold',
        color: '#00405d',
        textAlign: 'center',
        marginBottom: 20,
    },
    tableHeader: {
        flexDirection: 'row',
        paddingVertical: 10,
        backgroundColor: '#e8e8e8',
        borderRadius: 5,
    },
    tableHeaderText: {
        textAlign: 'center',
        fontWeight: 'bold',
        color: '#333',
    },
    billItem: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingVertical: 10,
        borderBottomWidth: 1,
        borderBottomColor: '#ddd',
    },
    billText: {
        textAlign: 'center',
        fontSize: 14,
        color: '#333',
    },
    actionButton: {
        alignItems: 'center',
        justifyContent: 'center',
    },
    columnSTT: { flex: 1 },
    columnMaHD: { flex: 2 },
    columnSoTien: { flex: 2 },
    columnTrangThai: { flex: 2 },
    columnNgayLap: { flex: 2 },
    columnDonViThu: { flex: 2 },
    columnHDDT: { flex: 1 },
    columnChiTiet: { flex: 1 },

    noBillsText: {
        fontSize: 16,
        color: '#333',
        textAlign: 'center',
        marginVertical: 20,
    },
    loading: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    errorContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    errorMessage: {
        fontSize: 16,
        color: 'red',
        marginBottom: 10,
        textAlign: 'center',
    },
    retryButton: {
        backgroundColor: '#00405d',
        padding: 10,
        borderRadius: 5,
        alignItems: 'center',
        justifyContent: 'center',
    },
    retryButtonText: {
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
        width: '40%',
        backgroundColor: 'white',
        borderRadius: 10,
        padding: 20,
    },
    modalTitle: {
        fontSize: 20,
        fontWeight: 'bold',
        marginBottom: 10,
    },
    modalSubTitle: {
        fontSize: 16,
        marginBottom: 20,
    },
    modalScroll: {
        maxHeight: 300,
        marginBottom: 20,
    },
    modalCloseButton: {
        backgroundColor: '#00405d',
        padding: 10,
        borderRadius: 5,
        alignItems: 'center',
    },
    modalCloseButtonText: {
        color: 'white',
        fontWeight: 'bold',
    },
    closeButton: {
        padding: 10,
        backgroundColor: '#00405d',
        borderRadius: 5,
        alignItems: 'center',
        marginTop: 10,
        width: '50%',
        alignSelf: 'center'

    },
    closeButtonText: {
        color: '#fff',
        fontWeight: 'bold',
    },
});

export default BillScreen;

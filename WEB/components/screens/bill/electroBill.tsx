import React from 'react';
import { View, Text, StyleSheet, ScrollView, Image } from 'react-native';

const ElectroBill = ({ bill, paymentDetails }: { bill: any; paymentDetails: any }) => {
    console.log(paymentDetails);
    const payerInfo = paymentDetails && paymentDetails.length > 0 ? paymentDetails[0].nguoiThanhToan : {};

    return (
        <ScrollView contentContainerStyle={styles.container}>
            <View style={styles.logoContainer}>
                <Image source={require('../../../image/efy.png')} style={styles.logo} />
            </View>

            <Text style={styles.header}>HÓA ĐƠN MUA BÁN</Text>
            <Text style={styles.subHeader}>Bản thể hiện của hóa đơn điện tử</Text>

            <View style={styles.infoSection}>
                <Text style={styles.title}>Thông tin hóa đơn</Text>
                <View style={styles.row}>
                    <Text style={styles.label}>Mã hóa đơn:</Text>
                    <Text style={styles.value}>{bill.idHoaDon}</Text>
                </View>
                <View style={styles.row}>
                    <Text style={styles.label}>Ngày lập:</Text>
                    <Text style={styles.value}>{new Date(bill.ngayLap).toLocaleDateString()}</Text>
                </View>
                <View style={styles.row}>
                    <Text style={styles.label}>Người lập:</Text>
                    <Text style={styles.value}>{bill.nguoiLap.hoTen}</Text>
                </View>
                <View style={styles.row}>
                    <Text style={styles.label}>Tổng tiền:</Text>
                    <Text style={styles.value}>{bill.thanhTien.toLocaleString()} VND</Text>
                </View>
            </View>

            <View style={styles.infoSection}>
                <Text style={styles.title}>Thông tin người thanh toán</Text>
                <View style={styles.row}>
                    <Text style={styles.label}>Họ tên:</Text>
                    <Text style={styles.value}>{payerInfo.hoTen || 'Không xác định'}</Text>
                </View>
                <View style={styles.row}>
                    <Text style={styles.label}>Số điện thoại:</Text>
                    <Text style={styles.value}>{payerInfo.sdt || 'Không xác định'}</Text>
                </View>
                <View style={styles.row}>
                    <Text style={styles.label}>Email:</Text>
                    <Text style={styles.value}>{payerInfo.email || 'Không xác định'}</Text>
                </View>
            </View>

            <View style={styles.table}>
                <View style={[styles.tableRow, styles.tableHeader]}>
                    <Text style={[styles.tableCell, styles.center]}>STT</Text>
                    <Text style={styles.tableCell}>Khóa học</Text>
                    <Text style={[styles.tableCell, styles.center]}>Lớp học</Text>
                    <Text style={[styles.tableCell, styles.right]}>Số tiền</Text>
                </View>
                {paymentDetails.map((payment: any, index: number) => (
                    <View style={styles.tableRow} key={payment.idTT || index}>
                        <Text style={[styles.tableCell, styles.center]}>{index + 1}</Text>
                        <Text style={styles.tableCell}>{payment.tenKhoaHoc || 'Không xác định'}</Text>
                        <Text style={[styles.tableCell, styles.center]}>{payment.tenLopHoc || 'Không xác định'}</Text>
                        <Text style={[styles.tableCell, styles.right]}>
                            {(payment.soTien || 0).toLocaleString()} VND
                        </Text>
                    </View>
                ))}
            </View>

            <Text style={styles.footerNote}>
                Số tiền viết bằng chữ: {convertNumberToWords(bill.thanhTien)} đồng.
            </Text>

            <View style={styles.signatureSection}>
                <View style={styles.signatureBlock}>
                    <Text style={styles.signature}>Người mua hàng</Text>
                    <Text style={styles.signatureName}>{payerInfo.hoTen || 'Không xác định'}</Text>
                </View>
                <View style={styles.signatureBlock}>
                    <Text style={styles.signature}>Người bán hàng</Text>
                    <Text style={styles.signatureName}>{bill.nguoiLap.hoTen || 'Không xác định'}</Text>
                </View>
            </View>
        </ScrollView>
    );
};

const convertNumberToWords = (number: number): string => {
    const units = ['', 'một', 'hai', 'ba', 'bốn', 'năm', 'sáu', 'bảy', 'tám', 'chín'];
    const scales = ['', 'nghìn', 'triệu', 'tỷ'];

    const processThreeDigits = (num: number): string => {
        let result = '';
        const hundreds = Math.floor(num / 100);
        const tens = Math.floor((num % 100) / 10);
        const ones = num % 10;

        if (hundreds > 0) result += `${units[hundreds]} trăm `;
        if (tens > 0) {
            if (tens === 1) result += 'mười ';
            else result += `${units[tens]} mươi `;
        }
        if (ones > 0) {
            if (tens > 0 && ones === 5) result += 'lăm ';
            else result += `${units[ones]} `;
        }

        return result.trim();
    };

    let result = '';
    let scaleIndex = 0;

    while (number > 0) {
        const threeDigits = number % 1000;
        if (threeDigits > 0) {
            const scale = scales[scaleIndex];
            result = `${processThreeDigits(threeDigits)} ${scale} ${result}`.trim();
        }
        number = Math.floor(number / 1000);
        scaleIndex++;
    }

    return result.charAt(0).toUpperCase() + result.slice(1) + ' đồng';
};

const styles = StyleSheet.create({
    container: {
        margin: 20,
        padding: 20,
        backgroundColor: '#f5f5f5',
        borderRadius: 10,
        width: '50%',
        alignSelf: 'center',
        elevation: 5,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.2,
        shadowRadius: 3,
    },
    logoContainer: {
        position: 'absolute',
        top: -30,
        left: 10,
    },
    logo: {
        width: 150,
        height: 150,
        resizeMode: 'contain',
    },
    header: {
        fontSize: 24,
        fontWeight: 'bold',
        textAlign: 'center',
    },
    subHeader: {
        fontSize: 18,
        textAlign: 'center',
        marginBottom: 20,
    },
    infoSection: {
        marginVertical: 10,
    },
    title: {
        fontWeight: 'bold',
        marginBottom: 5,
    },
    row: {
        flexDirection: 'row',
        marginBottom: 5,
    },
    label: {
        flex: 1,
        fontWeight: 'bold',
    },
    value: {
        flex: 2,
    },
    table: {
        borderWidth: 1,
        borderColor: '#000',
        marginVertical: 10,
    },
    tableRow: {
        flexDirection: 'row',
    },
    tableHeader: {
        backgroundColor: '#ddd',
    },
    tableCell: {
        flex: 1,
        borderWidth: 1,
        borderColor: '#000',
        padding: 5,
    },
    center: {
        textAlign: 'center',
    },
    right: {
        textAlign: 'right',
    },
    footerNote: {
        marginVertical: 10,
        fontStyle: 'italic',
    },
    signatureSection: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginTop: 20,
    },
    signatureBlock: {
        textAlign: 'center',
        width: '40%',
    },
    signature: {
        fontWeight: 'bold',
        textAlign: 'center',
    },
    signatureName: {
        marginTop: 10,
        fontStyle: 'italic',
        textAlign: 'center',
    },
});

export default ElectroBill;
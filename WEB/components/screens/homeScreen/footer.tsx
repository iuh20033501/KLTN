import React from 'react';
import { View, Text, StyleSheet, Linking, ScrollView } from 'react-native';
import { FontAwesome } from '@expo/vector-icons'; 

const FooterComponent = () => {
  return (
    <View style={styles.footerContainer}>
      <ScrollView horizontal showsHorizontalScrollIndicator={false}>
     
      

        <View style={styles.column}>
        <Text style={styles.columnTitle}>VỀ English For You</Text>
          {['Khóa học', 'Chính sách bảo mật thông tin', 'Chính sách dịch vụ'].map((item, index) => (
            <Text key={index} style={styles.linkText}>
              {item}
            </Text>
          ))}
          <Text style={styles.copyright}>©English for You | All rights reserved</Text>
        </View>

        <View style={styles.column2}>
          {['Quy định và hình thức thanh toán','Nội quy học viên','Tuyển dụng','Giới thiệu bạn bè'].map((item, index) => (
            <Text key={index} style={styles.linkText}>
              {item}
            </Text>
          ))}
        </View>

        <View style={styles.column}>
          <Text style={styles.columnTitle}>THÔNG TIN CÔNG TY</Text>
          <Text style={styles.linkText}>Tên công ty: Công ty cổ phần Giáo Dục English for You</Text>
          <Text style={styles.linkText}>Mã số thuế: 9090909090</Text>
          <Text style={styles.linkText}>Văn phòng HCM: 12 Nguyễn Văn Bảo, Phường 4, Quận Gò Vấp, TP. Hồ Chí Minh</Text>
        </View>

        <View style={styles.column}>
          <Text style={styles.columnTitle}>LIÊN HỆ</Text>
          <View style={styles.contactRow}>
            <FontAwesome name="envelope" size={20} color="white" />
            <Text
              style={styles.linkText}
              onPress={() => Linking.openURL('mailto:hungro8102@gmail.com')}
            >
                 efycenter@gmail.com
            </Text>
          </View>
          <View style={styles.contactRow}>
            <FontAwesome name="phone" size={20} color="white" />
            <Text style={styles.linkText}>1800 9999</Text>
          </View>
          <Text style={styles.linkText}>Vào lớp học EFY</Text>
        </View>
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  footerContainer: {
    backgroundColor: '#00405d',
    padding: 20,
    alignItems: 'center', 
  },
  column: {
    flex: 1,
    marginRight: 20,
    alignItems: 'center', 
  },
  column2: {
    flex: 1,
    marginRight: 20,
    marginTop: 30,
    alignItems: 'center', 
  },
  columnTitle: {
    color: '#f0a500',
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 10,
    textAlign: 'center', 
  },
  linkText: {
    color: 'white',
    fontSize: 14,
    marginBottom: 5,
    textAlign: 'center', 
  },
  contactRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center', 
    marginBottom: 5,
  },
  copyright: {
    marginTop: 10,
    color: '#ccc',
    fontSize: 12,
    textAlign: 'center', 
  },
});
export default FooterComponent;

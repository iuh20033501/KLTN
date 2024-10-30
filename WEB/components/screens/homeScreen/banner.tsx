import React from 'react';
import { View, Text, Image, TouchableOpacity, StyleSheet, Dimensions } from 'react-native';

const { width, height } = Dimensions.get('window'); 

const BannerComponent = ({navigation}: {navigation: any}) => {
  return (
    <View style={styles.container}>
      <Image
        source={require('../../../image/banner.png')} 
        style={styles.backgroundImage}
      />
      <View style={styles.contentContainer}>
        <View style={styles.textContainer}>
          <Text style={styles.title}>
            <Text style={styles.highlight}>
            Bắt đầu học tiếng Anh</Text>
          </Text>
          <Text style={styles.subTitle}>Bắt đầu tại EFY!</Text>
          <Text style={styles.description}>
            Cơ hội luyện tập tiếng Anh giao tiếp với người bản xứ, tăng cường khả năng nói
          </Text>
          <Text style={styles.description}>
            và phản xạ tự nhiên. Học trực tuyến thỏa thích lên đến 16 giờ mỗi ngày, không
            </Text>
            <Text style={styles.description}>
            không giới hạn lớp luyện nghe. Đặc biệt, miễn phí kiểm tra trình độ đầu vào.
            </Text>  
          <TouchableOpacity style={styles.registerButton}
           onPress={() => navigation.navigate('SignUpScreen')}>
            <Text style={styles.registerButtonText}>ĐĂNG KÝ NGAY</Text>
          </TouchableOpacity>
        </View>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    position: 'relative',
    width: '100%',
    height: height * 0.8, 
    backgroundColor: '#fff'
  },
  backgroundImage: {
    width: '100%',
    height: '100%',
    resizeMode: 'cover', 
  },
  contentContainer: {
    position: 'absolute', 
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
    paddingVertical: 40,
  },
  textContainer: {
    flex: 1,
    paddingRight: 20,
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    color: 'white',
    marginBottom: 10,
  },
  highlight: {
    marginLeft:80,
    color: '#FF0000',
    fontSize:40
  },
  subTitle: {
    marginLeft:80,
    fontSize: 40,
    fontWeight: 'bold',
    color: '#00405d',
    marginBottom: 20,
  },
  description: {
    marginLeft:80,
    fontSize: 20,
    color: '#00405d',
    lineHeight: 24,
    marginBottom: 10,
  },
  registerButton: {
    marginTop:50,
    marginLeft:200,
    backgroundColor: '#FF0000',
    paddingVertical: 12,
    paddingHorizontal: 25,
    borderRadius: 8,
    width:400,
    height:60
  },
  registerButtonText: {
    color: 'white',
    fontWeight: 'bold',
    fontSize: 30,
    textAlign: 'center',
    marginTop:-3
  },
  studentImage: {
    width: 180,
    height: 250,
    resizeMode: 'contain',
  },
});

export default BannerComponent;

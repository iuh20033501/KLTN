// screens/HomeScreen.js

import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ImageBackground } from 'react-native';
import { useNavigation } from '@react-navigation/native';

const HomeScreen = ({navigation}: {navigation: any}) => {
  
  const englishImage = require('../../../image/background/mainBg.png');
  return (
    <ImageBackground
      source={englishImage}
    style={{width:'100%',height:"100%"}}
    >
      <View style={styles.container}>
        <Text style={{ display: 'flex', fontSize: 16 }}>Chinh phục ngôn ngữ, khám phá tương lai</Text>
        <Text style={{ display: 'flex', marginTop: 5 }}>Học tiếng anh mỗi ngày cùng EFY!</Text>
        <View style={{ marginTop: -100 }}>
          <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('CreateAccount')}>
            <Text style={{ color: 'white', fontSize: 26, fontWeight: 'bold' }}>Bắt đầu</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.button2} onPress={() => navigation.navigate('LoginScreen')}>
            <Text style={{ color: '#000022', fontSize: 26, fontWeight: 'bold' }}>Tôi đã có tài khoản</Text>
          </TouchableOpacity>
        </View>
      </View>
    </ImageBackground>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  button: {
    width: 300,
    height: 45,
    backgroundColor: "#000022",
    borderRadius: 15,
    justifyContent: 'center',
    alignItems: 'center',
    top: 200,
    marginLeft: -10,
  },
  button2: {
    width: 300,
    height: 45,
    backgroundColor: "#FFC125",
    borderRadius: 15,
    justifyContent: 'center',
    alignItems: 'center',
    top: 200,
    marginTop: 20,
    marginLeft: -10,
  },
});

export default HomeScreen;

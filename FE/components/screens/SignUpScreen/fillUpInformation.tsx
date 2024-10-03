import React, { useState } from 'react';
import { StyleSheet, Text, View, TextInput, TouchableOpacity, ScrollView, Alert, ImageBackground } from 'react-native';
import { Picker } from '@react-native-picker/picker'; // Nhớ import Picker nếu cần
import LinearGradient from 'react-native-linear-gradient';

export default function FillUpInformation({navigation}: {navigation: any}) {
  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [age, setAge] = useState('');
  const [gmail, setGmail] = useState('');
  const backgroundImg = require("../../../image/background/bg7.png"); // Path to your image

  return (
    <ImageBackground 
    source={backgroundImg} 
    style={styles.backgroundImage}
    resizeMode="cover"
>
    <ScrollView contentContainerStyle={styles.container}>
      <View style={styles.innerContainer}>
        <Text style={styles.title}>Nhập thông tin của bạn</Text>
        <TextInput
          style={styles.input}
          placeholder="Số điện thoại"
          placeholderTextColor="#A8A8A8"
          keyboardType="phone-pad"
          value={phone}
          onChangeText={setPhone}
        />

        <TextInput
          style={styles.input}
          placeholder="Họ tên học viên"
          placeholderTextColor="#A8A8A8"
          value={name}
          onChangeText={setName}
        />

        <View style={styles.pickerContainer}>
          <Picker
            selectedValue={age}
            onValueChange={(itemValue) => setAge(itemValue)}
            style={styles.picker}
          >
            <Picker.Item label="Độ tuổi của bạn" value="" />
            {Array.from({ length: 50 }, (_, i) => 11 + i).map(age => (
              <Picker.Item key={age} label={`${age}`} value={`${age}`} />
            ))}
          </Picker>
        </View>
        <TextInput
          style={styles.input}
          placeholder="Nhập gmail của bạn"
          placeholderTextColor="#A8A8A8"
          value={gmail}
          onChangeText={setGmail}
        />
        <Text style={styles.terms}>
          Bằng việc tiếp tục, bạn đã chấp nhận và đồng ý với những
        </Text>
        <TouchableOpacity>
          <Text style={styles.link}>điều kiện và điều khoản sử dụng ứng dụng.</Text>
          </TouchableOpacity>
        <TouchableOpacity style={styles.button}
         onPress={() => navigation.navigate('Authentication')}>
          <Text style={styles.buttonText}>Tiếp tục</Text>
        </TouchableOpacity>
       
        
       
      </View>
    </ScrollView>
    </ImageBackground>
  );
};

const styles = StyleSheet.create({
  backgroundImage: {
    flex: 1,
    justifyContent: 'center',
    width: "100%",
    height: "100%",
  },
  container: {
    flexGrow: 1,
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    height: '100%'
  },
  innerContainer: {
    alignItems: 'center',
    width: '100%',
    marginTop: -100
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 50,
  },
  input: {
    width: '77%',
    height: 50,
    borderColor: '#DADADA',
    borderWidth: 1,
    borderRadius: 10,
    paddingLeft: 15,
    marginBottom: 15,
    backgroundColor: '#FFFFFF',
    fontSize: 18,
  },
  pickerContainer: {
    width: '77%',
    borderRadius: 10,
    marginBottom: 15,
    borderColor: '#DADADA',
    backgroundColor: '#FFFFFF',
    borderWidth: 1,
  },
  picker: {
    height: 50,
    width: '100%',
    borderRadius: 15,
    paddingLeft: 10,
    fontSize: 18

  },
  button: {
    width: '77%',
    backgroundColor: '#00bf63',
    padding: 15,
    borderRadius: 20,
    alignItems: 'center',
    marginTop: 30,
  },
  buttonText: {
    fontSize: 18,
    color: '#333333',
    fontWeight: 'bold'
  },
  terms: {
    marginTop: 15,
    fontSize: 12,
    color: '#666666',
  },
  link: {
    color: '#007bff',
    textDecorationLine: 'underline',
    fontWeight: 'bold',
    marginTop: 1,
    fontSize:12,
  },
  link2: {
    color: '#FFC125',
    fontWeight: 'bold',
    marginTop: 18,
    fontSize:14,
  },
  footerNote: {
    marginTop: 20,
    fontSize: 12,
    textAlign: 'center',
    color: '#666666',
  },
});

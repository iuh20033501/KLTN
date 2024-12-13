import React, { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import ChatModal from './chat';


const HeaderComponent = ({navigation}: {navigation: any}) =>  {
   const [isChatModalVisible, setIsChatModalVisible] = useState(false);
    const openChat = () => setIsChatModalVisible(true);
    const closeChat = () => setIsChatModalVisible(false);
  return (
    <View style={styles.headerContainer}>
      <Text style={styles.logoText}>English for <Text style={{color:'#FF0000'}}>You</Text></Text>
      <TouchableOpacity style={{marginLeft:1300,paddingHorizontal: 10,}} onPress={openChat}>
      <Text style={styles.navText} >Bạn cần tư vấn?</Text>
      </TouchableOpacity>
      <ChatModal isVisible={isChatModalVisible} onClose={closeChat} />
      <TouchableOpacity style={styles.navItem}
      onPress={() => navigation.navigate('LoginScreen')}>
        <Text style={styles.navText}>Đăng nhập</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  headerContainer: {
    backgroundColor: '#00405d',
    padding: 20,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  logoText: {
    color: '#fff',
    fontSize: 30,
    fontWeight: 'bold',
    marginLeft:100
  },
  navItem: {
    paddingHorizontal: 10,
  },
  navText: {
    color: '#fff',
    fontSize: 16,
  },
});

export default HeaderComponent;

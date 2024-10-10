import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TextInput, Image, TouchableOpacity, Platform, Modal, ScrollView, Alert } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import DateTimePicker from '@react-native-community/datetimepicker';
import http from "@/utils/http";

export default function DetailProfileScreen({ navigation }: { navigation: any }) {
  const [phone, setPhone] = useState('');
  const [email, setEmail] = useState('');
  const [birthday, setBirthday] = useState('');
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [gender, setGender] = useState('');
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [isModalVisible, setModalVisible] = useState(false);
  const [selectedAvatar, setSelectedAvatar] = useState(require('../../../image/avatar/1.png'));

  const avatars = [
    require('../../../image/avatar/1.png'),
    require('../../../image/avatar/2.png'),
    require('../../../image/avatar/3.png'),
    require('../../../image/avatar/4.png'),
    require('../../../image/avatar/5.png'),
    require('../../../image/avatar/6.png'),
    require('../../../image/avatar/7.png'),
    require('../../../image/avatar/8.png'),
  ];

  const validPhonePrefixes = ['032', '033', '034', '035', '036', '037', '038', '039', '081', '082', '083', '084', '085', '088', '070', '076', '077', '078', '079', '052', '056', '058', '092', '059', '099'];
  const gmailRegex = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;

  const validatePhone = (phone: string) => {
    const phonePrefix = phone.slice(0, 3);
    return validPhonePrefixes.includes(phonePrefix) && phone.length === 10;
  };

  const formatDateForServer = (dateString: string): string | null => {
    if (!dateString) return null;
    const [day, month, year] = dateString.split('/');
    return `${year}-${month}-${day}`;
  };
  
  
  const validateForm = () => {
    if (!phone.trim()) {
      Alert.alert('Lỗi nhập', 'Số điện thoại không được để trống');
      return false;
    } else if (!validatePhone(phone)) {
      Alert.alert('Lỗi nhập', 'Đầu số không hợp lệ');
      return false;
    }

    if (!email.trim()) {
      Alert.alert('Lỗi nhập', 'Email không được để trống');
      return false;
    } else if (!gmailRegex.test(email)) {
      Alert.alert('Lỗi nhập', 'Vui lòng nhập đúng định dạng Gmail (@gmail.com)');
      return false;
    }

    if (!birthday.trim()) {
      Alert.alert('Lỗi nhập', 'Ngày sinh không được để trống');
      return false;
    }

    if (!gender.trim()) {
      Alert.alert('Lỗi nhập', 'Giới tính không được để trống');
      return false;
    }

    return true;
  };

  const handleUpdate = async () => {
    if (!validateForm()) {
      return;
    }

    try {
      const genderValue = gender === 'Nam' ? true : false;
      const formattedBirthday = formatDateForServer(birthday);
      const token = await AsyncStorage.getItem('accessToken');
      const response = await http.put(`hocvien/update/${user.u.idUser}`, {
        idUser: user.u.idUser,
        hoTen: user.u.hoTen,
        sdt: phone,
        email:email,
        ngaySinh: formattedBirthday,
        gioiTinh: genderValue,
        image: '1', 
        
      }, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.status === 200) {
        Alert.alert('Thành công', 'Cập nhật thông tin thành công');
        navigation.goBack();
      } else {
        Alert.alert('Lỗi', 'Cập nhật thông tin thất bại');
      }
    } catch (error) {
      console.error('Có lỗi xảy ra:', error);
      Alert.alert('Lỗi', 'Có lỗi xảy ra khi cập nhật thông tin');
    }
  };

  const getUserInfo = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (token) {
        const response = await http.get('auth/profile', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.status === 200) {
          const userData = response.data;
          console.log(userData)
          setPhone(userData.u.sdt || '');
          setEmail(userData.u.email || '');
          setBirthday(formatDate(userData.u.ngaySinh || ''));
          setGender(userData.u.gioiTinh === true ? 'Nam' : 'Nữ');
          setUser(userData);
        } else {
          console.error('Lấy thông tin người dùng thất bại.');
        }
      } else {
        console.error('Token không tồn tại');
      }
    } catch (error) {
      console.error('Có lỗi xảy ra khi lấy thông tin người dùng:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    getUserInfo();
  }, []);

  const formatDate = (dateString: string) => {
    if (!dateString) return 'N/A';
    const dateParts = dateString.split('-');
    if (dateParts.length !== 3) return dateString;

    const [year, month, day] = dateParts;
    return `${day}/${month}/${year}`;
  };

  const handleDateChange = (event: any, selectedDate?: Date) => {
    const currentDate = selectedDate || new Date();
    setShowDatePicker(Platform.OS === 'ios');
    setSelectedDate(currentDate);
    setBirthday(formatDate(currentDate.toISOString().split('T')[0]));
  };

  const handleAvatarSelect = (avatar: any) => {
    setSelectedAvatar(avatar);
    setModalVisible(false);
  };

  const renderAvatarRows = () => {
    const rows = [];
    for (let i = 0; i < avatars.length; i += 2) {
      rows.push(
        <View key={i} style={styles.avatarRow}>
          <TouchableOpacity onPress={() => handleAvatarSelect(avatars[i])}>
            <Image source={avatars[i]} style={styles.modalAvatar} />
          </TouchableOpacity>
          {avatars[i + 1] && (
            <TouchableOpacity onPress={() => handleAvatarSelect(avatars[i + 1])}>
              <Image source={avatars[i + 1]} style={styles.modalAvatar} />
            </TouchableOpacity>
          )}
        </View>
      );
    }
    return rows;
  };
  return (
    <View style={styles.container}>
      <View style={{ flexDirection: 'row' }}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Icon name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
        <Text style={styles.title}>Thay đổi thông tin cá nhân</Text>
      </View>

      <View style={styles.avatarSection}>
        <Image source={selectedAvatar} style={styles.avatar} />
        <TouchableOpacity onPress={() => setModalVisible(true)}>
          <Text style={styles.editAvatarText}>Đổi ảnh đại diện</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.formSection}>
        <TextInput
          style={styles.input}
          value={phone}
          onChangeText={setPhone}
          placeholder="Số điện thoại"
        />

        <TextInput
          style={styles.input}
          value={email}
          onChangeText={setEmail}
          placeholder="Email"
        />

        <TouchableOpacity onPress={() => setShowDatePicker(true)} style={styles.input}>
          <Text style={{ color: birthday ? '#000' : '#A8A8A8', fontSize: 18, marginTop: 10 }}>
            {birthday || 'Chọn ngày sinh'}
          </Text>
        </TouchableOpacity>

        {showDatePicker && (
          <DateTimePicker
            value={selectedDate}
            mode="date"
            display={Platform.OS === 'ios' ? 'spinner' : 'default'}
            onChange={handleDateChange}
            maximumDate={new Date()}
          />
        )}

        <View style={styles.inputRow}>
          <Text style={styles.label}>Giới tính</Text>
          <TouchableOpacity onPress={() => setGender(gender === 'Nam' ? 'Nữ' : 'Nam')}>
            <Text style={styles.dropdown}>{gender}</Text>
          </TouchableOpacity>
        </View>

        <View style={styles.buttonContainer}>
          <TouchableOpacity style={styles.button} onPress={handleUpdate}>
            <Text style={{ color: 'black', fontSize: 26, fontWeight: 'bold' }}>Cập nhật</Text>
          </TouchableOpacity>
        </View>
      </View>

      <Modal
        visible={isModalVisible}
        animationType="slide"
        transparent={true}
        onRequestClose={() => setModalVisible(false)}
      >
        <View style={styles.modalContainer}>
          <View style={styles.modalContent}>
            <Text style={styles.modalTitle}>Chọn Avatar</Text>
            <ScrollView>
              {renderAvatarRows()}
            </ScrollView>
            <TouchableOpacity onPress={() => setModalVisible(false)} style={styles.closeButton}>
              <Text style={styles.closeButtonText}>Đóng</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
    marginLeft: 10,
  },
  avatarSection: {
    marginTop: 30,
    alignItems: 'center',
    marginBottom: 20,
  },
  buttonContainer: {
    marginTop: 50,
    alignItems: 'center',
  },
  button: {
    width: 300,
    height: 45,
    backgroundColor: '#00bf63',
    borderRadius: 15,
    justifyContent: 'center',
    alignItems: 'center',
  },
  avatar: {
    width: 150,
    height: 150,
    borderRadius: 40,
    marginBottom: 10,
  },
  editAvatarText: {
    color: '#1DA1F2',
    fontSize: 16,
  },
  formSection: {
    marginBottom: 20,
  },
  input: {
    borderRadius: 10,
    padding: 10,
    marginBottom: 10,
    borderWidth: 1,
    borderColor: '#ccc',
    fontSize: 18,
  },
  inputRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  label: {
    fontSize: 16,
  },
  dropdown: {
    fontSize: 16,
    borderRadius: 10,
    padding: 10,
    borderWidth: 1,
  },
  avatarRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginVertical: 10,
  },
  modalAvatar: {
    width: 120,
    height: 120,
    marginLeft: 15,
    left: -10,
  },
  modalContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },
  modalContent: {
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 20,
    alignItems: 'center',
    width: '90%',
    height: '70%',
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
    color: '#00bf63',
  },
  closeButton: {
    marginTop: 20,
    padding: 10,
    backgroundColor: '#00bf63',
    borderRadius: 5,
  },
  closeButtonText: {
    color: '#fff',
    fontWeight: 'bold',
  },
});

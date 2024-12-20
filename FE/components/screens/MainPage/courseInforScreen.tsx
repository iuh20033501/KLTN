import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, ScrollView, TouchableOpacity } from 'react-native';
import { FontAwesome, Ionicons, MaterialIcons, Entypo, Feather, AntDesign } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import http from '@/utils/http';

export default function CourseInfomationScreen({ navigation }: { navigation: any }) {
  const [userInfo, setUserInfo] = useState<{
    u: any; idUser: number; nameUser: string
  } | null>(null);

  const fetchUserInfo = async () => {
    try {
      const token = await AsyncStorage.getItem('accessToken');
      if (!token) {
        console.error('Token không tồn tại');
        return;
      }
      const response = await http.get('auth/profile', {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (response.status === 200) {
        setUserInfo(response.data);
        console.log(response.data)
      } else {
        console.error('Không thể lấy thông tin người dùng');
      }
    } catch (error) {
      console.error('Lỗi khi lấy thông tin người dùng:', error);
    }
  };

  useEffect(() => {
    fetchUserInfo();
  }, []);

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <Text style={{ fontSize: 20, fontWeight: 'bold', textAlign: "center", color: '#00bf63', marginBottom: 10 }}>
          Thông tin
        </Text>

        <Text style={styles.courseTitle}>Trung Tâm EFY</Text>
        <View style={styles.infoBox}>
          <View style={styles.infoRow}>
            <Entypo name="location-pin" size={24} color="black" />
            <Text style={styles.infoText}>
              12 Nguyễn Văn Bảo, Phường 4, Gò Vấp,     Hồ Chí Minh
            </Text>
          </View>
          <View style={styles.infoRow}>
            <FontAwesome name="phone" size={24} color="black" />
              <Text style={styles.infoText}>Hotline: 0909090900</Text>
            </View>
        </View>
      </View>

      <View style={styles.optionList}>
        <TouchableOpacity
          style={styles.option}
          onPress={() => {
            if (userInfo) {
              navigation.navigate('LessonDetailScreen', { idUser: userInfo.u.idUser, nameUser: userInfo.nameUser });
            } else {
              console.error("Thông tin người dùng chưa sẵn sàng");
            }
          }}
        >
          <View style={styles.optionRow}>
            <MaterialIcons name="schedule" size={24} color="orange" />
            <Text style={styles.optionText}>Lịch học</Text>
          </View>
        </TouchableOpacity>

        <TouchableOpacity style={styles.option} onPress={() => {
          if (userInfo) {
            navigation.navigate('ScoreBoardScreen', { idUser: userInfo.u.idUser, nameUser: userInfo.nameUser });
          } else {
            console.error("Thông tin người dùng chưa sẵn sàng");
          }
        }}>
          <View style={styles.optionRow}>
            <FontAwesome name="list-alt" size={24} color="green" />
            <Text style={styles.optionText}>Bảng điểm</Text>
          </View>
        </TouchableOpacity>

        <TouchableOpacity style={styles.option}
          onPress={() => {
            if (userInfo) {
              navigation.navigate('ProgressScreen', { idUser: userInfo.u.idUser, nameUser: userInfo.nameUser });
            } else {
              console.error("Thông tin người dùng chưa sẵn sàng");
            }
          }}>
          <View style={styles.optionRow}>
            <AntDesign name="circledowno" size={24} color="purple" />
            <Text style={styles.optionText}>Tiến trình học tập</Text>
          </View>
        </TouchableOpacity>

        <TouchableOpacity style={styles.option}
          onPress={() => {
            if (userInfo) {
              navigation.navigate('CourseInfomationScreen', { idUser: userInfo.u.idUser, nameUser: userInfo.nameUser });
            } else {
              console.error("Thông tin người dùng chưa sẵn sàng");
            }
          }}>
          <View style={styles.optionRow}>
          <Feather name="file" size={24} color="red" />
          <Text style={styles.optionText}>Thông tin khóa học</Text>
          </View>
        </TouchableOpacity>

        <TouchableOpacity style={styles.option}
         onPress={() => {
            if (userInfo) {
              navigation.navigate('LeaderboardScreen', { idUser: userInfo.u.idUser, nameUser: userInfo.nameUser });
            } else {
              console.error("Thông tin người dùng chưa sẵn sàng");
            }
          }}>
          <View style={styles.optionRow}>
            <FontAwesome name="trophy" size={24} color="gold" />
            <Text style={styles.optionText}>Bảng xếp hạng</Text>
          </View>
        </TouchableOpacity>


      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: '100%',
    backgroundColor: '#fff',
  },
  header: {
    padding: 20,
    backgroundColor: '#fff',
  },
  courseTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  infoBox: {
    backgroundColor: '#f0f0f0',
    padding: 15,
    borderRadius: 8,
  },
  infoRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 8,
  },
  infoText: {
    marginLeft: 10,
    fontSize: 16,
  },
  optionList: {
    backgroundColor: "#fff"
  },
  option: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 20,
    backgroundColor: '#fff',
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 10,
    marginTop: 10,
    width: '90%',
    alignSelf: 'center'
  },
  optionRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  optionText: {
    marginLeft: 10,
    fontSize: 16,
  },
  optionValue: {
    fontSize: 16,
    color: 'orange',
  },
});

import React from 'react';
import { View, Text, StyleSheet, ScrollView, Image, TouchableOpacity } from 'react-native';

export default function NewsFeedScreen(){
  const logoEFY = require('../../../image/logo/EFY.png');

  return (
    <ScrollView style={styles.container}>
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Tin tức từ trung tâm</Text>
        <TouchableOpacity>
        <View style={styles.newsCard}>
          <Image
            source={logoEFY}
            style={styles.newsImage}
          />
          <View style={styles.newsContent}>
            <Text style={styles.newsTitle}>Sự kiện tháng 10: "Ngày hội học tập"</Text>
            <Text style={styles.newsDescription}>
              Trung tâm sẽ tổ chức một buổi hội thảo về kỹ năng học tập hiệu quả vào ngày 15 tháng 10.
            </Text>
          </View>
        </View>
        </TouchableOpacity>

        <TouchableOpacity>
        <View style={styles.newsCard}>
          <Image
            source={logoEFY}
            style={styles.newsImage}
          />
          <View style={styles.newsContent}>
            <Text style={styles.newsTitle}>Thông báo về lịch học mới</Text>
            <Text style={styles.newsDescription}>
              Lịch học tháng 11 sẽ được cập nhật vào cuối tháng 10, các học viên chú ý theo dõi thông tin mới nhất.
            </Text>
          </View>
        </View>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  section: {
    marginBottom: 24,
  },
  sectionTitle: {
    padding:20,
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 8,
    color: '#00bf63',
  },
  newsCard: {
    flexDirection: 'row',
    backgroundColor: '#f9f9f9',
    padding: 12,
    borderRadius: 8,
    marginBottom: 12,
    alignItems: 'center',
  },
  newsImage: {
    width: 100,
    height: 100,
    borderRadius: 8,
    marginRight: 12,
  },
  newsContent: {
    flex: 1,
  },
  newsTitle: {
    fontSize: 16,
    fontWeight: 'bold',
  },
  newsDescription: {
    fontSize: 14,
    color: 'gray',
  },
  forumCard: {
    backgroundColor: '#f0f0f0',
    padding: 12,
    borderRadius: 8,
    marginBottom: 12,
  },
  forumTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 4,
  },
  forumAuthor: {
    fontSize: 12,
    fontStyle: 'italic',
    marginBottom: 8,
    color: 'gray',
  },
  forumDescription: {
    fontSize: 14,
    color: 'gray',
  },
});


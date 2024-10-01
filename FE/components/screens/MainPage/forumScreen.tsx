import React from 'react';
import { View, Text, ScrollView, StyleSheet, TouchableOpacity } from 'react-native';

export default function ForumScreen() {
  return (
    <ScrollView style={styles.container}>
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Bài viết từ diễn đàn</Text>
        <TouchableOpacity>
        <View style={styles.forumCard}>
          <Text style={styles.forumTitle}>[Diễn đàn] Cách học từ vựng hiệu quả?</Text>
          <Text style={styles.forumAuthor}>Người đăng: Hoàng Anh</Text>
          <Text style={styles.forumDescription}>
            Các bạn có cách nào để ghi nhớ từ vựng nhanh và lâu hơn không? Mình học mãi mà quên hoài.
          </Text>
        </View>
        </TouchableOpacity>
        <TouchableOpacity>
        <View style={styles.forumCard}>
          <Text style={styles.forumTitle}>[Diễn đàn] Chia sẻ kinh nghiệm học giao tiếp tiếng Anh</Text>
          <Text style={styles.forumAuthor}>Người đăng: Quỳnh Trang</Text>
          <Text style={styles.forumDescription}>
            Mình đã tham gia khóa học giao tiếp tại trung tâm và muốn chia sẻ lại một số kinh nghiệm giúp các bạn tự tin hơn khi nói tiếng Anh.
          </Text>
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
    mainTitle: {
      fontSize: 24,
      fontWeight: 'bold',
      marginBottom: 20,
      textAlign: 'center',
      color:'#388E3C',
    },
    section: {
      marginBottom: 24,
    },
    sectionTitle: {
        padding:20,
      fontSize: 18,
      fontWeight: 'bold',
      marginBottom: 8,
      color: '#388E3C',
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
  




    
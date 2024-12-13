import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  StyleSheet,
  ScrollView,
  Modal,
  TouchableOpacity,
} from 'react-native';
import http from '../../../utils/http'; 
import { sendMessageToOpenAI } from '../../../utils/getAI';

const ChatModal = ({ isVisible, onClose }: { isVisible: boolean; onClose: () => void }) => {
  const [userMessage, setUserMessage] = useState('');
  const [chatHistory, setChatHistory] = useState<{ sender: string; text: string }[]>([]);
  const [loading, setLoading] = useState(false);

  const handleSend = async () => {
    if (userMessage.trim()) {
      const userInput = { sender: 'user', text: userMessage };
      setChatHistory((prev) => [...prev, userInput]);
      setLoading(true);

      try {
        let dynamicData = '';
        let aiResponseText = '';

        if (/khóa học|course/i.test(userMessage)) {
          const response = await http.get('auth/noauth/findAllKhoa');
          const courses = response.data;

          dynamicData = courses
            .map((course: { tenKhoaHoc: string; giaTien: number; thoiGianDienRa: string; soBuoi: number; moTa: string }) =>
              `- Tên khóa học: ${course.tenKhoaHoc}\n  Giá tiền: ${course.giaTien.toLocaleString()} VND\n  Thời gian: ${course.thoiGianDienRa}\n  Số buổi: ${course.soBuoi}\n  Mô tả: ${course.moTa}`
            )
            .join('\n\n');
        } else if (/giảng viên|teacher/i.test(userMessage)) {
          const response = await http.get('auth/noauth/getAllGiangVien');
          const teachers = response.data;

          const teacherNames = teachers.map((teacher: { hoTen: string }) => teacher.hoTen).join(', ');
          const totalTeachers = teachers.length;

          dynamicData = `Danh sách giảng viên: ${teacherNames}\nTổng số giảng viên: ${totalTeachers}`;
        } else if (/lớp học|class/i.test(userMessage)) {
          const response = await http.get('auth/noauth/getAllLop');
          const classes = response.data;

          if (/khóa học (.+)/i.test(userMessage)) {
            const matchedCourse = userMessage.match(/khóa học (.+)/i);
            if (matchedCourse && matchedCourse[1]) {
              const courseName = matchedCourse[1].trim();
              const filteredClasses = classes.filter((cls: { khoaHoc: { tenKhoaHoc: string } }) =>
                cls.khoaHoc.tenKhoaHoc.toLowerCase().includes(courseName.toLowerCase())
              );

              if (filteredClasses.length > 0) {
                dynamicData = filteredClasses
                  .map((cls: { tenLopHoc: string; soHocVien: number; trangThai: string; khoaHoc: { tenKhoaHoc: string } }) =>
                    `- Tên lớp học: ${cls.tenLopHoc}\n  Số học viên: ${cls.soHocVien}\n  Trạng thái: ${cls.trangThai}\n  Khóa học: ${cls.khoaHoc.tenKhoaHoc}`
                  )
                  .join('\n\n');
              } else {
                dynamicData = `Không tìm thấy lớp học nào thuộc khóa học "${courseName}".`;
              }
            } else {
              dynamicData = 'Không tìm thấy khóa học được chỉ định trong lớp học.';
            }
          } else {
            dynamicData = classes
              .map((cls: { tenLopHoc: string; soHocVien: number; trangThai: string; khoaHoc: { tenKhoaHoc: string } }) =>
                `- Tên lớp học: ${cls.tenLopHoc}\n  Số học viên: ${cls.soHocVien}\n  Trạng thái: ${cls.trangThai}\n  Khóa học: ${cls.khoaHoc.tenKhoaHoc}`
              )
              .join('\n\n');
          }
        } else if (/học phí|tuition|giá tiền|chi phí|mệnh giá/i.test(userMessage)) {
          const response = await http.get('auth/noauth/findAllKhoa');
          const courses = response.data;

          const prices = courses.map((course: { giaTien: number }) => course.giaTien);
          const minPrice = Math.min(...prices);
          const maxPrice = Math.max(...prices);

          dynamicData = `Học phí dao động từ ${minPrice.toLocaleString()} VND đến ${maxPrice.toLocaleString()} VND.`;
        }

        const aiPrompt = `
          Bạn là một trợ lý hỗ trợ khách hàng cho trung tâm đào tạo tiếng Anh. 
          Dưới đây là thông tin trung tâm:
          - Địa chỉ: 12 Nguyễn Văn Bảo, Gò Vấp, TP.HCM.
          - Liên hệ: 0909090900.
          - Dữ liệu từ API: 
          ${dynamicData || 'Hiện tại không có thông tin phù hợp.'}
          Khách hàng hỏi: "${userMessage}"
          Hãy trả lời khách hàng chi tiết và thân thiện, sử dụng thông tin trên nếu phù hợp.
        `;
        const aiReply = await sendMessageToOpenAI(aiPrompt);

        aiResponseText = aiReply;
        const aiResponse = { sender: 'ai', text: aiResponseText };
        setChatHistory((prev) => [...prev, aiResponse]);
      } catch (error) {
        console.error('Error with API or AI response:', error);
        setChatHistory((prev) => [
          ...prev,
          { sender: 'ai', text: 'Xin lỗi, hiện tại không thể lấy thông tin. Vui lòng thử lại sau.' },
        ]);
      } finally {
        setLoading(false);
      }

      setUserMessage('');
    }
  };

  return (
    <Modal visible={isVisible} animationType="slide" transparent={true} onRequestClose={onClose}>
      <View style={styles.modalOverlay}>
        <View style={styles.modalContent}>
          <Text style={styles.title}>Hỗ trợ khách hàng</Text>
          <ScrollView style={styles.chatContainer}>
            {chatHistory.map((chat, index) => (
              <Text
                key={index}
                style={[
                  styles.message,
                  chat.sender === 'user' ? styles.userMessage : styles.aiMessage,
                ]}
              >
                {chat.sender === 'user' ? 'Bạn: ' : 'AI: '}
                {chat.text}
              </Text>
            ))}
            {loading && <Text style={styles.loadingText}>AI đang trả lời...</Text>}
          </ScrollView>
          <TextInput
            style={styles.input}
            placeholder="Nhập tin nhắn..."
            value={userMessage}
            onChangeText={setUserMessage}
          />
          <View style={styles.buttonContainer}>
            <TouchableOpacity style={styles.sendButton} onPress={handleSend}>
              <Text style={styles.buttonText}>Gửi</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.closeButton} onPress={onClose}>
              <Text style={styles.buttonText}>Đóng</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  modalContent: {
    backgroundColor: 'white',
    padding: 16,
    borderRadius: 8,
    width: '60%',
    maxHeight: '80%',
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 8,
  },
  chatContainer: {
    flex: 1,
    marginBottom: 8,
  },
  message: {
    marginVertical: 4,
    padding: 8,
    borderRadius: 4,
  },
  userMessage: {
    backgroundColor: '#d4f1f4',
    alignSelf: 'flex-end',
  },
  aiMessage: {
    backgroundColor: '#f4d4d4',
    alignSelf: 'flex-start',
  },
  input: {
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 4,
    padding: 8,
    marginBottom: 8,
  },
  loadingText: {
    fontStyle: 'italic',
    color: 'gray',
    marginTop: 8,
  },
  buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  sendButton: {
    backgroundColor: '#007bff',
    padding: 10,
    borderRadius: 5,
    flex: 1,
    alignItems: 'center',
    marginRight: 8,
  },
  closeButton: {
    backgroundColor: '#dc3545',
    padding: 10,
    borderRadius: 5,
    flex: 1,
    alignItems: 'center',
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: 'bold',
  },
});

export default ChatModal;

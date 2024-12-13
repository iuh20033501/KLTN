import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

export default function FAQScreen({ navigation }: { navigation: any }) {
    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <TouchableOpacity onPress={() => navigation.goBack()}>
                    <Icon name="arrow-back-outline" size={24} color="black" />
                </TouchableOpacity>
                <Text style={styles.headerText}>Câu Hỏi Thường Gặp</Text>
            </View>

            <ScrollView style={styles.contentContainer}>
                <View style={styles.faqItem}>
                    <Text style={styles.question}>1. Làm thế nào để đăng ký khóa học?</Text>
                    <Text style={styles.answer}>Bạn có thể đăng ký khóa học thông qua web hoặc đến trực tiếp trung tâm để được hỗ trợ đăng ký.</Text>
                </View>

                <View style={styles.faqItem}>
                    <Text style={styles.question}>2. Tôi có thể hủy khóa học không?</Text>
                    <Text style={styles.answer}>Có, bạn có thể hủy khóa học theo chính sách của trung tâm. Vui lòng liên hệ bộ phận hỗ trợ để biết thêm chi tiết.</Text>
                </View>

                <View style={styles.faqItem}>
                    <Text style={styles.question}>3. Trung tâm có hỗ trợ học trực tuyến không?</Text>
                    <Text style={styles.answer}>Có, chúng tôi cung cấp các lớp học trực tuyến lẫn trực tiếp với giảng viên bản ngữ lẫn giảng viên bình thường và giáo trình phù hợp.</Text>
                </View>

                <View style={styles.faqItem}>
                    <Text style={styles.question}>4. Làm sao để nhận tài liệu học?</Text>
                    <Text style={styles.answer}>Tài liệu học sẽ được phát tại các buổi học hoặc tài liệu sẽ được đăng tải trực tuyến trên ứng dụng.</Text>
                </View>

                <View style={styles.faqItem}>
                    <Text style={styles.question}>5. Chính sách hoàn tiền như thế nào?</Text>
                    <Text style={styles.answer}>Chính sách hoàn tiền sẽ áp dụng nếu bạn hủy khóa học trước khi khóa học bắt đầu. Vui lòng xem chi tiết tại trung tâm.</Text>
                </View>
            </ScrollView>
        </View>
    );
}

const styles = StyleSheet.create({
    container: { flex: 1, backgroundColor: '#fff' },
    header: { flexDirection: 'row', alignItems: 'center', padding: 15 },
    headerText: { fontSize: 18, fontWeight: 'bold', marginLeft: 10 },
    contentContainer: { padding: 15 },
    faqItem: { marginBottom: 20 },
    question: { fontSize: 16, fontWeight: 'bold', color: '#00bf63' },
    answer: { fontSize: 14, color: '#555', marginTop: 5, lineHeight: 20 },
});
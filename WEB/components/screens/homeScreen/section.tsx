  import React, { ReactNode } from 'react';
  import { View, Text, StyleSheet } from 'react-native';
  import { FontAwesome, MaterialIcons, Ionicons } from '@expo/vector-icons'; 
  const SectionComponent = () => {
    return (
      <View style={styles.container}>
        <View style={styles.header}>
          <Text style={styles.title}>
            Về trung tâm Anh ngữ <Text style={styles.highlight}>English for</Text> 
            <Text style={{color:'#FF0000'}}> You</Text>
          </Text>
          <Text style={styles.description}>
          English for You hướng tới mục tiêu mang đến chương trình đào tạo tiêu chuẩn, chất lượng nhất đến với người Việt.
            Với chương trình học TOÀN DIỆN phát triển đồng đều ở cả 4 kỹ năng Nghe – Nói – Đọc – Viết, thế hệ trẻ Việt Nam
            chắc chắn sẽ tự tin giao tiếp, kết nối và hòa nhập với nền văn hóa thế giới. Từ đó, nâng cao trình độ chất lượng
            nguồn nhân lực tại Việt Nam với khả năng sử dụng tiếng Anh hàng đầu Châu Á.
          </Text>
        </View>

        <View style={styles.features}>
          <Feature
            icon={<FontAwesome name="rocket" size={40} color="white" />}
            title="Tiên phong dạy toàn diện 4 kỹ năng đầu tiên tại Việt Nam"
            description="Là trung tâm đầu tiên tại Việt Nam dạy tiếng Anh toàn diện cả 4 kỹ năng ứng dụng cho học tập và công việc."
            backgroundColor="#e74c3c"
          />
          <Feature
            icon={<MaterialIcons name="public" size={40} color="white" />}
            title="Giảng dạy bằng phương pháp chuẩn Quốc Tế"
            description="Giảng dạy bằng phương pháp học chuẩn Quốc Tế được các trường đại học hàng đầu tại Mỹ sử dụng."
            backgroundColor="#e74c3c"
          />
          <Feature
            icon={<Ionicons name="chatbubbles" size={40} color="white" />}
            title="Học phát âm và giao tiếp với giáo viên nước ngoài"
            description="Luyện phát âm chuẩn kết hợp với rèn luyện nghe nói, giao tiếp cùng với giáo viên bản ngữ 100%."
            backgroundColor="#3498db"
          />
          <Feature
            icon={<MaterialIcons name="attach-money" size={40} color="white" />}
            title="Thanh toán đa dạng nhiều hình thức"
            description="Hỗ trợ nhiều hình thức thanh toán đa dạng cho học viên."
            backgroundColor="#3498db"
          />
        </View>
      </View>
    );
  };

  interface FeatureProps {
      icon: ReactNode;
      title: string;
      description: string;
      backgroundColor: string;
    }
    
    const Feature: React.FC<FeatureProps> = ({ icon, title, description, backgroundColor }) => (
      <View style={styles.featureContainer}>
         <View style={styles.topBorder} />
        <View style={[styles.iconContainer, { backgroundColor }]}>
          {icon}
        </View>
          <Text style={styles.featureTitle}>{title}</Text>
          <Text style={styles.featureDescription}>{description}</Text>
      </View>
    );
  const styles = StyleSheet.create({
    container: {
      flex: 1,
      justifyContent: 'center', 
      alignItems: 'center', 
      padding: 20,
      backgroundColor: 'white',
      marginTop:50
    },
    header: {
      width:'80%',
      marginBottom: 30,
      alignItems: 'center',
      flexDirection:'row', 
    },
    title: {
      fontSize: 40,
      fontWeight: 'bold',
      marginBottom: 10,
      width:'250%',
      textAlign:'center',
      padding:80
    },
    highlight: {
      color: '#00405d',
    },
    description: {
      fontSize: 16,
      lineHeight: 24,
      color: '#333',
      marginLeft:200 ,
      padding:80,
      width:'300%',
      height:'95%',
      top:-15
    },
    features: {
      flexDirection: 'row',
      flexWrap: 'wrap',
      justifyContent: 'center', 
      
    },
    featureContainer: {
      width: "40%",
      height:250, 
      alignItems: 'center', 
      padding:50,
      margin:40,
    },
    iconContainer: {
      width: 80,
      height: 80,
      borderRadius: 30,
      justifyContent: 'center',
      alignItems: 'center',
      marginBottom: 10,
    
    },
    featureTitle: {
      fontSize: 20,
      fontWeight: 'bold',
      marginBottom: 5,
      textAlign: 'center',
    },
    featureDescription: {
      fontSize: 16,
      color: '#555',
      lineHeight: 20,
      textAlign: 'center', 
    },
    topBorder: {
      width: '80%', 
      height: 1,
      backgroundColor: '#ccc',
      marginBottom:50,
      marginTop:-100
    },
  });

  export default SectionComponent;

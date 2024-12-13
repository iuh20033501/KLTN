import React from 'react';
import { ScrollView, StyleSheet, Text, View, TouchableOpacity } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

const TermsAndConditions = ({ navigation }: { navigation: any }) => {
  return (
    <View style={{ flex: 1 }}>
      <View style={styles.header}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Icon name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
        <Text style={styles.headerText}>Điều Khoản Sử Dụng</Text>
      </View>

      <ScrollView style={styles.container}>
        <View style={styles.section}>
          <Text style={styles.header}>1. Giới thiệu</Text>
          <Text style={styles.text}>
            Chào mừng bạn đến với ứng dụng học tiếng Anh của chúng tôi (English for You). Việc sử dụng ứng dụng này đồng nghĩa với việc bạn chấp nhận toàn bộ các điều khoản và điều kiện dưới đây. Nếu bạn không đồng ý với bất kỳ điều khoản nào, vui lòng không sử dụng ứng dụng. Chúng tôi có quyền thay đổi các điều khoản này mà không cần thông báo trước. Việc bạn tiếp tục sử dụng ứng dụng sau khi có thay đổi đồng nghĩa với việc bạn chấp nhận các thay đổi đó.
          </Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.header}>2. Đối tượng sử dụng</Text>
          <Text style={styles.text}>
            Ứng dụng này được thiết kế cho người dùng từ độ tuổi 11 trở lên có nhu cầu học tiếng Anh. Người dùng dưới 18 tuổi phải có sự đồng ý của phụ huynh hoặc người giám hộ hợp pháp trước khi sử dụng ứng dụng. Nếu bạn là phụ huynh hoặc người giám hộ hợp pháp, bạn có trách nhiệm giám sát việc sử dụng ứng dụng của trẻ em dưới quyền quản lý của bạn.
          </Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.header}>3. Điều khoản đăng ký</Text>
          <Text style={styles.text}>
            - Thông tin đăng ký: Khi đăng ký tài khoản, bạn phải cung cấp thông tin chính xác, đầy đủ và cập nhật, bao gồm nhưng không giới hạn tên, địa chỉ email và mật khẩu.
            {'\n'}- Bảo mật tài khoản: Bạn chịu trách nhiệm bảo vệ mật khẩu và tài khoản của mình. Bạn đồng ý thông báo ngay cho chúng tôi về bất kỳ việc sử dụng trái phép hoặc vi phạm bảo mật nào liên quan đến tài khoản của bạn.
            {'\n'}- Chia sẻ tài khoản: Bạn không được phép chia sẻ tài khoản của mình với bất kỳ ai khác. Nếu bạn phát hiện bất kỳ hoạt động không được phép trên tài khoản của mình, bạn phải thông báo cho chúng tôi ngay lập tức.
          </Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.header}>4. Quyền sử dụng</Text>
          <Text style={styles.text}>
            - Quyền sử dụng: Bạn được cấp quyền không độc quyền, không chuyển nhượng, có thể thu hồi để sử dụng ứng dụng chỉ cho mục đích học tập cá nhân.
            {'\n'}- Sử dụng thương mại: Việc sử dụng ứng dụng cho các mục đích thương mại, bao gồm nhưng không giới hạn việc bán nội dung ứng dụng hoặc tích hợp vào dịch vụ thương mại khác, phải được sự đồng ý bằng văn bản từ nhà phát triển.
          </Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.header}>5. Nội dung và Bản quyền</Text>
          <Text style={styles.text}>
            - Quyền sở hữu nội dung: Tất cả nội dung trong ứng dụng, bao gồm văn bản, âm thanh, video, hình ảnh và tài liệu học tập, đều thuộc quyền sở hữu của nhà phát triển hoặc các bên thứ ba có bản quyền. Bạn không được phép sao chép, phân phối, chỉnh sửa hoặc sử dụng nội dung cho bất kỳ mục đích thương mại nào mà không có sự cho phép bằng văn bản từ nhà phát triển.
            {'\n'}- Yêu cầu bản quyền: Trong trường hợp bạn phát hiện nội dung vi phạm bản quyền, vui lòng liên hệ với chúng tôi để xử lý.
          </Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.header}>6. Quy tắc ứng xử</Text>
          <Text style={styles.text}>
            - Nội dung vi phạm pháp luật: Bạn không được đăng tải, truyền tải hoặc chia sẻ bất kỳ nội dung nào vi phạm pháp luật, mang tính thù hận, xúc phạm hoặc xâm phạm quyền riêng tư của người khác.
            {'\n'}- Bảo mật hệ thống: Bạn không được cố gắng hack, phá hoại, làm ảnh hưởng hoặc can thiệp vào tính bảo mật, hoạt động của hệ thống hoặc dữ liệu của ứng dụng.
          </Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.header}>7. Phí dịch vụ</Text>
          <Text style={styles.text}>
            - Dịch vụ miễn phí và trả phí: Ứng dụng có thể cung cấp các dịch vụ miễn phí và trả phí. Các dịch vụ trả phí sẽ được mô tả chi tiết trong ứng dụng, bao gồm mức phí và cách thức thanh toán.
            {'\n'}- Thanh toán: Bạn đồng ý thanh toán đầy đủ và kịp thời cho các dịch vụ trả phí. Trong trường hợp không thanh toán đúng hạn, chúng tôi có quyền tạm ngưng hoặc chấm dứt quyền truy cập của bạn vào các dịch vụ trả phí.
          </Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.header}>8. Bảo mật thông tin</Text>
          <Text style={styles.text}>
            - Cam kết bảo mật: Chúng tôi cam kết bảo mật thông tin cá nhân của bạn theo các chính sách bảo mật hiện hành.
            {'\n'}- Rủi ro bảo mật: Mặc dù chúng tôi sử dụng các biện pháp bảo mật hợp lý, không có hệ thống bảo mật nào hoàn toàn an toàn. Chúng tôi không chịu trách nhiệm cho các thiệt hại phát sinh từ việc truy cập trái phép vào thông tin của bạn.
          </Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.header}>9. Chấm dứt sử dụng</Text>
          <Text style={styles.text}>
            - Chấm dứt tài khoản: Chúng tôi có quyền chấm dứt hoặc tạm ngưng tài khoản của bạn nếu phát hiện vi phạm bất kỳ điều khoản nào trong thỏa thuận này.
            {'\n'}- Hoàn tiền: Nếu bạn ngừng sử dụng ứng dụng, mọi khoản phí đã thanh toán sẽ không được hoàn lại.
          </Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.header}>10. Trách nhiệm pháp lý</Text>
          <Text style={styles.text}>
            - Miễn trừ trách nhiệm: Nhà phát triển không chịu trách nhiệm cho bất kỳ thiệt hại trực tiếp, gián tiếp hoặc đặc biệt nào phát sinh từ việc sử dụng hoặc không sử dụng ứng dụng.
            {'\n'}- Thông tin "như hiện tại": Mọi thông tin và nội dung trong ứng dụng đều được cung cấp "như hiện tại" và không có bảo hành hoặc cam kết về tính chính xác, đầy đủ hoặc hoàn chỉnh.
          </Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.header}>11. Thay đổi điều khoản</Text>
          <Text style={styles.text}>
            - Cập nhật điều khoản: Chúng tôi có quyền cập nhật hoặc thay đổi các điều khoản sử dụng bất kỳ lúc nào mà không cần thông báo trước.
            {'\n'}- Chấp nhận thay đổi: Việc bạn tiếp tục sử dụng ứng dụng sau khi điều khoản được cập nhật đồng nghĩa với việc bạn đồng ý với các thay đổi đó.
          </Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.header}>12. Luật áp dụng</Text>
          <Text style={styles.text}>
            - Luật điều chỉnh: Các điều khoản này được điều chỉnh và hiểu theo pháp luật của nước Cộng hòa Xã hội Chủ nghĩa Việt Nam.
            {'\n'}- Giải quyết tranh chấp: Mọi tranh chấp phát sinh từ việc sử dụng ứng dụng sẽ được giải quyết tại tòa án có thẩm quyền tại Việt Nam.
          </Text>
        </View>
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 15,
    backgroundColor: '#fff',
  },
  header: {
    fontSize: 16,
    flexDirection: 'row',
    alignItems: 'center',
    padding: 15,
    color: '#00bf63',
    fontWeight:'bold',
  },
  headerText: {
    fontSize: 18,
    fontWeight: 'bold',
    marginLeft: 10,
  },
  section: {
    marginBottom: 20,
  },
  text: {
    fontSize: 14,
    lineHeight: 22,
  },
});

export default TermsAndConditions;

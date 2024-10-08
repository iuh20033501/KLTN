import React, { useState, useEffect } from "react";
import {
  View,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  Modal,
  TouchableWithoutFeedback,
  ImageBackground
} from "react-native";
import Icon from "react-native-vector-icons/Ionicons";
// import ArrowIcon from "../../assets/icon/ArrowIcon";
// import http from "../../utils/http";

export default function AuthenticationPass({navigation}: {navigation: any}) {
  const backgroundImg = require("../../../image/background/bg7.png"); 

  const [isContinueEnabled, setIsContinueEnabled] = useState(false);
  const [verificationCode, setVerificationCode] = useState("");
  const [failureModalVisible, setFailureModalVisible] = useState(false);
  const [successRequest, setSuccessRequest] = useState(false);

  const isCodeComplete = verificationCode.replace(/\s/g, "").length === 6;
 
  const handleCloseModal2 = () => {
    setSuccessRequest(false);
  };

  const handleCloseModal = () => {
    setFailureModalVisible(false);
  }

  return (
    <ImageBackground
    source={backgroundImg}
    style={styles.backgroundImage}
    resizeMode="cover"
  >
     <TouchableOpacity  onPress={() => navigation.goBack()} style={{padding:15, alignSelf:'baseline'}}>
        <Icon  name="arrow-back-outline" size={24} color="black" />
        </TouchableOpacity>
    <View style={styles.container}>
      <View>
        <Text style={styles.font}>Nhập mã xác thực</Text>
      </View>

      <View>
        <Text
          style={{
            color: "#868c92",
            fontSize: 13,
            textAlign: "center",
            marginTop: 50,
          }}
        >
          Nhập 6 dãy số được gửi đến số điện thoại{" "}
        </Text>
        <Text
          style={{
            color: "#000",
            fontSize: 15,
            textAlign: "center",
            marginTop: 20,
            fontWeight: "bold",
          }}
        >

        </Text>
      </View>

      <View style={styles.phoneInputContainer}>
        <TextInput
          style={styles.input}
          value={verificationCode}
          onChangeText={(value) =>
            setVerificationCode(value.replace(/\s/g, ""))
          } 
          keyboardType="numeric"
          maxLength={6}
        />
      </View>

      <View>
        <TouchableOpacity
          style={[
            styles.button,
            { backgroundColor: isCodeComplete ? "#00bf63" : "#d3d6db" },
          ]}
          // onPress={async () => {
          //   handleSubmit();
          // }}
          disabled={!isCodeComplete}
        >
          <Text
            style={{
              color: isCodeComplete ? "black" : "#abaeb3",
              textAlign: "center",
              fontSize:18,
              fontWeight:'bold'
            }}
          >
           Xác thực
          </Text>
        </TouchableOpacity>
      </View>

      <View style={{ flexDirection: "row", marginLeft: 50, marginTop: 20 }}>
        <Text>Bạn không nhận được mã?</Text>
        <TouchableOpacity>
          <Text style={{ color: "#0867ef" }}> Gửi lại</Text>
        </TouchableOpacity>
      </View>

      <View style={{ flexDirection: "row",marginLeft:30, marginTop: 200 }}>
        <TouchableOpacity style={{ flexDirection: "row" }}>
          <Text
            style={{
              color: "#0867ef",
              fontWeight: "bold",
              fontSize: 15,
              textAlign: "center",
            }}
          >
            {" "}
            Tôi cần hỗ trợ thêm về mã xác thực
          </Text>
        </TouchableOpacity>
      </View>

      {/* FAIL OTP */}
      <Modal
        animationType="fade"
        transparent={true}
        visible={failureModalVisible}
        onRequestClose={handleCloseModal}
      >
        <View style={styles.modalContainer}>
          <View style={styles.modalContent2}>
            <Text
              style={{ fontSize: 20, textAlign: "center", fontWeight: "bold" }}
            >
              Nhập OTP thất bại
            </Text>
            <View style={styles.separator} />
            <Text style={{ fontSize: 13 }}>
              Có thể bạn đã gặp phải một số trường hợp:
            </Text>
            <Text>• Mã OTP không đúng</Text>
            <Text>• Mã OTP đã hết hiệu lực</Text>
            <TouchableOpacity onPress={() => handleCloseModal()}>
              <Text
                style={{
                  color: "#0867ef",
                  textAlign: "center",
                  fontSize: 15,
                  marginTop: 15,
                }}
              >
                Xác nhận
              </Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
      {/* REQUEST MORE TIME */}
      <Modal
        animationType="slide"
        transparent={true}
        visible={successRequest}
        onRequestClose={() => handleCloseModal2()}
      >
        <View style={styles.modalContainer}>
          <View style={styles.modalContent2}>
            <Text
              style={{ fontSize: 20, textAlign: "center", fontWeight: "bold" }}
            >
              Đã gửi lại mã OTP
            </Text>
            <View style={styles.separator} />
            <Text>• Kiểm tra mã OTP trên số điện thoại của bạn</Text>
            <Text>• Đảm bảo rằng số điện thoại của bạn vẫn còn hoạt động</Text>
            <TouchableOpacity
              onPress={() => {
                handleCloseModal2();
              }}
            >
              <Text
                style={{
                  color: "#0867ef",
                  textAlign: "center",
                  fontSize: 15,
                  marginTop: 15,
                }}
              >
                Xác nhận
              </Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    </View>
        </ImageBackground>
  );
};

const styles = StyleSheet.create({
  backgroundImage: {
    flex: 1,
    justifyContent: 'center',
    width: "100%",
    height: "100%",
    alignItems:'center'
  
  },
  container: {
    position: "relative",
    flex: 1,
  },
  font: {
    marginTop:100,
    fontSize: 25,
    color: "#1a1a1a",
    fontWeight: "bold",
    textAlign: "center",
  },
  phoneInputContainer: {
    width: "80%",
    flexDirection: "row",
    justifyContent: "center",
    marginVertical: 50,
    alignItems: "center",
    marginTop: 30,
    borderRadius: 30,

  },
  button: {
    width: 300,
    height: 45,
    borderRadius: 20,
    alignItems: "center",
    justifyContent: "center",
    marginTop: -20,
    marginLeft:5
  },
  input: {
    height: 45,
    width: 300,
    borderColor: "gray",
    borderWidth: 1,
    borderRadius: 10,
    fontSize: 30,
    textAlign: "center",
    fontWeight: "bold",
    letterSpacing: 4,
  },
  modalContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  modalContent2: {
    width: 250,
    height: 240,
    backgroundColor: "#fff",
    padding: 20,
    borderRadius: 10,
    borderWidth: 1,
    borderColor: "black",
    textAlign: "center",
  },
  closeButton: {
    position: "absolute",
    top: 0,
    right: 10,
    width: 10,
    height: 10,
  },
  separator: {
    borderBottomWidth: 0.5,
    borderBottomColor: "black",
    marginVertical: 10,
    marginTop: 20,
  },

});



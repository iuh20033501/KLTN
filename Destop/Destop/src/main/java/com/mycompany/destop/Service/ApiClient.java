package com.mycompany.destop.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mycompany.destop.DTO.OTPRequestDTO;
import com.mycompany.destop.DTO.OTPResponseDTO;
import com.mycompany.destop.DTO.PhoneNumberDTO;
import com.mycompany.destop.DTO.ResetPassDto;
import com.mycompany.destop.DTO.SigninDTO;
import com.mycompany.destop.Enum.ChucVuEnum;
import com.mycompany.destop.Modul.TaiKhoanLogin;
import com.mycompany.destop.Modul.User;
import com.mycompany.destop.Reponse.ApiResponse;
import com.mycompany.destop.Reponse.JwtResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import javax.swing.JOptionPane;

public class ApiClient {

    private Gson gson = new Gson();

    public JwtResponse callLoginApi(String username, String password) throws Exception {
        String apiUrl = "http://localhost:8081/auth/noauth/signin"; // URL của API bạn cần gọi
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Cấu hình yêu cầu POST
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Tạo dữ liệu yêu cầu JSON
        String jsonInputString = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";

        // Gửi dữ liệu JSON lên server
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Đọc phản hồi từ server
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // HTTP 200
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Chuyển đổi phản hồi JSON thành đối tượng JwtResponse
            String jsonResponse = response.toString();
            Gson gson = new Gson();
            return gson.fromJson(jsonResponse, JwtResponse.class);
        } else {
            JOptionPane.showMessageDialog(null, "Đăng nhập thất bại (lỗi " + responseCode + ")");
            return null; // Hoặc ném một ngoại lệ tùy ý
        }
    }

    public SigninDTO callProfileApi(String token) throws Exception {
        String profileUrl = "http://localhost:8081/auth/profile"; // Đảm bảo URL này đúng
        URL url = new URL(profileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Cấu hình GET request với JWT token trong header
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Tạo đối tượng Gson với TypeAdapter cho LocalDate
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class, new TypeAdapter())
                        .create();

                // Chuyển đổi chuỗi JSON thành đối tượng SigninDTO
                return gson.fromJson(response.toString(), SigninDTO.class);
            }
        } else {
            throw new Exception("Không thể gọi API profile, mã phản hồi: " + responseCode);
        }
    }

    public String sendOTP(PhoneNumberDTO phoneNumberDTO) throws Exception {
        String profileUrl = "http://localhost:8081/auth/noauth/send";
        URL url = new URL(profileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Cấu hình yêu cầu POST
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Tạo dữ liệu yêu cầu JSON
        String jsonInputString = "{ \"phone\": \"" + phoneNumberDTO.getPhone() + "\" }";
        System.out.println(jsonInputString);
        // Gửi dữ liệu JSON lên server
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("UTF-8"); // sửa thành "UTF-8"
            os.write(input, 0, input.length);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi gửi yêu cầu: " + e.getMessage());
            return null;
        }

        // Đọc phản hồi từ server
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // HTTP 200
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Trả về toàn bộ phản hồi JSON dưới dạng chuỗi
            return response.toString();
        } else {
            JOptionPane.showMessageDialog(null, "Gửi mã OTP thất bại (lỗi " + responseCode + ")");
            return null;
        }
    }

   public TaiKhoanLogin getTaiKhoanBySDT(String soDienThoai) throws Exception {
    String profileUrl = "http://localhost:8081/auth/noauth/findBySdt/" + soDienThoai; // Đảm bảo URL đúng
    HttpURLConnection conn = (HttpURLConnection) new URL(profileUrl).openConnection();

    // Thiết lập GET request
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Accept", "application/json");

    // Kiểm tra mã phản hồi
    int responseCode = conn.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
        // Đọc phản hồi
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // Chuyển đổi JSON thành đối tượng TaiKhoanLogin
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new TypeAdapter())
                    .create();
            return gson.fromJson(response.toString(), TaiKhoanLogin.class);
        }
    } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
        throw new Exception("Không tìm thấy tài khoản với số điện thoại: " + soDienThoai);
    } else {
        throw new Exception("Lỗi khi gọi API, mã phản hồi: " + responseCode);
    }
}


    public OTPResponseDTO verifyOTPFromClient(OTPRequestDTO otpRequestDTO) throws Exception {
        String url = "http://localhost:8081/auth/noauth/validate"; // Đảm bảo URL này chính xác
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

        // Thiết lập POST request
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        // Chuyển đổi đối tượng OTPRequestDTO thành chuỗi JSON
        Gson gson = new Gson();
        String jsonInput = gson.toJson(otpRequestDTO);

        // Gửi dữ liệu request
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Đọc phản hồi từ server
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Chuyển đổi phản hồi JSON thành đối tượng OTPResponseDTO
                return gson.fromJson(response.toString(), OTPResponseDTO.class);
            }
        } else {
            throw new Exception("Gọi API xác thực OTP thất bại, mã phản hồi: " + responseCode);
        }
    }

    public String resetPasswordFromClient(String token, String newPassword) throws Exception {
        String url = "http://localhost:8081/auth/account/reset"; // Đảm bảo URL này chính xác
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

        // Thiết lập POST request
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        // Tạo đối tượng ResetPassDto và chuyển đổi thành JSON
        ResetPassDto resetPassDto = new ResetPassDto(); 
        resetPassDto.setPassword(newPassword);
        Gson gson = new Gson();
        String jsonInput = gson.toJson(resetPassDto);

        // Thêm token vào header (nếu cần xác thực)
         conn.setRequestProperty("Authorization", "Bearer " + token);

        // Gửi dữ liệu request
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Đọc phản hồi từ server
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString(); // Phản hồi dạng String từ server
            }
        } else {
            throw new Exception("Gọi API reset mật khẩu thất bại, mã phản hồi: " + responseCode);
        }
    }

}

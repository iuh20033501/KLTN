/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mycompany.destop.DTO.ResetPassDto;
import com.mycompany.destop.DTO.SigninDTO;
import com.mycompany.destop.Modul.KhoaHoc;
import com.mycompany.destop.Modul.TaiKhoanLogin;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Windows 10
 */
public class KhoaHocService {

    private Gson gson = new Gson();

    public List<KhoaHoc> getAllKhoaHocApi(String token) throws Exception {
        String profileUrl = "http://localhost:8081/khoahoc/getAll"; // Đảm bảo URL này đúng
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
                        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                        .create();

                // Chuyển đổi chuỗi JSON thành danh sách TaiKhoanLogin
                Type listType = new TypeToken<List<KhoaHoc>>() {
                }.getType();
                return gson.fromJson(response.toString(), listType);
            }
        } else {
            throw new Exception("Không thể gọi API profile, mã phản hồi: " + responseCode);
        }
    }

    public KhoaHoc loadKhoaHocById(String token, Long idKhoa) throws Exception {
        String apiUrl = "http://localhost:8081/khoahoc/findKhoa/" + idKhoa; // URL API với tham số ID
        HttpURLConnection conn = null;

        try {
            // Mở kết nối đến API
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();

            // Cấu hình kết nối
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token); // Gửi token xác thực trong header
            conn.setRequestProperty("Accept", "application/json");

            // Kiểm tra mã phản hồi từ server
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Đọc dữ liệu JSON trả về
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }

                    // Chuyển đổi JSON thành đối tượng KhoaHoc
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) // Xử lý LocalDate nếu cần
                            .create();
                    return gson.fromJson(response.toString(), KhoaHoc.class);
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                // Trường hợp không tìm thấy khóa học
                throw new Exception("Không tìm thấy khóa học với ID: " + idKhoa);
            } else {
                throw new Exception("Không thể gọi API, mã phản hồi: " + responseCode);
            }
        } finally {
            if (conn != null) {
                conn.disconnect(); // Đóng kết nối
            }
        }
    }
    
    public KhoaHoc deleteKhoaHocApi(String token, Long idKhoa) throws Exception {
        String apiUrl = "http://localhost:8081/khoahoc/xoaKhoa/" + idKhoa; // URL API với tham số ID
        HttpURLConnection conn = null;

        try {
            // Mở kết nối đến API
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();

            // Cấu hình kết nối
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token); // Gửi token xác thực trong header
            conn.setRequestProperty("Accept", "application/json");

            // Kiểm tra mã phản hồi từ server
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Đọc dữ liệu JSON trả về
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }

                    // Chuyển đổi JSON thành đối tượng KhoaHoc
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) // Xử lý LocalDate nếu cần
                            .create();
                    return gson.fromJson(response.toString(), KhoaHoc.class);
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                // Trường hợp không tìm thấy khóa học
                throw new Exception("Không tìm thấy khóa học với ID: " + idKhoa);
            } else {
                throw new Exception("Không thể gọi API, mã phản hồi: " + responseCode);
            }
        } finally {
            if (conn != null) {
                conn.disconnect(); // Đóng kết nối
            }
        }
    }

    public Boolean createKhoaHocFromClient(String token, KhoaHoc khoaHoc) throws Exception {
        String url = "http://localhost:8081/khoahoc/create"; // Đảm bảo URL này chính xác
        HttpURLConnection conn = null;

        try {
            // Mở kết nối
            conn = (HttpURLConnection) new URL(url).openConnection();

            // Thiết lập POST request
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Tạo đối tượng KhoaHoc và chuyển đổi thành JSON
            Gson gson = new Gson();
            String jsonInput = gson.toJson(khoaHoc);

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
                    // Trả về true nếu có phản hồi từ server, tức là gọi API thành công
                    return true;
                }
            } else {
                // Nếu phản hồi từ server không phải HTTP_OK, ném ngoại lệ
                throw new Exception("Gọi API thất bại, mã phản hồi: " + responseCode);
            }
        } catch (IOException e) {
            // Nếu có lỗi trong quá trình kết nối hoặc gửi yêu cầu, ném ngoại lệ
            throw new Exception("Lỗi kết nối đến API: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.disconnect(); // Đảm bảo kết nối được đóng
            }
        }
    }

}

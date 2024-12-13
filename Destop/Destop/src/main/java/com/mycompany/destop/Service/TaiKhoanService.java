/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mycompany.destop.DTO.SigninDTO;
import com.mycompany.destop.Enum.ChucVu;
import com.mycompany.destop.Modul.TaiKhoanLogin;
import com.mycompany.destop.Modul.User;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Windows 10
 */
public class TaiKhoanService {

    private Gson gson = new Gson();

    public List<TaiKhoanLogin> getAllTKhoanApi(String token) throws Exception {
        String profileUrl = "http://54.169.251.110:8081/auth/noauth/findAll"; // Đảm bảo URL này đúng
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
                Type listType = new TypeToken<List<TaiKhoanLogin>>() {
                }.getType();
                return gson.fromJson(response.toString(), listType);
            }
        } else {
            throw new Exception("Không thể gọi API profile, mã phản hồi: " + responseCode);
        }
    }

    public List<TaiKhoanLogin> getAllTKhoanLikeNameApi(String token, String name) throws Exception {
        String profileUrl = "http://54.169.251.110:8081/auth/findTKhoan/" + name; // Đảm bảo URL này đúng
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
                Type listType = new TypeToken<List<TaiKhoanLogin>>() {
                }.getType();
                return gson.fromJson(response.toString(), listType);
            }
        } else {
            throw new Exception("Không thể gọi API profile, mã phản hồi: " + responseCode);
        }
    }

    public List<TaiKhoanLogin> getAllTKhoanAcTiveLikeNameApi(String token, String name) throws Exception {
        String profileUrl = "http://54.169.251.110:8081/auth/findTKhoanActiveLikeName/" + name; // Đảm bảo URL này đúng
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
                Type listType = new TypeToken<List<TaiKhoanLogin>>() {
                }.getType();
                return gson.fromJson(response.toString(), listType);
            }
        } else {
            throw new Exception("Không thể gọi API profile, mã phản hồi: " + responseCode);
        }
    }

    public TaiKhoanLogin callFindByIdtaiKhoanApi(String token, Long id) throws Exception {
        String apiUrl = "http://54.169.251.110:8081/auth/findById/" + id; // URL API
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Cấu hình GET request với JWT token trong header
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        int responseCode = conn.getResponseCode();

        // Xử lý phản hồi từ server
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Tạo đối tượng Gson với LocalDateAdapter
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                        .create();

                // Chuyển đổi chuỗi JSON thành đối tượng TaiKhoanLogin
                return gson.fromJson(response.toString(), TaiKhoanLogin.class);
            }
        } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new Exception("Không tìm thấy tài khoản với ID: " + id);
        } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new Exception("Bạn không có quyền thực hiện thao tác này. Vui lòng kiểm tra token.");
        } else {
            throw new Exception("Không thể gọi API xóa tài khoản. Mã phản hồi: " + responseCode);
        }
    }

    public List<TaiKhoanLogin> callFindByRoleTKApi(String token, String role) throws Exception {
        String profileUrl = "http://54.169.251.110:8081/auth/findTKhoanByroleDestop/"+role; // URL chính xác của API
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
                Type listType = new TypeToken<List<TaiKhoanLogin>>() {
                }.getType();
                return gson.fromJson(response.toString(), listType);
            }
        } else {
            throw new Exception("Không thể gọi API profile, mã phản hồi: " + responseCode);
        }
    }
//     public List<User> callFindGVTrueTKApi(String token) throws Exception {
//        String profileUrl = "http://localhost:8081/giangVien/findAllLamViec"; // URL chính xác của API
//         URL url = new URL(profileUrl);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//        // Cấu hình GET request với JWT token trong header
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Authorization", "Bearer " + token);
//
//        int responseCode = conn.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
//                StringBuilder response = new StringBuilder();
//                String responseLine;
//                while ((responseLine = br.readLine()) != null) {
//                    response.append(responseLine.trim());
//                }
//
//                // Tạo đối tượng Gson với TypeAdapter cho LocalDate
//                Gson gson = new GsonBuilder()
//                        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
//                        .create();
//
//                // Chuyển đổi chuỗi JSON thành danh sách TaiKhoanLogin
//                Type listType = new TypeToken<List<User>>() {
//                }.getType();
//                return gson.fromJson(response.toString(), listType);
//            }
//        } else {
//            throw new Exception("Không thể gọi API profile, mã phản hồi: " + responseCode);
//        }
//    }

}

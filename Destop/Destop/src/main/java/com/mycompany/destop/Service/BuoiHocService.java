/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Service;

import com.google.gson.Gson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mycompany.destop.DTO.CreateBuoiDTO;
import com.mycompany.destop.DTO.CreateLopDTO;
import com.mycompany.destop.DTO.SigninDTO;
import com.mycompany.destop.Enum.ChucVu;
import com.mycompany.destop.Modul.BuoiHoc;
import com.mycompany.destop.Modul.HoaDon;
import com.mycompany.destop.Modul.KetQuaTest;
import com.mycompany.destop.Modul.LopHoc;
import com.mycompany.destop.Modul.TaiKhoanLogin;
import com.mycompany.destop.Modul.ThanhToan;
import com.mycompany.destop.Modul.TienTrinh;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * /**
 *
 * @author Windows 10
 */
public class BuoiHocService {

    private Gson gson = new Gson();

    public List<BuoiHoc> getAllBuoiByLopApi(String token, Long idLop) throws Exception {
        String profileUrl = "http://18.141.201.212:8080/buoihoc/getBuoiDaHoc/" + idLop; // Đảm bảo URL này đúng
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
                Type listType = new TypeToken<List<BuoiHoc>>() {
                }.getType();
                return gson.fromJson(response.toString(), listType);
            }
        } else {
            throw new Exception("Không thể gọi API profile, mã phản hồi: " + responseCode);
        }
    }

    public List<BuoiHoc> getAllBuoiByLopAllApi(String token, Long idLop) throws Exception {
        String profileUrl = "http://18.141.201.212:8080/buoihoc/getbuoiHocByLop/" + idLop; // Đảm bảo URL này đúng
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
                Type listType = new TypeToken<List<BuoiHoc>>() {
                }.getType();
                return gson.fromJson(response.toString(), listType);
            }
        } else {
            throw new Exception("Không thể gọi API profile, mã phản hồi: " + responseCode);
        }
    }

    public BuoiHoc createBuoiHoc(String token, BuoiHoc buoiHoc, Long idLop) throws Exception {
        String apiUrl = "http://localhost:8080/buoihoc/createBuoiHoc/" + idLop;
        HttpURLConnection conn = null;

        try {
            // Mở kết nối đến APIf
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();

            // Cấu hình kết nối
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + token); // Gửi token xác thực
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true); // Cho phép gửi dữ liệu qua request body

            // Chuyển đổi ngày sang định dạng ISO 8601
            CreateBuoiDTO buoiDTO = new CreateBuoiDTO();
            SimpleDateFormat isoDateFormat = new SimpleDateFormat("MMM dd, yyyy, h:mm:ss a");

            if (buoiHoc.getNgayHoc() != null) {
                buoiDTO.setNgayHoc(isoDateFormat.format(buoiHoc.getNgayHoc()));
                buoiHoc.setNgayHoc(null);
                buoiHoc.setLopHoc(null);
            }
            buoiDTO.setBuoiHoc(buoiHoc);
            System.out.println(buoiDTO.getNgayHoc());
//            System.out.println(buoiHoc.getNgayHoc().toString());
            // Chuyển đối tượng CreateBuoiDTO sang JSON
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();
            String jsonBody = gson.toJson(buoiDTO);

            // Log dữ liệu JSON để kiểm tra
            System.out.println("Payload gửi lên API: " + jsonBody);

            // Gửi dữ liệu qua request body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }

            // Kiểm tra mã phản hồi từ server
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                // Đọc dữ liệu JSON trả về
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }

                    // Log phản hồi để kiểm tra
                    System.out.println("Phản hồi từ API: " + response);

                    // Chuyển đổi JSON trả về thành đối tượng BuoiHoc
                    return gson.fromJson(response.toString(), BuoiHoc.class);
                }
            } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                // Đọc thông tin lỗi từ response body
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorResponse.append(line.trim());
                    }

                    // Log thông tin lỗi
                    System.err.println("Lỗi từ API (400): " + errorResponse);
                    throw new Exception("Yêu cầu không hợp lệ: " + errorResponse);
                }
            } else {
                throw new Exception("Lỗi không xác định: " + responseCode);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public BuoiHoc loadApiDeleteBuoiHoc(String token, Long id) throws Exception {
        String apiUrl = "http://18.141.201.212:8080/buoihoc/deleteBuoiById/" + id;
        HttpURLConnection conn = null;

        try {
            // Mở kết nối đến API
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();

            // Cấu hình kết nối
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token); // Gửi token xác thực
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

                    // Log phản hồi để kiểm tra
                    System.out.println("Phản hồi từ API: " + response);

                    // Chuyển đổi JSON trả về thành đối tượng ThanhToan
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                            .create();
                    return gson.fromJson(response.toString(), BuoiHoc.class);
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new Exception("Không tìm thấy thanh toán.");
            } else {
                throw new Exception("Lỗi khi gọi API, mã phản hồi: " + responseCode);
            }
        } finally {
            if (conn != null) {
                conn.disconnect(); // Đóng kết nối
            }
        }
    }

    public BuoiHoc loadApiGetBuoiHoc(String token, Long id) throws Exception {
        String apiUrl = "http://18.141.201.212:8080/buoihoc/getBuoiById/" + id;
        HttpURLConnection conn = null;

        try {
            // Mở kết nối đến API
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();

            // Cấu hình kết nối
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token); // Gửi token xác thực
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

                    // Log phản hồi để kiểm tra
                    System.out.println("Phản hồi từ API: " + response);

                    // Chuyển đổi JSON trả về thành đối tượng ThanhToan
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                            .create();
                    return gson.fromJson(response.toString(), BuoiHoc.class);
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new Exception("Không tìm thấy thanh toán.");
            } else {
                throw new Exception("Lỗi khi gọi API, mã phản hồi: " + responseCode);
            }
        } finally {
            if (conn != null) {
                conn.disconnect(); // Đóng kết nối
            }
        }
    }
}

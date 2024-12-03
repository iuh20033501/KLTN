/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mycompany.destop.DTO.SigninDTO;
import com.mycompany.destop.Modul.HoaDon;
import com.mycompany.destop.Modul.TaiKhoanLogin;
import com.mycompany.destop.Modul.ThanhToan;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
public class HoaDonService {

    private Gson gson = new Gson();

    public List<HoaDon> getAllHoaDonApi(String token) throws Exception {
        String profileUrl = "http://localhost:8081/hoaDon/getAll"; // Đảm bảo URL này đúng
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
                Type listType = new TypeToken<List<HoaDon>>() {
                }.getType();
                return gson.fromJson(response.toString(), listType);
            }
        } else {
            throw new Exception("Không thể gọi API profile, mã phản hồi: " + responseCode);
        }
    }
    public HoaDon getHoaDonByIdApi(String token,Long idHoaDon) throws Exception {
        String profileUrl = "http://localhost:8081/hoaDon/getById/"+idHoaDon; // Đảm bảo URL này đúng
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
                Type hoaDon = new TypeToken<HoaDon>() {
                }.getType();
                return gson.fromJson(response.toString(), hoaDon);
            }
        } else {
            throw new Exception("Không thể gọi API profile, mã phản hồi: " + responseCode);
        }
    }

    public List<ThanhToan> FindThanhToanByIdLop(String token, Long idLop) throws Exception {
        String apiUrl = "http://localhost:8081/thanhToan/findByIdLop/" + idLop;
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

                    // Chuyển đổi JSON trả về thành danh sách đối tượng ThanhToan
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                            .create();
                    Type listType = new TypeToken<List<ThanhToan>>() {
                    }.getType();
                    return gson.fromJson(response.toString(), listType);
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new Exception("Không tìm thấy thông tin thanh toán cho lớp học ID: " + idLop);
            } else {
                throw new Exception("Lỗi khi gọi API, mã phản hồi: " + responseCode);
            }
        } finally {
            if (conn != null) {
                conn.disconnect(); // Đóng kết nối
            }
        }
    }
    public List<ThanhToan> FindThanhToanWaitByIdHocVien(String token, Long idHocVien) throws Exception {
        String apiUrl = "http://localhost:8081/thanhToan/findByIdHocVienWait/" + idHocVien;
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

                    // Chuyển đổi JSON trả về thành danh sách đối tượng ThanhToan
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                            .create();
                    Type listType = new TypeToken<List<ThanhToan>>() {
                    }.getType();
                    return gson.fromJson(response.toString(), listType);
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new Exception("Không tìm thấy thông tin thanh toán cho lớp học ID: " + idHocVien);
            } else {
                throw new Exception("Lỗi khi gọi API, mã phản hồi: " + responseCode);
            }
        } finally {
            if (conn != null) {
                conn.disconnect(); // Đóng kết nối
            }
        }
    }
    public List<ThanhToan> FindThanhToanByIdHoaDon(String token, Long idHoaDon) throws Exception {
        String apiUrl = "http://localhost:8081/thanhToan/findByIdHD/" + idHoaDon;
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

                    // Chuyển đổi JSON trả về thành danh sách đối tượng ThanhToan
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                            .create();
                    Type listType = new TypeToken<List<ThanhToan>>() {
                    }.getType();
                    return gson.fromJson(response.toString(), listType);
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new Exception("Không tìm thấy thông tin thanh toán cho Hóa Đơn ID: " + idHoaDon);
            } else {
                throw new Exception("Lỗi khi gọi API, mã phản hồi: " + responseCode);
            }
        } finally {
            if (conn != null) {
                conn.disconnect(); // Đóng kết nối
            }
        }
    }

    public ThanhToan loadApiCreateThanhToan(String token, Long idLop, Long idHocVien) throws Exception {
        String apiUrl = "http://localhost:8081/thanhToan/createCoKiemTra/" + idLop + "/" + idHocVien;
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
                    return gson.fromJson(response.toString(), ThanhToan.class);
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new Exception("Không tìm thấy lớp học hoặc học viên.");
            } else {
                throw new Exception("Lỗi khi gọi API, mã phản hồi: " + responseCode);
            }
        } finally {
            if (conn != null) {
                conn.disconnect(); // Đóng kết nối
            }
        }
    }

    public List<ThanhToan> loadApiUploadThanhToanByLop(String token, Long idLop) throws Exception {
        String apiUrl = "http://localhost:8081/thanhToan/uploadByLop/" + idLop;
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

                    // Chuyển đổi JSON trả về thành danh sách đối tượng ThanhToan
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                            .create();
                    Type listType = new TypeToken<List<ThanhToan>>() {
                    }.getType();
                    return gson.fromJson(response.toString(), listType);
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new Exception("Không tìm thấy lớp học.");
            } else {
                throw new Exception("Lỗi khi gọi API, mã phản hồi: " + responseCode);
            }
        } finally {
            if (conn != null) {
                conn.disconnect(); // Đóng kết nối
            }
        }
    }
    public ThanhToan loadApiDeleteThanhToan(String token, Long id) throws Exception {
    String apiUrl = "http://localhost:8081/thanhToan/delete/" + id;
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
                return gson.fromJson(response.toString(), ThanhToan.class);
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

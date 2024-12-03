/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mycompany.destop.DTO.CreateLopDTO;
import com.mycompany.destop.DTO.SigninDTO;
import com.mycompany.destop.Modul.KhoaHoc;
import com.mycompany.destop.Modul.LopHoc;
import com.mycompany.destop.Modul.TaiKhoanLogin;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Windows 10
 */
public class LopHocService {

    private Gson gson = new Gson();

    public List<LopHoc> getAllLopHocApi(String token) throws Exception {
        String profileUrl = "http://localhost:8081/lopHoc/getAll"; // Đảm bảo URL này đúng
        URL url = new URL(profileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
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

                    // Sử dụng Gson để chuyển đổi JSON thành danh sách LopHoc
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                            .create();
                    Type listType = new TypeToken<List<LopHoc>>() {
                    }.getType();
                    return gson.fromJson(response.toString(), listType); // Trả về danh sách LopHoc
                }
            } else {
                throw new Exception("Không thể gọi API lớp học, mã phản hồi: " + responseCode);
            }
        } catch (IOException e) {
            throw new Exception("Lỗi khi kết nối tới API: " + e.getMessage());
        } finally {
            conn.disconnect(); // Đảm bảo đóng kết nối
        }
    }

    public LopHoc UpdateLopHoc(String token, LopHoc lopHoc, Long idKhoa, Long idGV, Long idLop) throws Exception {
        String apiUrl = "http://localhost:8081/lopHoc/create/" + idKhoa + "/" + idGV + "/" + idLop; // URL API
        HttpURLConnection conn = null;

        try {
            // Mở kết nối đến API
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();

            // Cấu hình kết nối
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + token); // Gửi token xác thực
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true); // Cho phép gửi dữ liệu qua request body

            // Chuyển đối tượng LopHoc sang JSON
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) // Xử lý LocalDate nếu cần
                    .create();
            String jsonBody = gson.toJson(lopHoc);

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

                    // Chuyển đổi JSON trả về thành đối tượng LopHoc
                    return gson.fromJson(response.toString(), LopHoc.class);
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new Exception("Không tìm thấy giảng viên hoặc khóa học.");
            } else {
                throw new Exception("Lỗi khi gọi API, mã phản hồi: " + responseCode);
            }
        } finally {
            if (conn != null) {
                conn.disconnect(); // Đóng kết nối
            }
        }
    }

    public LopHoc createLopHoc(String token, LopHoc lopHoc, Long idKhoa, Long idGV) throws Exception {
        String apiUrl = "http://localhost:8081/lopHoc/createLopDestop/" + idKhoa + "/" + idGV;
        HttpURLConnection conn = null;

        try {
            // Mở kết nối đến API
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();

            // Cấu hình kết nối
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + token); // Gửi token xác thực
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true); // Cho phép gửi dữ liệu qua request body

            // Chuyển đổi ngày sang định dạng ISO 8601
            CreateLopDTO lopDTO = new CreateLopDTO();
            SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            if (lopHoc.getNgayBD() != null) {
                lopDTO.setNgayBD(isoDateFormat.format(lopHoc.getNgayBD()));
                lopHoc.setNgayBD(null);
            }
            if (lopHoc.getNgayKT() != null) {
                lopDTO.setNgayKT(isoDateFormat.format(lopHoc.getNgayKT()));
                lopHoc.setNgayKT(null);
            }
            lopDTO.setLop(lopHoc);

            // Chuyển đối tượng CreateLopDTO sang JSON
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();
            String jsonBody = gson.toJson(lopDTO);

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

                    // Chuyển đổi JSON trả về thành đối tượng LopHoc
                    return gson.fromJson(response.toString(), LopHoc.class);
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
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new Exception("Không tìm thấy giảng viên hoặc khóa học.");
            } else {
                throw new Exception("Lỗi khi gọi API, mã phản hồi: " + responseCode);
            }
        } finally {
            if (conn != null) {
                conn.disconnect(); // Đóng kết nối
            }
        }
    }

    public LopHoc loadLopHocById(String token, Long idLop) throws Exception {
        String apiUrl = "http://localhost:8081/lopHoc/getLop/" + idLop; // URL API với tham số ID
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
                    return gson.fromJson(response.toString(), LopHoc.class);
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                // Trường hợp không tìm thấy khóa học
                throw new Exception("Không tìm thấy khóa học với ID: " + idLop);
            } else {
                throw new Exception("Không thể gọi API, mã phản hồi: " + responseCode);
            }
        } finally {
            if (conn != null) {
                conn.disconnect(); // Đóng kết nối
            }
        }
    }

    public LopHoc deleteLopHoc(String token, Long id) throws Exception {
        String apiUrl = "http://localhost:8081/lopHoc/delete/" + id; // URL endpoint của API xóa lớp học
        HttpURLConnection conn = null;

        try {
            // Mở kết nối đến API
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();

            // Cấu hình kết nối
            conn.setRequestMethod("GET"); // Phương thức GET
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

                    // Chuyển đổi JSON trả về thành đối tượng LopHoc
                   Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()) // Xử lý LocalDate nếu cần
                            .create();
                    return gson.fromJson(response.toString(), LopHoc.class);
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new Exception("Lớp học không tồn tại.");
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
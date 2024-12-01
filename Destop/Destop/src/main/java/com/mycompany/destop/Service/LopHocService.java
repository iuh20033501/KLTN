/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mycompany.destop.DTO.SigninDTO;
import com.mycompany.destop.Modul.LopHoc;
import com.mycompany.destop.Modul.TaiKhoanLogin;
import java.io.BufferedReader;
import java.io.IOException;
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
                Type listType = new TypeToken<List<LopHoc>>() {}.getType();
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

}

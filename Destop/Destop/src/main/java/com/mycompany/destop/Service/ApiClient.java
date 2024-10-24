package com.mycompany.destop;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mycompany.destop.Reponse.ApiResponse;
import com.mycompany.destop.Reponse.JwtResponse;
import com.mycompany.destop.Reponse.LoginRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JOptionPane;

public class ApiClient {
    private Gson gson = new Gson();

    // Hàm GET
    public ApiResponse fetchAPI(String urlString) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return gson.fromJson(response.toString(), ApiResponse.class);
            } else {
                System.out.println("GET request không thành công, mã lỗi: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
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
        JOptionPane.showMessageDialog(null, "Đăng nhập thất bại (lỗi " + responseCode +")");
        return null; // Hoặc ném một ngoại lệ tùy ý
    }
   }

//    // Hàm POST
//    public <T> ApiResponse<T> postAPI(String urlString, LoginRequest requestBody, Class<T> responseType) {
//  
//    StringBuilder response = new StringBuilder();
//    try {
//        URL url = new URL(urlString);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json"); 
//        conn.setDoOutput(true);
//
//        // Ghi dữ liệu vào body
//        try (OutputStream os = conn.getOutputStream()) {
//            String jsonInputString = gson.toJson(requestBody);
//            os.write(jsonInputString.getBytes("UTF-8"));
//            os.flush();
//        }
//
//        int responseCode = conn.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            // Chuyển đổi phản hồi sang kiểu dữ liệu mong muốn
//           ApiResponse<T> apiResponse = gson.fromJson(response.toString(), new TypeToken<ApiResponse<T>>(){}.getType());
//            System.out.println(apiResponse);
//            return apiResponse;
//        } else {
//            System.out.println(requestBody.toString());
//            System.out.println("POST request không thành công, mã lỗi: " + responseCode);
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//    return null;
//}


}

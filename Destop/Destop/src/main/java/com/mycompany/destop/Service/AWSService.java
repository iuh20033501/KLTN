package com.mycompany.destop.Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.regions.Region;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class AWSService {

   

    private final S3Client s3Client = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
            ))
            .build();

    /**
     * Hàm upload ảnh lên AWS S3
     *
     * @param localFilePath Đường dẫn file ảnh trong máy
     * @param s3Key Tên file hoặc đường dẫn file trong S3 bucket
     * @return URL công khai của file đã upload
     * @throws IOException Nếu file không tồn tại hoặc xảy ra lỗi khi upload
     */
    public String uploadImage(String localFilePath, String s3Key) throws IOException {
        File file = new File(localFilePath);

// Kiểm tra file có tồn tại không
        if (!file.exists()) {
            throw new IOException("File không tồn tại: " + localFilePath);
        }

// Kiểm tra định dạng file
        String fileName = file.getName();
        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".png")) {
            throw new IOException("Chỉ cho phép upload các file ảnh định dạng .jpg, .jpeg, .png");
        }

// Kiểm tra kích thước file (giới hạn dưới 25MB)
        long fileSizeInMB = file.length() / (1024 * 1024);
        if (fileSizeInMB > 25) {
            throw new IOException("Kích thước file vượt quá 25MB");
        }

// Thêm ngày tháng năm giờ phút giây vào tên file để tránh trùng lặp
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        String extension = fileName.substring(fileName.lastIndexOf(".")); // Lấy phần đuôi file (.jpg, .png)
        String uniqueFileName = fileName.replace(extension, "") + "_" + timeStamp + extension;

// Thêm thư mục images vào s3Key
        String s3KeyWithImagesFolder = "images/" + uniqueFileName;

// Cấu hình request để upload file
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3KeyWithImagesFolder) // Đưa file vào thư mục images
                .acl("public-read") // Để file có thể truy cập công khai
                .build();

// Upload file lên S3
        s3Client.putObject(putObjectRequest, Paths.get(localFilePath));

// Trả về URL công khai của file
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + s3KeyWithImagesFolder;
    }

    /**
     * Hàm xóa ảnh trên AWS S3
     *
     * @param s3Key Tên file hoặc đường dẫn file trong S3 bucket cần xóa
     * @return Trạng thái xóa (true nếu xóa thành công)
     */
    public boolean deleteImage(String s3Key) {
        try {
            // Cấu hình request để xóa file
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            return true;
        } catch (Exception e) {
            System.err.println("Xóa file thất bại: " + e.getMessage());
            return false;
        }
    }

    // Đóng kết nối S3Client
    public void close() {
        s3Client.close();
    }
}

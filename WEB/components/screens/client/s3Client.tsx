import AWS from 'aws-sdk';
import { AWSConfig } from '@/config/AWSConfig';

//Cấu hình AWS S3 sử dụng các biến môi trường
const s3 = new AWS.S3({
    accessKeyId: AWSConfig.accessKeyId,
    secretAccessKey: AWSConfig.secretAccessKey,
    region: AWSConfig.region,
});

const bucketName = AWSConfig.bucketName;

// Hàm upload file lên S3
export const uploadFileToS3 = async (file: Blob, fileName: string): Promise<string | null> => {
    const params = {
        Bucket: bucketName,
        Key: fileName,
        Body: file,
        ContentType: file.type,
    };

    try {
        const data = await s3.upload(params).promise();
        console.log('File uploaded successfully:', data.Location);
        return data.Location; // URL của file sau khi upload
    } catch (error) {
        console.error('Upload failed:', error);
        return null;
    }
};

// Hàm download file từ S3
export const getFileFromS3 = async (fileName: string): Promise<Blob | null> => {
    const params = {
        Bucket: bucketName,
        Key: fileName, // fileName phải đúng và bao gồm cả đường dẫn nếu có
    };

    console.log('Fetching file with key:', fileName);

    try {
        const data = await s3.getObject(params).promise();
        if (data.Body) {
            console.log('File fetched successfully:', fileName);
            return new Blob([data.Body as ArrayBuffer]);
        }
        console.warn('No body found in the response for:', fileName);
        return null;
    } catch (error) {
        console.error('Download failed for key:', fileName, error);
        return null;
    }
};

export const deleteFileFromS3 = async (fileName: string): Promise<boolean> => {
    const params = {
        Bucket: bucketName,
        Key: fileName,
    };

    try {
        await s3.deleteObject(params).promise();
        console.log('File deleted successfully:', fileName);
        return true;
    } catch (error) {
        console.error('Delete failed:', error);
        return false;
    }
};
export const checkFileExistsInS3 = async (fileName: string): Promise<boolean> => {
    const params = {
        Bucket: bucketName,
        Key: fileName,
    };
    try {
        await s3.headObject(params).promise(); // Kiểm tra sự tồn tại
        return true;
    } catch (error) {
        console.error("File not found:", fileName, error);
        return false;
    }
};

export default s3;

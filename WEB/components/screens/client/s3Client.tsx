import AWS from 'aws-sdk';

// Cấu hình AWS S3 sử dụng các biến môi trường
const s3 = new AWS.S3({
    accessKeyId: process.env.AWS_ACCESS_KEY_ID,
    secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY,
    region: process.env.AWS_REGION,
});

const bucketName = process.env.AWS_BUCKET_NAME || '';

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
        Key: fileName,
    };

    try {
        const data = await s3.getObject(params).promise();
        if (data.Body) {
            return new Blob([data.Body as ArrayBuffer]);
        }
        return null;
    } catch (error) {
        console.error('Download failed:', error);
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

export default s3;
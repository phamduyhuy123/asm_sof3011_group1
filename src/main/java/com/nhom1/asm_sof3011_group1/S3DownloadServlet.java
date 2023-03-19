package com.nhom1.asm_sof3011_group1;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.nhom1.asm_sof3011_group1.utils.AwsS3Service;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@WebServlet(name = "testS3DownloadService", value = "/awsS3Download")
@MultipartConfig
public class S3DownloadServlet extends HttpServlet {
    private static final String BUCKET_NAME = AwsS3Service.BUCKET_NAME;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String key = "video/324244737_1221019981860656_886954504863316806_n_2.mp4"; // Replace with the path to your S3 object
        String filename = "324244737_1221019981860656_886954504863316806_n_2.mp4"; // Replace with the name of the file that will be downloaded by the client

        AmazonS3 s3client = AwsS3Service.s3Client();

        S3Object s3Object = s3client.getObject(BUCKET_NAME, key);

        response.setContentType(s3Object.getObjectMetadata().getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        InputStream inputStream = s3Object.getObjectContent();
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outputStream.close();
    }
}

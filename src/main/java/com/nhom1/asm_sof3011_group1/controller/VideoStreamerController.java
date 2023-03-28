package com.nhom1.asm_sof3011_group1.controller;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.nhom1.asm_sof3011_group1.dao.VideoDao;
import com.nhom1.asm_sof3011_group1.model.Video;
import com.nhom1.asm_sof3011_group1.utils.AwsS3Service;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.Date;

@WebServlet("/streamVideo")
public class VideoStreamerController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id =  Long.parseLong(request.getParameter("videoId"));
        VideoDao videoDao=new VideoDao();
        Video video= videoDao.findById(id);
        String fileName=video.getVideoUrl();
        // Get the S3 bucket and key for the video file
        String bucketName = AwsS3Service.BUCKET_NAME;
        String key = "video/"+fileName;
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1 hour
        expiration.setTime(expTimeMillis);
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, key)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        AmazonS3 s3client = AwsS3Service.s3Client();
        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);


        response.setContentType("text/plain");
        response.getWriter().write(url.toString());



    }
}
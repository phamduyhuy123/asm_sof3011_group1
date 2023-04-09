package com.nhom1.asm_sof3011_group1.controller;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.nhom1.asm_sof3011_group1.dao.UserDao;
import com.nhom1.asm_sof3011_group1.dao.VideoDao;
import com.nhom1.asm_sof3011_group1.dao.ViewHistoryDao;
import com.nhom1.asm_sof3011_group1.model.User;
import com.nhom1.asm_sof3011_group1.model.Video;
import com.nhom1.asm_sof3011_group1.model.ViewHistory;
import com.nhom1.asm_sof3011_group1.utils.AwsS3Service;

import javax.servlet.ServletConfig;
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


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Long id = Long.parseLong(request.getParameter("videoId"));
        Long userId = request.getParameter("userId") == null ? null : Long.parseLong(request.getParameter("userId"));
        ViewHistoryDao viewHistoryDao=new ViewHistoryDao();
        VideoDao videoDao=new VideoDao();
        UserDao userDao=new UserDao();
        if (userId != null  ){
            ViewHistory tempViewHistory=viewHistoryDao.findByUserIdAndVideoId(userId,id);
            if(tempViewHistory == null){
                Video video = videoDao.findById(id);
                User user = userDao.findById(userId);
                viewHistoryDao.insert(
                        ViewHistory.builder()
                                .viewDate(new Date())
                                .video(video)
                                .user(user)
                                .build()
                );
            }else {
                tempViewHistory.setViewDate(new Date());
                viewHistoryDao.update(tempViewHistory);
            }

        }
        Video video = videoDao.findById(id);
        long views = video.getViews();
        video.setViews(views + 1);
        videoDao.update(video);
        String fileName = video.getVideoUrl();
        String bucketName = AwsS3Service.BUCKET_NAME;
        String key = "video/" + video.getUser().getId() + "_" + fileName;
//        Date expiration = new Date();
//        long expTimeMillis = expiration.getTime();
//        expTimeMillis += 1000 * 60 * 60; // 1 hour
//        expiration.setTime(expTimeMillis);
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, key)
                        .withMethod(HttpMethod.GET);
        AmazonS3 s3client = AwsS3Service.s3Client();
        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

        response.setContentType("text/plain");
        response.getWriter().write(url.toString());


    }
}
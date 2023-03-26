package com.nhom1.asm_sof3011_group1.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom1.asm_sof3011_group1.dao.VideoDao;
import com.nhom1.asm_sof3011_group1.model.Video;
import com.nhom1.asm_sof3011_group1.utils.AwsS3Service;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

@WebServlet( name = "videos", value = {"/videos","/videoDetail","/video/poster"})
public class VideosServlet extends HttpServlet {
    private VideoDao videoDao;
    private ObjectMapper mapper;
    @Override
    public void init(ServletConfig config) throws ServletException {

        videoDao=new VideoDao();
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String jsonData="";
        Long id = req.getParameter("videoId")==null ? null:Long.parseLong(req.getParameter("videoId"));
        if(req.getRequestURI().contains("/videos")){
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            List<Video> videos=videoDao.findAll();
            jsonData=mapper.writeValueAsString(videos);

            out.print(jsonData);
            out.close();

        }else if(req.getRequestURI().contains("/videoDetail")){
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            Video video=videoDao.findById(id);
            jsonData=mapper.writeValueAsString(video);

            out.print(jsonData);
            out.close();

        } else if(req.getRequestURI().contains("/video/poster")){

            Video video=videoDao.findById(id);

            String bucketName = AwsS3Service.BUCKET_NAME;
            String key = "video/"+video.getVideoUrl();

            // Create an S3 client
            AmazonS3 s3client = AwsS3Service.s3Client();

            // Get the S3 object for the video file
            S3Object s3Object = s3client.getObject(new GetObjectRequest(bucketName, key));
            InputStream inputStream = s3Object.getObjectContent();

            // Create a new FFmpegFrameGrabber for the video file input stream
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputStream);

            // Start the grabber to get the video frames
            grabber.start();

            // Get the first frame of the video and save it as an image
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage image = converter.convert(grabber.grabImage());
            grabber.stop();

            resp.setContentType("image/jpeg"); // set content type of response
            OutputStream outputStream = resp.getOutputStream();
            ImageIO.write(image, "jpeg", outputStream);
            outputStream.close();
        }


    }

    @Override
    public void destroy() {

    }
}

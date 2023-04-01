package com.nhom1.asm_sof3011_group1.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom1.asm_sof3011_group1.dao.VideoDao;
import com.nhom1.asm_sof3011_group1.model.Video;
import com.nhom1.asm_sof3011_group1.utils.AwsS3Service;
import org.apache.commons.io.FilenameUtils;
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
import java.io.*;
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
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String jsonData="";
        Long id = req.getParameter("videoId")==null ? null:Long.parseLong(req.getParameter("videoId"));
        if(req.getRequestURI().contains("/videos")){
            String limit =req.getParameter("limit");
            String offSet=req.getParameter("offset");
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            List<Video> videos=videoDao.findAllPagination(limit,offSet);
            jsonData=mapper.writeValueAsString(videos);
            System.out.println(jsonData);
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
            AmazonS3 s3client = AwsS3Service.s3Client();
            if(video.getThumbnailUrl()==null ||video.getThumbnailUrl().isEmpty()){
                S3Object s3Object = s3client.getObject(new GetObjectRequest(bucketName, key));
                InputStream inputStream = s3Object.getObjectContent();
                FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputStream);
                grabber.start();
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage image = converter.convert(grabber.grabImage());
                grabber.stop();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(image, "jpeg", outputStream);
                byte[] thumbnailBytes = outputStream.toByteArray();
                String thumbnailKey = "thumbnails/" +video.getId()+"_" +FilenameUtils.removeExtension(video.getVideoUrl()) + ".jpg";
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(thumbnailBytes.length);
                s3client.putObject(AwsS3Service.BUCKET_NAME, thumbnailKey, new ByteArrayInputStream(thumbnailBytes), metadata);
                inputStream.close();
                outputStream.close();
                video.setThumbnailUrl(FilenameUtils.removeExtension(video.getVideoUrl()) + ".jpg");
                videoDao.update(video);
                s3Object = s3client.getObject(new GetObjectRequest(bucketName, thumbnailKey));
                InputStream imageFromAws = s3Object.getObjectContent();
                resp.setContentType("image/jpeg"); // set content type of response
                OutputStream respOutputStream = resp.getOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = imageFromAws .read(buffer)) != -1) {
                    respOutputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            }else {
                String thumbnailKey = "thumbnails/" +video.getId()+"_" + video.getThumbnailUrl();
                S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, thumbnailKey));
                InputStream inputStream = s3object.getObjectContent();
                resp.setContentType("image/jpeg"); // set content type of response
                OutputStream outputStream = resp.getOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            }
        }
    }
    @Override
    public void destroy() {

    }
}

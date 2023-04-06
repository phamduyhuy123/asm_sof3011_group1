package com.nhom1.asm_sof3011_group1.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom1.asm_sof3011_group1.dao.VideoDao;
import com.nhom1.asm_sof3011_group1.model.User;
import com.nhom1.asm_sof3011_group1.model.Video;
import com.nhom1.asm_sof3011_group1.utils.AwsS3Service;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MultipartConfig
@WebServlet( name = "videos", value = {"/videos","/videoDetail","/video/poster","/video/likes","/video/upload"})
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

        }
        else if(req.getRequestURI().contains("/videoDetail")){
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            Video video=videoDao.findById(id);
            jsonData=mapper.writeValueAsString(video);

            out.print(jsonData);
            out.close();

        }
        else if(req.getRequestURI().contains("/video/poster")){
            getVideoThumnail(id,req,resp);
        }
        else if (req.getRequestURI().contains("video/likes")) {

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getRequestURI().contains("video/upload")) {
            resp.setContentType("application/json");
            userUploadNewVideo(req,resp);
        }
    }

    private void getVideoThumnail(Long id, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
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
    private void userUploadNewVideo(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
//        DiskFileItemFactory factory = new DiskFileItemFactory();
//        ServletFileUpload upload = new ServletFileUpload(factory);
//        List<FileItem> items = upload.parseRequest(request);
        Part partVideo= request.getPart("videoFile")==null?null:request.getPart("videoFile");
        System.out.println(partVideo.getSize());
        Part partThumbnail=request.getPart("videoThumbnailFile").equals("undefined")?null:request.getPart("videoThumbnailFile");
        String videoTitle = request.getParameter("videoTitle").equals("undefined")?null:request.getParameter("videoTitle");
        String videoDescription = request.getParameter("videoDescription")==null?null:request.getParameter("videoDescription");
        String userString=request.getParameter("user");
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(userString, User.class);
        StringBuilder errorMsg=new StringBuilder();
        boolean isValidated=true;
        if(videoTitle==null){
            isValidated=false;
            errorMsg.append("Tiêu đề video đang để trống").append('\n');
        }
        if(partVideo==null){
            isValidated=false;
            errorMsg.append("Vui lòng chọn file video bạn muốn upload");
        }

       if(isValidated){
           Video video= Video.builder()
                   .description(videoDescription)
                   .title(videoTitle)
                   .user(user)
                   .uploadDate(new Date())
                   .thumbnailUrl(getFileName(partThumbnail))
                   .videoUrl(getFileName(partVideo))
                   .views((long)0)
                   .build();
           AmazonS3 s3client = AwsS3Service.s3Client();
           FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(partVideo.getInputStream());
           try {
               grabber.start();
               int durationInSeconds = grabber.getLengthInFrames() / (int) grabber.getFrameRate();
               System.out.println("Duration: " + durationInSeconds + " seconds");
               video.setDuration((long)durationInSeconds);
           } catch (FrameGrabber.Exception e) {
               e.printStackTrace();
           }
           s3client.putObject(
                   AwsS3Service.BUCKET_NAME,
                   "video/"+video.getUser().getId()+"_"+video.getVideoUrl(),
                   partVideo.getInputStream(),
                   new ObjectMetadata());
           if(partThumbnail!=null){
               s3client.putObject(
                       AwsS3Service.BUCKET_NAME,
                       "thumbnails/"+video.getUser().getId()+"_"+video.getThumbnailUrl(),
                       partThumbnail.getInputStream(),
                       new ObjectMetadata());
           }else {
               InputStream inputStream=partVideo.getInputStream();
               grabber = new FFmpegFrameGrabber(inputStream);
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
           }
           Video videoResponse=videoDao.findById(videoDao.insert(video));
           System.out.println(videoResponse);
           String jsonData = "";
           PrintWriter out = resp.getWriter();
           jsonData = mapper.writeValueAsString(videoResponse);
           System.out.println(jsonData);
           out.print(jsonData);
           out.close();
       }
        else {
           Map<String, Object> responseMap = new HashMap<String, Object>();
           PrintWriter out = resp.getWriter();
           responseMap.put("error", errorMsg.toString());
           out.print(mapper.writeValueAsString(responseMap));
           out.close();
       }

    }
    private String getFileName(Part part) {
        String contentDispositionHeader = part.getHeader("content-disposition");
        String[] elements = contentDispositionHeader.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
    @Override
    public void destroy() {

    }
}

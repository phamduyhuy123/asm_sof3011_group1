package com.nhom1.asm_sof3011_group1.controller.admin;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.apache.commons.io.FilenameUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom1.asm_sof3011_group1.dao.UserDao;
import com.nhom1.asm_sof3011_group1.dao.VideoDao;
import com.nhom1.asm_sof3011_group1.model.User;
import com.nhom1.asm_sof3011_group1.model.Video;
import com.nhom1.asm_sof3011_group1.utils.AwsS3Service;

@WebServlet(name = "VideoAdmin", value = {
        "/api/admin/video/edit",
        "/api/admin/video/insert",
        "/api/admin/video/delete"
})
@MultipartConfig
public class VideoAdminServlet extends HttpServlet {

    private ObjectMapper mapper;


    @Override
    public void init() throws ServletException {
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        super.doGet(req, resp);
        String uri = req.getRequestURI();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        String uri = request.getRequestURI();
        request.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        VideoDao videoDao=new VideoDao();
        if (uri.contains("api/admin/video/insert")) {
            resp.setContentType("application/json");
            Part partVideo = request.getPart("videoFile") == null ? null : request.getPart("videoFile");
            Part partThumbnail = request.getPart("videoThumbnailFile")==null ? null : request.getPart("videoThumbnailFile");
            String videoTitle = request.getParameter("videoTitle").equals("undefined") ? null : request.getParameter("videoTitle");
            String videoDescription = request.getParameter("videoDescription").equals("undefined") ? null : request.getParameter("videoDescription");
            String UserID = request.getParameter("user").equals("undefined") ? null : request.getParameter("user");
            StringBuilder errorMsg = new StringBuilder();
            System.out.println("video part" + partVideo);
            System.out.println("video part" + partThumbnail);
            UserDao userDao=new UserDao();
            boolean isValidated = true;
            if(UserID==null){
                isValidated = false;
                errorMsg.append("user id o đang để trống").append('\n');
            }else if(userDao.findById(Long.parseLong(UserID))==null){
                isValidated = false;
                errorMsg.append("Không tìm thấy user với id là:"+UserID).append('\n');
            }
            if (videoTitle == null) {
                isValidated = false;
                errorMsg.append("Tiêu đề video đang để trống").append('\n');
            }
            if (getFileName(partVideo) == null) {
                isValidated = false;
                errorMsg.append("Vui lòng chọn file video bạn muốn upload");
            }
            if(!isValidated){
                Map<String, Object> responseMap = new HashMap<String, Object>();
                PrintWriter out = resp.getWriter();
                responseMap.put("error", errorMsg.toString());
                out.print(mapper.writeValueAsString(responseMap));
                out.close();
                return;
            }

            Video video = Video.builder()
                    .description(videoDescription)
                    .title(videoTitle)
                    .user(userDao.findById(Long.parseLong(UserID)))
                    .uploadDate(new Date())
                    .thumbnailUrl(getFileName(partThumbnail))
                    .videoUrl(getFileName(partVideo))
                    .views((long) 0)
                    .build();
            AmazonS3 s3client = AwsS3Service.s3Client();
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(partVideo.getInputStream());
            try {
                grabber.start();
                int durationInSeconds = grabber.getLengthInFrames() / (int) grabber.getFrameRate();
                System.out.println("Duration: " + durationInSeconds + " seconds");
                video.setDuration((long) durationInSeconds);
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
            s3client.putObject(
                    AwsS3Service.BUCKET_NAME,
                    "video/" + video.getUser().getId() + "_" + video.getVideoUrl(),
                    partVideo.getInputStream(),
                    new ObjectMetadata());
            if (getFileName(partThumbnail) != null) {
                s3client.putObject(
                        AwsS3Service.BUCKET_NAME,
                        "thumbnails/" + video.getUser().getId() + "_" + video.getThumbnailUrl(),
                        partThumbnail.getInputStream(),
                        new ObjectMetadata());
            } else {
                InputStream inputStream = partVideo.getInputStream();
                grabber = new FFmpegFrameGrabber(inputStream);
                grabber.start();
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage image = converter.convert(grabber.grabImage());
                grabber.stop();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(image, "jpeg", outputStream);
                byte[] thumbnailBytes = outputStream.toByteArray();
                String thumbnailKey = "thumbnails/" + video.getUser().getId() + "_" + FilenameUtils.removeExtension(video.getVideoUrl()) + ".jpg";
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(thumbnailBytes.length);
                s3client.putObject(AwsS3Service.BUCKET_NAME, thumbnailKey, new ByteArrayInputStream(thumbnailBytes), metadata);
                inputStream.close();
                outputStream.close();
                video.setThumbnailUrl(FilenameUtils.removeExtension(video.getVideoUrl()) + ".jpg");
            }
            Long idReturn = videoDao.insert(video);
            Video videoResponse = videoDao.findById(idReturn);
            String jsonData = "";
            PrintWriter out = resp.getWriter();
            jsonData = mapper.writeValueAsString(videoResponse);
            System.out.println(jsonData);
            out.print(jsonData);
            out.close();


        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        String uri = request.getRequestURI();
        request.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        VideoDao videoDao=new VideoDao();
        UserDao userDao=new UserDao();
        Map<String, Object> responseMap = new HashMap<String, Object>();
        boolean isValidated=true;
        if (uri.contains("api/admin/video/edit")) {
            Part partVideo = request.getPart("videoFile") == null ? null : request.getPart("videoFile");
            Part partThumbnail = request.getPart("videoThumbnailFile")==null ? null : request.getPart("videoThumbnailFile");
            String videoTitle = request.getParameter("videoTitle").equals("undefined") ? null : request.getParameter("videoTitle");
            Long videoId = request.getParameter("videoId").equals("undefined") ? null : Long.parseLong(request.getParameter("videoId"));
            String videoDescription = request.getParameter("videoDescription").equals("undefined") ? null : request.getParameter("videoDescription");
            Long views = request.getParameter("views").equals("undefined") ? null : Long.parseLong(request.getParameter("views"));
            Long userID = request.getParameter("user").equals("undefined") ? null : Long.parseLong(request.getParameter("user"));
            Long uploadDate = request.getParameter("uploadDate").equals("undefined") ? null : Long.parseLong(request.getParameter("uploadDate"));
            StringBuilder errorMsg = new StringBuilder();
            Video videoToUpdate = videoDao.findById(videoId);
            User user = userDao.findById(userID);
            System.out.println("video to update"+videoToUpdate);
            if (videoToUpdate == null ) {
                errorMsg.append("Không tìm thấy video với id "+videoId).append('\n');
                isValidated=false;

            }
            if(user == null){
                errorMsg.append("Không tìm thấy user với id "+userID).append('\n');
                isValidated=false;
            }
            if(!isValidated){
                responseMap.put("error",errorMsg.toString());
                PrintWriter out=resp.getWriter();
                out.print(mapper.writeValueAsString(responseMap));
                out.close();
                return;
            }
            videoToUpdate.setDescription(videoDescription);
            videoToUpdate.setViews(views);
            videoToUpdate.setTitle(videoTitle);

            videoToUpdate.setUser(user);
            System.out.println(new Date(uploadDate));
            System.out.println(mapper.readValue(String.valueOf(uploadDate),Date.class));
            videoToUpdate.setUploadDate(new Date(uploadDate));
            AmazonS3 s3client = AwsS3Service.s3Client();
            assert partVideo != null;
            if (getFileName(partVideo) != null &&getFileName(partThumbnail)==null) {
                updateVideoFile(partVideo, videoToUpdate, s3client);
                FFmpegFrameGrabber grabber;
                String objectKeyThumbnail = "thumbnails/" + videoToUpdate.getUser().getId()+ "_" + videoToUpdate.getThumbnailUrl();
                DeleteObjectRequest deleteThumbnailObjectRequest = new DeleteObjectRequest(AwsS3Service.BUCKET_NAME, objectKeyThumbnail);
                s3client.deleteObject(deleteThumbnailObjectRequest);
                InputStream inputStream = partVideo.getInputStream();
                grabber = new FFmpegFrameGrabber(inputStream);
                grabber.start();
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage image = converter.convert(grabber.grabImage());
                grabber.stop();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(image, "jpeg", outputStream);
                byte[] thumbnailBytes = outputStream.toByteArray();
                String thumbnailKey = "thumbnails/" + videoToUpdate.getUser().getId() + "_" + FilenameUtils.removeExtension(videoToUpdate.getVideoUrl()) + ".jpg";
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(thumbnailBytes.length);
                s3client.putObject(AwsS3Service.BUCKET_NAME, thumbnailKey, new ByteArrayInputStream(thumbnailBytes), metadata);
                inputStream.close();
                outputStream.close();
                videoToUpdate.setThumbnailUrl(FilenameUtils.removeExtension(videoToUpdate.getVideoUrl()) + ".jpg");
            }else if(getFileName(partVideo) != null){
                updateVideoFile(partVideo, videoToUpdate, s3client);
            }
            assert partThumbnail != null;
            if (getFileName(partThumbnail) != null) {

                String objectKeyThumbnail = "thumbnails/" + videoToUpdate.getUser().getId()+ "_" + videoToUpdate.getThumbnailUrl();
                DeleteObjectRequest deleteThumbnailObjectRequest = new DeleteObjectRequest(AwsS3Service.BUCKET_NAME, objectKeyThumbnail);
                s3client.deleteObject(deleteThumbnailObjectRequest);
                videoToUpdate.setThumbnailUrl(getFileName(partThumbnail));
                s3client.putObject(
                        AwsS3Service.BUCKET_NAME,
                        "thumbnails/" + videoToUpdate.getUser().getId() + "_" + videoToUpdate.getThumbnailUrl(),
                        partThumbnail.getInputStream(),
                        new ObjectMetadata());
            }
            long videoIdReturn = videoDao.update(videoToUpdate);
            resp.getWriter().print(videoIdReturn);
        }
    }

    private void updateVideoFile(Part partVideo, Video videoToUpdate, AmazonS3 s3client) throws IOException {
        String objectKeyVideo = "video/" + videoToUpdate.getUser().getId() + "_" + videoToUpdate.getVideoUrl();
        DeleteObjectRequest deleteVideoObjectRequest = new DeleteObjectRequest(AwsS3Service.BUCKET_NAME, objectKeyVideo);
        s3client.deleteObject(deleteVideoObjectRequest);
        videoToUpdate.setVideoUrl(getFileName(partVideo));
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(partVideo.getInputStream());
        try {
            grabber.start();
            int durationInSeconds = grabber.getLengthInFrames() / (int) grabber.getFrameRate();
            System.out.println("Duration: " + durationInSeconds + " seconds");
            videoToUpdate.setDuration((long) durationInSeconds);
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        s3client.putObject(
                AwsS3Service.BUCKET_NAME,
                "video/" + videoToUpdate.getUser().getId() + "_" + videoToUpdate.getVideoUrl(),
                partVideo.getInputStream(),
                new ObjectMetadata());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        VideoDao videoDao=new VideoDao();
        if (uri.contains("api/admin/video/delete")) {
            Long videoId = req.getParameter("videoId") == null ? null : Long.parseLong(req.getParameter("videoId"));
            if(videoId!=null){
                Video videoFromRequest = videoDao.findById(videoId);
                Long videoIdReturn = videoDao.delete(videoId);
                AmazonS3 s3Client = AwsS3Service.s3Client();
                String bucketName = AwsS3Service.BUCKET_NAME;
                String objectKeyVideo = "video/" + videoFromRequest.getUser().getId() + "_" + videoFromRequest.getVideoUrl();
                String objectKeyThumbnail = "thumbnails/" + videoFromRequest.getUser().getId()+ "_" + videoFromRequest.getThumbnailUrl();
                DeleteObjectRequest deleteVideoObjectRequest = new DeleteObjectRequest(bucketName, objectKeyVideo);
                s3Client.deleteObject(deleteVideoObjectRequest);
                DeleteObjectRequest deleteThumbnailObjectRequest = new DeleteObjectRequest(bucketName, objectKeyThumbnail);
                s3Client.deleteObject(deleteThumbnailObjectRequest);
                System.out.println("File deleted successfully.");
                PrintWriter out = resp.getWriter();
                StringBuilder successMsg = new StringBuilder();
                successMsg.append("Video " + videoIdReturn + " deleted successfully");
                Map<String, Object> responseMap = new HashMap<String, Object>();
                responseMap.put("id",videoIdReturn);
                responseMap.put("successMsg",successMsg.toString());
                out.print(mapper.writeValueAsString(responseMap));
                out.close();
            }
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
}

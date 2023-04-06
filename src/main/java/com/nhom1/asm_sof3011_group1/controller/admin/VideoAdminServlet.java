package com.nhom1.asm_sof3011_group1.controller.admin;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
@WebServlet(name="VideoAdmin", value = {
		
		"/api/admin/video/insert",
		"/api/admin/uploadFile"
})
@MultipartConfig
public class VideoAdminServlet extends HttpServlet{
	private VideoDao videoDao;
	private ObjectMapper mapper;
	private UserDao userDao;
	
	@Override
	public void init() throws ServletException {
		videoDao = new VideoDao();
		mapper = new ObjectMapper();
		userDao=new UserDao();
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
		if(uri.contains("api/admin/video/insert")) {
			resp.setContentType("application/json");
			Part partVideo= request.getPart("videoFile")==null?null:request.getPart("videoFile");
	        Part partThumbnail=request.getPart("videoThumbnailFile").equals("undefined")?null:request.getPart("videoThumbnailFile");
	        String videoTitle = request.getParameter("videoTitle").equals("undefined")?null:request.getParameter("videoTitle");
	        String videoDescription = request.getParameter("videoDescription")==null?null:request.getParameter("videoDescription");
	        String UserID =request.getParameter("user");
	        StringBuilder errorMsg=new StringBuilder();
	        System.out.println("video part"+partVideo);
	        System.out.println("video part"+partThumbnail);
	        boolean isValidated=true;
	        if(videoTitle==null){
	            isValidated=false;
	            errorMsg.append("Tiêu đề video đang để trống").append('\n');
	        }
	        if(getFileName(partVideo)==null){
	            isValidated=false;
	            errorMsg.append("Vui lòng chọn file video bạn muốn upload");
	        }

	       if(isValidated){
	           Video video= Video.builder()
	                   .description(videoDescription)
	                   .title(videoTitle)
	                   .user(userDao.findById(Long.parseLong(UserID)))
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
	           if(getFileName(partThumbnail)!=null){
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
	           Long idReturn=videoDao.insert(video);
	           Video videoResponse=videoDao.findById(idReturn);
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

		}else if(uri.contains("api/admin/uploadFile")) {
			
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

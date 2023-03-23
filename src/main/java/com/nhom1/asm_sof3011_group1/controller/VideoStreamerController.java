package com.nhom1.asm_sof3011_group1.controller;

import com.amazonaws.services.s3.AmazonS3;
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


        AmazonS3 s3client = AwsS3Service.s3Client();


        S3Object s3Object = s3client.getObject(new GetObjectRequest(bucketName, key));

        String contentType = getServletContext().getMimeType(fileName);
        response.setContentType(contentType);


        long contentLength = s3Object.getObjectMetadata().getContentLength();
        response.setContentLengthLong(contentLength);


        String range = request.getHeader("Range");
        InputStream inputStream=null;
        String[] rangeParts = range.substring(6).split("-");
        long start = Long.parseLong(rangeParts[0]);
        if (range != null) {

            long end = contentLength - 1;
            if (rangeParts.length == 2) {
                end = Long.parseLong(rangeParts[1]);
            }
            long contentRangeLength = end - start + 1;
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + contentLength);
            response.setContentLengthLong(contentRangeLength);
            s3Object.getObjectMetadata().setContentLength(contentRangeLength);

        }


        inputStream = new BufferedInputStream(s3Object.getObjectContent());
        long skipped = inputStream.skip(start);
        if (skipped < start) {

            response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            return;
        }
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }
}
package com.nhom1.asm_sof3011_group1.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet("/streamVideo")
public class VideoStreamerController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //get the video file's absolute path
        String videoFilePath = "C:\\Users\\ACER\\Dropbox\\PC\\Downloads\\Video\\received_1165370370973872.mp4";

        File videoFile = new File(videoFilePath);
        long fileSize = videoFile.length();

        // Check if Range header is present
        String rangeHeader = request.getHeader("Range");
        System.out.println(rangeHeader);
        if (rangeHeader != null) {
            // Parse the Range header to get the requested byte range
            String[] rangeValues = rangeHeader.substring("bytes=".length()).split("-");
            System.out.println(rangeValues);
            long start = Long.parseLong(rangeValues[0]);
            long end = fileSize - 1;
            if (rangeValues.length > 1 && !rangeValues[1].isEmpty()) {
                end = Long.parseLong(rangeValues[1]);
            }

            // Set the Content-Range header to the requested byte range
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileSize);

            // Set the content length to the requested byte range
            response.setContentLength((int) (end - start + 1));

            // Set the input stream to read from the requested byte range
            InputStream videoInputStream = new FileInputStream(videoFile);
            videoInputStream.skip(start);

            byte[] buffer = new byte[4096];
            int bytesRead;
            OutputStream outStream = response.getOutputStream();
            while ((bytesRead = videoInputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            outStream.flush();
            outStream.close();
            videoInputStream.close();
        } else {
            // Range header not present, stream entire video file
            response.setContentType("video/mp4");
            response.setContentLength((int) fileSize);
            response.setHeader("Content-Disposition", "inline");

            InputStream videoInputStream = new FileInputStream(videoFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            OutputStream outStream = response.getOutputStream();
            while ((bytesRead = videoInputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            outStream.flush();
            outStream.close();
            videoInputStream.close();
        }
    }
}
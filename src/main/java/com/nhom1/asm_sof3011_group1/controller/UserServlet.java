package com.nhom1.asm_sof3011_group1.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom1.asm_sof3011_group1.dao.UserDao;
import com.nhom1.asm_sof3011_group1.model.Role;
import com.nhom1.asm_sof3011_group1.model.User;
import com.nhom1.asm_sof3011_group1.model.Video;
import com.nhom1.asm_sof3011_group1.utils.AwsS3Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@WebServlet(name = "user", value = {"/api/user/insert", //thêm mới user
        "/api/user/update", //update user với id truyền vào
        "/api/user/delete", //delete user với id truyền vào
        "/api/users", //Lấy tất cả user
        "/api/findUser", //Lấy 1 user bằng id truyền vào
        "/api/user/loadAvatar",
        "/user/login",
        "/user/dangky"//Lấy ảnh user từ aws s3 khi tìm được user bằng id và load ảnh lên user n
})
public class UserServlet extends HttpServlet {


    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String filename = request.getParameter("filename");
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        // Set the S3 bucket and key for the image
        String uri = req.getRequestURI();
        Long id = req.getParameter("userId") == null ? null : Long.parseLong(req.getParameter("userId"));
        UserDao userDao = new UserDao();
        if (uri.contains("/api/user/loadAvatar") && id != null) {
            getUserAvatarImage(id, resp);
        } else if (uri.contains("/api/findUser") && id != null) {
            PrintWriter out = resp.getWriter();
            String jsonData = "";
            resp.setContentType("application/json");
            User user = userDao.findById(id);
            jsonData = mapper.writeValueAsString(user);
            System.out.println(jsonData);
            out.print(jsonData);
            out.close();

        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();
        UserDao userDao = new UserDao();
        if (uri.contains("user/login")) {
            resp.setContentType("application/json");
            String username = req.getParameter("username") == null ? null : req.getParameter("username");
            String password = req.getParameter("password") == null ? null : req.getParameter("password");

            if (username != null && password != null) {
                User user = userDao.checkLogin(username, password, Role.USER);
                if (user != null) {
                    String jsonData = "";
                    PrintWriter out = resp.getWriter();
                    jsonData = mapper.writeValueAsString(user);
                    System.out.println(jsonData);
                    out.print(jsonData);
                    out.close();
                } else {
                    Map<String, Object> responseMap = new HashMap<String, Object>();
                    StringBuilder errorMsg = new StringBuilder();
                    errorMsg.append("Sai username hoặc password");
                    PrintWriter out = resp.getWriter();
                    responseMap.put("error", errorMsg.toString());
                    out.print(mapper.writeValueAsString(responseMap));
                    out.close();
                }

            }

        } else if (uri.contains("user/dangky")) {
            userRegister(req, resp);

        }

    }

    private void getUserAvatarImage(Long id, HttpServletResponse resp) throws IOException {
        UserDao userDao = new UserDao();
        User user = userDao.findById(id);
        if (user.getAvatarUrl() == null) {
            String bucketName = AwsS3Service.BUCKET_NAME;
            String key = "avatars/default-avatar-profile-icon-vector-social-media-user-portrait-176256935.jpg";
            AmazonS3 s3client = AwsS3Service.s3Client();
            S3Object s3Object = s3client.getObject(new GetObjectRequest(bucketName, key));
            InputStream inputStream = s3Object.getObjectContent();
            BufferedImage image = ImageIO.read(inputStream);
            resp.setContentType("image/jpeg");
            OutputStream outputStream = resp.getOutputStream();
            ImageIO.write(image, "jpeg", outputStream);
            outputStream.close();
            return;
        }
        String bucketName = AwsS3Service.BUCKET_NAME;
        String key = "avatars/" + user.getAvatarUrl();
        AmazonS3 s3client = AwsS3Service.s3Client();
        S3Object s3Object = s3client.getObject(new GetObjectRequest(bucketName, key));
        InputStream inputStream = s3Object.getObjectContent();
        BufferedImage image = ImageIO.read(inputStream);
        resp.setContentType("image/jpeg");
        OutputStream outputStream = resp.getOutputStream();
        ImageIO.write(image, "jpeg", outputStream);
        outputStream.close();
    }

    private void userRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        boolean isValidated = true;
        StringBuilder errorMsg = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } finally {
            reader.close();
        }
        UserDao userDao = new UserDao();
        String jsonDataFromRequest = sb.toString();
        System.out.println(jsonDataFromRequest);
        User user = mapper.readValue(jsonDataFromRequest, User.class);
        user.setJoinDate(new Date());
        System.out.println(user.toString());
        resp.setContentType("application/json");
        Map<String, Object> responseMap = new HashMap<String, Object>();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        if (user.getUsername() == null) {
            errorMsg.append("UserName đang để trống").append('\n');
            isValidated = false;
        }
        if (user.getPassword() == null) {
            errorMsg.append("Password đang để trống").append('\n');
            isValidated = false;
        }
        if (user.getEmail() == null) {
            errorMsg.append("Email đang để trống").append('\n');
            isValidated = false;
        } else if (!patternMatches(user.getEmail(), "^(.+)@(\\S+)$")) {

            errorMsg.append("Định dang email không đúng").append('\n');
            isValidated = false;
        }
        boolean isDuplicate = false;

        User checkUsername = userDao.findUserByUsername(user);
        User checkEmail = userDao.findUserByEmail(user);
        if (checkUsername != null) {
            errorMsg.append("Username này đã tồn tại trong hệ thống").append('\n');
            isDuplicate = true;
        }
        if (checkEmail != null) {
            errorMsg.append("Email này đã tồn tại trong hệ thống").append('\n');
            isDuplicate = true;
        }

        if (!isValidated || isDuplicate) {
            System.out.println("lỗi");
            PrintWriter out = resp.getWriter();
            responseMap.put("error", errorMsg.toString());
            out.print(mapper.writeValueAsString(responseMap));
            out.close();
        } else {
            System.out.println("không lỗi");
            resp.setContentType("application/json");
            Long idReturn = userDao.insert(user);
            User userResponse = userDao.findById(idReturn);
            String jsonData = "";
            PrintWriter out = resp.getWriter();
            jsonData = mapper.writeValueAsString(userResponse);
            System.out.println(jsonData);
            out.print(jsonData);
            out.close();
        }
    }

    private boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}

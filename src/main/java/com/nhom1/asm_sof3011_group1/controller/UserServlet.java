package com.nhom1.asm_sof3011_group1.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom1.asm_sof3011_group1.dao.UserDao;
import com.nhom1.asm_sof3011_group1.model.User;
import com.nhom1.asm_sof3011_group1.model.Video;
import com.nhom1.asm_sof3011_group1.utils.AwsS3Service;

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

@WebServlet(name = "user",value = {"/api/user/insert", //thêm mới user
                                    "/api/user/update", //update user với id truyền vào
                                    "/api/user/delete", //delete user với id truyền vào
                                    "/api/users", //Lấy tất cả user
                                    "/api/findUser", //Lấy 1 user bằng id truyền vào
                                    "/api/user/loadAvatar" //Lấy ảnh user từ aws s3 khi tìm được user bằng id và load ảnh lên user n
})
public class UserServlet extends HttpServlet {

    private UserDao userDao;
    @Override
    public void init(ServletConfig config) throws ServletException {
        userDao=new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String filename = request.getParameter("filename");

        // Set the S3 bucket and key for the image
        String uri =req.getRequestURI();
        Long id = req.getParameter("userId").isEmpty()? null:Long.parseLong(req.getParameter("userId"));

        if(uri.contains("/api/user/loadAvatar")){
            getUserAvatarImage(id,resp);
        }else if(uri.contains("/api/findUser")){
            PrintWriter out = resp.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            String jsonData="";
            resp.setContentType("application/json");
            User user=userDao.findById(id);
            jsonData=mapper.writeValueAsString(user);
            System.out.println(jsonData);
            out.print(jsonData);
            out.close();

        }

    }
    private void getUserAvatarImage(Long id,HttpServletResponse resp) throws IOException {
            User user= userDao.findById(id);
            String bucketName = AwsS3Service.BUCKET_NAME;
            String key = "avatars/"+user.getAvatarUrl();
            AmazonS3 s3client = AwsS3Service.s3Client();
            S3Object s3Object = s3client.getObject(new GetObjectRequest(bucketName, key));
            InputStream inputStream = s3Object.getObjectContent();
            BufferedImage image = ImageIO.read(inputStream);
            resp.setContentType("image/jpeg");
            OutputStream outputStream = resp.getOutputStream();
            ImageIO.write(image, "jpeg", outputStream);
            outputStream.close();


    }

}

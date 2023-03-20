package com.nhom1.asm_sof3011_group1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom1.asm_sof3011_group1.Employee;
import com.nhom1.asm_sof3011_group1.dao.VideoDao;
import com.nhom1.asm_sof3011_group1.model.Video;
import com.nhom1.asm_sof3011_group1.utils.JpaUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet( name = "test2", value = {"/videos","/video/*"})
public class VideosServlet extends HttpServlet {
    private VideoDao videoDao;
    @Override
    public void init(ServletConfig config) throws ServletException {

        videoDao=new VideoDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String uri=req.getRequestURI();
        String pathVariable=uri.substring(uri.lastIndexOf("/")+1);

        PrintWriter out = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        String jsonData="";
        if(uri.contains("videos")){
            List<Video> videos=videoDao.findAll();
            jsonData=mapper.writeValueAsString(videos);
            System.out.println(jsonData);
            out.print(jsonData);
            out.close();
            return;
        }else {
            System.out.println(pathVariable);
            Video videos=videoDao.findById(pathVariable);
            jsonData=mapper.writeValueAsString(videos);
            System.out.println(jsonData);
            out.print(jsonData);
            out.close();
            return;
        }


    }

    @Override
    public void destroy() {

    }
}

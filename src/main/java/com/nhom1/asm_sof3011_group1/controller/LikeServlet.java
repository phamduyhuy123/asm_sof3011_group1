package com.nhom1.asm_sof3011_group1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom1.asm_sof3011_group1.dao.LikesDao;
import com.nhom1.asm_sof3011_group1.model.Like;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "LikesVideo",value = {"/api/like/get/likeCount","/api/like/get/likeVideo"})
public class LikeServlet extends HttpServlet{

    private ObjectMapper objectMapper;
    @Override
    public void init(ServletConfig config) throws ServletException {

        objectMapper=new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String uri=req.getRequestURI();
        LikesDao likesDao=new LikesDao();
        if(uri.contains("api/like/get/likeCount")){
            Long videoId = req.getParameter("videoId")==null ? null:Long.parseLong(req.getParameter("videoId"));
            if(videoId!=null){
                LikesDao.VideoLikes likeList=likesDao.countLikeAndDisLike(videoId);
                resp.setContentType("application/json");
                PrintWriter printWriter=resp.getWriter();
                String jsonData=objectMapper.writeValueAsString(likeList);
                printWriter.print(jsonData);
                System.out.println(jsonData);
                printWriter.close();
            }
        }
        else if (uri.contains("api/like/get/likeVideo")) {
            Long videoId = req.getParameter("videoId")==null ? null:Long.parseLong(req.getParameter("videoId"));
            Long userId = req.getParameter("userId")==null ? null:Long.parseLong(req.getParameter("userId"));
            if(videoId!=null&&userId!=null){

                resp.setContentType("application/json");
                Like like=likesDao.findByUserIdAndVideoId(userId,videoId);
                PrintWriter printWriter=resp.getWriter();
                String jsonData=objectMapper.writeValueAsString(like);
                printWriter.print(jsonData);
                System.out.println(jsonData);
                printWriter.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        LikesDao likesDao=new LikesDao();
    }
}

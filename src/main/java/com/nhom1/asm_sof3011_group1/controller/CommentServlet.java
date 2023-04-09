package com.nhom1.asm_sof3011_group1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom1.asm_sof3011_group1.dao.CommentDao;
import com.nhom1.asm_sof3011_group1.dao.UserDao;
import com.nhom1.asm_sof3011_group1.dao.VideoDao;
import com.nhom1.asm_sof3011_group1.model.Comment;
import com.nhom1.asm_sof3011_group1.model.User;
import com.nhom1.asm_sof3011_group1.model.Video;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "comment",
        value = {"/api/comments",
                "/api/comment/get/childrenComment",
                "/api/comment/post/commentVideo"})
public class CommentServlet extends HttpServlet {
    private ObjectMapper mapper;
    @Override
    public void init(ServletConfig config) throws ServletException {
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String uri=req.getRequestURI();
        CommentDao commentDao =new CommentDao();
        Long videoId = req.getParameter("videoId")==null? null:Long.parseLong(req.getParameter("videoId"));
        if(uri.contains("comments") && videoId!=null){

            PrintWriter out = resp.getWriter();
            String jsonData="";
            resp.setContentType("application/json");
            List<Comment> comments =commentDao.findParentCommentsByVideoId(videoId);
            jsonData=mapper.writeValueAsString(comments);

            out.print(jsonData);
            out.close();
        }
        if(uri.contains("get/childrenComment")){
            Long parentId = req.getParameter("parentId")==null? null:Long.parseLong(req.getParameter("parentId"));
            if(parentId!=null){
                PrintWriter out = resp.getWriter();
                String jsonData="";
                resp.setContentType("application/json");

                List<Comment> comments =commentDao.findChildrenCommentByParentId(parentId);
                jsonData=mapper.writeValueAsString(comments);
                out.print(jsonData);
                System.out.println(jsonData);
                out.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String uri=req.getRequestURI();
        Long videoId = req.getParameter("videoId")==null? null:Long.parseLong(req.getParameter("videoId"));
        Long userId = req.getParameter("userId")==null? null:Long.parseLong(req.getParameter("userId"));
        Long commentId=req.getParameter("parentId")==null?null:Long.parseLong(req.getParameter("parentId"));
        CommentDao commentDao =new CommentDao();
        UserDao userDao=new UserDao();
        VideoDao videoDao=new VideoDao();
        if(uri.contains("api/comment/post/commentVideo")){
            BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream(),"UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String value = stringBuilder.toString();
            System.out.println(value);
            if(videoId!=null&&userId!=null){
                //parent comment
                if(commentId==null){
                    System.out.println("parrent comment");
                    Long returnId= commentDao.insert(
                            Comment.builder()
                                    .commentDate(new Date())
                                    .commentText(value)
                                    .user(userDao.findById(userId))
                                    .video(videoDao.findById(videoId))
                                    .build());
                    PrintWriter out = resp.getWriter();
                    String jsonData="";
                    resp.setContentType("application/json");
                    Comment comment =commentDao.findById(returnId);
                    jsonData=mapper.writeValueAsString(comment);
                    out.print(jsonData);
                    System.out.println(jsonData);
                    out.close();
                }
                //children comment
                else {
                    System.out.println("childrenComment");
                    System.out.println(commentId);
                    System.out.println(value);
                    Comment comment= Comment.builder()
                            .commentText(value)
                            .user(userDao.findById(userId))
                            .video(videoDao.findById(videoId))
                            .parentComment(commentDao.findById(commentId))
                            .commentDate(new Date())
                            .build();
                    Long returnId= commentDao.insert(comment);
                    PrintWriter out = resp.getWriter();
                    String jsonData="";
                    resp.setContentType("application/json");
                    jsonData=mapper.writeValueAsString(commentDao.findById(returnId));
                    out.print(jsonData);
                    System.out.println(jsonData);
                    out.close();

                }
            }
        }
    }
}

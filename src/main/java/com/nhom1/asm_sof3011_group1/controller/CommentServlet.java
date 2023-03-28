package com.nhom1.asm_sof3011_group1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom1.asm_sof3011_group1.dao.CommentDao;
import com.nhom1.asm_sof3011_group1.model.Comment;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "comment",
        value = {"/api/comments",
                "api/comment/get/childrenComment"})
public class CommentServlet extends HttpServlet {
    private CommentDao commentDao;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        commentDao=new CommentDao();
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        String uri=req.getRequestURI();
        Long videoId = req.getParameter("videoId").isEmpty()? null:Long.parseLong(req.getParameter("videoId"));
        if(uri.contains("comments") && videoId!=null){
            PrintWriter out = resp.getWriter();
            String jsonData="";
            resp.setContentType("application/json");
            List<Comment> comments =commentDao.findParentCommentsByVideoId(videoId);
            jsonData=mapper.writeValueAsString(comments);
            System.out.println(jsonData);
            out.print(jsonData);
            out.close();
        }
        if(uri.contains("get/childrenComment")){
            Long parentId = req.getParameter("parentId").isEmpty()? null:Long.parseLong(req.getParameter("parentId"));
            if(parentId!=null){
                PrintWriter out = resp.getWriter();
                String jsonData="";
                resp.setContentType("application/json");
                List<Comment> comments =commentDao.findParentCommentsByVideoId(videoId);
                jsonData=mapper.writeValueAsString(comments);
                System.out.println(jsonData);
                out.print(jsonData);
                out.close();
            }
        }
    }
}

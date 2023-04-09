package com.nhom1.asm_sof3011_group1.controller.admin;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom1.asm_sof3011_group1.dao.UserDao;
import com.nhom1.asm_sof3011_group1.dao.VideoDao;
import com.nhom1.asm_sof3011_group1.model.Video;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@WebServlet(name="admin",
        value = {
        "/admin",
        "/api/admin/videos",//lấy tất cả video
        "/api/admin/user",
        "/api/admin/insert", 
        "/api/admin/update",
        "/api/admin/delete", 
        "/api/admin/findById"})
public class AdminServlet extends HttpServlet {
	private UserDao userDao;
    private ObjectMapper mapper;
    private VideoDao videoDao;
    @Override
    public void init(ServletConfig config) throws ServletException {
        userDao=new UserDao();
        mapper = new ObjectMapper();
        videoDao = new VideoDao();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri=req.getRequestURI();
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        System.out.println(uri.equals("/admin"));
        if(uri.equals("/admin")){
            req.getRequestDispatcher("/views/index.jsp").forward(req,resp);
        }
        else if(uri.contains("api/admin/videos")) {
			PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            String jsonData="";
            List<Video> videos=videoDao.findAll();
            jsonData=mapper.writeValueAsString(videos);
            System.out.println(jsonData);
            out.print(jsonData);
            out.close();
		}

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String uri = req.getRequestURI();
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
    	if(uri.contains("/api/admin/videos")) {
    		
    	}
    }

}

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

import java.io.IOException;


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
        System.out.println(uri.equals("/admin"));
        if(uri.equals("/admin")){
            req.getRequestDispatcher("/views/index.jsp").forward(req,resp);
        }

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String uri = req.getRequestURI();
    	if(uri.contains("/api/admin/videos")) {
    		
    	}
    }

}

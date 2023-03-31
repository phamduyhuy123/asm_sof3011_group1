package com.nhom1.asm_sof3011_group1.controller.admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name="admin",
        value = {
        "/admin",
        "/api/admin/video",
        "/api/admin/user"})
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri=req.getRequestURI();
        System.out.println(uri.equals("/admin"));
        if(uri.equals("/admin")){
            req.getRequestDispatcher("/views/admin.jsp").forward(req,resp);
        }

    }

}

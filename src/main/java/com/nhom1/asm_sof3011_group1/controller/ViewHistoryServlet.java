package com.nhom1.asm_sof3011_group1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom1.asm_sof3011_group1.dao.ViewHistoryDao;
import com.nhom1.asm_sof3011_group1.model.ViewHistory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "viewHistory", value = {
        "/api/user/get/viewHistory"
})
public class ViewHistoryServlet extends HttpServlet {

    private ObjectMapper mapper;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mapper=new ObjectMapper();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        ViewHistoryDao viewHistoryDao=new ViewHistoryDao();
        if(req.getRequestURI().contains("api/user/get/viewHistory")){
            Long userId=req.getParameter("userId")==null||req.getParameter("userId").equals("undefined")?
                    null
                    :
                    Long.parseLong(req.getParameter("userId"));
            if(userId!=null){
                List<ViewHistory> viewHistories= viewHistoryDao.findAllByUserId(userId);
                String jsonData="";
                jsonData=mapper.writeValueAsString(viewHistories);
                PrintWriter out=resp.getWriter();
                out.print(jsonData);
                out.close();

            }
        }
    }
}

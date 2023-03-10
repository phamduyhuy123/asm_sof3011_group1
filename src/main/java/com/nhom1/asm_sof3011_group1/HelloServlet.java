package com.nhom1.asm_sof3011_group1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/testapi1")
public class HelloServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        Employee emp=new Employee();
        
        Gson gson=new Gson();
        
        List<Employee> employees=new ArrayList<>();
        employees.add(Employee.builder().id(1).name("wert").build());
        employees.add(Employee.builder().id(2).name("wefewf").build());
        employees.add(Employee.builder().id(3).name("ggwef").build());

        String jsonData=gson.toJson(employees);
        out.print(jsonData);
        out.close();
    }


}
package com.nhom1.asm_sof3011_group1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "test", value = "/testapi1")
public class HelloServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        List<Employee> employees=new ArrayList<>();
        employees.add(Employee.builder().id(1).name("wert").build());
        employees.add(Employee.builder().id(2).name("wefewf").build());
        employees.add(Employee.builder().id(3).name("ggwef").build());
        String jsonData=mapper.writeValueAsString(employees);
//        String jsonData=gson.toJson(employees);
        System.out.println(jsonData);
        out.print(jsonData);
        out.close();
    }


}
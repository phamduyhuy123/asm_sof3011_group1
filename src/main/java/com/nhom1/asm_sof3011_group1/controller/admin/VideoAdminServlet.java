package com.nhom1.asm_sof3011_group1.controller.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom1.asm_sof3011_group1.dao.UserDao;
import com.nhom1.asm_sof3011_group1.dao.VideoDao;
import com.nhom1.asm_sof3011_group1.model.User;
import com.nhom1.asm_sof3011_group1.model.Video;
@WebServlet(name="VideoAdmin", value = {
		"/api/admin/video/insert",
		"/api/admin/uploadFile"
})
public class VideoAdminServlet extends HttpServlet{
	private VideoDao videoDao;
	private ObjectMapper mapper;
	
	@Override
	public void init() throws ServletException {
		videoDao = new VideoDao();
		mapper = new ObjectMapper();
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		if(uri.contains("api/admin/video/insert")) {
			System.out.println("insert");
			StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();;
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } finally {
                reader.close();
            }

            String jsonDataFromRequest = sb.toString();
            System.out.println(jsonDataFromRequest);
            Video video = mapper.readValue(jsonDataFromRequest, Video.class);
            Long idReturn = videoDao.insert(video);
            Video userResponse= videoDao.findById(idReturn);
            String jsonData="";
    		PrintWriter out = resp.getWriter();
    		jsonData=mapper.writeValueAsString(userResponse);
            System.out.println(jsonData);
            out.print(jsonData);
            out.close();

		}else if(uri.contains("api/admin/uploadFile")) {
			
		}
	}
}

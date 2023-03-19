package com.nhom1.asm_sof3011_group1;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.nhom1.asm_sof3011_group1.utils.AwsS3Service;

@WebServlet(name = "testS3Service", value = "/asmS3")
@MultipartConfig
public class S3Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("form.jsp").forward(req,resp);
    }

    private static final String BUCKET_NAME = AwsS3Service.BUCKET_NAME;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the file from the request
        Part filePart = request.getPart("file");

        // Get the file name
        String fileName = filePart.getSubmittedFileName();

        // Create a PutObjectRequest with the file name and the file content
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, fileName, filePart.getInputStream(), null);

        try {
            // Create an S3 client using the default credentials provider
            AmazonS3 s3client = AwsS3Service.s3Client();

            // Upload the file to S3
            s3client.putObject(putObjectRequest);
            request.setAttribute("message","nice xu");
            // Redirect the user to a success page
            response.sendRedirect("success.jsp");

        } catch (AmazonServiceException e) {
            // Handle errors from the service
            e.printStackTrace();
            response.sendRedirect("error.jsp");

        } catch (AmazonClientException e) {
            // Handle errors from the client
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
package com.nhom1.asm_sof3011_group1.utils;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class AwsS3Service {
    private static final String ACCESS_KEY = "AKIAYMOFQDLLUPPFE74M";
    private static final String SECRET_KEY = "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";
    public static final String BUCKET_NAME="pdhasmsof3011";
    public static AmazonS3 s3Client(){
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
    }

    public static void main(String[] args) {
        s3Client();
    }


}

package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

@Configuration
public class AwsConfig {

  @Value("${s3.region.name}")
  private String s3RegionName;

  @Value("${sns.region.name}")
  private String snsRegionName;

  @Bean
  public AmazonS3 getAmazonS3Client() {
      //final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKeyId, accessKeySecret);
      // Get Amazon S3 client and return the S3 client object
      return AmazonS3ClientBuilder
              .standard()
              .withRegion(s3RegionName)
              .build();
  }
  
  @Bean
  public AmazonSNS getAmazonSnsClient() {
      return AmazonSNSClientBuilder
           .standard()
           .withRegion(snsRegionName)
           .build();
  }
  
}

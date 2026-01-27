package com.jhone.album.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.net.URI;

@Configuration
public class S3Config {
    @Value("${minio.endpoint}") private String endpoint;
    @Value("${minio.access-key}") private String accessKey;
    @Value("${minio.secret-key}") private String secretKey;

    @Bean
    public CommandLineRunner createBucketIfNotExists(S3Client s3Client) {
        return args -> {
            String bucketName = "album-capas";
            try {
                s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
            } catch (S3Exception e) {
                if (e.statusCode() == 404) {
                    s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
                    System.out.println("Bucket '" + bucketName + "' criado com sucesso!");
                }
            }
        };
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.US_EAST_1)
                .build();
    }
}

package com.example.demo.S3Service;


import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class S3Service {

    private final S3Client s3Client;

    public S3Service(String accessKey, String secretKey, String region) {
        this.s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.of(region))
                .build();
    }

    public void uploadFile(String bucketName, String key, String filePath) {
        s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(key).build(),
                RequestBody.fromFile(Paths.get(filePath)));
        System.out.println("Uploaded file: " + key);
    }

    public List<String> listFiles(String bucketName) {
        List<S3Object> objects = s3Client.listObjects(ListObjectsRequest.builder().bucket(bucketName).build()).contents();
        return objects.stream().map(S3Object::key).collect(Collectors.toList());
    }
}

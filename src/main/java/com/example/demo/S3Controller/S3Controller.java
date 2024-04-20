package com.example.demo.S3Controller;


import com.example.demo.S3Service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(@Value("${cloud.aws.credentials.accessKey}") String accessKey,
                        @Value("${cloud.aws.credentials.secretKey}") String secretKey,
                        @Value("${bucketName}") String bucketName,
                        @Value("${cloud.aws.region.static}") String region) {
        this.s3Service = new S3Service(accessKey, secretKey, region);
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("bucketName") String bucketName) throws IOException {
        Path tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile.toFile());
        s3Service.uploadFile(bucketName, file.getOriginalFilename(), tempFile.toString());
        return "File uploaded successfully";
    }

    @GetMapping("/list")
    public List<String> listFiles(@RequestParam("bucketName") String bucketName) {
        return s3Service.listFiles(bucketName);
    }
}

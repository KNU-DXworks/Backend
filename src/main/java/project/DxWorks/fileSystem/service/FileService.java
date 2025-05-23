package project.DxWorks.fileSystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.url}")
    private String url;

//    @Value("${aws.cloudfront.domain}")
//    private String CLOUDFRONT_DOMAIN;

    // 이미지 업로드
    public String uploadFile(MultipartFile file) throws IOException {

        String bucketName = "comhere";
        String region = "ap-northeast-2";

        String fileName = null;
        if (file.getContentType() != null){
            // pdf, image 폴더 따로 관리
            System.out.println(file.getContentType());
            if (file.getContentType().equals("application/pdf"))
            {
                fileName = "pdfs/" + UUID.randomUUID() + file.getOriginalFilename();
            }
            else {
                fileName =  "images/" + UUID.randomUUID() + file.getOriginalFilename();
            }
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
    }

    // 프론트 서버 배포 후 사용
//    private String getCloudFrontUrl(String fileName) {
//        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
//        return CLOUDFRONT_DOMAIN + "/" + encodedFileName;
//    }
}

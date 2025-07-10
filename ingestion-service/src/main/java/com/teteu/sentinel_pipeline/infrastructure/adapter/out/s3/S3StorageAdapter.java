package com.teteu.sentinel_pipeline.infrastructure.adapter.out.s3;

import com.teteu.sentinel_pipeline.application.port.out.ImageStoragePort;
import com.teteu.sentinel_pipeline.domain.model.ImageFile;
import io.awspring.cloud.autoconfigure.s3.properties.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3StorageAdapter implements ImageStoragePort {
    private final S3Client s3Client;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String save(ImageFile imageFile) {
        String key = generateKey(imageFile);

        try (InputStream inputStream = new ByteArrayInputStream(imageFile.getContent())) {

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(imageFile.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, imageFile.getContent().length));

            log.info("File uploaded successfully to S3 Bucket: {}, Key: {}", bucketName, key);
        }catch (Exception e){
            log.error("Error while uploading file to S3 Bucket: {}, Key: {}", bucketName, key, e);
            throw new RuntimeException("Error while uploading file to S3 Bucket: " + bucketName + ", Key: " + key);
        }
        return key;
    }

    private String generateKey(ImageFile imageFile){
        return UUID.randomUUID() + "-" + imageFile.getName();
    }
}

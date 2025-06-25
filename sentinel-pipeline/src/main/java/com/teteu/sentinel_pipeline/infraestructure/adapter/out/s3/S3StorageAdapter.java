package com.teteu.sentinel_pipeline.infraestructure.adapter.out.s3;

import com.teteu.sentinel_pipeline.application.port.out.ImageStoragePort;
import com.teteu.sentinel_pipeline.domain.model.ImageFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3StorageAdapter implements ImageStoragePort {
    private final S3Client s3Client;
    private final S3Properties s3Properties;

    @Override
    public String save(ImageFile imageFile) {
        String key = generateKey(imageFile);

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(key)
                    .contentType(imageFile.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(imageFile.getContent()));

            log.info("File uploaded successfully to S3 Bucket: {}, Key: {}", s3Properties.getBucketName(), key);
        }catch (Exception e){
            log.error("Error while uploading file to S3 Bucket: {}, Key: {}", s3Properties.getBucketName(), key, e);
            throw new RuntimeException("Error while uploading file to S3 Bucket: " + s3Properties.getBucketName() + ", Key: " + key);
        }
        return key;
    }

    private String generateKey(ImageFile imageFile){
        return imageFile.getName() + "_" + System.currentTimeMillis();
    }
}

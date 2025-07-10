package com.teteu.analysis_service.infraestructure.adapter.out.s3;

import com.teteu.analysis_service.application.port.out.ImageStoragePort;
import com.teteu.analysis_service.domain.exception.ImageNotFoundException;
import com.teteu.analysis_service.domain.exception.ImageProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3ImageStorageAdapter implements ImageStoragePort {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name:satellite-images}")
    private String bucketName;

    @Override
    public InputStream downloadImage(String imageId) {
        log.debug("Fazendo download da imagem do S3: bucket={}, key={}", bucketName, imageId);

        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imageId)
                    .build();

            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);

            log.debug("Download da imagem {} concluído com sucesso", imageId);
            return response;

        } catch (NoSuchKeyException e) {
            log.warn("Imagem não encontrada no S3: bucket={}, key={}", bucketName, imageId);
            throw new ImageNotFoundException(imageId);

        } catch (S3Exception e) {
            log.error("Erro do S3 ao fazer download da imagem: bucket={}, key={}",
                    bucketName, imageId, e);
            throw new ImageProcessingException("Erro ao acessar S3 para imagem: " + imageId, e);

        } catch (Exception e) {
            log.error("Erro inesperado ao fazer download da imagem: bucket={}, key={}",
                    bucketName, imageId, e);
            throw new ImageProcessingException("Erro inesperado ao fazer download da imagem: " + imageId, e);
        }
    }

    @Override
    public byte[] downloadImageAsBytes(String imageId) {
        log.debug("Fazendo download da imagem como bytes: {}", imageId);

        try (InputStream inputStream = downloadImage(imageId);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = outputStream.toByteArray();
            log.debug("Download da imagem {} concluído: {} bytes", imageId, imageBytes.length);

            return imageBytes;

        } catch (IOException e) {
            log.error("Erro ao ler dados da imagem: {}", imageId, e);
            throw new ImageProcessingException("Erro ao ler dados da imagem: " + imageId, e);
        }
    }
}
package com.teteu.sentinel_pipeline.infraestructure.adapter.config;


import com.teteu.sentinel_pipeline.infraestructure.adapter.out.s3.S3Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class S3ClientConfig {

    // Bean para instanciar o cliente do S3
    @Bean
    public S3Client s3Client(S3Properties s3Properties) {
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(s3Properties.getAccessKey(), s3Properties.getSecretKey())))
                .endpointOverride(URI.create(s3Properties.getEndpointUrl())) // Útil para testes locais
                .region(Region.of(s3Properties.getRegion()))
                .build();
    }
}


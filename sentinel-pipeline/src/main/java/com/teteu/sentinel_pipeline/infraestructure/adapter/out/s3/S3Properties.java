package com.teteu.sentinel_pipeline.infraestructure.adapter.out.s3;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.cloud.aws")
public class S3Properties {
    private String bucketName;
    private String accessKey;
    private String secretKey;
    private String region;
    private String endpointUrl;
}

package com.teteu.analysis_service.infraestructure.adapter.in.sqs.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SqsImageMessage {
    private String imageId;
    private String originalFilename;
    private String status;
    private Long timestamp;
}
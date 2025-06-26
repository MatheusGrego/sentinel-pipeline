package com.teteu.sentinel_pipeline.infraestructure.adapter.out.sqs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqsImageMessage {
    private String imageId;
    private String originalFilename;
    private String status; // Ex.: "ingested", "failed", etc.
}

package com.teteu.analysis_service.infraestructure.adapter.in.web.dto;

import com.teteu.analysis_service.domain.model.enums.AnalysisStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageAnalysisResponse {
    private String id;
    private String imageId;
    private String originalFilename;
    private AnalysisStatus status;
    private ImageClassificationResponse classification;
    private Map<String, Object> metadata;
    private String errorMessage;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Long processingTimeMs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
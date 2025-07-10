package com.teteu.analysis_service.domain.model;

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
public class ImageAnalysis {
    private String id;
    private String imageId;
    private String originalFilename;
    private AnalysisStatus status;
    private ImageClassification classification;
    private Map<String, Object> metadata;
    private String errorMessage;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Long processingTimeMs;
}
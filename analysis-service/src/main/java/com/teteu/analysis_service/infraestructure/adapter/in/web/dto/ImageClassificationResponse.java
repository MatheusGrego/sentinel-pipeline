package com.teteu.analysis_service.infraestructure.adapter.in.web.dto;

import com.teteu.analysis_service.domain.model.enums.TerrainType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageClassificationResponse {
    private TerrainType primaryTerrain;
    private BigDecimal confidence;
    private List<DetectedFeatureResponse> features;
    private QualityMetricsResponse quality;
}
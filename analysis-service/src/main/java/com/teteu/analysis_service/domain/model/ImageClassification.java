package com.teteu.analysis_service.domain.model;

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
public class ImageClassification {
    private TerrainType primaryTerrain;
    private BigDecimal confidence;
    private List<DetectedFeature> features;
    private QualityMetrics quality;
}
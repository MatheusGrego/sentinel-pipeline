package com.teteu.analysis_service.infraestructure.adapter.in.web.dto;

import com.teteu.analysis_service.domain.model.enums.FeatureType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetectedFeatureResponse {
    private FeatureType type;
    private BigDecimal confidence;
    private String description;
    private BoundingBoxResponse boundingBox;
}
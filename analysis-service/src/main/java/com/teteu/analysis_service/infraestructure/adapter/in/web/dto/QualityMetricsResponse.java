package com.teteu.analysis_service.infraestructure.adapter.in.web.dto;

import com.teteu.analysis_service.domain.model.enums.QualityRating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityMetricsResponse {
    private BigDecimal sharpness;
    private BigDecimal brightness;
    private BigDecimal contrast;
    private QualityRating overallQuality;
}
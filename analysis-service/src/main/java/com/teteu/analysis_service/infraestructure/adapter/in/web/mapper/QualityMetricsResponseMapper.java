package com.teteu.analysis_service.infraestructure.adapter.in.web.mapper;

import com.teteu.analysis_service.domain.model.QualityMetrics;
import com.teteu.analysis_service.infraestructure.adapter.in.web.dto.QualityMetricsResponse;
import org.springframework.stereotype.Component;

@Component
public class QualityMetricsResponseMapper {

    public QualityMetricsResponse toResponse(QualityMetrics domain) {
        if (domain == null) {
            return null;
        }

        return QualityMetricsResponse.builder()
                .sharpness(domain.getSharpness())
                .brightness(domain.getBrightness())
                .contrast(domain.getContrast())
                .overallQuality(domain.getOverallQuality())
                .build();
    }
}
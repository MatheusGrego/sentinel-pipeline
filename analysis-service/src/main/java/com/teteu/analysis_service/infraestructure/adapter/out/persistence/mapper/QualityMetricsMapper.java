package com.teteu.analysis_service.infraestructure.adapter.out.persistence.mapper;

import com.teteu.analysis_service.domain.model.QualityMetrics;
import com.teteu.analysis_service.infraestructure.adapter.out.persistence.entity.ImageClassificationEntity;
import com.teteu.analysis_service.infraestructure.adapter.out.persistence.entity.QualityMetricsEntity;
import org.springframework.stereotype.Component;

@Component
public class QualityMetricsMapper {

    public QualityMetricsEntity toEntity(QualityMetrics domain, ImageClassificationEntity classificationEntity) {
        if (domain == null) {
            return null;
        }

        return QualityMetricsEntity.builder()
                .classification(classificationEntity)
                .sharpness(domain.getSharpness())
                .brightness(domain.getBrightness())
                .contrast(domain.getContrast())
                .overallQuality(domain.getOverallQuality())
                .build();
    }

    public QualityMetrics toDomain(QualityMetricsEntity entity) {
        if (entity == null) {
            return null;
        }

        return QualityMetrics.builder()
                .sharpness(entity.getSharpness())
                .brightness(entity.getBrightness())
                .contrast(entity.getContrast())
                .overallQuality(entity.getOverallQuality())
                .build();
    }
}
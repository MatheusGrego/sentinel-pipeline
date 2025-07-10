package com.teteu.analysis_service.infraestructure.adapter.out.persistence.mapper;

import com.teteu.analysis_service.domain.model.ImageClassification;
import com.teteu.analysis_service.infraestructure.adapter.out.persistence.entity.ImageAnalysisEntity;
import com.teteu.analysis_service.infraestructure.adapter.out.persistence.entity.ImageClassificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class ImageClassificationMapper {

    private final DetectedFeatureMapper featureMapper;
    private final QualityMetricsMapper qualityMapper;

    public ImageClassificationEntity toEntity(ImageClassification domain, ImageAnalysisEntity analysisEntity) {
        if (domain == null) {
            return null;
        }

        ImageClassificationEntity entity = ImageClassificationEntity.builder()
                .imageAnalysis(analysisEntity)
                .primaryTerrain(domain.getPrimaryTerrain())
                .confidence(domain.getConfidence())
                .features(new ArrayList<>())
                .build();

        // Mapear features se existirem
        if (domain.getFeatures() != null) {
            domain.getFeatures().forEach(feature -> {
                entity.getFeatures().add(featureMapper.toEntity(feature, entity));
            });
        }

        // Mapear qualidade se existir
        if (domain.getQuality() != null) {
            entity.setQuality(qualityMapper.toEntity(domain.getQuality(), entity));
        }

        return entity;
    }

    public ImageClassification toDomain(ImageClassificationEntity entity) {
        if (entity == null) {
            return null;
        }

        return ImageClassification.builder()
                .primaryTerrain(entity.getPrimaryTerrain())
                .confidence(entity.getConfidence())
                .features(featureMapper.toDomainList(entity.getFeatures()))
                .quality(qualityMapper.toDomain(entity.getQuality()))
                .build();
    }
}
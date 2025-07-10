package com.teteu.analysis_service.infraestructure.adapter.out.persistence.mapper;

import com.teteu.analysis_service.domain.model.ImageAnalysis;
import com.teteu.analysis_service.infraestructure.adapter.out.persistence.entity.ImageAnalysisEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageAnalysisMapper {

    private final ImageClassificationMapper classificationMapper;

    public ImageAnalysisEntity toEntity(ImageAnalysis domain) {
        if (domain == null) {
            return null;
        }

        ImageAnalysisEntity entity = ImageAnalysisEntity.builder()
                .id(domain.getId())
                .imageId(domain.getImageId())
                .originalFilename(domain.getOriginalFilename())
                .status(domain.getStatus())
                .metadata(domain.getMetadata())
                .errorMessage(domain.getErrorMessage())
                .startedAt(domain.getStartedAt())
                .completedAt(domain.getCompletedAt())
                .processingTimeMs(domain.getProcessingTimeMs())
                .build();

        // Mapear classificação se existir
        if (domain.getClassification() != null) {
            entity.setClassification(classificationMapper.toEntity(domain.getClassification(), entity));
        }

        return entity;
    }

    public ImageAnalysis toDomain(ImageAnalysisEntity entity) {
        if (entity == null) {
            return null;
        }

        return ImageAnalysis.builder()
                .id(entity.getId())
                .imageId(entity.getImageId())
                .originalFilename(entity.getOriginalFilename())
                .status(entity.getStatus())
                .classification(classificationMapper.toDomain(entity.getClassification()))
                .metadata(entity.getMetadata())
                .errorMessage(entity.getErrorMessage())
                .startedAt(entity.getStartedAt())
                .completedAt(entity.getCompletedAt())
                .processingTimeMs(entity.getProcessingTimeMs())
                .build();
    }
}
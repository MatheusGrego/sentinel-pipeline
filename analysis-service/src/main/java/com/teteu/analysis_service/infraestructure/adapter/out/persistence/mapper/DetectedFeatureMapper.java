package com.teteu.analysis_service.infraestructure.adapter.out.persistence.mapper;

import com.teteu.analysis_service.domain.model.DetectedFeature;
import com.teteu.analysis_service.infraestructure.adapter.out.persistence.entity.DetectedFeatureEntity;
import com.teteu.analysis_service.infraestructure.adapter.out.persistence.entity.ImageClassificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DetectedFeatureMapper {

    private final BoundingBoxMapper boundingBoxMapper;

    public DetectedFeatureEntity toEntity(DetectedFeature domain, ImageClassificationEntity classificationEntity) {
        if (domain == null) {
            return null;
        }

        DetectedFeatureEntity entity = DetectedFeatureEntity.builder()
                .classification(classificationEntity)
                .type(domain.getType())
                .confidence(domain.getConfidence())
                .description(domain.getDescription())
                .build();

        // Mapear bounding box se existir
        if (domain.getBoundingBox() != null) {
            entity.setBoundingBox(boundingBoxMapper.toEntity(domain.getBoundingBox(), entity));
        }

        return entity;
    }

    public DetectedFeature toDomain(DetectedFeatureEntity entity) {
        if (entity == null) {
            return null;
        }

        return DetectedFeature.builder()
                .type(entity.getType())
                .confidence(entity.getConfidence())
                .description(entity.getDescription())
                .boundingBox(boundingBoxMapper.toDomain(entity.getBoundingBox()))
                .build();
    }

    public List<DetectedFeature> toDomainList(List<DetectedFeatureEntity> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}
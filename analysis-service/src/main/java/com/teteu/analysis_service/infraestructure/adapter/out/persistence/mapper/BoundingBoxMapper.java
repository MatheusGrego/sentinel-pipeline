package com.teteu.analysis_service.infraestructure.adapter.out.persistence.mapper;

import com.teteu.analysis_service.domain.model.BoundingBox;
import com.teteu.analysis_service.infraestructure.adapter.out.persistence.entity.BoundingBoxEntity;
import com.teteu.analysis_service.infraestructure.adapter.out.persistence.entity.DetectedFeatureEntity;
import org.springframework.stereotype.Component;

@Component
public class BoundingBoxMapper {

    public BoundingBoxEntity toEntity(BoundingBox domain, DetectedFeatureEntity featureEntity) {
        if (domain == null) {
            return null;
        }

        return BoundingBoxEntity.builder()
                .feature(featureEntity)
                .x(domain.getX())
                .y(domain.getY())
                .width(domain.getWidth())
                .height(domain.getHeight())
                .build();
    }

    public BoundingBox toDomain(BoundingBoxEntity entity) {
        if (entity == null) {
            return null;
        }

        return BoundingBox.builder()
                .x(entity.getX())
                .y(entity.getY())
                .width(entity.getWidth())
                .height(entity.getHeight())
                .build();
    }
}
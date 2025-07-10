package com.teteu.analysis_service.infraestructure.adapter.in.web.mapper;

import com.teteu.analysis_service.domain.model.ImageClassification;
import com.teteu.analysis_service.infraestructure.adapter.in.web.dto.ImageClassificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageClassificationResponseMapper {

    private final DetectedFeatureResponseMapper featureMapper;
    private final QualityMetricsResponseMapper qualityMapper;

    public ImageClassificationResponse toResponse(ImageClassification domain) {
        if (domain == null) {
            return null;
        }

        return ImageClassificationResponse.builder()
                .primaryTerrain(domain.getPrimaryTerrain())
                .confidence(domain.getConfidence())
                .features(featureMapper.toResponseList(domain.getFeatures()))
                .quality(qualityMapper.toResponse(domain.getQuality()))
                .build();
    }
}
package com.teteu.analysis_service.infraestructure.adapter.in.web.mapper;

import com.teteu.analysis_service.domain.model.DetectedFeature;
import com.teteu.analysis_service.infraestructure.adapter.in.web.dto.DetectedFeatureResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DetectedFeatureResponseMapper {

    private final BoundingBoxResponseMapper boundingBoxMapper;

    public DetectedFeatureResponse toResponse(DetectedFeature domain) {
        if (domain == null) {
            return null;
        }

        return DetectedFeatureResponse.builder()
                .type(domain.getType())
                .confidence(domain.getConfidence())
                .description(domain.getDescription())
                .boundingBox(boundingBoxMapper.toResponse(domain.getBoundingBox()))
                .build();
    }

    public List<DetectedFeatureResponse> toResponseList(List<DetectedFeature> domainList) {
        if (domainList == null) {
            return null;
        }

        return domainList.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
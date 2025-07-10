package com.teteu.analysis_service.infraestructure.adapter.in.web.mapper;

import com.teteu.analysis_service.domain.model.ImageAnalysis;
import com.teteu.analysis_service.infraestructure.adapter.in.web.dto.ImageAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ImageAnalysisResponseMapper {

    private final ImageClassificationResponseMapper classificationMapper;

    public ImageAnalysisResponse toResponse(ImageAnalysis domain) {
        if (domain == null) {
            return null;
        }

        return ImageAnalysisResponse.builder()
                .id(domain.getId())
                .imageId(domain.getImageId())
                .originalFilename(domain.getOriginalFilename())
                .status(domain.getStatus())
                .classification(classificationMapper.toResponse(domain.getClassification()))
                .metadata(domain.getMetadata())
                .errorMessage(domain.getErrorMessage())
                .startedAt(domain.getStartedAt())
                .completedAt(domain.getCompletedAt())
                .processingTimeMs(domain.getProcessingTimeMs())
                .createdAt(domain.getStartedAt())
                .updatedAt(domain.getCompletedAt() != null ? domain.getCompletedAt() : domain.getStartedAt())
                .build();
    }

    public List<ImageAnalysisResponse> toResponseList(List<ImageAnalysis> domainList) {
        if (domainList == null) {
            return null;
        }

        return domainList.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
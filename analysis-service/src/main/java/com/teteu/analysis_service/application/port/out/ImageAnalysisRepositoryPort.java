package com.teteu.analysis_service.application.port.out;

import com.teteu.analysis_service.domain.model.ImageAnalysis;

import java.util.List;
import java.util.Optional;

public interface ImageAnalysisRepositoryPort {
    ImageAnalysis save(ImageAnalysis imageAnalysis);
    Optional<ImageAnalysis> findById(String id);
    Optional<ImageAnalysis> findByImageId(String imageId);

    List<ImageAnalysis> findAll();
}
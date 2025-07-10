package com.teteu.analysis_service.application.port.in;

import com.teteu.analysis_service.domain.model.ImageAnalysis;
import com.teteu.analysis_service.infraestructure.adapter.in.web.dto.AnalysisStatsResponse;

import java.util.List;
import java.util.Optional;

public interface QueryImageAnalysisUseCase {
    Optional<ImageAnalysis> findById(String id);
    Optional<ImageAnalysis> findByImageId(String imageId);
    List<ImageAnalysis> findAll();
    AnalysisStatsResponse getAnalysisStatistics();
}
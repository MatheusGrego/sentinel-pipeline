package com.teteu.analysis_service.application.port.in;

import com.teteu.analysis_service.domain.model.ImageAnalysis;

public interface ProcessImageAnalysisUseCase {
    ImageAnalysis processImage(String imageId, String originalFilename);
}
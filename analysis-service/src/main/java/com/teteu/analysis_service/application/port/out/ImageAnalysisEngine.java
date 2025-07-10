package com.teteu.analysis_service.application.port.out;

import com.teteu.analysis_service.domain.model.ImageClassification;

public interface ImageAnalysisEngine {
    ImageClassification analyzeImage(byte[] imageData, String filename);
}
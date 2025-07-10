package com.teteu.analysis_service.infraestructure.adapter.out.analysis;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageQualityFactors {
    private double byteVariation;        // 0.0 - 1.0
    private double distribution;         // 0.0 - 1.0
    private double gradientStrength;     // 0.0 - 1.0
    private double noiseLevel;           // 0.0 - 1.0
    private double compressionArtifacts; // 0.0 - 1.0
    private int imageSize;               // em bytes
}
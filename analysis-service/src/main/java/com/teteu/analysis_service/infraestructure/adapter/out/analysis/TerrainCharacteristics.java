package com.teteu.analysis_service.infraestructure.adapter.out.analysis;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TerrainCharacteristics {
    private int colorVariation;      // 0-100
    private int textureComplexity;   // 0-100
    private int edgeDensity;         // 0-100
    private int brightnessLevel;     // 0-100
    private int contrastLevel;       // 0-100
}
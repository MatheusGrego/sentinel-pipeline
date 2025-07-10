package com.teteu.analysis_service.infraestructure.adapter.out.analysis;

import com.teteu.analysis_service.domain.model.enums.TerrainType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TerrainAnalysisResult {
    private TerrainType terrain;
    private BigDecimal confidence;
    private TerrainCharacteristics characteristics;
}
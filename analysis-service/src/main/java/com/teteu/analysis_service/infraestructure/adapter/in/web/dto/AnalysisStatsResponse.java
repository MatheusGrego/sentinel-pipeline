package com.teteu.analysis_service.infraestructure.adapter.in.web.dto;

import com.teteu.analysis_service.domain.model.enums.AnalysisStatus;
import com.teteu.analysis_service.domain.model.enums.TerrainType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisStatsResponse {
    private Map<AnalysisStatus, Long> statusCounts;
    private Map<TerrainType, Long> terrainCounts;
    private Double avgProcessingTimeMs;
    private Long totalAnalyses;
    private Long todayAnalyses;
}

package com.teteu.analysis_service.infraestructure.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisListResponse {
    private List<ImageAnalysisResponse> analyses;
    private int totalCount;
    private int page;
    private int size;
    private boolean hasNext;
    private boolean hasPrevious;
}
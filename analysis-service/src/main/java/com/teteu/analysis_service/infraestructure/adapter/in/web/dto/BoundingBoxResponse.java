package com.teteu.analysis_service.infraestructure.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoundingBoxResponse {
    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;
}
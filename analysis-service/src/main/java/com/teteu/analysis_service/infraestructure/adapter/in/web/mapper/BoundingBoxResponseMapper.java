package com.teteu.analysis_service.infraestructure.adapter.in.web.mapper;

import com.teteu.analysis_service.domain.model.BoundingBox;
import com.teteu.analysis_service.infraestructure.adapter.in.web.dto.BoundingBoxResponse;
import org.springframework.stereotype.Component;

@Component
public class BoundingBoxResponseMapper {

    public BoundingBoxResponse toResponse(BoundingBox domain) {
        if (domain == null) {
            return null;
        }

        return BoundingBoxResponse.builder()
                .x(domain.getX())
                .y(domain.getY())
                .width(domain.getWidth())
                .height(domain.getHeight())
                .build();
    }
}
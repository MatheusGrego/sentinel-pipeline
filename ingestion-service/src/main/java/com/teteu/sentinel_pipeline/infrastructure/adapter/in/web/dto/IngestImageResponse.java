package com.teteu.sentinel_pipeline.infrastructure.adapter.in.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IngestImageResponse {
    private final String message;
    private final String imageId;
}
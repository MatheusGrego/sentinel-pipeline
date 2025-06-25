package com.teteu.sentinel_pipeline.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageFile {
    private String name;
    private String contentType;
    private Long size;
    private byte[] content;
}

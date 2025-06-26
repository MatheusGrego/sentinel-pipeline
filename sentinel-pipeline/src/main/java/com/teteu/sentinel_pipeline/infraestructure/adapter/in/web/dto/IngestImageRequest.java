package com.teteu.sentinel_pipeline.infraestructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class IngestImageRequest {
    @NotNull(message = "The file cannot be null.")
    private MultipartFile file;
}
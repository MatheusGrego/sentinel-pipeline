package com.teteu.sentinel_pipeline.infrastructure.adapter.in.web;

import com.teteu.sentinel_pipeline.application.port.in.IngestImageUseCase;
import com.teteu.sentinel_pipeline.infrastructure.adapter.in.web.constants.ApiResponseMessages;
import com.teteu.sentinel_pipeline.infrastructure.adapter.in.web.dto.IngestImageRequest;
import com.teteu.sentinel_pipeline.infrastructure.adapter.in.web.dto.IngestImageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ingestion/v1/images")
@RequiredArgsConstructor
@Tag(name = "Image Ingestion Controller", description = "API for ingesting images to be processed.")
@Validated
public class ImageIngestionController {

    private final IngestImageUseCase ingestImageUseCase;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Image Ingestion",
            description = "Sends an image to be processed by the service and publishes a message to SQS."
    )
    public ResponseEntity<IngestImageResponse> ingestImage(@RequestParam("file") MultipartFile file) {
        String imageId = ingestImageUseCase.ingestImageFromMultipartFile(file);

        return ResponseEntity.ok(new IngestImageResponse(
                ApiResponseMessages.SUCCESS_MESSAGE,
                imageId
        ));
    }
}
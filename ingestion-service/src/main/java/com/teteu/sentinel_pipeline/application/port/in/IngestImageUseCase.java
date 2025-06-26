package com.teteu.sentinel_pipeline.application.port.in;

import com.teteu.sentinel_pipeline.domain.model.ImageFile;
import org.springframework.web.multipart.MultipartFile;

public interface IngestImageUseCase {
    String ingestImage(ImageFile imageFile);
    String ingestImageFromMultipartFile(MultipartFile file);
}

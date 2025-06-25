package com.teteu.sentinel_pipeline.application.port.in;

import com.teteu.sentinel_pipeline.domain.model.ImageFile;

public interface IngestImageUseCase {
    String ingestImage(ImageFile imageFile);
}

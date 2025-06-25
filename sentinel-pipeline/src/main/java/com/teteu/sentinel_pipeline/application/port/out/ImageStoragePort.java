package com.teteu.sentinel_pipeline.application.port.out;

import com.teteu.sentinel_pipeline.domain.model.ImageFile;

public interface ImageStoragePort {
    String save(ImageFile imageFile);
}

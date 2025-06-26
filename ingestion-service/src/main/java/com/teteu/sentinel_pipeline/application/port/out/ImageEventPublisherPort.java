package com.teteu.sentinel_pipeline.application.port.out;

public interface ImageEventPublisherPort {
    void publishImageIngestedEvent(String imageId, String originalFilename);
}

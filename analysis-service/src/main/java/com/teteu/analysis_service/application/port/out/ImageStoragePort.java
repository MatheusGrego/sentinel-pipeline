package com.teteu.analysis_service.application.port.out;

import java.io.InputStream;

public interface ImageStoragePort {
    InputStream downloadImage(String imageId);
    byte[] downloadImageAsBytes(String imageId);
}
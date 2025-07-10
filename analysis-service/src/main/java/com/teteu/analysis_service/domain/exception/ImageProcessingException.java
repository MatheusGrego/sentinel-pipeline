package com.teteu.analysis_service.domain.exception;

public class ImageProcessingException extends ImageAnalysisException {
    public ImageProcessingException(String message) {
        super(message);
    }

    public ImageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
package com.teteu.analysis_service.domain.exception;

public class ImageAnalysisException extends RuntimeException {
    public ImageAnalysisException(String message) {
        super(message);
    }

    public ImageAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
package com.teteu.analysis_service.domain.exception;

public class ImageNotFoundException extends ImageAnalysisException {
    public ImageNotFoundException(String imageId) {
        super("Imagem não encontrada com ID: " + imageId);
    }
}
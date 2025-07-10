package com.teteu.analysis_service.domain.constants;

import java.math.BigDecimal;

public final class AnalysisConstants {

    private AnalysisConstants() {
        // Utility class
    }

    // Limites de confiança
    public static final BigDecimal MIN_CONFIDENCE_THRESHOLD = new BigDecimal("0.70");
    public static final BigDecimal HIGH_CONFIDENCE_THRESHOLD = new BigDecimal("0.85");

    // Limites de qualidade
    public static final BigDecimal MIN_QUALITY_SCORE = new BigDecimal("0.50");
    public static final BigDecimal GOOD_QUALITY_SCORE = new BigDecimal("0.75");

    // Timeouts
    public static final long MAX_PROCESSING_TIME_MS = 240_000L; // 240 segundos

    // Tamanhos de imagem
    public static final int MIN_IMAGE_WIDTH = 100;
    public static final int MIN_IMAGE_HEIGHT = 100;
    public static final long MAX_IMAGE_SIZE_BYTES = 50 * 1024 * 1024L; // 50MB

    // Metadados
    public static final String METADATA_FILE_SIZE = "fileSize";
    public static final String METADATA_IMAGE_WIDTH = "imageWidth";
    public static final String METADATA_IMAGE_HEIGHT = "imageHeight";
    public static final String METADATA_COLOR_DEPTH = "colorDepth";
    public static final String METADATA_FORMAT = "format";
}
package com.teteu.analysis_service.infraestructure.adapter.in.web.constants;

public final class ApiConstants {

    private ApiConstants() {
        throw new IllegalStateException("Utility class");
    }

    // Base paths
    public static final String API_BASE_PATH = "/api/v1";
    public static final String ANALYSIS_PATH = API_BASE_PATH + "/analysis";

    // Query parameters
    public static final String DEFAULT_PAGE = "0";
    public static final String DEFAULT_SIZE = "20";
    public static final String MAX_SIZE = "100";

    // Headers
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String REQUEST_ID_HEADER = "X-Request-ID";

    // Cache
    public static final String CACHE_ANALYSIS_STATS = "analysis-stats";
    public static final int CACHE_TTL_SECONDS = 300; // 5 minutos

    // API Documentation
    public static final String API_TITLE = "🛰️ Sentinel Pipeline - Analysis Service";
    public static final String API_DESCRIPTION = "API para análise de imagens de satélite";
    public static final String API_VERSION = "1.0.0";

    // Error codes
    public static final String ERROR_ANALYSIS_NOT_FOUND = "ANALYSIS_NOT_FOUND";
    public static final String ERROR_IMAGE_NOT_FOUND = "IMAGE_NOT_FOUND";
    public static final String ERROR_INVALID_REQUEST = "INVALID_REQUEST";
    public static final String ERROR_INTERNAL_SERVER = "INTERNAL_SERVER_ERROR";
}
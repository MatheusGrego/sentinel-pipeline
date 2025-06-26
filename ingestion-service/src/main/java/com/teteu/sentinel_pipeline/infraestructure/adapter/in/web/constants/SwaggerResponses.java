package com.teteu.sentinel_pipeline.infraestructure.adapter.in.web.constants;

import lombok.Data;

@Data
public class SwaggerResponses {

    public static final String CODE_200 = "200";
    public static final String CODE_400 = "400";
    public static final String CODE_500 = "500";

    public static final String RESPONSE_200 = "200 OK - Request succeeded.";
    public static final String RESPONSE_400 = "400 Bad Request - Validation error in the request.";
    public static final String RESPONSE_500 = "500 Internal Server Error - Internal server error.";
}
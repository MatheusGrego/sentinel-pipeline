package com.teteu.sentinel_pipeline.infraestructure.adapter.in.web.swagger.annotations;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.teteu.sentinel_pipeline.infraestructure.adapter.in.web.constants.SwaggerResponses.CODE_400;
import static com.teteu.sentinel_pipeline.infraestructure.adapter.in.web.constants.SwaggerResponses.RESPONSE_400;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = CODE_400, description = RESPONSE_400, useReturnTypeSchema = true)
public @interface BadRequestResponse {
}
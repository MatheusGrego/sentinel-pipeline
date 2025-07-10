package com.teteu.analysis_service.infraestructure.adapter.in.web;

import com.teteu.analysis_service.infraestructure.adapter.in.web.constants.ApiConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
@Slf4j
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        String correlationId = request.getHeader(ApiConstants.CORRELATION_ID_HEADER);

        if (correlationId == null) {
            correlationId = requestId;
        }

        // Adicionar headers de resposta
        response.addHeader(ApiConstants.REQUEST_ID_HEADER, requestId);
        response.addHeader(ApiConstants.CORRELATION_ID_HEADER, correlationId);

        // Log da requisição
        log.info("🌐 {} {} - RequestId: {} - CorrelationId: {} - UserAgent: {}",
                request.getMethod(),
                request.getRequestURI(),
                requestId,
                correlationId,
                request.getHeader("User-Agent"));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        String requestId = response.getHeader(ApiConstants.REQUEST_ID_HEADER);

        if (ex != null) {
            log.error("❌ Erro na requisição - RequestId: {} - Status: {}",
                    requestId, response.getStatus(), ex);
        } else {
            log.info("✅ Requisição concluída - RequestId: {} - Status: {}",
                    requestId, response.getStatus());
        }
    }
}

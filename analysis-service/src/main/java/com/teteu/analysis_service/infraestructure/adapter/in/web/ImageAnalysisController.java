package com.teteu.analysis_service.infraestructure.adapter.in.web;

import com.teteu.analysis_service.application.port.in.QueryImageAnalysisUseCase;
import com.teteu.analysis_service.domain.exception.ImageAnalysisException;
import com.teteu.analysis_service.domain.exception.ImageNotFoundException;
import com.teteu.analysis_service.domain.model.ImageAnalysis;
import com.teteu.analysis_service.domain.model.enums.AnalysisStatus;
import com.teteu.analysis_service.infraestructure.adapter.in.web.constants.ApiConstants;
import com.teteu.analysis_service.infraestructure.adapter.in.web.dto.AnalysisListResponse;
import com.teteu.analysis_service.infraestructure.adapter.in.web.dto.AnalysisStatsResponse;
import com.teteu.analysis_service.infraestructure.adapter.in.web.dto.ErrorResponse;
import com.teteu.analysis_service.infraestructure.adapter.in.web.dto.ImageAnalysisResponse;
import com.teteu.analysis_service.infraestructure.adapter.in.web.mapper.ImageAnalysisResponseMapper;
import com.teteu.analysis_service.infraestructure.adapter.in.web.swagger.annotations.ApiAnalysisResponses;
import com.teteu.analysis_service.infraestructure.adapter.in.web.swagger.annotations.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiConstants.ANALYSIS_PATH)
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Análise de Imagens", description = "API para consulta de análises de imagens de satélite")
@ApiAnalysisResponses
public class ImageAnalysisController {

    private final QueryImageAnalysisUseCase queryUseCase;
    private final ImageAnalysisResponseMapper responseMapper;

    @Operation(summary = "🔍 Buscar análise por ID",
            description = "Retorna os detalhes completos de uma análise específica")
    @SuccessResponse(ImageAnalysisResponse.class)
    @GetMapping("/{id}")
    public ResponseEntity<ImageAnalysisResponse> getAnalysisById(
            @Parameter(description = "ID único da análise", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable String id) {

        log.info("🔍 Buscando análise por ID: {}", id);

        validateId(id);

        Optional<ImageAnalysis> analysis = queryUseCase.findById(id);

        if (analysis.isPresent()) {
            ImageAnalysisResponse response = responseMapper.toResponse(analysis.get());
            log.info("✅ Análise encontrada: {} - Status: {}", id, response.getStatus());
            return ResponseEntity.ok(response);
        } else {
            log.warn("❌ Análise não encontrada: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "🖼️ Buscar análise por ID da imagem",
            description = "Retorna a análise associada a uma imagem específica")
    @SuccessResponse(ImageAnalysisResponse.class)
    @GetMapping("/by-image/{imageId}")
    public ResponseEntity<ImageAnalysisResponse> getAnalysisByImageId(
            @Parameter(description = "ID da imagem", required = true, example = "img_123456789")
            @PathVariable String imageId) {

        log.info("🔍 Buscando análise por imageId: {}", imageId);

        validateId(imageId);

        Optional<ImageAnalysis> analysis = queryUseCase.findByImageId(imageId);

        if (analysis.isPresent()) {
            ImageAnalysisResponse response = responseMapper.toResponse(analysis.get());
            log.info("✅ Análise encontrada para imagem: {} - ID da análise: {}",
                    imageId, response.getId());
            return ResponseEntity.ok(response);
        } else {
            log.warn("❌ Análise não encontrada para imagem: {}", imageId);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "📋 Listar todas as análises",
            description = "Retorna uma lista paginada de todas as análises com filtros opcionais")
    @SuccessResponse(AnalysisListResponse.class)
    @GetMapping
    public ResponseEntity<AnalysisListResponse> getAllAnalyses(
            @Parameter(description = "Número da página (0-based)", example = "0")
            @RequestParam(defaultValue = ApiConstants.DEFAULT_PAGE)
            @Min(0) int page,

            @Parameter(description = "Tamanho da página", example = "20")
            @RequestParam(defaultValue = ApiConstants.DEFAULT_SIZE)
            @Min(1) @Max(100) int size,

            @Parameter(description = "Filtrar por status", example = "COMPLETED")
            @RequestParam(required = false) String status) {

        log.info("📋 Listando análises - Página: {}, Tamanho: {}, Status: {}", page, size, status);

        List<ImageAnalysis> analyses = queryUseCase.findAll();

        // Aplicar filtro de status se fornecido
        if (status != null && !status.trim().isEmpty()) {
            try {
                AnalysisStatus statusEnum = AnalysisStatus.valueOf(status.toUpperCase());
                analyses = analyses.stream()
                        .filter(analysis -> analysis.getStatus() == statusEnum)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                log.warn("Status inválido fornecido: {}", status);
                throw new IllegalArgumentException("Status inválido: " + status);
            }
        }

        // Simular paginação (em um cenário real, seria feito no repositório)
        int start = page * size;
        int end = Math.min(start + size, analyses.size());
        List<ImageAnalysis> paginatedAnalyses = analyses.subList(
                Math.min(start, analyses.size()),
                Math.min(end, analyses.size())
        );

        List<ImageAnalysisResponse> responseList = responseMapper.toResponseList(paginatedAnalyses);

        AnalysisListResponse response = AnalysisListResponse.builder()
                .analyses(responseList)
                .totalCount(analyses.size())
                .page(page)
                .size(size)
                .hasNext(end < analyses.size())
                .hasPrevious(page > 0)
                .build();

        log.info("✅ Retornando {} análises (página {}/{})",
                responseList.size(), page + 1, (analyses.size() + size - 1) / size);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "📊 Obter estatísticas das análises",
            description = "Retorna estatísticas agregadas das análises realizadas (cache de 5 minutos)")
    @SuccessResponse(AnalysisStatsResponse.class)
    @GetMapping("/stats")
    @Cacheable(value = ApiConstants.CACHE_ANALYSIS_STATS, unless = "#result == null")
    public ResponseEntity<AnalysisStatsResponse> getAnalysisStats() {
        log.info("📊 Gerando estatísticas das análises");

        try {
            AnalysisStatsResponse stats = queryUseCase.getAnalysisStatistics();
            log.info("✅ Estatísticas geradas: {} análises totais, {} hoje",
                    stats.getTotalAnalyses(), stats.getTodayAnalyses());
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            log.error("❌ Erro ao gerar estatísticas", e);
            throw e;
        }
    }

    @Operation(summary = "💚 Verificar status de saúde",
            description = "Endpoint para verificação de saúde do serviço de análise")
    @ApiResponse(responseCode = "200", description = "Serviço funcionando normalmente")
    @ApiResponse(responseCode = "503", description = "Serviço indisponível")
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.debug("💚 Health check do serviço de análise");

        try {
            // Verificar se o serviço está funcionando
            long analysisCount = queryUseCase.findAll().size();

            String healthMessage = String.format(
                    "🛰️ Analysis Service is healthy! Total analyses: %d - Timestamp: %s",
                    analysisCount,
                    LocalDateTime.now()
            );

            return ResponseEntity.ok(healthMessage);

        } catch (Exception e) {
            log.error("❌ Health check failed", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("❌ Analysis Service is unhealthy: " + e.getMessage());
        }
    }

    // Métodos de validação
    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID não pode ser vazio");
        }

        if (id.length() > 100) {
            throw new IllegalArgumentException("ID muito longo (máximo 100 caracteres)");
        }
    }

    // Exception Handlers
    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleImageNotFound(
            ImageNotFoundException ex,
            HttpServletRequest request) {

        log.warn("❌ Imagem não encontrada: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .error(ApiConstants.ERROR_IMAGE_NOT_FOUND)
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ImageAnalysisException.class)
    public ResponseEntity<ErrorResponse> handleImageAnalysisException(
            ImageAnalysisException ex,
            HttpServletRequest request) {

        log.error("❌ Erro de análise: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .error("ANALYSIS_ERROR")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        log.warn("❌ Argumento inválido: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .error(ApiConstants.ERROR_INVALID_REQUEST)
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("❌ Erro interno: {}", ex.getMessage(), ex);

        ErrorResponse error = ErrorResponse.builder()
                .error(ApiConstants.ERROR_INTERNAL_SERVER)
                .message("Erro interno do servidor")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
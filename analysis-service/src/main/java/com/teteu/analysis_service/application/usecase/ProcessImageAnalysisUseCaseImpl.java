package com.teteu.analysis_service.application.usecase;

import com.teteu.analysis_service.application.port.in.ProcessImageAnalysisUseCase;
import com.teteu.analysis_service.application.port.out.ImageAnalysisEngine;
import com.teteu.analysis_service.application.port.out.ImageAnalysisRepositoryPort;
import com.teteu.analysis_service.application.port.out.ImageStoragePort;
import com.teteu.analysis_service.domain.constants.AnalysisConstants;
import com.teteu.analysis_service.domain.exception.ImageNotFoundException;
import com.teteu.analysis_service.domain.exception.ImageProcessingException;
import com.teteu.analysis_service.domain.model.ImageAnalysis;
import com.teteu.analysis_service.domain.model.ImageClassification;
import com.teteu.analysis_service.domain.model.enums.AnalysisStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessImageAnalysisUseCaseImpl implements ProcessImageAnalysisUseCase {

    private final ImageStoragePort imageStoragePort;
    private final ImageAnalysisRepositoryPort repositoryPort;
    private final ImageAnalysisEngine analysisEngine;

    @Override
    @Transactional
    public ImageAnalysis processImage(String imageId, String originalFilename) {
        log.info("Iniciando análise da imagem: {} ({})", imageId, originalFilename);

        LocalDateTime startTime = LocalDateTime.now();

        // Criar registro inicial da análise
        ImageAnalysis analysis = createInitialAnalysis(imageId, originalFilename, startTime);
        analysis = repositoryPort.save(analysis);

        try {
            // Fazer download da imagem
            byte[] imageData = downloadImageSafely(imageId);

            // Atualizar status para processando
            analysis.setStatus(AnalysisStatus.PROCESSING);
            analysis = repositoryPort.save(analysis);

            // Executar análise da imagem
            ImageClassification classification = executeImageAnalysis(imageData, originalFilename);

            // Extrair metadados adicionais
            Map<String, Object> metadata = extractImageMetadata(imageData, originalFilename);

            // Finalizar análise com sucesso
            analysis = completeAnalysisSuccessfully(analysis, classification, metadata, startTime);

            log.info("Análise concluída com sucesso para imagem: {} em {}ms",
                    imageId, analysis.getProcessingTimeMs());

        } catch (Exception e) {
            log.error("Erro durante análise da imagem: {}", imageId, e);
            analysis = completeAnalysisWithError(analysis, e, startTime);
        }

        return repositoryPort.save(analysis);
    }

    private ImageAnalysis createInitialAnalysis(String imageId, String originalFilename, LocalDateTime startTime) {
        return ImageAnalysis.builder()
                .id(UUID.randomUUID().toString())
                .imageId(imageId)
                .originalFilename(originalFilename)
                .status(AnalysisStatus.PENDING)
                .startedAt(startTime)
                .metadata(new HashMap<>())
                .build();
    }

    private byte[] downloadImageSafely(String imageId) {
        try {
            log.debug("Fazendo download da imagem: {}", imageId);
            byte[] imageData = imageStoragePort.downloadImageAsBytes(imageId);

            if (imageData == null || imageData.length == 0) {
                throw new ImageNotFoundException(imageId);
            }

            validateImageSize(imageData);
            return imageData;

        } catch (Exception e) {
            if (e instanceof ImageNotFoundException) {
                throw e;
            }
            throw new ImageProcessingException("Erro ao fazer download da imagem: " + imageId, e);
        }
    }

    private void validateImageSize(byte[] imageData) {
        if (imageData.length > AnalysisConstants.MAX_IMAGE_SIZE_BYTES) {
            throw new ImageProcessingException(
                    String.format("Imagem muito grande: %d bytes. Máximo permitido: %d bytes",
                            imageData.length, AnalysisConstants.MAX_IMAGE_SIZE_BYTES)
            );
        }
    }

    private ImageClassification executeImageAnalysis(byte[] imageData, String filename) {
        try {
            log.debug("Executando análise da imagem: {}", filename);
            return analysisEngine.analyzeImage(imageData, filename);

        } catch (Exception e) {
            throw new ImageProcessingException("Erro durante análise da imagem: " + filename, e);
        }
    }

    private Map<String, Object> extractImageMetadata(byte[] imageData, String filename) {
        Map<String, Object> metadata = new HashMap<>();

        try {
            // Metadados básicos
            metadata.put(AnalysisConstants.METADATA_FILE_SIZE, imageData.length);
            metadata.put(AnalysisConstants.METADATA_FORMAT, extractFileFormat(filename));

            // Simulados obv
            metadata.put(AnalysisConstants.METADATA_IMAGE_WIDTH, 1024);
            metadata.put(AnalysisConstants.METADATA_IMAGE_HEIGHT, 768);
            metadata.put(AnalysisConstants.METADATA_COLOR_DEPTH, 24);

            log.debug("Metadados extraídos para {}: {}", filename, metadata);

        } catch (Exception e) {
            log.warn("Erro ao extrair metadados da imagem: {}", filename, e);
        }

        return metadata;
    }

    private String extractFileFormat(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "unknown";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    private ImageAnalysis completeAnalysisSuccessfully(
            ImageAnalysis analysis,
            ImageClassification classification,
            Map<String, Object> metadata,
            LocalDateTime startTime) {

        LocalDateTime endTime = LocalDateTime.now();
        long processingTime = java.time.Duration.between(startTime, endTime).toMillis();

        analysis.setStatus(AnalysisStatus.COMPLETED);
        analysis.setClassification(classification);
        analysis.setMetadata(metadata);
        analysis.setCompletedAt(endTime);
        analysis.setProcessingTimeMs(processingTime);

        // Validar se o processamento não demorou muito
        if (processingTime > AnalysisConstants.MAX_PROCESSING_TIME_MS) {
            log.warn("Análise demorou mais que o esperado: {}ms para imagem: {}",
                    processingTime, analysis.getImageId());
        }

        return analysis;
    }

    private ImageAnalysis completeAnalysisWithError(
            ImageAnalysis analysis,
            Exception error,
            LocalDateTime startTime) {

        LocalDateTime endTime = LocalDateTime.now();
        long processingTime = java.time.Duration.between(startTime, endTime).toMillis();

        analysis.setStatus(AnalysisStatus.FAILED);
        analysis.setErrorMessage(buildErrorMessage(error));
        analysis.setCompletedAt(endTime);
        analysis.setProcessingTimeMs(processingTime);

        return analysis;
    }

    private String buildErrorMessage(Exception error) {
        StringBuilder message = new StringBuilder();
        message.append(error.getClass().getSimpleName()).append(": ");
        message.append(error.getMessage());

        // Adicionar causa raiz se existir
        Throwable cause = error.getCause();
        if (cause != null && cause != error) {
            message.append(" (Causa: ").append(cause.getMessage()).append(")");
        }

        // Limitar tamanho da mensagem para evitar problemas no banco
        String result = message.toString();
        return result.length() > 500 ? result.substring(0, 497) + "..." : result;
    }
}
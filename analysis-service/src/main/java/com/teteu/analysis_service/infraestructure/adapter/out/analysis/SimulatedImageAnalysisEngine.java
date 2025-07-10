package com.teteu.analysis_service.infraestructure.adapter.out.analysis;

import com.teteu.analysis_service.application.port.out.ImageAnalysisEngine;
import com.teteu.analysis_service.domain.constants.AnalysisConstants;
import com.teteu.analysis_service.domain.exception.ImageProcessingException;
import com.teteu.analysis_service.domain.model.DetectedFeature;
import com.teteu.analysis_service.domain.model.ImageClassification;
import com.teteu.analysis_service.domain.model.QualityMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class SimulatedImageAnalysisEngine implements ImageAnalysisEngine {

    private final TerrainClassifier terrainClassifier;
    private final FeatureDetector featureDetector;
    private final QualityAnalyzer qualityAnalyzer;

    @Override
    public ImageClassification analyzeImage(byte[] imageData, String filename) {
        log.info("🔬 Iniciando análise simulada da imagem: {} ({} bytes)", filename, imageData.length);

        try {
            validateImageData(imageData, filename);

            // Usar seed determinístico para resultados consistentes
            long seed = AnalysisUtils.generateSeed(imageData, filename);
            Random deterministicRandom = new Random(seed);

            // Simular tempo de processamento baseado no tamanho da imagem
            simulateProcessingTime(imageData.length, deterministicRandom);

            // Análise do terreno principal
            TerrainAnalysisResult terrainResult = terrainClassifier.classifyTerrain(imageData, filename);

            // Detecção de features
            List<DetectedFeature> features = featureDetector.detectFeatures(imageData, terrainResult.getTerrain());

            // Análise de qualidade
            QualityMetrics quality = qualityAnalyzer.analyzeQuality(imageData);

            ImageClassification classification = ImageClassification.builder()
                    .primaryTerrain(terrainResult.getTerrain())
                    .confidence(terrainResult.getConfidence())
                    .features(features)
                    .quality(quality)
                    .build();

            log.info("✅ Análise concluída: Terreno={}, Confiança={}, Features={}, Qualidade={}",
                    classification.getPrimaryTerrain(),
                    classification.getConfidence(),
                    features.size(),
                    quality.getOverallQuality());

            return classification;

        } catch (Exception e) {
            log.error("❌ Erro durante análise da imagem: {}", filename, e);
            throw new ImageProcessingException("Falha na análise da imagem: " + filename, e);
        }
    }

    private void validateImageData(byte[] imageData, String filename) {
        if (imageData == null || imageData.length == 0) {
            throw new ImageProcessingException("Dados da imagem estão vazios");
        }

        if (imageData.length < 1024) { // Mínimo de 1KB
            throw new ImageProcessingException("Imagem muito pequena para análise: " + imageData.length + " bytes");
        }

        if (imageData.length > AnalysisConstants.MAX_IMAGE_SIZE_BYTES) {
            throw new ImageProcessingException(
                    String.format("Imagem muito grande para análise: %d bytes (máximo: %d)",
                            imageData.length, AnalysisConstants.MAX_IMAGE_SIZE_BYTES));
        }

        if (!AnalysisUtils.isValidImageData(imageData)) {
            throw new ImageProcessingException("Dados da imagem não parecem ser válidos");
        }

        String detectedType = AnalysisUtils.detectFileType(imageData);
        log.debug("Tipo de arquivo detectado: {} para {}", detectedType, filename);
    }

    private void simulateProcessingTime(long imageSize, Random random) {
        try {
            // Simula tempo de processamento baseado no tamanho
            long baseTime = 300; // 300ms base
            long sizeBasedTime = imageSize / 15000; // +1ms por 15KB
            long jitter = random.nextInt(100); // ±100ms de variação

            long totalTime = Math.min(baseTime + sizeBasedTime + jitter, 2000); // Máximo 2s

            log.debug("⏱️ Simulando processamento por {}ms", totalTime);
            Thread.sleep(totalTime);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ImageProcessingException("Processamento foi interrompido", e);
        }
    }
}
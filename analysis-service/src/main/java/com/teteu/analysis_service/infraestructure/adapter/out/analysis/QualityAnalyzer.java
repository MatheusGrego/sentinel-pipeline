package com.teteu.analysis_service.infraestructure.adapter.out.analysis;

import com.teteu.analysis_service.domain.model.QualityMetrics;
import com.teteu.analysis_service.domain.model.enums.QualityRating;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Component
@Slf4j
public class QualityAnalyzer {

    private final Random random = new Random();

    public QualityMetrics analyzeQuality(byte[] imageData) {
        log.debug("Analisando qualidade da imagem ({} bytes)", imageData.length);

        // Simular análise de qualidade baseada nas características da imagem
        ImageQualityFactors factors = extractQualityFactors(imageData);

        // Calcular métricas individuais
        BigDecimal sharpness = calculateSharpness(factors);
        BigDecimal brightness = calculateBrightness(factors);
        BigDecimal contrast = calculateContrast(factors);

        // Determinar qualidade geral
        QualityRating overallQuality = determineOverallQuality(sharpness, brightness, contrast);

        QualityMetrics metrics = QualityMetrics.builder()
                .sharpness(sharpness)
                .brightness(brightness)
                .contrast(contrast)
                .overallQuality(overallQuality)
                .build();

        log.debug("Qualidade analisada: Nitidez={}, Brilho={}, Contraste={}, Geral={}",
                sharpness, brightness, contrast, overallQuality);

        return metrics;
    }

    private ImageQualityFactors extractQualityFactors(byte[] imageData) {

        // Análise de variação de bytes (simula análise de frequência)
        double byteVariation = calculateByteVariation(imageData);

        // Análise de distribuição (simula histograma)
        double distribution = calculateDistribution(imageData);

        // Análise de gradientes (simula detecção de bordas)
        double gradientStrength = calculateGradientStrength(imageData);

        // Análise de ruído (baseado em padrões repetitivos)
        double noiseLevel = calculateNoiseLevel(imageData);

        // Análise de compressão (baseado no tamanho vs conteúdo)
        double compressionArtifacts = calculateCompressionArtifacts(imageData);

        return ImageQualityFactors.builder()
                .byteVariation(byteVariation)
                .distribution(distribution)
                .gradientStrength(gradientStrength)
                .noiseLevel(noiseLevel)
                .compressionArtifacts(compressionArtifacts)
                .imageSize(imageData.length)
                .build();
    }

    private double calculateByteVariation(byte[] imageData) {
        if (imageData.length < 2) return 0.5;

        long totalVariation = 0;
        int samples = Math.min(imageData.length - 1, 1000); // Amostra até 1000 pontos

        for (int i = 0; i < samples; i++) {
            int index = i * (imageData.length - 1) / samples;
            totalVariation += Math.abs(imageData[index] - imageData[index + 1]);
        }

        double avgVariation = (double) totalVariation / samples;
        return Math.min(avgVariation / 127.0, 1.0); // Normalizar para 0-1
    }

    private double calculateDistribution(byte[] imageData) {
        // Simular análise de histograma
        int[] histogram = new int[256];
        int sampleSize = Math.min(imageData.length, 2000);

        for (int i = 0; i < sampleSize; i++) {
            int value = imageData[i] & 0xFF; // Converter para unsigned
            histogram[value]++;
        }

        // Calcular dispersão do histograma
        double variance = 0;
        double mean = 127.5; // Valor médio esperado

        for (int i = 0; i < 256; i++) {
            double frequency = (double) histogram[i] / sampleSize;
            variance += frequency * Math.pow(i - mean, 2);
        }

        return Math.min(Math.sqrt(variance) / 127.0, 1.0);
    }

    private double calculateGradientStrength(byte[] imageData) {
        if (imageData.length < 3) return 0.5;

        long gradientSum = 0;
        int samples = Math.min(imageData.length - 2, 500);

        for (int i = 0; i < samples; i++) {
            int index = i * (imageData.length - 2) / samples;
            int gradient = Math.abs(imageData[index] - imageData[index + 2]);
            gradientSum += gradient;
        }

        double avgGradient = (double) gradientSum / samples;
        return Math.min(avgGradient / 127.0, 1.0);
    }

    private double calculateNoiseLevel(byte[] imageData) {
        // Simular detecção de ruído baseado em padrões de alta frequência
        if (imageData.length < 10) return 0.3;

        long highFrequencyChanges = 0;
        int samples = Math.min(imageData.length - 1, 800);

        for (int i = 0; i < samples; i++) {
            int index = i * (imageData.length - 1) / samples;
            if (Math.abs(imageData[index] - imageData[index + 1]) > 50) {
                highFrequencyChanges++;
            }
        }

        double noiseRatio = (double) highFrequencyChanges / samples;
        return Math.min(noiseRatio * 2, 1.0); // Amplificar para tornar mais sensível
    }

    private double calculateCompressionArtifacts(byte[] imageData) {
        // Simular detecção de artefatos de compressão
        // Baseado na relação tamanho/complexidade

        double sizeKB = imageData.length / 1024.0;
        double expectedSize = 500; // KB esperado para uma imagem de qualidade

        double compressionRatio = sizeKB / expectedSize;

        // Imagens muito pequenas podem ter mais artefatos
        if (compressionRatio < 0.3) {
            return 0.8; // Alta probabilidade de artefatos
        } else if (compressionRatio < 0.6) {
            return 0.4; // Média probabilidade
        } else {
            return 0.1; // Baixa probabilidade
        }
    }

    private BigDecimal calculateSharpness(ImageQualityFactors factors) {
        // Nitidez baseada na força dos gradientes e baixo ruído
        double sharpness = factors.getGradientStrength() * 0.7 +
                (1.0 - factors.getNoiseLevel()) * 0.3;

        sharpness += (random.nextDouble() - 0.5) * 0.1;

        sharpness = Math.max(0.0, Math.min(1.0, sharpness));

        return new BigDecimal(sharpness).setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateBrightness(ImageQualityFactors factors) {
        // Brilho baseado na distribuição e variação
        double brightness = factors.getDistribution() * 0.6 +
                factors.getByteVariation() * 0.4;

        if (brightness > 0.4 && brightness < 0.8) {
            brightness += 0.1; // Bonus para faixa ideal
        }

        brightness += (random.nextDouble() - 0.5) * 0.15;

        brightness = Math.max(0.0, Math.min(1.0, brightness));

        return new BigDecimal(brightness).setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateContrast(ImageQualityFactors factors) {
        double contrast = factors.getByteVariation() * 0.8 +
                (1.0 - factors.getCompressionArtifacts()) * 0.2;

        contrast *= (1.0 - factors.getNoiseLevel() * 0.3);

        contrast += (random.nextDouble() - 0.5) * 0.12;

        contrast = Math.max(0.0, Math.min(1.0, contrast));

        return new BigDecimal(contrast).setScale(4, RoundingMode.HALF_UP);
    }

    private QualityRating determineOverallQuality(BigDecimal sharpness, BigDecimal brightness, BigDecimal contrast) {
        double sharpnessWeight = 0.4;
        double brightnessWeight = 0.3;
        double contrastWeight = 0.3;

        double overallScore = sharpness.doubleValue() * sharpnessWeight +
                brightness.doubleValue() * brightnessWeight +
                contrast.doubleValue() * contrastWeight;

        if (overallScore >= 0.85) {
            return QualityRating.EXCELLENT;
        } else if (overallScore >= 0.70) {
            return QualityRating.GOOD;
        } else if (overallScore >= 0.50) {
            return QualityRating.FAIR;
        } else {
            return QualityRating.POOR;
        }
    }
}
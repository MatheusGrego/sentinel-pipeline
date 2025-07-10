package com.teteu.analysis_service.infraestructure.adapter.out.analysis;

import com.teteu.analysis_service.domain.model.enums.TerrainType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Component
@Slf4j
public class TerrainClassifier {

    private final Random random = new Random();

    public TerrainAnalysisResult classifyTerrain(byte[] imageData, String filename) {
        log.debug("Classificando terreno para: {}", filename);

        // Simular análise baseada em características da imagem
        TerrainCharacteristics characteristics = extractCharacteristics(imageData, filename);

        // Determinar tipo de terreno baseado nas características
        TerrainType terrain = determineTerrainType(characteristics);

        // Calcular confiança baseada na clareza das características
        BigDecimal confidence = calculateConfidence(terrain, characteristics);

        log.debug("Terreno classificado: {} (confiança: {})", terrain, confidence);

        return new TerrainAnalysisResult(terrain, confidence, characteristics);
    }

    private TerrainCharacteristics extractCharacteristics(byte[] imageData, String filename) {
        // Simular extração de características baseadas no conteúdo da imagem
        // Em um cenário real, isso seria feito com algoritmos de visão computacional

        int colorVariation = calculateColorVariation(imageData);
        int textureComplexity = calculateTextureComplexity(imageData);
        int edgeDensity = calculateEdgeDensity(imageData);

        // Adicionar alguma variação baseada no nome do arquivo
        int filenameHash = Math.abs(filename.hashCode() % 100);

        return TerrainCharacteristics.builder()
                .colorVariation(colorVariation)
                .textureComplexity(textureComplexity)
                .edgeDensity(edgeDensity)
                .brightnessLevel(50 + filenameHash / 2)
                .contrastLevel(40 + (filenameHash * 2) % 60)
                .build();
    }

    private int calculateColorVariation(byte[] imageData) {
        // Simular análise de variação de cores
        long sum = 0;
        for (int i = 0; i < Math.min(imageData.length, 1000); i += 10) {
            sum += Math.abs(imageData[i]);
        }
        return (int) ((sum % 100) + random.nextInt(20));
    }

    private int calculateTextureComplexity(byte[] imageData) {
        // Simular análise de textura
        int complexity = 0;
        for (int i = 1; i < Math.min(imageData.length, 500); i++) {
            complexity += Math.abs(imageData[i] - imageData[i-1]);
        }
        return (complexity % 100) + random.nextInt(15);
    }

    private int calculateEdgeDensity(byte[] imageData) {
        // Simular detecção de bordas
        return (imageData.length % 100) + random.nextInt(25);
    }

    private TerrainType determineTerrainType(TerrainCharacteristics characteristics) {
        // Lógica para determinar tipo de terreno baseado nas características

        if (characteristics.getEdgeDensity() > 70 && characteristics.getTextureComplexity() > 60) {
            return TerrainType.URBAN;
        }

        if (characteristics.getColorVariation() > 80 && characteristics.getTextureComplexity() > 70) {
            return TerrainType.FOREST;
        }

        if (characteristics.getBrightnessLevel() > 80 && characteristics.getColorVariation() < 30) {
            return TerrainType.DESERT;
        }

        if (characteristics.getContrastLevel() < 30 && characteristics.getColorVariation() < 40) {
            return TerrainType.WATER;
        }

        if (characteristics.getTextureComplexity() > 50 && characteristics.getEdgeDensity() < 40) {
            return TerrainType.AGRICULTURAL;
        }

        if (characteristics.getContrastLevel() > 70 && characteristics.getEdgeDensity() > 60) {
            return TerrainType.MOUNTAIN;
        }

        if (characteristics.getEdgeDensity() > 50 && characteristics.getBrightnessLevel() > 60) {
            return TerrainType.INDUSTRIAL;
        }

        // Verificar se está próximo a características costeiras
        if (characteristics.getColorVariation() > 40 && characteristics.getColorVariation() < 70
                && characteristics.getBrightnessLevel() > 50) {
            return TerrainType.COASTAL;
        }

        return TerrainType.UNKNOWN;
    }

    private BigDecimal calculateConfidence(TerrainType terrain, TerrainCharacteristics characteristics) {
        BigDecimal baseConfidence = switch (terrain) {
            case URBAN -> new BigDecimal("0.85");
            case WATER -> new BigDecimal("0.90");
            case DESERT -> new BigDecimal("0.82");
            case FOREST -> new BigDecimal("0.78");
            case AGRICULTURAL -> new BigDecimal("0.75");
            case MOUNTAIN -> new BigDecimal("0.80");
            case INDUSTRIAL -> new BigDecimal("0.83");
            case COASTAL -> new BigDecimal("0.70");
            default -> new BigDecimal("0.60");
        };

        // Confiança base dependendo do tipo de terreno

        // Ajustar confiança baseado na qualidade das características
        BigDecimal qualityFactor = calculateQualityFactor(characteristics);
        BigDecimal finalConfidence = baseConfidence.multiply(qualityFactor);

        // Adicionar pequena variação aleatória
        BigDecimal randomFactor = new BigDecimal(0.95 + (random.nextDouble() * 0.1)); // 0.95 a 1.05
        finalConfidence = finalConfidence.multiply(randomFactor);

        // Garantir que está entre 0.5 e 1.0
        if (finalConfidence.compareTo(new BigDecimal("1.0")) > 0) {
            finalConfidence = new BigDecimal("1.0");
        }
        if (finalConfidence.compareTo(new BigDecimal("0.5")) < 0) {
            finalConfidence = new BigDecimal("0.5");
        }

        return finalConfidence.setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateQualityFactor(TerrainCharacteristics characteristics) {
        // Características claras resultam em maior confiança
        int totalScore = characteristics.getColorVariation() +
                characteristics.getTextureComplexity() +
                characteristics.getEdgeDensity() +
                characteristics.getContrastLevel();

        // Normalizar para fator entre 0.8 e 1.1
        double factor = 0.8 + (totalScore % 100) * 0.003; // 0.8 a 1.1
        return new BigDecimal(factor).setScale(4, RoundingMode.HALF_UP);
    }
}

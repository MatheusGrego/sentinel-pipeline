package com.teteu.analysis_service.infraestructure.adapter.out.analysis;

import com.teteu.analysis_service.domain.model.BoundingBox;
import com.teteu.analysis_service.domain.model.DetectedFeature;
import com.teteu.analysis_service.domain.model.enums.FeatureType;
import com.teteu.analysis_service.domain.model.enums.TerrainType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class FeatureDetector {

    private final Random random = new Random();

    public List<DetectedFeature> detectFeatures(byte[] imageData, TerrainType terrainType) {
        log.debug("Detectando features para terreno: {}", terrainType);

        List<DetectedFeature> features = new ArrayList<>();

        switch (terrainType) {
            case URBAN -> features.addAll(detectUrbanFeatures(imageData));
            case FOREST -> features.addAll(detectForestFeatures(imageData));
            case WATER -> features.addAll(detectWaterFeatures(imageData));
            case AGRICULTURAL -> features.addAll(detectAgriculturalFeatures(imageData));
            case INDUSTRIAL -> features.addAll(detectIndustrialFeatures(imageData));
            case MOUNTAIN -> features.addAll(detectMountainFeatures(imageData));
            case COASTAL -> features.addAll(detectCoastalFeatures(imageData));
            case DESERT -> features.addAll(detectDesertFeatures(imageData));
            default -> features.addAll(detectGenericFeatures(imageData));
        }

        log.debug("Detectadas {} features", features.size());
        return features;
    }

    private List<DetectedFeature> detectUrbanFeatures(byte[] imageData) {
        List<DetectedFeature> features = new ArrayList<>();

        if (random.nextDouble() > 0.3) {
            features.add(createFeature(FeatureType.BUILDING, 0.85, "Edifício comercial detectado"));
        }

        if (random.nextDouble() > 0.2) {
            features.add(createFeature(FeatureType.ROAD, 0.90, "Sistema viário principal"));
        }

        if (random.nextDouble() > 0.6) {
            features.add(createFeature(FeatureType.VEHICLE, 0.70, "Concentração de veículos"));
        }

        if (random.nextDouble() > 0.5) {
            features.add(createFeature(FeatureType.INFRASTRUCTURE, 0.75, "Infraestrutura urbana"));
        }

        return features;
    }

    private List<DetectedFeature> detectForestFeatures(byte[] imageData) {
        List<DetectedFeature> features = new ArrayList<>();

        features.add(createFeature(FeatureType.VEGETATION, 0.95, "Cobertura florestal densa"));

        if (random.nextDouble() > 0.4) {
            features.add(createFeature(FeatureType.NATURAL_FORMATION, 0.80, "Formação florestal natural"));
        }

        return features;
    }

    private List<DetectedFeature> detectWaterFeatures(byte[] imageData) {
        List<DetectedFeature> features = new ArrayList<>();

        features.add(createFeature(FeatureType.WATER_BODY, 0.92, "Corpo d'água principal"));

        if (random.nextDouble() > 0.7) {
            features.add(createFeature(FeatureType.HUMAN_ACTIVITY, 0.60, "Atividade humana próxima à água"));
        }

        return features;
    }

    private List<DetectedFeature> detectAgriculturalFeatures(byte[] imageData) {
        List<DetectedFeature> features = new ArrayList<>();

        features.add(createFeature(FeatureType.VEGETATION, 0.88, "Área de cultivo"));

        features.add(createFeature(FeatureType.HUMAN_ACTIVITY, 0.85, "Atividade agrícola"));

        if (random.nextDouble() > 0.6) {
            features.add(createFeature(FeatureType.INFRASTRUCTURE, 0.65, "Infraestrutura rural"));
        }

        return features;
    }

    private List<DetectedFeature> detectIndustrialFeatures(byte[] imageData) {
        List<DetectedFeature> features = new ArrayList<>();

        features.add(createFeature(FeatureType.BUILDING, 0.90, "Complexo industrial"));

        features.add(createFeature(FeatureType.INFRASTRUCTURE, 0.85, "Infraestrutura industrial"));

        if (random.nextDouble() > 0.5) {
            features.add(createFeature(FeatureType.VEHICLE, 0.75, "Equipamentos industriais"));
        }

        return features;
    }

    private List<DetectedFeature> detectMountainFeatures(byte[] imageData) {
        List<DetectedFeature> features = new ArrayList<>();

        features.add(createFeature(FeatureType.NATURAL_FORMATION, 0.95, "Formação montanhosa"));

        if (random.nextDouble() > 0.4) {
            features.add(createFeature(FeatureType.VEGETATION, 0.70, "Vegetação montanhosa"));
        }

        return features;
    }

    private List<DetectedFeature> detectCoastalFeatures(byte[] imageData) {
        List<DetectedFeature> features = new ArrayList<>();

        features.add(createFeature(FeatureType.WATER_BODY, 0.90, "Área costeira"));

        features.add(createFeature(FeatureType.NATURAL_FORMATION, 0.85, "Linha costeira"));

        if (random.nextDouble() > 0.6) {
            features.add(createFeature(FeatureType.HUMAN_ACTIVITY, 0.70, "Atividade costeira"));
        }

        return features;
    }

    private List<DetectedFeature> detectDesertFeatures(byte[] imageData) {
        List<DetectedFeature> features = new ArrayList<>();

        features.add(createFeature(FeatureType.NATURAL_FORMATION, 0.90, "Formação desértica"));

        if (random.nextDouble() > 0.8) {
            features.add(createFeature(FeatureType.HUMAN_ACTIVITY, 0.50, "Atividade humana esparsa"));
        }

        return features;
    }

    private List<DetectedFeature> detectGenericFeatures(byte[] imageData) {
        List<DetectedFeature> features = new ArrayList<>();

        features.add(createFeature(FeatureType.NATURAL_FORMATION, 0.60, "Características indefinidas"));

        return features;
    }

    private DetectedFeature createFeature(FeatureType type, double confidence, String description) {
        BoundingBox boundingBox = BoundingBox.builder()
                .x(random.nextInt(800))
                .y(random.nextInt(600))
                .width(50 + random.nextInt(200))
                .height(50 + random.nextInt(200))
                .build();

        return DetectedFeature.builder()
                .type(type)
                .confidence(new BigDecimal(confidence).setScale(4, RoundingMode.HALF_UP))
                .description(description)
                .boundingBox(boundingBox)
                .build();
    }
}
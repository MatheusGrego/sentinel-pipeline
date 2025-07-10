package com.teteu.analysis_service.domain.model.enums;

import lombok.Getter;

@Getter
public enum FeatureType {
    BUILDING("Edifício"),
    ROAD("Estrada"),
    VEHICLE("Veículo"),
    VEGETATION("Vegetação"),
    WATER_BODY("Corpo d'água"),
    INFRASTRUCTURE("Infraestrutura"),
    NATURAL_FORMATION("Formação Natural"),
    HUMAN_ACTIVITY("Atividade Humana");

    private final String description;

    FeatureType(String description) {
        this.description = description;
    }

}
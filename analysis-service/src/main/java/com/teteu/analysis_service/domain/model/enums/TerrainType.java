package com.teteu.analysis_service.domain.model.enums;

import lombok.Getter;

@Getter
public enum TerrainType {
    URBAN("Urbano"),
    FOREST("Floresta"),
    WATER("Água"),
    AGRICULTURAL("Agrícola"),
    DESERT("Deserto"),
    MOUNTAIN("Montanha"),
    COASTAL("Costeiro"),
    INDUSTRIAL("Industrial"),
    UNKNOWN("Desconhecido");

    private final String description;

    TerrainType(String description) {
        this.description = description;
    }

}
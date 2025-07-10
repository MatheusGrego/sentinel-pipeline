package com.teteu.analysis_service.domain.model.enums;

import lombok.Getter;

@Getter
public enum QualityRating {
    EXCELLENT("Excelente", 5),
    GOOD("Boa", 4),
    FAIR("Regular", 3),
    POOR("Ruim", 2),
    VERY_POOR("Muito Ruim", 1);

    private final String description;
    private final Integer score;

    QualityRating(String description, Integer score) {
        this.description = description;
        this.score = score;
    }

}
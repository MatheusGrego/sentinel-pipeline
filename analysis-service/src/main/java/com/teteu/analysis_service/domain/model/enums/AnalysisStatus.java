package com.teteu.analysis_service.domain.model.enums;

import lombok.Getter;

@Getter
public enum AnalysisStatus {
    PENDING("Pendente"),
    PROCESSING("Processando"),
    COMPLETED("Concluída"),
    FAILED("Falhou");

    private final String description;

    AnalysisStatus(String description) {
        this.description = description;
    }

}
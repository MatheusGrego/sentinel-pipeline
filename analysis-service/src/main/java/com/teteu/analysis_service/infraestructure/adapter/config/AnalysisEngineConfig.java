package com.teteu.analysis_service.infraestructure.adapter.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@Slf4j
public class AnalysisEngineConfig {

    @PostConstruct
    public void initializeAnalysisEngine() {
        log.info("🔬 Motor de Análise de Imagens inicializado");
        log.info("📊 Componentes disponíveis:");
        log.info("   ✅ Classificador de Terreno");
        log.info("   ✅ Detector de Features");
        log.info("   ✅ Analisador de Qualidade");
        log.info("   ✅ Motor Principal Simulado");
    }
}
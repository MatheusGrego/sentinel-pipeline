package com.teteu.analysis_service.application.usecase;

import com.teteu.analysis_service.application.port.in.QueryImageAnalysisUseCase;
import com.teteu.analysis_service.application.port.out.ImageAnalysisRepositoryPort;
import com.teteu.analysis_service.domain.model.ImageAnalysis;
import com.teteu.analysis_service.domain.model.enums.AnalysisStatus;
import com.teteu.analysis_service.domain.model.enums.TerrainType;
import com.teteu.analysis_service.infraestructure.adapter.in.web.dto.AnalysisStatsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueryImageAnalysisUseCaseImpl implements QueryImageAnalysisUseCase {

    private final ImageAnalysisRepositoryPort repositoryPort;

    @Override
    public Optional<ImageAnalysis> findById(String id) {
        log.debug("Buscando análise por ID: {}", id);
        return repositoryPort.findById(id);
    }

    @Override
    public Optional<ImageAnalysis> findByImageId(String imageId) {
        log.debug("Buscando análise por imageId: {}", imageId);
        return repositoryPort.findByImageId(imageId);
    }

    @Override
    public List<ImageAnalysis> findAll() {
        log.debug("Buscando todas as análises");
        return repositoryPort.findAll();
    }

    @Override
    public AnalysisStatsResponse getAnalysisStatistics() {
        log.debug("Gerando estatísticas das análises");

        List<ImageAnalysis> allAnalyses = repositoryPort.findAll();

        // Contagem por status
        Map<AnalysisStatus, Long> statusCounts = allAnalyses.stream()
                .collect(Collectors.groupingBy(
                        ImageAnalysis::getStatus,
                        Collectors.counting()
                ));

        // Contagem por tipo de terreno (apenas análises completas)
        Map<TerrainType, Long> terrainCounts = allAnalyses.stream()
                .filter(analysis -> analysis.getClassification() != null)
                .collect(Collectors.groupingBy(
                        analysis -> analysis.getClassification().getPrimaryTerrain(),
                        Collectors.counting()
                ));

        // Tempo médio de processamento
        Double avgProcessingTime = allAnalyses.stream()
                .filter(analysis -> analysis.getProcessingTimeMs() != null)
                .mapToLong(ImageAnalysis::getProcessingTimeMs)
                .average()
                .orElse(0.0);

        // Análises de hoje
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        Long todayAnalyses = allAnalyses.stream()
                .filter(analysis -> analysis.getStartedAt() != null)
                .filter(analysis -> analysis.getStartedAt().isAfter(startOfDay))
                .count();

        return AnalysisStatsResponse.builder()
                .statusCounts(statusCounts)
                .terrainCounts(terrainCounts)
                .avgProcessingTimeMs(avgProcessingTime)
                .totalAnalyses((long) allAnalyses.size())
                .todayAnalyses(todayAnalyses)
                .build();
    }
}
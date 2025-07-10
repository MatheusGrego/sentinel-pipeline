package com.teteu.analysis_service.infraestructure.adapter.out.persistence;

import com.teteu.analysis_service.application.port.out.ImageAnalysisRepositoryPort;
import com.teteu.analysis_service.domain.model.ImageAnalysis;
import com.teteu.analysis_service.infraestructure.adapter.out.persistence.entity.ImageAnalysisEntity;
import com.teteu.analysis_service.infraestructure.adapter.out.persistence.mapper.ImageAnalysisMapper;
import com.teteu.analysis_service.infraestructure.adapter.out.persistence.repository.ImageAnalysisJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageAnalysisRepositoryAdapter implements ImageAnalysisRepositoryPort {

    private final ImageAnalysisJpaRepository jpaRepository;
    private final ImageAnalysisMapper mapper;

    @Override
    public ImageAnalysis save(ImageAnalysis imageAnalysis) {
        log.debug("Salvando análise de imagem: {}", imageAnalysis.getId());

        ImageAnalysisEntity entity = mapper.toEntity(imageAnalysis);
        ImageAnalysisEntity savedEntity = jpaRepository.save(entity);

        log.debug("Análise de imagem salva com sucesso: {}", savedEntity.getId());
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ImageAnalysis> findById(String id) {
        log.debug("Buscando análise por ID: {}", id);

        return jpaRepository.findByIdWithDetails(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<ImageAnalysis> findByImageId(String imageId) {
        log.debug("Buscando análise por imageId: {}", imageId);

        return jpaRepository.findByImageId(imageId)
                .map(mapper::toDomain);
    }

    @Override
    public List<ImageAnalysis> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }
}
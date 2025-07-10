package com.teteu.analysis_service.infraestructure.adapter.out.persistence.repository;

import com.teteu.analysis_service.infraestructure.adapter.out.persistence.entity.ImageAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageAnalysisJpaRepository extends JpaRepository<ImageAnalysisEntity, String> {

    @Query("SELECT ia FROM ImageAnalysisEntity ia WHERE ia.imageId = :imageId")
    Optional<ImageAnalysisEntity> findByImageId(@Param("imageId") String imageId);

    @Query("SELECT ia FROM ImageAnalysisEntity ia " +
            "LEFT JOIN FETCH ia.classification c " +
            "LEFT JOIN FETCH c.features f " +
            "LEFT JOIN FETCH f.boundingBox " +
            "LEFT JOIN FETCH c.quality " +
            "WHERE ia.id = :id")
    Optional<ImageAnalysisEntity> findByIdWithDetails(@Param("id") String id);
}
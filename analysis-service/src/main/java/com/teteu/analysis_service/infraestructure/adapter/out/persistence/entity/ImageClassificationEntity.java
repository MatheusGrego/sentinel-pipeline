package com.teteu.analysis_service.infraestructure.adapter.out.persistence.entity;

import com.teteu.analysis_service.domain.model.enums.TerrainType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "image_classification")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageClassificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_analysis_id", nullable = false)
    private ImageAnalysisEntity imageAnalysis;

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_terrain", nullable = false, length = 50)
    private TerrainType primaryTerrain;

    @Column(name = "confidence", nullable = false, precision = 5, scale = 4)
    private BigDecimal confidence;

    @OneToMany(mappedBy = "classification", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetectedFeatureEntity> features;

    @OneToOne(mappedBy = "classification", cascade = CascadeType.ALL)
    private QualityMetricsEntity quality;
}

package com.teteu.analysis_service.infraestructure.adapter.out.persistence.entity;

import com.teteu.analysis_service.domain.model.enums.FeatureType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "detected_feature")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetectedFeatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification_id", nullable = false)
    private ImageClassificationEntity classification;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private FeatureType type;

    @Column(name = "confidence", nullable = false, precision = 5, scale = 4)
    private BigDecimal confidence;

    @Column(name = "description", length = 500)
    private String description;

    @OneToOne(mappedBy = "feature", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BoundingBoxEntity boundingBox;
}
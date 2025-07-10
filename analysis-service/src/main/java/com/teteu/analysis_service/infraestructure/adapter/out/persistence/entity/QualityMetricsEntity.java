package com.teteu.analysis_service.infraestructure.adapter.out.persistence.entity;

import com.teteu.analysis_service.domain.model.enums.QualityRating;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "quality_metrics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityMetricsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification_id", nullable = false)
    private ImageClassificationEntity classification;

    @Column(name = "sharpness", nullable = false, precision = 5, scale = 4)
    private BigDecimal sharpness;

    @Column(name = "brightness", nullable = false, precision = 5, scale = 4)
    private BigDecimal brightness;

    @Column(name = "contrast", nullable = false, precision = 5, scale = 4)
    private BigDecimal contrast;

    @Enumerated(EnumType.STRING)
    @Column(name = "overall_quality", nullable = false, length = 20)
    private QualityRating overallQuality;
}
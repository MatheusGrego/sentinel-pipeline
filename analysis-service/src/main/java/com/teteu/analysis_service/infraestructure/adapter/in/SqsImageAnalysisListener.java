package com.teteu.analysis_service.infraestructure.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teteu.analysis_service.application.port.in.ProcessImageAnalysisUseCase;
import com.teteu.analysis_service.domain.model.ImageAnalysis;
import com.teteu.analysis_service.infraestructure.adapter.in.sqs.dto.SqsImageMessage;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SqsImageAnalysisListener {

    private final ProcessImageAnalysisUseCase processImageAnalysisUseCase;
    private final ObjectMapper objectMapper;

    @SqsListener("${aws.sqs.queue-name:image-analysis-queue}")
    public void processImageAnalysisMessage(@Payload String message) {
        log.info("Mensagem recebida da fila SQS: {}", message);

        try {
            SqsImageMessage sqsMessage = parseMessage(message);
            validateMessage(sqsMessage);

            log.info("Processando análise da imagem: {} ({})",
                    sqsMessage.getImageId(), sqsMessage.getOriginalFilename());

            ImageAnalysis result = processImageAnalysisUseCase.processImage(
                    sqsMessage.getImageId(),
                    sqsMessage.getOriginalFilename()
            );

            log.info("Análise concluída com sucesso. ID da análise: {}, Status: {}",
                    result.getId(), result.getStatus());

        } catch (Exception e) {
            log.error("Erro ao processar mensagem SQS: {}", message, e);
            // Em um ambiente real, você poderia enviar para uma DLQ (Dead Letter Queue)
            throw new RuntimeException("Falha no processamento da mensagem SQS", e);
        }
    }

    private SqsImageMessage parseMessage(String message) {
        try {
            return objectMapper.readValue(message, SqsImageMessage.class);
        } catch (Exception e) {
            log.error("Erro ao fazer parse da mensagem SQS: {}", message, e);
            throw new IllegalArgumentException("Formato de mensagem SQS inválido", e);
        }
    }

    private void validateMessage(SqsImageMessage message) {
        if (message.getImageId() == null || message.getImageId().trim().isEmpty()) {
            throw new IllegalArgumentException("ImageId é obrigatório na mensagem SQS");
        }

        if (message.getOriginalFilename() == null || message.getOriginalFilename().trim().isEmpty()) {
            throw new IllegalArgumentException("OriginalFilename é obrigatório na mensagem SQS");
        }

        log.debug("Mensagem SQS validada: {}", message);
    }
}
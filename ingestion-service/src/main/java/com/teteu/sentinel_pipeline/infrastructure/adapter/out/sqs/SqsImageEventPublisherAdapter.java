package com.teteu.sentinel_pipeline.infrastructure.adapter.out.sqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teteu.sentinel_pipeline.application.port.out.ImageEventPublisherPort;
import com.teteu.sentinel_pipeline.infrastructure.adapter.out.sqs.dto.SqsImageMessage;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SqsImageEventPublisherAdapter implements ImageEventPublisherPort {
    private final SqsTemplate sqsTemplate;
    private final ObjectMapper objectMapper;
    @Value("${spring.cloud.aws.sqs.queue-name}")
    private String queueName;

    @Override
    public void publishImageIngestedEvent(String imageId, String originalFilename) {
        SqsImageMessage message = new SqsImageMessage(imageId, originalFilename, "ingested");

        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            sqsTemplate.sendAsync(queueName, MessageBuilder.withPayload(jsonMessage).build());
            log.info("Message published successfully to SQS Queue: {}", queueName);
        } catch (Exception e) {
            log.error("Error while publishing message to SQS Queue: {}", queueName, e);
            throw new RuntimeException("Error while publishing message to SQS Queue: " + queueName);
        }
    }
}


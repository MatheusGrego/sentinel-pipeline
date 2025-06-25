package com.teteu.sentinel_pipeline.infraestructure.adapter.out.sqs;

import com.teteu.sentinel_pipeline.application.port.out.ImageEventPublisherPort;
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
    @Value("${spring.cloud.aws.sqs.queue-name}")
    private String queueName;

    @Override
    public void publishImageIngestedEvent(String imageId, String originalFilename) {
        String message = String.format("Image %s with name %s was ingested successfully!", imageId, originalFilename);

        try {
            sqsTemplate.send(queueName, MessageBuilder.withPayload(message).build());
            log.info("Message published successfully to SQS Queue: {}", queueName);
        } catch (Exception e) {
            log.error("Error while publishing message to SQS Queue: {}", queueName, e);
            throw new RuntimeException("Error while publishing message to SQS Queue: " + queueName);
        }
    }
}

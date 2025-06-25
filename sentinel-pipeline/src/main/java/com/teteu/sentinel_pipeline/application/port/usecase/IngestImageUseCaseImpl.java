package com.teteu.sentinel_pipeline.application.port.usecase;

import com.teteu.sentinel_pipeline.application.port.in.IngestImageUseCase;
import com.teteu.sentinel_pipeline.application.port.out.ImageEventPublisherPort;
import com.teteu.sentinel_pipeline.application.port.out.ImageStoragePort;
import com.teteu.sentinel_pipeline.domain.model.ImageFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngestImageUseCaseImpl implements IngestImageUseCase {

    private final ImageStoragePort imageStoragePort;
    private final ImageEventPublisherPort imageEventPublisherPort;

    @Override
    public String ingestImage(ImageFile imageFile) {
        String imageId = imageStoragePort.save(imageFile);

        // Passo 2: Com o ID da imagem salva, delega a responsabilidade de publicar o evento.
        // A implementação real (SQSAdapter) cuidará de enviar a mensagem.
        imageEventPublisherPort.publishImageIngestedEvent(imageId, imageFile.getName());

        return imageId;
    }
}

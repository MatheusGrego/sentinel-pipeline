package com.teteu.sentinel_pipeline.application.port.usecase;

import com.teteu.sentinel_pipeline.application.port.in.IngestImageUseCase;
import com.teteu.sentinel_pipeline.application.port.out.ImageEventPublisherPort;
import com.teteu.sentinel_pipeline.application.port.out.ImageStoragePort;
import com.teteu.sentinel_pipeline.domain.model.ImageFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @Override
    public String ingestImageFromMultipartFile(MultipartFile file) {
        try {
            // Converte MultipartFile para ImageFile
            ImageFile imageFile = new ImageFile(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    file.getBytes()
            );
            return ingestImage(imageFile); // Chama o método principal
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o arquivo de imagem: " + e.getMessage(), e);
        }
    }

}

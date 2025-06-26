package com.teteu.sentinel_pipeline.application.port.usecase;

import com.teteu.sentinel_pipeline.application.port.out.ImageEventPublisherPort;
import com.teteu.sentinel_pipeline.application.port.out.ImageStoragePort;
import com.teteu.sentinel_pipeline.domain.model.ImageFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class IngestImageUseCaseImplTest {

    @Mock
    private ImageStoragePort imageStoragePort;

    @Mock
    private ImageEventPublisherPort imageEventPublisherPort;

    @InjectMocks
    private IngestImageUseCaseImpl ingestImageUseCase;

    private ImageFile imageFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks
        imageFile = new ImageFile("test-image.jpg", "image/jpeg", 1024L, new byte[]{1, 2, 3, 4});
    }

    @Test
    void shouldIngestImageSuccessfully() {
        // Configura o comportamento do mock
        when(imageStoragePort.save(imageFile)).thenReturn("1234");

        // Executa o caso de uso
        String imageId = ingestImageUseCase.ingestImage(imageFile);

        // Verifica os resultados
        assertEquals("1234", imageId, "O ID da imagem deve ser '1234'");
        verify(imageStoragePort, times(1)).save(imageFile); // Confirma que o método foi chamado
        verify(imageEventPublisherPort, times(1)).publishImageIngestedEvent("1234", "test-image.jpg");
    }

    @Test
    void shouldThrowExceptionWhenSaveFails() {
        // Configura o mock para lançar uma exceção
        when(imageStoragePort.save(imageFile)).thenThrow(new RuntimeException("Erro ao salvar imagem"));

        // Verifica se a exceção é lançada na execução
        RuntimeException exception = assertThrows(RuntimeException.class, () -> ingestImageUseCase.ingestImage(imageFile));

        assertEquals("Erro ao salvar imagem", exception.getMessage(), "A mensagem de erro deve ser 'Erro ao salvar imagem'");
        verify(imageStoragePort, times(1)).save(imageFile); // Confirma que o método foi chamado apenas uma vez
        verify(imageEventPublisherPort, never()).publishImageIngestedEvent(any(), any()); // O evento não deve ser publicado
    }
}
package com.teteu.sentinel_pipeline.infraestructure.adapter.in.web;

import com.teteu.sentinel_pipeline.application.port.in.IngestImageUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(ImageIngestionController.class)
class ImageIngestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private IngestImageUseCase ingestImageUseCase;

    @Test
    void shouldProcessImageSuccessfully() throws Exception {
        when(ingestImageUseCase.ingestImageFromMultipartFile(any())).thenReturn("1234");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[]{1, 2, 3, 4}
        );

        mockMvc.perform(multipart("/api/ingestion/v1/images")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk()) // Espera um HTTP 200
                .andExpect(content().string("Imagem processada com sucesso. ID: 1234"));

        verify(ingestImageUseCase, times(1)).ingestImageFromMultipartFile(any());
    }

    @Test
    void shouldReturnBadRequestForEmptyFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "", MediaType.IMAGE_JPEG_VALUE, new byte[]{});

        mockMvc.perform(multipart("/api/ingestion/v1/images")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());

        verify(ingestImageUseCase, never()).ingestImageFromMultipartFile(any());
    }
}
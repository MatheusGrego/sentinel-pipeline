package com.teteu.analysis_service.infraestructure.adapter.out.analysis;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UtilityClass
@Slf4j
public class AnalysisUtils {

    /**
     * Gera um hash determinístico baseado no conteúdo da imagem
     * Útil para garantir consistência em análises da mesma imagem
     */
    public static String generateImageHash(byte[] imageData) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(imageData);

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString().substring(0, 8); // Primeiros 8 caracteres

        } catch (NoSuchAlgorithmException e) {
            log.warn("Erro ao gerar hash da imagem, usando fallback", e);
            return String.valueOf(imageData.length % 100000);
        }
    }

    /**
     * Calcula um seed determinístico para análises consistentes
     */
    public static long generateSeed(byte[] imageData, String filename) {
        String hash = generateImageHash(imageData);
        return hash.hashCode() + filename.hashCode();
    }

    /**
     * Normaliza um valor para o range 0.0 - 1.0
     */
    public static double normalize(double value, double min, double max) {
        if (max == min) return 0.5;
        return Math.max(0.0, Math.min(1.0, (value - min) / (max - min)));
    }

    /**
     * Aplica suavização a um valor usando uma função sigmoide
     */
    public static double sigmoid(double value) {
        return 1.0 / (1.0 + Math.exp(-value));
    }

    /**
     * Verifica se uma imagem parece ser válida baseado em padrões básicos
     */
    public static boolean isValidImageData(byte[] imageData) {
        if (imageData == null || imageData.length < 100) {
            return false;
        }

        // Verificar se não é um arquivo completamente uniforme
        byte firstByte = imageData[0];
        int sameBytes = 0;
        for (int i = 1; i < Math.min(imageData.length, 50); i++) {
            if (imageData[i] == firstByte) {
                sameBytes++;
            }
        }

        // Se mais de 80% dos primeiros bytes são iguais, provavelmente não é uma imagem válida
        return sameBytes < 40;
    }

    /**
     * Estima o tipo de arquivo baseado nos magic numbers
     */
    public static String detectFileType(byte[] imageData) {
        if (imageData.length < 4) {
            return "unknown";
        }

        // JPEG
        if (imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8) {
            return "jpeg";
        }

        // PNG
        if (imageData[0] == (byte) 0x89 && imageData[1] == 0x50 &&
                imageData[2] == 0x4E && imageData[3] == 0x47) {
            return "png";
        }

        // GIF
        if (imageData[0] == 0x47 && imageData[1] == 0x49 && imageData[2] == 0x46) {
            return "gif";
        }

        // BMP
        if (imageData[0] == 0x42 && imageData[1] == 0x4D) {
            return "bmp";
        }

        return "unknown";
    }
}
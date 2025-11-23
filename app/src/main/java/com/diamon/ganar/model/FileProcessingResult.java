package com.diamon.ganar.model;

/**
 * Resultado del procesamiento de un archivo como semilla.
 * Contiene información sobre el archivo procesado y si se aplicó compresión.
 * 
 * @author Bitcoin Wallet Generator
 * @version 1.0
 */
public class FileProcessingResult {

    /** Nombre del archivo original */
    public final String fileName;

    /** Tamaño original del archivo en bytes */
    public final long originalSize;

    /** Tamaño procesado (después de compresión si aplica) */
    public final long processedSize;

    /** Indica si se aplicó compresión */
    public final boolean wasCompressed;

    /** Tipo MIME del archivo */
    public final String mimeType;

    /** Bytes procesados del archivo (para generar hash) */
    public final byte[] processedBytes;

    /**
     * Constructor para resultado de procesamiento de archivo.
     * 
     * @param fileName       Nombre del archivo
     * @param originalSize   Tamaño original en bytes
     * @param processedSize  Tamaño después de procesar
     * @param wasCompressed  Si se comprimió
     * @param mimeType       Tipo MIME
     * @param processedBytes Bytes finales procesados
     */
    public FileProcessingResult(String fileName, long originalSize, long processedSize,
            boolean wasCompressed, String mimeType, byte[] processedBytes) {
        this.fileName = fileName;
        this.originalSize = originalSize;
        this.processedSize = processedSize;
        this.wasCompressed = wasCompressed;
        this.mimeType = mimeType;
        this.processedBytes = processedBytes;
    }

    /**
     * Calcula el ratio de compresión.
     * 
     * @return Porcentaje de reducción (0-100)
     */
    public int getCompressionRatio() {
        if (!wasCompressed || originalSize == 0) {
            return 0;
        }
        return (int) ((1.0 - ((double) processedSize / originalSize)) * 100);
    }

    /**
     * Formatea el tamaño en formato legible (KB, MB).
     * 
     * @param bytes Tamaño en bytes
     * @return String formateado
     */
    public static String formatSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        }
    }

    @Override
    public String toString() {
        return "FileProcessingResult{" +
                "fileName='" + fileName + '\'' +
                ", originalSize=" + formatSize(originalSize) +
                ", processedSize=" + formatSize(processedSize) +
                ", wasCompressed=" + wasCompressed +
                ", compressionRatio=" + getCompressionRatio() + "%" +
                '}';
    }
}

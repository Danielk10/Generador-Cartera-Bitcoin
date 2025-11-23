package com.diamon.ganar.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.diamon.ganar.model.FileProcessingResult;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Utilidades para procesamiento de archivos como semilla.
 * Implementa límites de tamaño y compresión automática.
 * 
 * @author Bitcoin Wallet Generator
 * @version 1.0
 */
public class FileUtils {

    /** Límite máximo de archivo: 10 MB */
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

    /** Umbral para aplicar compresión: 1 MB */
    public static final long COMPRESSION_THRESHOLD = 1 * 1024 * 1024; // 1 MB

    /**
     * Procesa un archivo para usarlo como semilla.
     * 
     * Proceso:
     * 1. Verifica que el archivo no exceda 10 MB
     * 2. Lee el contenido completo en memoria
     * 3. Si el archivo > 1 MB, aplica compresión GZIP
     * 4. Retorna resultado con información del procesamiento
     * 
     * @param context Contexto de la aplicación
     * @param fileUri URI del archivo seleccionado
     * @return FileProcessingResult con bytes procesados e información
     * @throws Exception Si el archivo es muy grande o hay error de lectura
     */
    public static FileProcessingResult processFile(Context context, Uri fileUri) throws Exception {
        ContentResolver resolver = context.getContentResolver();

        // Obtener nombre del archivo
        String fileName = getFileName(context, fileUri);

        // Obtener tipo MIME
        String mimeType = getMimeType(context, fileUri);

        // Leer archivo completo
        byte[] fileBytes;
        long originalSize;

        try (InputStream inputStream = resolver.openInputStream(fileUri);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            if (inputStream == null) {
                throw new Exception("No se pudo abrir el archivo");
            }

            // Leer en bloques de 4 KB
            byte[] data = new byte[4096];
            int nRead;
            long totalRead = 0;

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                totalRead += nRead;

                // Verificar límite de tamaño
                if (totalRead > MAX_FILE_SIZE) {
                    throw new Exception("El archivo excede el límite de " +
                            FileProcessingResult.formatSize(MAX_FILE_SIZE));
                }

                buffer.write(data, 0, nRead);
            }

            fileBytes = buffer.toByteArray();
            originalSize = fileBytes.length;
        }

        // Decidir si comprimir
        boolean shouldCompress = originalSize > COMPRESSION_THRESHOLD;
        byte[] processedBytes;
        long processedSize;

        if (shouldCompress) {
            // Aplicar compresión GZIP
            processedBytes = compressBytes(fileBytes);
            processedSize = processedBytes.length;
        } else {
            // Usar bytes originales
            processedBytes = fileBytes;
            processedSize = originalSize;
        }

        return new FileProcessingResult(
                fileName,
                originalSize,
                processedSize,
                shouldCompress,
                mimeType,
                processedBytes);
    }

    /**
     * Comprime bytes usando GZIP.
     * 
     * @param input Bytes a comprimir
     * @return Bytes comprimidos
     * @throws Exception Si hay error en compresión
     */
    private static byte[] compressBytes(byte[] input) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (GZIPOutputStream gzipStream = new GZIPOutputStream(outputStream)) {
            gzipStream.write(input);
            gzipStream.finish();
        }

        return outputStream.toByteArray();
    }

    /**
     * Obtiene el nombre del archivo desde su URI.
     * 
     * @param context Contexto
     * @param uri     URI del archivo
     * @return Nombre del archivo o "archivo_desconocido"
     */
    private static String getFileName(Context context, Uri uri) {
        String fileName = "archivo_desconocido";

        if (uri.getLastPathSegment() != null) {
            fileName = uri.getLastPathSegment();
        }

        return fileName;
    }

    /**
     * Obtiene el tipo MIME del archivo.
     * 
     * @param context Contexto
     * @param uri     URI del archivo
     * @return Tipo MIME o "application/octet-stream"
     */
    private static String getMimeType(Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);

        if (mimeType == null) {
            // Intentar obtener por extensión
            String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            if (extension != null) {
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
        }

        return mimeType != null ? mimeType : "application/octet-stream";
    }

    /**
     * Verifica si un tipo MIME es de imagen.
     * 
     * @param mimeType Tipo MIME
     * @return true si es imagen
     */
    public static boolean isImage(String mimeType) {
        return mimeType != null && mimeType.startsWith("image/");
    }

    /**
     * Verifica si un tipo MIME es de video.
     * 
     * @param mimeType Tipo MIME
     * @return true si es video
     */
    public static boolean isVideo(String mimeType) {
        return mimeType != null && mimeType.startsWith("video/");
    }

    /**
     * Verifica si un tipo MIME es de documento.
     * 
     * @param mimeType Tipo MIME
     * @return true si es documento
     */
    public static boolean isDocument(String mimeType) {
        return mimeType != null && (mimeType.startsWith("application/pdf") ||
                mimeType.startsWith("application/msword") ||
                mimeType.startsWith("application/vnd.") ||
                mimeType.startsWith("text/"));
    }
}

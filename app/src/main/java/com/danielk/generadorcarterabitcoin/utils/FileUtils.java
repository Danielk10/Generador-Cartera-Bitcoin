package com.danielk.generadorcarterabitcoin.utils;

import android.content.Context;
import android.net.Uri;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class FileUtils {
    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024;
    
    public static byte[] readBytesFromUri(Context context, Uri uri) throws Exception {
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                throw new Exception("No se pudo abrir el archivo");
            }
            
            outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            int totalBytesRead = 0;
            
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                totalBytesRead += bytesRead;
                if (totalBytesRead > MAX_FILE_SIZE) {
                    throw new Exception("El archivo es demasiado grande (máximo 10 MB)");
                }
                outputStream.write(buffer, 0, bytesRead);
            }
            
            return outputStream.toByteArray();
        } finally {
            if (inputStream != null) {
                try { inputStream.close(); } catch (Exception e) {}
            }
            if (outputStream != null) {
                try { outputStream.close(); } catch (Exception e) {}
            }
        }
    }
}
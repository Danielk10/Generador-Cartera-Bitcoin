package com.diamon.ganar.utils;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Utilidades de seguridad para la aplicación.
 * Maneja FLAG_SECURE para prevenir capturas de pantalla.
 * 
 * @author Bitcoin Wallet Generator
 * @version 1.0
 */
public class SecurityUtils {

    /**
     * Habilita FLAG_SECURE para prevenir capturas de pantalla.
     * Debe llamarse cuando se muestran claves privadas.
     * 
     * @param activity Activity actual
     */
    public static void enableScreenshotProtection(Activity activity) {
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
    }

    /**
     * Deshabilita FLAG_SECURE para permitir capturas de pantalla.
     * Debe llamarse cuando se ocultan claves privadas.
     * 
     * @param activity Activity actual
     */
    public static void disableScreenshotProtection(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    /**
     * Limpia datos sensibles de memoria (sobrescribiendo con ceros).
     * 
     * @param data Array de bytes a limpiar
     */
    public static void clearSensitiveData(byte[] data) {
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                data[i] = 0;
            }
        }
    }

    /**
     * Limpia string sensible de memoria.
     * Nota: En Java los Strings son inmutables, esta es una medida de "mejor
     * esfuerzo".
     * 
     * @param data String a limpiar
     */
    public static void clearSensitiveData(String data) {
        if (data != null) {
            // Convertir a char array y limpiar
            char[] chars = data.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                chars[i] = '\0';
            }
        }
    }
}

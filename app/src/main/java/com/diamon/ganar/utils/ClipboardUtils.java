package com.diamon.ganar.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

/**
 * Utilidades para copiar texto al portapapeles.
 * Proporciona feedback visual con Snackbar.
 * 
 * @author Bitcoin Wallet Generator
 * @version 1.0
 */
public class ClipboardUtils {

    /**
     * Copia texto al portapapeles y muestra Snackbar de confirmación.
     * 
     * @param context Contexto de la aplicación
     * @param view    Vista para anclar el Snackbar
     * @param label   Etiqueta del clip (ej: "Dirección Bitcoin")
     * @param text    Texto a copiar
     */
    public static void copyToClipboard(Context context, View view, String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText(label, text);
            clipboard.setPrimaryClip(clip);

            // Mostrar confirmación
            Snackbar.make(view, label + " copiado al portapapeles", Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Copia texto sensible (clave privada) con advertencia.
     * 
     * @param context Contexto
     * @param view    Vista para Snackbar
     * @param label   Etiqueta
     * @param text    Texto sensible
     */
    public static void copySensitiveToClipboard(Context context, View view, String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText(label, text);
            clipboard.setPrimaryClip(clip);

            // Advertencia para datos sensibles
            Snackbar.make(view, "⚠️ " + label + " copiado. Manténlo seguro.", Snackbar.LENGTH_LONG)
                    .setAction("OK", v -> {
                    })
                    .show();
        }
    }

    /**
     * Limpia el portapapeles (útil al pausar la app).
     * 
     * @param context Contexto
     */
    public static void clearClipboard(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("", "");
            clipboard.setPrimaryClip(clip);
        }
    }
}

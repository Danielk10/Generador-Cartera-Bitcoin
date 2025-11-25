package com.diamon.ganar.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

/**
 * Utilidad para gestionar el modo de pantalla completa e inmersivo en Android.
 * Oculta la barra de estado, barra de navegación y botones virtuales para una
 * experiencia de pantalla completa.
 * 
 * @author Bitcoin Wallet Generator
 * @version 2.0.0
 */
public class PantallaCompleta {

    private final AppCompatActivity actividad;

    /**
     * Constructor que recibe la actividad donde se aplicará el modo pantalla
     * completa.
     * 
     * @param actividad La actividad AppCompatActivity
     */
    public PantallaCompleta(AppCompatActivity actividad) {
        this.actividad = actividad;
    }

    /**
     * Coloca la actividad en modo pantalla completa ocultando la barra de estado.
     */
    public void pantallaCompleta() {
        // Con Edge-to-Edge habilitado, esto se maneja principalmente ocultando los
        // insets
        // pero mantenemos el método para compatibilidad con la estructura existente.
        ocultarBotonesVirtuales();
    }

    /**
     * Oculta los botones virtuales de navegación y activa el modo inmersivo sticky.
     * El modo inmersivo sticky permite que los controles del sistema reaparezcan
     * temporalmente con un gesto de deslizamiento, pero se ocultan automáticamente
     * después de unos segundos.
     */
    public void ocultarBotonesVirtuales() {
        WindowInsetsControllerCompat windowInsetsController = WindowCompat.getInsetsController(actividad.getWindow(),
                actividad.getWindow().getDecorView());

        if (windowInsetsController != null) {
            // Configurar el comportamiento de los insets del sistema
            windowInsetsController.setSystemBarsBehavior(
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

            // Ocultar las barras del sistema (estado y navegación)
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
        }
    }
}

package com.diamon.ganar.utils;

import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

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
        actividad
                .getWindow()
                .setFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Oculta los botones virtuales de navegación y activa el modo inmersivo sticky.
     * El modo inmersivo sticky permite que los controles del sistema reaparezcan
     * temporalmente con un gesto de deslizamiento, pero se ocultan automáticamente
     * después de unos segundos.
     */
    public void ocultarBotonesVirtuales() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actividad
                    .getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }
}

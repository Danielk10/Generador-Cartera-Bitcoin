# Generador de Cartera Bitcoin - Android Nativo

Aplicación Android de código abierto que genera carteras de Bitcoin (Direcciones P2PKH) a partir de una "semilla" (texto o archivo) utilizando operaciones criptográficas matemáticas puras.

Esta aplicación es una migración directa y fiel de un script de Python educativo, implementado ahora en Java Nativo con Material Design 3.

## Características

*   **Generación determinista:** `Clave Privada = SHA256(SHA256(semilla))`.
*   **Soporte de archivos:** Use una imagen, video o documento como su llave maestra.
*   **Criptografía Estándar:** Implementación de curvas elípticas SECP256k1 y RIPEMD160 mediante Bouncy Castle.
*   **Offline por diseño:** Sin permisos de internet. Todo ocurre en su dispositivo.
*   **Seguridad Visual:** Las claves privadas están ocultas por defecto.

## Compatibilidad

Las claves generadas en formato WIF (Wallet Import Format) son compatibles para importación en monederos estándar como **Electrum**, **Mycelium** o **Bitcoin Core**.

## Cómo compilar

1. Clonar este repositorio.
2. Abrir en Android Studio (Hedgehog o superior).
3. Sincronizar con Gradle.
4. Ejecutar en un dispositivo o emulador (API 23+).

## DESCARGO DE RESPONSABILIDAD (Disclaimer)

**ÚSELO BAJO SU PROPIO RIESGO.**

Esta aplicación se proporciona "tal cual", sin garantía de ningún tipo. El desarrollador no se hace responsable de la pérdida de fondos derivada de un mal uso, fallos en el generador de números aleatorios (aunque esta app es determinista basada en su input), o compromiso del dispositivo Android.

*   Si pierde el archivo o frase "semilla", **perderá sus fondos para siempre**.
*   Cualquier persona con acceso a su archivo "semilla" puede recrear su clave privada y gastar sus fondos.

## Licencia

Apache License 2.0
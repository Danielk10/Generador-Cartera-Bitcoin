# Generador de Cartera Bitcoin (Android)

Aplicación Android nativa para generar carteras Bitcoin (Legacy P2PKH) de forma segura y offline.

## Características

*   **Generación Determinista**: Crea claves a partir de una frase semilla (texto) o un archivo (imagen, video, etc.) usando SHA-256.
*   **100% Offline**: Todo el proceso ocurre localmente en tu dispositivo. No se requiere conexión a internet.
*   **Estándares Bitcoin**: Implementación fiel de SHA-256, RIPEMD-160, y Curva Elíptica secp256k1.
*   **Privacidad**: Las claves se generan en memoria RAM y se destruyen al cerrar la aplicación.

## Cómo Funciona

La aplicación sigue el proceso estándar de generación de direcciones Bitcoin:

1.  **Entropía**: Se calcula el hash SHA-256 de tu entrada (texto o archivo).
2.  **Clave Privada**: El resultado del hash es tu clave privada (256 bits).
3.  **WIF (Wallet Import Format)**: La clave privada se codifica en Base58Check para facilitar su importación.
4.  **Clave Pública**: Se deriva de la clave privada usando ECDSA (secp256k1).
5.  **Dirección**: Se aplica SHA-256 y luego RIPEMD-160 a la clave pública, seguido de codificación Base58Check.

## Importar en Electrum

Las claves generadas son totalmente compatibles con carteras estándar como Electrum.

1.  Abre Electrum y crea una nueva cartera.
2.  Selecciona "Importar claves privadas o direcciones".
3.  Copia el **WIF** (comienza con '5', 'K' o 'L') generado por esta app.
4.  Pégalo en Electrum.

> **Nota**: Esta app genera direcciones Legacy (empiezan con '1').

## Descargo de Responsabilidad (Disclaimer)

**USAR BAJO SU PROPIO RIESGO.**

Este software se proporciona "tal cual", sin garantía de ningún tipo, expresa o implícita. En ningún caso los autores serán responsables de ninguna reclamación, daño u otra responsabilidad, ya sea en una acción de contrato, agravio o de otro tipo, que surja de, fuera de o en conexión con el software o el uso u otros tratos en el software.

*   Si pierdes tu semilla o archivo de entrada, **pierdes tus fondos**.
*   Si alguien más tiene acceso a tu semilla o archivo, **puede robar tus fondos**.
*   Se recomienda usar este generador en un dispositivo seguro y libre de malware.

## Licencia

Apache License 2.0
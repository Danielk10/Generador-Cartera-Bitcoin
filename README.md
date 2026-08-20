# 🪙 Generador de Cartera Bitcoin

<div align="center">

[![Android](https://img.shields.io/badge/Android-6.0%20(API%2023)%20a%20Android%2017%20(API%2037)-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-API%2037%20(Android%2017)-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![AGP](https://img.shields.io/badge/AGP-9.2.1-blue?logo=android)](https://developer.android.com/studio/releases/gradle-plugin)
[![Gradle](https://img.shields.io/badge/Gradle-9.6.0-02303A?logo=gradle&logoColor=white)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](./LICENSE.txt)

**Aplicación Android nativa para generar carteras Bitcoin (Legacy P2PKH) de forma segura y 100% offline**

[Características](#-características) • [Compilación](#-compilación-y-ejecución) • [Uso](#-uso) • [Seguridad](#-seguridad) • [Arquitectura](#%EF%B8%8F-arquitectura-técnica) • [Licencia](#-licencia)

</div>

---

## 🌟 Características

### 🔐 Seguridad Primero
- **100% Offline**: Sin conexión a internet, sin servidores externos
- **Sin Persistencia**: Las claves se generan en RAM y se destruyen al cerrar
- **Protección de Pantalla**: FLAG_SECURE previene capturas cuando se muestran claves
- **Código Abierto**: Auditable y verificable por cualquiera
- **Estándares Bitcoin**: Implementación fiel de SHA-256, RIPEMD-160 y secp256k1

### ⚡ Funcionalidades
- ✅ Generación determinista desde texto o archivo
- ✅ Soporte para imágenes, videos, documentos como semilla (límite 10 MB)
- ✅ Compresión automática para archivos grandes (>1 MB)
- ✅ Visualización de Private Key (Hex), WIF, Public Key y Address
- ✅ Ocultación de claves privadas por defecto
- ✅ **Botones de copiar** para cada campo con confirmación para datos sensibles
- ✅ **Protección de pantalla** (FLAG_SECURE) al mostrar claves privadas
- ✅ Compatible con Electrum y otras wallets estándar

### 🎨 Diseño Moderno
- Material Design 3 (Material You)
- Tema Bitcoin con colores naranja/dorado
- Interfaz intuitiva y profesional
- Feedback visual con Snackbars
- Soporte para modo claro/oscuro
- Compatible con Android 6.0 (API 23) a Android 17 (API 37)

---

## 🚀 Compilación y Ejecución

### Especificaciones Técnicas
- **Android SDK:** Compile SDK 37 / Target SDK 37 / Min SDK 23
- **Android Build Tools:** 37.0.0
- **Android Gradle Plugin (AGP):** 9.2.1
- **Gradle:** 9.6.0 (Gradle Wrapper)
- **Firma Release:** Configurada vía `keystore.properties` (y variables de entorno para CI/CD).
- **Ruta de compilación:** Redirigida a `/tmp/calculo` para mantener limpio el entorno.

### Preparación del SDK:
```bash
bash setup-sdk.sh
```

### Comandos de Compilación:
- **APK Debug:** `./gradlew assembleDebug` (Salida: `/tmp/calculo/outputs/apk/debug/app-debug.apk`)
- **APK Release firmado:** `./gradlew assembleRelease` (Salida: `/tmp/calculo/outputs/apk/release/app-release.apk`)
- **AAB Release firmado (Play Store):** `./gradlew bundleRelease` (Salida: `/tmp/calculo/outputs/bundle/release/app-release.aab`)

Consulta [GEMINI.md](GEMINI.md) y [GUIA_PUBLICACION_PLAY_STORE.md](GUIA_PUBLICACION_PLAY_STORE.md) para más detalles.

---

## 🚀 Uso

### 1️⃣ Generar desde Texto
1. Ingresa una frase semilla en el campo de texto
2. Presiona **"Generar"**
3. Visualiza tu cartera Bitcoin

### 2️⃣ Generar desde Archivo
1. Presiona **"Archivo"**
2. Selecciona cualquier archivo (imagen, video, documento)
   - **Límite**: 10 MB máximo
   - **Compresión**: Archivos >1 MB se comprimen automáticamente con GZIP
3. La app procesará el archivo y mostrará información del tamaño
4. Visualiza tu cartera Bitcoin

### 3️⃣ Copiar Claves
- **Dirección y Clave Pública**: Copia directa con un clic
- **Clave Privada y WIF**: Requiere confirmación por seguridad
- Feedback visual con Snackbar al copiar

### 4️⃣ Importar en Electrum

Las claves generadas son **100% compatibles** con Electrum:

1. Abre Electrum
2. Crea una nueva cartera
3. Selecciona **"Importar claves privadas o direcciones"**
4. Copia el **WIF** de esta app (comienza con '5', 'K' o 'L')
5. Pégalo en Electrum

> **Nota**: Esta app genera direcciones **Legacy (P2PKH)** que empiezan con '1'.

---

## 🔬 Cómo Funciona

La aplicación sigue el proceso estándar de generación de direcciones Bitcoin:

```
Semilla (Texto/Archivo)
    ↓
[Procesamiento de Archivo]
    ├─→ Límite: 10 MB
    └─→ Compresión GZIP si >1 MB
    ↓
SHA-256
    ↓
SHA-256 (nuevamente)
    ↓
Clave Privada (256 bits)
    ↓
    ├─→ WIF (Base58Check con prefijo 0x80)
    └─→ ECDSA secp256k1
        ↓
    Clave Pública
        ↓
    SHA-256
        ↓
    RIPEMD-160
        ↓
    Dirección Bitcoin (Base58Check con prefijo 0x00)
```

### Implementación Criptográfica

- **Entropía**: `SHA-256(input)`
- **Clave Privada**: `SHA-256(SHA-256(semilla))`
- **WIF**: `Base58Check(0x80 + PrivateKey)`
- **Clave Pública**: `ECDSA_secp256k1(PrivateKey)`
- **Dirección**: `Base58Check(0x00 + RIPEMD160(SHA256(PublicKey)))`

---

## 🛡️ Seguridad

### ✅ Buenas Prácticas
- Usa esta app en un dispositivo **sin malware**
- Genera carteras en un entorno **offline**
- **Guarda tu semilla** de forma segura (papel, metal, USB cifrado)
- **Nunca compartas** tu clave privada o WIF
- La app bloquea capturas de pantalla cuando muestras claves privadas
- Las claves se ocultan automáticamente al cambiar de app

### 🔄 Recuperación de Claves

**IMPORTANTE**: Puedes regenerar tus claves privadas usando la **misma semilla** en esta app.

- ✅ Guarda tu semilla de forma segura
- ✅ Usa la misma semilla para recuperar tus claves
- ⚠️ Si alguien más encuentra tu semilla, también puede regenerar tus claves
- ⚠️ Protege tu semilla como si fuera tu clave privada

### ⚠️ Descargo de Responsabilidad

**USAR BAJO SU PROPIO RIESGO.**

Este software se proporciona "tal cual", sin garantía de ningún tipo. Los autores no son responsables de:

- ❌ Pérdida de fondos por pérdida de semilla
- ❌ Robo de fondos por exposición de claves
- ❌ Errores en la generación (aunque improbables)

**Recomendaciones**:
- Prueba primero con pequeñas cantidades
- Verifica las direcciones generadas
- Mantén backups seguros de tus semillas

---

## 🏗️ Arquitectura Técnica

### Stack Tecnológico
- **Lenguaje**: Java (Nativo)
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **UI**: Material Design 3
- **Criptografía**: Bouncy Castle (`bcprov-jdk15to18`)
- **Build**: Gradle (Groovy DSL)

### Estructura del Proyecto
```
app/
├── src/main/java/com/diamon/ganar/
│   ├── MainActivity.java           # UI, navegación y listeners
│   ├── MainViewModel.java          # Lógica de negocio y estado
│   ├── BitcoinUtils.java           # Fachada criptográfica (legacy)
│   ├── utils/
│   │   ├── CryptoUtils.java        # SHA-256, RIPEMD-160, ECDSA
│   │   ├── Base58Utils.java        # Codificación Base58Check
│   │   ├── FileUtils.java          # Procesamiento de archivos
│   │   ├── ClipboardUtils.java     # Copiar al portapapeles
│   │   └── SecurityUtils.java      # FLAG_SECURE y limpieza
│   └── model/
│       ├── WalletData.java         # Modelo de cartera
│       └── FileProcessingResult.java # Info de archivo procesado
├── src/main/res/
│   ├── layout/
│   │   └── activity_main.xml       # Layout con botones de copiar
│   ├── values/
│   │   ├── colors.xml              # Tema Bitcoin
│   │   └── themes.xml              # Material 3
│   └── drawable/
│       ├── ic_btc_shield.xml       # Logo
│       ├── ic_copy.xml             # Icono copiar
│       ├── ic_file.xml             # Icono archivo
│       ├── ic_compress.xml         # Icono compresión
│       ├── ic_visibility.xml       # Icono mostrar
│       └── ic_visibility_off.xml   # Icono ocultar
└── build.gradle                    # Dependencias
```

### Clases Principales

#### `CryptoUtils.java`
Operaciones criptográficas core:
- `generatePrivateKey()`: SHA-256 doble
- `derivePublicKey()`: ECDSA secp256k1
- `generateAddress()`: SHA-256 + RIPEMD-160 + Base58Check
- `generateWIF()`: Base58Check con prefijo 0x80

#### `FileUtils.java`
Procesamiento robusto de archivos:
- Límite de 10 MB
- Compresión GZIP automática para archivos >1 MB
- Detección de tipo MIME
- Manejo de errores

#### `SecurityUtils.java`
Seguridad mejorada:
- `enableScreenshotProtection()`: Activa FLAG_SECURE
- `disableScreenshotProtection()`: Desactiva FLAG_SECURE
- `clearSensitiveData()`: Limpieza de memoria

---

## 🤝 Contribuir

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 📄 Licencia

Este proyecto está bajo la Licencia Apache 2.0. Ver el archivo [LICENSE.txt](LICENSE.txt) para más detalles.

```
Copyright 2025

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

## 📞 Soporte

Si encuentras algún problema o tienes sugerencias:

- 🐛 [Reportar un Bug](https://github.com/TU_USUARIO/Generador-Cartera-Bitcoin/issues)
- 💡 [Solicitar una Característica](https://github.com/TU_USUARIO/Generador-Cartera-Bitcoin/issues)

---

<div align="center">

**⚠️ Recuerda: No confíes, verifica. Este es software de código abierto.**

Hecho con ❤️ para la comunidad Bitcoin

</div>
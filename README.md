# 🪙 Generador de Cartera Bitcoin

<div align="center">

![Bitcoin](https://img.shields.io/badge/Bitcoin-000?style=for-the-badge&logo=bitcoin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Material Design](https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=material-design&logoColor=white)

**Aplicación Android nativa para generar carteras Bitcoin (Legacy P2PKH) de forma segura y 100% offline**

[Características](#-características) • [Instalación](#-instalación) • [Uso](#-uso) • [Seguridad](#-seguridad) • [Licencia](#-licencia)

</div>

---

## 🌟 Características

### 🔐 Seguridad Primero
- **100% Offline**: Sin conexión a internet, sin servidores externos
- **Sin Persistencia**: Las claves se generan en RAM y se destruyen al cerrar
- **Código Abierto**: Auditable y verificable por cualquiera
- **Estándares Bitcoin**: Implementación fiel de SHA-256, RIPEMD-160 y secp256k1

### ⚡ Funcionalidades
- ✅ Generación determinista desde texto o archivo
- ✅ Soporte para imágenes, videos, documentos como semilla
- ✅ Visualización de Private Key (Hex), WIF, Public Key y Address
- ✅ Ocultación de claves privadas por defecto
- ✅ Compatible con Electrum y otras wallets estándar

### 🎨 Diseño Moderno
- Material Design 3 (Material You)
- Tema Bitcoin con colores naranja/dorado
- Interfaz intuitiva y profesional
- Soporte para modo claro/oscuro

---

## 📱 Instalación

### Requisitos
- Android 6.0 (API 23) o superior
- ~5 MB de espacio libre

### Desde el Código Fuente

```bash
# Clonar el repositorio
git clone https://github.com/TU_USUARIO/Generador-Cartera-Bitcoin.git

# Abrir en Android Studio
# Build > Make Project
# Run > Run 'app'
```

---

## 🚀 Uso

### 1️⃣ Generar desde Texto
1. Ingresa una frase semilla en el campo de texto
2. Presiona **"Generar"**
3. Visualiza tu cartera Bitcoin

### 2️⃣ Generar desde Archivo
1. Presiona **"Archivo"**
2. Selecciona cualquier archivo (imagen, video, documento)
3. La app calculará el hash SHA-256 del archivo
4. Visualiza tu cartera Bitcoin

### 3️⃣ Importar en Electrum

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
- **Guarda tu semilla** de forma segura (papel, metal)
- **Nunca compartas** tu clave privada o WIF

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
│   ├── MainActivity.java       # UI y navegación
│   ├── MainViewModel.java      # Lógica de negocio
│   └── BitcoinUtils.java       # Criptografía
├── src/main/res/
│   ├── layout/
│   │   └── activity_main.xml   # Layout principal
│   ├── values/
│   │   ├── colors.xml          # Tema Bitcoin
│   │   └── themes.xml          # Material 3
│   └── drawable/
│       └── ic_btc_shield.xml   # Logo
└── build.gradle                # Dependencias
```

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
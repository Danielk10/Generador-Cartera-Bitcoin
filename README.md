# 🪙 Generador de Cartera Bitcoin - Android Nativo

<p align="center">
  <img src="app/src/main/res/drawable/ic_launcher_foreground.xml" alt="Logo" width="120">
</p>

<p align="center">
  <strong>Aplicación Android de código abierto para generar carteras Bitcoin (P2PKH) de forma segura y offline</strong>
</p>

<p align="center">
  <a href="#características">Características</a> •
  <a href="#cómo-funciona">Cómo Funciona</a> •
  <a href="#instalación">Instalación</a> •
  <a href="#uso">Uso</a> •
  <a href="#seguridad">Seguridad</a> •
  <a href="#compatibilidad">Compatibilidad</a> •
  <a href="#licencia">Licencia</a>
</p>

---

## 📋 Descripción

Esta aplicación es una **migración fiel y directa** de un script educativo de Python a una aplicación Android nativa con **Material Design 3**. Genera carteras de Bitcoin (Direcciones P2PKH) a partir de una "semilla" (texto o archivo) utilizando operaciones criptográficas matemáticas puras.

### 🎯 Características Principales

- ✅ **Generación Determinista**: `Clave Privada = SHA256(SHA256(semilla))`
- 📁 **Soporte de Archivos**: Use cualquier archivo (imagen, video, documento) como llave maestra
- 🔐 **Criptografía Estándar**: Implementación SECP256k1 y RIPEMD160 vía Bouncy Castle
- 🔒 **100% Offline**: Sin permisos de internet. Todo ocurre en tu dispositivo
- 👁️ **Seguridad Visual**: Claves privadas ocultas por defecto con toggle para mostrar/ocultar
- 🎨 **Material Design 3**: Interfaz moderna con tema profesional Bitcoin (naranjas/dorados)
- 🏗️ **Arquitectura MVVM**: Código limpio, mantenible y testeable
- 📱 **Responsive**: Funciona en todos los tamaños de pantalla Android

---

## 🔬 Cómo Funciona

### Algoritmo de Generación

```
1. ENTRADA: Semilla (texto o bytes de archivo)
2. Entropía = SHA256(SHA256(semilla))
3. Clave Privada (32 bytes) = Entropía
4. Clave Pública = ECDSA_SECP256k1(Clave Privada)
5. WIF = Base58CheckEncode(0x80 + Clave Privada)
6. Hash160 = RIPEMD160(SHA256(Clave Pública))
7. Dirección Bitcoin = Base58CheckEncode(0x00 + Hash160)
```

### Diagrama de Flujo

```
┌─────────────────┐
│  Semilla (Seed) │
│  Texto o Archivo│
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ SHA256(SHA256()) │ ◄── Doble hash para entropía
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Clave Privada   │ ◄── 32 bytes (256 bits)
│   (Hexadecimal) │
└────────┬────────┘
         │
         ├──────────────────────┐
         │                      │
         ▼                      ▼
┌─────────────────┐    ┌─────────────────┐
│  WIF Format     │    │ ECDSA SECP256k1 │
│ Base58Check     │    │  Clave Pública  │
└─────────────────┘    └────────┬────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │ SHA256 + RIPEMD │
                       │    Hash 160     │
                       └────────┬────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │Dirección Bitcoin│
                       │   Base58Check   │
                       └─────────────────┘
```

---

## 🛠️ Instalación

### Requisitos Previos

- **Android Studio** Hedgehog (2023.1.1) o superior
- **JDK** 11 o superior
- **Android SDK** API 23+ (Android 6.0 Marshmallow)

### Pasos de Instalación

1. **Clonar el repositorio**

```bash
git clone https://github.com/Danielk10/Generador-Cartera-Bitcoin.git
cd Generador-Cartera-Bitcoin
```

2. **Abrir en Android Studio**

- Abrir Android Studio
- File → Open
- Seleccionar la carpeta del proyecto
- Esperar sincronización de Gradle

3. **Ejecutar la aplicación**

- Conectar un dispositivo Android (API 23+) o iniciar un emulador
- Click en el botón "Run" (▶️) o presionar `Shift + F10`

### Construcción desde Línea de Comandos

```bash
# Debug APK
./gradlew assembleDebug

# Release APK (firmado)
./gradlew assembleRelease

# Instalar en dispositivo conectado
./gradlew installDebug
```

Los APKs se generarán en: `app/build/outputs/apk/`

---

## 📱 Uso

### Generar una Cartera

#### Opción 1: Desde Texto (Frase Semilla)

1. Abrir la aplicación
2. Escribir tu frase secreta en el campo "Semilla (Texto)"
3. Click en **"GENERAR CARTERA"**
4. Visualizar:
   - ✅ Dirección Bitcoin (pública)
   - 🔐 Clave Privada (oculta por defecto)
   - 📄 WIF (Wallet Import Format)
   - 🔑 Clave Pública

#### Opción 2: Desde Archivo

1. Click en el botón **"SELECCIONAR ARCHIVO"**
2. Elegir cualquier archivo (imagen, video, PDF, etc.)
3. La aplicación leerá los bytes del archivo y generará las claves
4. Visualizar los resultados

### Mostrar/Ocultar Claves Sensibles

- Por defecto, la **Clave Privada** y el **WIF** están ocultos (`••••••••`)
- Click en el ícono del **👁️ (ojo)** junto a cada campo para revelar/ocultar

### Importar en Electrum

1. Copiar el valor **WIF** generado
2. Abrir Electrum Wallet
3. Ir a `Wallet` → `Private Keys` → `Import`
4. Pegar el WIF
5. ✅ Tu cartera está lista para usar

---

## 🔒 Seguridad

### ⚠️ DESCARGO DE RESPONSABILIDAD

**ÚSELO BAJO SU PROPIO RIESGO.**

Esta aplicación se proporciona **"tal cual"**, sin garantía de ningún tipo, expresa o implícita. El desarrollador **NO se hace responsable** de:

- ❌ Pérdida de fondos por mal uso
- ❌ Fallos en el generador (aunque es determinista)
- ❌ Compromiso del dispositivo Android
- ❌ Ataques de malware o keyloggers

### 🛡️ Buenas Prácticas de Seguridad

#### Antes de Generar tu Cartera Real:

1. **✈️ Modo Avión**: Activa el modo avión en tu dispositivo
2. **🔌 Desconecta Internet**: Asegúrate de que WiFi y datos móviles estén desactivados
3. **🧹 Dispositivo Limpio**: Usa un dispositivo sin malware o aplicaciones sospechosas
4. **📝 Backup Físico**: Escribe tu semilla y claves en papel (nunca digitalmente)
5. **🗑️ Elimina Historial**: Borra el portapapeles después de copiar claves

#### Gestión de la Semilla:

- ⚠️ **Si pierdes la semilla, PIERDES tus fondos para siempre**
- ⚠️ **Cualquier persona con tu semilla puede robar tus Bitcoins**
- ✅ Guarda tu semilla en un lugar físico seguro (caja fuerte)
- ✅ Nunca compartas tu semilla con nadie
- ✅ Considera usar un Passphrase adicional para mayor seguridad

### 🔐 Características de Seguridad de la App

| Característica | Implementación |
|----------------|----------------|
| Sin permisos de Internet | ❌ No declarado en Manifest |
| Procesamiento offline | ✅ Todo local en el dispositivo |
| Sin almacenamiento persistente | ✅ No guarda claves en disco |
| Generación determinista | ✅ Misma semilla = mismas claves |
| Código open source | ✅ Auditable por cualquiera |
| Criptografía estándar | ✅ Bouncy Castle (audited) |

---

## ⚙️ Compatibilidad

### Requisitos del Sistema

- **Android**: 6.0 (API 23) o superior
- **Arquitecturas**: ARM, ARM64, x86, x86_64
- **Espacio**: ~15 MB

### Carteras Compatibles

Las claves generadas en formato **WIF** son 100% compatibles con:

- ✅ **Electrum** (Desktop/Mobile)
- ✅ **Mycelium** (Android)
- ✅ **Bitcoin Core** (Desktop)
- ✅ **Exodus** (Multi-platform)
- ✅ **Trust Wallet** (Mobile)
- ✅ Cualquier cartera que soporte WIF (Wallet Import Format)

### Tipos de Direcciones

Esta aplicación genera direcciones **P2PKH (Legacy)**:

- Formato: `1xxxxxxxxxxxxxxxxxxxxxxxxxxxxx`
- Uso: Transacciones Bitcoin estándar
- Compatibilidad: Universal (todas las carteras)

**Nota**: No genera direcciones SegWit (bech32) ni Taproot. Solo P2PKH legacy.

---

## 🏗️ Arquitectura del Proyecto

### Estructura de Paquetes

```
com.danielk.generadorcarterabitcoin/
├── crypto/
│   ├── BitcoinUtils.java         # Lógica criptográfica completa
│   ├── Base58.java               # Codificación Base58Check
│   └── KeyGenerator.java         # Generación de claves
├── model/
│   └── WalletData.java           # Modelo de datos de cartera
├── ui/
│   ├── MainActivity.java         # Actividad principal
│   ├── AboutActivity.java        # Información de la app
│   └── PrivacyPolicyActivity.java# Políticas de privacidad
├── viewmodel/
│   └── WalletViewModel.java      # Gestión de estado UI
└── utils/
    └── FileUtils.java            # Utilidades de archivos
```

### Tecnologías Utilizadas

| Componente | Tecnología | Versión |
|------------|------------|---------|
| Lenguaje | Java | 11 |
| UI Framework | Material Design 3 | 1.12.0 |
| Arquitectura | MVVM | - |
| Build Tool | Gradle | 8.7 |
| Criptografía | Bouncy Castle | 1.78.1 |
| Min SDK | Android 6.0 | API 23 |
| Target SDK | Android 15 | API 36 |

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

### Reportar Problemas

Si encuentras un bug o tienes una sugerencia:

- Abre un [Issue](https://github.com/Danielk10/Generador-Cartera-Bitcoin/issues)
- Describe el problema detalladamente
- Incluye capturas de pantalla si es posible
- Especifica tu versión de Android

---

## 📄 Licencia

```
Copyright 2025 Danielk10

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

Ver el archivo [LICENSE.txt](LICENSE.txt) para más detalles.

---

## 📚 Referencias

- [Bitcoin Wiki - Wallet Import Format](https://en.bitcoin.it/wiki/Wallet_import_format)
- [Bitcoin Wiki - Base58Check Encoding](https://en.bitcoin.it/wiki/Base58Check_encoding)
- [Bitcoin Improvement Proposal 32 (BIP32)](https://github.com/bitcoin/bips/blob/master/bip-0032.mediawiki)
- [SECP256k1 Curve](https://en.bitcoin.it/wiki/Secp256k1)
- [Bouncy Castle Crypto APIs](https://www.bouncycastle.org/)

---

## 👨‍💻 Autor

**Danielk10**

- GitHub: [@Danielk10](https://github.com/Danielk10)
- Repositorio: [Generador-Cartera-Bitcoin](https://github.com/Danielk10/Generador-Cartera-Bitcoin)

---

## ⭐ Dale una Estrella

Si este proyecto te fue útil, considera darle una ⭐ en GitHub. ¡Gracias!

---

<p align="center">
  <sub>Hecho con ❤️ y ☕ en Venezuela</sub>
</p>
# Notas de Lanzamiento - Generador de Cartera Bitcoin v2.1.0

Esta versión oficial (`v2.1.0`, código de versión `31`) moderniza integralmente la plataforma y toolchain de compilación de la aplicación para Android, actualizando el soporte a Android 17 (API 37), toolchains de compilación modernos y configuración de firma oficial.

---

## 🚀 Nuevas Características y Mejoras

* **Actualización del SDK de Android y Plataformas:**
  - `compileSdk` y `targetSdk` actualizados a **API 37 (Android 17 / Cinnamon Bun)**.
  - Compatibilidad mantenida desde `minSdk 23` (Android 6.0 Marshmallow).
  - Android Build Tools actualizado a **37.0.0**.

* **Modernización del Toolchain de Gradle:**
  - Android Gradle Plugin (AGP) actualizado a **9.2.1**.
  - Gradle Wrapper actualizado a **9.6.0**.
  - `org.gradle.configuration-cache=true` habilitado para acelerar compilaciones incrementales.

* **Actualización de Librerías y Dependencias:**
  - Componentes AndroidX y Material Design actualizados (`appcompat:1.7.1`, `material:1.14.0`, `activity:1.13.0`, `constraintlayout:2.2.1`, `lifecycle:2.10.0`).
  - Actualización de biblioteca criptográfica Bouncy Castle (`bcprov-jdk15to18:1.84`).

* **Configuración de Firma Release Oficial:**
  - Integración de firma release automatizada mediante `keystore.properties` (`keystore.jks` con alias `tu_alias`).
  - Soporte para variables de entorno de firma (`SIGNING_STORE_FILE`, etc.) para pipelines de CI/CD.
  - Protección estricta en `.gitignore` para prevenir filtraciones de credenciales.

* **Redirección de Almacenamiento y Caché en `/tmp`:**
  - Redirección completa de la carpeta de compilación hacia `/tmp/calculo` para mantener limpio el espacio de trabajo.
  - Redirección automática de la caché de Gradle (`GRADLE_USER_HOME`) a `/tmp/.gradle` para optimizar entornos con disco persistente limitado.

* **Documentación y Automatización:**
  - Incorporación de `GEMINI.md` con instrucciones completas de SDK, compilación y despliegue.
  - Creación de `upload_play_store.py` y `GUIA_PUBLICACION_PLAY_STORE.md` para publicación automatizada.
  - Actualización del `README.md` con las nuevas insignias y sección de compilación.

---

## 📦 Artefactos de la Versión

- **`Generador_Cartera_Bitcoin_v2.1.0.apk`**: Paquete APK firmado y listo para instalación directa en dispositivos Android (API 23+).
- **`Generador_Cartera_Bitcoin_v2.1.0.aab`**: Android App Bundle firmado y optimizado para distribución en Google Play Store.

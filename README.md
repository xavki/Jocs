# 🎮 Jocs · Aplicación Android en Kotlin

Aplicación Android desarrollada en **Kotlin** que reúne varios mini juegos y funcionalidades multimedia.  
Forma parte de un proyecto educativo del Institut Marianao para practicar programación en Android con servicios, fragmentos, vistas personalizadas y gestión de sonidos.

---

## 🧩 Descripción general

**Jocs** (“Juegos” en catalán) es una aplicación compuesta por diferentes pantallas (Activities y Fragments) que permiten al usuario jugar, configurar opciones y reproducir música de fondo mientras utiliza la app.  

Incluye:
- Un menú principal con navegación entre juegos y configuración.  
- Un sistema de puntuaciones.  
- Reproducción de música mediante un servicio en segundo plano.  
- Control de preferencias y sonidos.  
- Gráficos y animaciones personalizados.  

---

## 🚀 Características principales
- Código 100 % **Kotlin**.
- **Vista personalizada (`VistaJoc.kt`)** para dibujar y actualizar el juego.  
- **`Musicservice.kt`** gestiona la música en segundo plano con control de inicio/pausa.  
- **`ConfiguracioActivity.kt`** permite cambiar opciones del juego.  
- **`PuntuacionsActivity.kt`** muestra las mejores puntuaciones.  
- **`Imagesfragments.kt`** implementa fragmentos con recursos gráficos.
- Uso de **Intents** para comunicación entre Activities.  
- Interfaz adaptada a diferentes densidades de pantalla (carpetas `drawable-`, `values-`, `mipmap-`).

---

## 🧠 Tecnologías utilizadas
- **Kotlin**
- **Android Studio**
- **ViewBinding / XML Layouts**
- **MediaPlayer API**
- **Services y Fragments**
- **SharedPreferences**
- **Animaciones (carpeta `/res/anim`)**

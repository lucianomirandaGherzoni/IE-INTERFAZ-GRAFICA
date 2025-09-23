# CASINO DE DADOS 
Proyecto colaborativo de un casino de dados escrito e implementado en Java usando herencia, polimorfismo, encapsulamiento y manipulación de Strings. 

## Instrucciones para correr el juego

### Requisitos
- Java JDK 8 o superior
- NetBeans IDE con soporte para Ant
- Sistema operativo: Windows, macOS o Linux

### Pasos para ejecutar

1. **Clonar el repositorio:**
```bash
   git https://github.com/lucianomirandaGherzoni/IE-INTERFAZ-GRAFICA.git
   cd casino-dados

2. **Abrir en NetBeans:**
   - Abrir NetBeans IDE
   - File → Open Project
   - Seleccionar la carpeta del proyecto
   - Hacer clic en "Open Project"

3. **Compilar y ejecutar:**
   - Hacer clic derecho en el proyecto
   - Seleccionar "Clean and Build"
   - Hacer clic derecho en `CasinoDados.java`
   - Seleccionar "Run File"

### Cómo jugar

1. Ingresa el número de jugadores (2-4)
2. Para cada jugador:
   - Ingresa el nombre
   - Ingresa un apodo válido (3-10 caracteres, solo letras y espacios)
   - Selecciona el tipo: 1=Novato, 2=Experto, 3=VIP, 4=Casino
3. El juego se ejecuta automáticamente durante 3 rondas
4. Al final se muestra el reporte con estadísticas e historial

### Comandos BONUS (durante el juego)
- `STATS` - Mostrar estadísticas actuales
- `HISTORY` - Mostrar historial de partidas
- `RANKING` - Mostrar ranking actual
- `TRAMPAS` - Mostrar registro de trampas del casino
- `SAVE [nombre]` - Guardar partida con nombre personalizado
- `QUIT` - Salir del juego

## Integrantes y Roles

### Nicolás Paredes
**Rama**: `Nicolas-Paredes`  
**Responsabilidades**:
- Implementación de la clase `JugadorCasino` con habilidades tramposas.
- Sistema de registro de trampas (`RegistroTrampas`).
- Estadísticas del juego: mayor apuesta, mejor puntaje, víctimas del casino.
- Integración de trampas en el flujo del juego.

### Santiago Altamirano
**Rama**: `Santiago-Altamirano`  
**Responsabilidades**:
- Sistema de historial de partidas (últimas 5 partidas).
- Implementación de métodos `guardarPartida()` y `mostrarHistorial()`.
- Generación del reporte final con `StringBuilder`.
- Formato de salida del historial.

### Luciano Miranda
**Rama**: `Luciano-Miranda`  
**Responsabilidades**:
- Sistema de validación de apodos (3-10 caracteres, solo letras y espacios).
- Implementación del sistema de comandos BONUS.
- Comandos: `STATS`, `HISTORY`, `RANKING`, `TRAMPAS`, `SAVE`, `QUIT`.
- Mejoras en la experiencia de usuario.

# Proyecto Casino

## Recursos Adicionales

### 📁 Documentación del Proceso
**Enlace a Drive**: https://drive.google.com/drive/folders/1P7tDpF66JrV2PwDkHdMiBDP_waO6M9HZ
Contenido disponible:
- Prompts utilizados por cada integrante durante el desarrollo.
- Videos explicativos individuales de cada funcionalidad implementada.
- Capturas de pantalla.

## Tecnologías Utilizadas

- **Lenguaje**: Java 8+
- **Build System**: Apache Ant
- **IDE**: NetBeans
- **Control de versiones**: Git/GitHub
- **Metodología**: Desarrollo colaborativo con feature branches
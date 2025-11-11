# CASINO DE DADOS 
Proyecto colaborativo de un casino de dados escrito e implementado en Java, reestructurado bajo el patrón de diseño Modelo-Vista-Controlador (MVC). Esta versión incluye una interfaz gráfica de usuario (GUI) completa construida con Java Swing, sistemas de persistencia (archivos de texto y base de datos) e integración con MySQL mediante JDBC.

## Arquitectura del Proyecto (MVC)
El proyecto ha sido refactorizado para seguir el patrón MVC, separando claramente las responsabilidades, tal como se refleja en la estructura de paquetes:
### casino.modelo
Contiene toda la lógica de negocio (reglas del juego, clases Jugador, Partida, Dado, etc.). No tiene dependencias con javax.swing

### casino.vista
Contiene todas las ventanas y elementos de la interfaz gráfica (VentanaConfiguracion, VentanaJuego, VentanaReporte). Muestra la información y captura las interacciones del usuario. No tiene lógica de negocio.

### casino.controlador
Contiene la lógica que maneja los eventos de la vista (clics, ingresos) y coordina las actualizaciones entre el Modelo y la Vista.

## Funcionalidaes Clave

### Interfaz Gráfica (GUI) Completa:
- `Ventana de Configuración`: Permite registrar y eliminar jugadores (2-4), validar apodos, configurar el dinero inicial y la cantidad de partidas.
- `Ventana de Juego`: Interfaz principal con paneles informativos (Pozo, Ronda), estado de jugadores (Dinero, Apuesta, Resultado de Dados) y un Log de eventos en tiempo real.
- `Ventana de Reporte Final`: Muestra un resumen del juego (Ranking, Estadísticas detalladas, Historial).

### Sistema de Persistencia :
- `Archivos`: Permite el guardado y la carga manual de partidas completas para reanudar el estado exacto del juego.
- `Base de Datos (JDBC): o`: Persistencia de estadísticas permanentes de jugadores y partidas mediante MySQL.

## Instrucciones para correr el juego

### Requisitos
- Java JDK 8 o superior
- NetBeans IDE con soporte para Ant
- Sistema operativo: Windows, macOS o Linux
- `Base de datos:` Acceos a una instancia de MySQL (preferiblemente Workbench).

### Pasos para ejecutar

1. **Clonar el repositorio:**
   Dentro del bash:
   git clone https://github.com/lucianomirandaGherzoni/IE-INTERFAZ-GRAFICA.git
   cd casino-dados

2. **Configurar Base de Datos**
   - Asegúrate de tener un servidor MySQL en ejecución.
   - Ejecuta los scripts SQL (provistos en la sección "Scripts de Base de Datos") en tu instancia de MySQL para crear la base de datos casino_db y las tablas jugadores y partidas.
   - Configura las credenciales de conexión en la clase ConexionDB (o donde corresponda) si es necesario.

2. **Abrir en NetBeans:**
   - Abrir NetBeans IDE
   - File → Open Project
   - Seleccionar la carpeta del proyecto
   - Hacer clic en "Open Project"

3. **Compilar y ejecutar:**
   - Compilar el poroyecto
   - Ejecutar la clase principal casino.Main.java, que levantará la Ventana de Configuración Inicial."

### Cómo jugar

1. **Configuración:** Al iniciar, se abre la Ventana de Configuración. Registra entre 2 y 4 jugadores y ajusta las opciones de la partida (dinero inicial, cantidad de partidas).
2. **Inicio:** Haz clic en el botón "Iniciar Juego".
3. **Juego Principal:** La ventana de juego principal gestiona el flujo (apuestas, tiradas, cálculos) automáticamente. Los resultados y eventos se muestran en el Log de Eventos.
4. **Menú:** Usa el menú superior para acceder a opciones como Guardar Partida, ver el Ranking Actual (desde BD), y revisar el Historial de Partidas.
5. **Reporte Final:** Al terminar el juego (por bancarrota o fin de partidas), se muestra la Ventana de Reporte con todos los detalles y estadísticas finales.

## Integrantes y Roles

### Nicolás Paredes
**Rama**: `Nicolas-Paredes`  
**Responsabilidades**:
- Migración completa del proyecto a la arquitectura MVC.
- Diseño visual de VentanaConfiguracion y VentanaJuego.
- Implementación de la lógica de VentanaConfiguracion (registro de jugadores, validaciones).
- Creación de la capa de acceso a datos (DAO) y la clase de conectividad JDBC (ConexionDB).
- Redacción de los scripts de la base de datos MySQL.
- Documentación técnica (guia.md) y actualización del README.md.

### Santiago Altamirano
**Rama**: `Santiago-Altamirano`  
**Responsabilidades**:
- Diseño e implementación completa de la Ventana de Reporte Final (visual y funcional).
- Implementación del ranking final de jugadores en la ventana de reporte.
- Consulta y visualización de las estadísticas generales de la partida (mayor apuesta, mejor puntaje, etc.).
- Integración del historial de las últimas partidas en el reporte.
- Agregación y procesamiento de datos para poblar la ventana de reporte.

### Luciano Miranda
**Rama**: `Luciano-Miranda`  
**Responsabilidades**:
- Implementación de toda la lógica de la Ventana Principal de Juego (Punto 3), basándose en el diseño visual.
- Manejo del bucle principal del juego y la lógica de rondas y partidas.
- Actualización del Log de Eventos en tiempo real.
- Implementación del sistema de persistencia (Punto 5) para guardado/cargado manual de partidas en archivos.

## 📁 Recursos Adicionales

### Documentación del Proceso
**Enlace a Drive**: https://drive.google.com/drive/folders/1P7tDpF66JrV2PwDkHdMiBDP_waO6M9HZ
Contenido disponible:
- Prompts utilizados por cada integrante durante el desarrollo.
- Videos explicativos individuales de cada funcionalidad implementada.
- Capturas de pantalla.

**Gestión de Proyecto**
Se utilizó Trello para la gestión ágil de tareas y organización del equipo. 

## 🗃️ Scripts de Base de Datos (MySQL)
```
   -- 1. Crear la base de datos
CREATE DATABASE IF NOT EXISTS casino_db;
USE casino_db;

-- 2. Crear la tabla 'jugadores'
-- Almacena estadísticas permanentes de los jugadores
CREATE TABLE IF NOT EXISTS jugadores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apodo VARCHAR(50) NOT NULL UNIQUE,
    tipo_jugador VARCHAR(50) NOT NULL,
    dinero_total_ganado INT DEFAULT 0,
    victorias_totales INT DEFAULT 0,
    -- Agrega aquí más campos si deseas rastrear más estadísticas
    CONSTRAINT chk_tipo_jugador CHECK (tipo_jugador IN ('Novato', 'Experto', 'VIP', 'Casino'))
);

-- 3. Crear la tabla 'partidas'
-- Almacena un registro de cada partida completada
CREATE TABLE IF NOT EXISTS partidas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ganador_id INT NULL, -- Puede ser NULL si gana el Casino (que no es un 'jugador' regular)
    rondas_jugadas INT NOT NULL,
    pozo_final INT NOT NULL,
    FOREIGN KEY (ganador_id) REFERENCES jugadores(id)
);
```

## Tecnologías Utilizadas

- **Lenguaje**: Java 8+
- **Build System**: Apache Ant
- **IDE**: NetBeans
- **Arquitectura**: Modelo-Vista-Controlador (MVC)
- **Interfaz Gráfica**: Java Swing (javax.swing)
- **Persistencia**: Manejo de Archivos (CSV/TXT)
- **Base de Datos:**: MySQL
- **Conectividad**: JDBC (Java Database Connectivity)
- **Conectividad**: JDBC (Java Database Connectivity)
- **Gestión de Proyecto**: Git/GitHub, Trello
- **Metodología**: Desarrollo colaborativo con feature branches



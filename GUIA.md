# Guía de Implementación - Ventanas del Juego

## Estado Actual
✅ Arquitectura MVC completa
✅ VentanaConfiguracion funcionando
✅ VentanaJuego - Diseño visual completo
✅ VentanaReporte - Estructura creada
❌ Falta implementar lógica de VentanaJuego y VentanaReporte

---

## 1. VentanaJuego - Implementación

### Pasos de Implementación

1. **Completar ControladorVentanaJuego**
   - Inicializar la vista con los jugadores
   - Crear paneles dinámicos para cada jugador
   - Implementar el loop de partidas y rondas
   - Actualizar labels del panel superior en cada cambio

2. **Agregar Métodos Públicos en VentanaJuego**
   - Métodos para actualizar labels (partida, ronda, pozo)
   - Método para agregar mensajes al log
   - Método para obtener el panel de jugadores
   - Métodos para limpiar/resetear componentes

3. **Conectar Eventos de Menú**
   - menuGuardarPartida → Guardar estado actual
   - menuSalir → Cerrar con confirmación
   - menuRanking → Mostrar ranking actual
   - menuHistorial → Mostrar historial de partidas
   - menuEstadisticas → Mostrar estadísticas generales

4. **Adaptar Lógica del Juego para GUI**
   - Reemplazar System.out.println() por actualizaciones de vista
   - Eliminar Scanner y uso de consola
   - Implementar avance de rondas (botón o automático)
   - Actualizar vista después de cada acción del modelo

5. **Crear Componentes para Jugadores**
   - Paneles individuales con información de cada jugador
   - Actualizar dinero, apuesta, dados, victorias
   - Resaltar visualmente al ganador de cada ronda

### Puntos Críticos

- **Threading**: No usar loops bloqueantes, congelan la interfaz
- **Sincronización**: Actualizar vista cada vez que cambia el modelo
- **Detección de Fin**: Verificar si terminaron las partidas o si quedan jugadores sin dinero
- **Transición**: Al finalizar, abrir VentanaReporte y cerrar VentanaJuego

---

## 2. VentanaReporte - Implementación

### Pasos de Implementación

1. **Diseñar la Interfaz Visual**
   - Panel con tabla de ranking (ordenado por dinero)
   - Panel con estadísticas generales
   - Panel con historial de últimas partidas
   - Botón "Volver al Inicio" o "Nueva Partida"

2. **Completar ControladorVentanaReporte**
   - Recibir datos: jugadores, partidas, estadísticas, historial
   - Ordenar jugadores por ranking
   - Formatear datos para mostrar en la vista

3. **Agregar Métodos en VentanaReporte**
   - Método para cargar datos en la tabla de ranking
   - Método para mostrar estadísticas
   - Método para mostrar historial

4. **Conectar Botones**
   - Botón para cerrar o volver a VentanaConfiguracion

### Datos a Mostrar

- **Ranking**: Nombre, Tipo, Dinero Final, Victorias (ordenado descendente por dinero)
- **Estadísticas**: Mayor apuesta, Mejor puntaje, Víctimas del casino
- **Historial**: Últimas 3-5 partidas jugadas

---

## 3. Persistencia (GestorPersistencia)

### Funcionalidad

- Guardar estado completo del juego en archivos
- Cargar partidas guardadas
- Guardar historial permanente
- Manejar IOException correctamente

### Métodos Sugeridos

- guardarPartida(PartidaModelo, String archivo)
- cargarPartida(String archivo) → PartidaModelo
- guardarHistorial(List historial)
- cargarHistorial() → List

### Integración

- Conectar con botón "Cargar Partida" en VentanaConfiguracion
- Conectar con "Guardar Partida" en menú de VentanaJuego

---

## 4. Base de Datos (BONUS)

### Estructura

- Tabla jugadores: id, nombre, apodo, tipo, dinero, victorias
- Tabla partidas: id, fecha, ganador_id, rondas, pozo

### Clases a Crear

- ConexionDB.java - Manejo de conexión JDBC
- JugadorDAO.java - Operaciones CRUD de jugadores
- PartidaDAO.java - Operaciones CRUD de partidas

### Integración

- Guardar jugadores al finalizar cada partida
- Consultar ranking desde BD en lugar de memoria
- Mostrar histórico completo de partidas

---

## Referencias

**Ejemplos implementados:**
- ControladorConfiguracion.java
- VentanaConfiguracion.java
- GeneradorReportes.java

**Modelo ya implementado:**
- PartidaModelo
- CasinoAdministrador
- Estadisticas
- RegistroTrampas
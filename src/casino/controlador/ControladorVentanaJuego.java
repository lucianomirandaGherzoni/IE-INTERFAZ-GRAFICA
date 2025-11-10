/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.controlador;

import casino.modelo.*;
import casino.vista.VentanaJuego;
import casino.vista.componentes.PanelJugador;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.util.Collections;
import java.util.Comparator;
import java.io.File;
import casino.vista.VentanaReporte;
import casino.controlador.ControladorVentanaReporte;

/**
 *
 * @author BANGHO
 */
public class ControladorVentanaJuego {

    private final VentanaJuego vista;
    private final PartidaModelo modelo;
    private final List<PanelJugador> panelesJugadores;
    private final GestorPersistencia gestorPersistencia;

    /**
     * Constructor para una NUEVA partida
     */
    public ControladorVentanaJuego(VentanaJuego vista, List<Jugador> jugadores, int cantidadPartidas) {
        this.vista = vista;
        this.modelo = new PartidaModelo(); // Crea un modelo nuevo
        this.modelo.setCantidadPartidas(cantidadPartidas); // Guarda las partidas en el modelo
        this.panelesJugadores = new ArrayList<>();
        this.gestorPersistencia = new GestorPersistencia();
        
        for (Jugador j : jugadores) {
            this.modelo.agregarJugador(j);
        }
    }
    
    /**
     * Constructor para una partida CARGADA
     */
    public ControladorVentanaJuego(VentanaJuego vista, PartidaModelo modeloCargado) {
        this.vista = vista;
        this.modelo = modeloCargado; // Usa el modelo cargado desde el archivo
        this.panelesJugadores = new ArrayList<>();
        this.gestorPersistencia = new GestorPersistencia();
    }

    /**
     * Inicia una NUEVA partida
     */
    public void iniciar() {
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        // Lee los datos iniciales del modelo
        vista.actualizarPanelSuperior(modelo.getPartidaActual(), modelo.getCantidadPartidas(), modelo.getRondaActual(), 0);

        crearPanelesJugadores();

        vista.agregarLog("¡Comienza el juego! Partida 1 de " + modelo.getCantidadPartidas());

        // Listeners de botones
        vista.getBtnSiguienteRonda().addActionListener(e -> ClicBotonPrincipal());
        vista.getBtnSiguienteRonda().setText("Iniciar Ronda 1");

        vista.getBtnReRoll().addActionListener(e -> ClicBotonReRoll());
        vista.getBtnReRoll().setEnabled(false);

        // Listeners de menú
        vista.getMenuSalir().addActionListener(e -> salirDelJuego());
        vista.getMenuRanking().addActionListener(e -> mostrarRanking());
        vista.getMenuHistorial().addActionListener(e -> mostrarHistorial());
        vista.getMenuEstadisticas().addActionListener(e -> mostrarEstadisticas());
        vista.getMenuGuardarPartida().addActionListener(e -> clicGuardarPartida());
    }
    
    /**
     * Inicia una partida CARGADA
     */
    public void iniciarPartidaCargada() {
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);

        // 1. Actualiza la UI con los datos guardados
        vista.actualizarPanelSuperior(
                modelo.getPartidaActual(),
                modelo.getCantidadPartidas(),
                modelo.getRondaActual(),
                modelo.getPozoRonda()
        );
        
        crearPanelesJugadores(); // Crea los paneles
        actualizarPanelesJugadores(); // Actualiza los datos (dinero, dados)

        vista.agregarLog("--- ¡Partida cargada exitosamente! ---");
        vista.agregarLog("Continúa la Partida " + modelo.getPartidaActual() + ", Ronda " + modelo.getRondaActual());

        // 2. Configura los listeners (igual que en iniciar())
        vista.getBtnSiguienteRonda().addActionListener(e -> ClicBotonPrincipal());
        vista.getBtnReRoll().addActionListener(e -> ClicBotonReRoll());
        
        vista.getMenuSalir().addActionListener(e -> salirDelJuego());
        vista.getMenuRanking().addActionListener(e -> mostrarRanking());
        vista.getMenuHistorial().addActionListener(e -> mostrarHistorial());
        vista.getMenuEstadisticas().addActionListener(e -> mostrarEstadisticas());
        vista.getMenuGuardarPartida().addActionListener(e -> clicGuardarPartida());

        // 3. Configura el estado de los botones según el modelo cargado
        vista.getBtnSiguienteRonda().setText("Siguiente Turno");
        vista.getBtnSiguienteRonda().setEnabled(true);
        
        // Revisa si hay un re-roll pendiente guardado
        if (modelo.getJugadorVIPActual() != null) {
            vista.getBtnReRoll().setEnabled(true);
            vista.getBtnSiguienteRonda().setText("Saltar Re-Roll");
        } else {
            vista.getBtnReRoll().setEnabled(false);
        }
    }

    
    private void clicGuardarPartida() {
        // Esto abre una ventana para que el usuario elija dónde guardar
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        chooser.setDialogTitle("Guardar partida");
        chooser.setSelectedFile(new File("mi_partida.sav")); // Sugiere un nombre

        if (chooser.showSaveDialog(vista) == javax.swing.JFileChooser.APPROVE_OPTION) {
            String archivo = chooser.getSelectedFile().getAbsolutePath();

            gestorPersistencia.guardarPartida(this.modelo, archivo);

            JOptionPane.showMessageDialog(vista, "Partida guardada exitosamente en " + archivo);
        }
    }
    
    private void crearPanelesJugadores() {
        vista.getPanelJugadores().removeAll();

        for (Jugador j : modelo.getJugadores()) {

            PanelJugador panel = new PanelJugador(j);

            this.panelesJugadores.add(panel);
            vista.getPanelJugadores().add(panel);
        }

        vista.getPanelJugadores().revalidate();
        vista.getPanelJugadores().repaint();
    }

    /**
     * El botón principal (Siguiente Ronda) controla el flujo del juego.
     */
    private void ClicBotonPrincipal() {
        if (modelo.getPartidaActual() > modelo.getCantidadPartidas()) {
            finalizarJuegoCompleto();
            return;
        }
        
        if (modelo.getJugadorVIPActual() != null) {
            vista.agregarLog(modelo.getJugadorVIPActual().getNombre() + " decide NO usar su re-roll.");

            procesarResultado(modelo.getJugadorVIPActual(), modelo.getResultadoVIPActual());

            modelo.limpiarEstadoReRoll(); // Limpia el estado en el modelo
            vista.getBtnReRoll().setEnabled(false);

            modelo.incrementarJugadorTurnoIndex();
            vista.getBtnSiguienteRonda().setText("Siguiente Turno");
            
            if (modelo.getJugadorTurnoIndex() > modelo.getCantidadJugadoresActivosRonda()) {
                finalizarRonda();
            } else {
                vista.agregarLog("Turno finalizado. Continúa.");
                vista.getBtnSiguienteRonda().setEnabled(true);
            }

        } else {
            procesarTurnoJugador();
        }
    }

    private void ClicBotonReRoll() {
        if (modelo.getJugadorVIPActual() == null) {
            return;
        }

        vista.getBtnReRoll().setEnabled(false);
        vista.getBtnSiguienteRonda().setEnabled(false);
        
        modelo.getJugadorVIPActual().usarReroll();
        vista.agregarLog("¡" + modelo.getJugadorVIPActual().getNombre() + " usa su re-roll!");

        JugadorCasino casino = modelo.getJugadores().stream()
                .filter(j -> j instanceof JugadorCasino).map(j -> (JugadorCasino) j).findFirst().orElse(null);

        ResultadoDados nuevoResultado = modelo.getCasino().getJuego().lanzarDados(
                modelo.getJugadorVIPActual(), casino,
                modelo.getCasino().getRegistroTrampas(),
                modelo.getEstadisticas()
        );

        for (String msg : nuevoResultado.getMensajes()) {
            vista.agregarLog(msg);
        }
        
        procesarResultado(modelo.getJugadorVIPActual(), nuevoResultado);

        modelo.limpiarEstadoReRoll();
        vista.getBtnSiguienteRonda().setText("Siguiente Turno");
        vista.getBtnSiguienteRonda().setEnabled(true);

        modelo.incrementarJugadorTurnoIndex();
        if (modelo.getJugadorTurnoIndex() > modelo.getCantidadJugadoresActivosRonda()) {
            finalizarRonda();
        } else {
            vista.agregarLog("Turno finalizado. Continúa.");
        }
    }

    private void procesarTurnoJugador() {

        vista.getBtnSiguienteRonda().setEnabled(false);
        vista.getBtnReRoll().setEnabled(false);

        if (modelo.getJugadorTurnoIndex() == 0) {
            if (!prepararNuevaRonda()) {
                return;
            }
            modelo.setJugadorTurnoIndex(1); // Actualiza el modelo

            vista.getBtnSiguienteRonda().setText("Siguiente Turno");
            vista.getBtnSiguienteRonda().setEnabled(true);
            return;
        }

        if (modelo.getJugadorTurnoIndex() > 0 && modelo.getJugadorTurnoIndex() <= modelo.getCantidadJugadoresActivosRonda()) {
            
            Jugador j = modelo.getJugadorActivoRonda(modelo.getJugadorTurnoIndex() - 1);
            vista.agregarLog("Turno de: " + j.getNombre());

            JugadorCasino casino = modelo.getJugadores().stream()
                    .filter(p -> p instanceof JugadorCasino).map(p -> (JugadorCasino) p).findFirst().orElse(null);

            ResultadoDados resultado = modelo.getCasino().getJuego().lanzarDados(
                    j, casino,
                    modelo.getCasino().getRegistroTrampas(),
                    modelo.getEstadisticas()
            );

            for (String msg : resultado.getMensajes()) {
                vista.agregarLog(msg);
            }

            if (j instanceof JugadorVIP vip && vip.puedeReroll()) {
                vista.agregarLog("¡" + j.getNombre() + " (VIP) puede usar su re-roll! (Resultado: " + resultado.getSuma() + ")");
                
                modelo.setJugadorVIPActual(vip); // Guarda el estado en el modelo
                modelo.setResultadoVIPActual(resultado);
                vista.getBtnReRoll().setEnabled(true);
                vista.getBtnSiguienteRonda().setText("Saltar Re-Roll");
                vista.getBtnSiguienteRonda().setEnabled(true);
                return;
            }

            procesarResultado(j, resultado);
            modelo.incrementarJugadorTurnoIndex();
            vista.getBtnSiguienteRonda().setText("Siguiente Turno");
            vista.getBtnSiguienteRonda().setEnabled(true);

            if (modelo.getJugadorTurnoIndex() > modelo.getCantidadJugadoresActivosRonda()) {
                finalizarRonda();
            }
            return;
        }
    }

    private boolean prepararNuevaRonda() {
        if (modelo.getPartidaActual() > modelo.getCantidadPartidas()) {
            finalizarJuegoCompleto();
            return false;
        }
        
        vista.agregarLog("\n--- PARTIDA " + modelo.getPartidaActual() + " / RONDA " + modelo.getRondaActual() + " ---");

        for (Jugador j : modelo.getJugadores()) {
            if (j instanceof JugadorVIP vip) {
                vip.resetReroll();
            }
        }

        //  Obtener jugadores activos
        modelo.limpiarJugadoresActivosRonda();
        for (Jugador j : modelo.getJugadores()) {
            if (j.getDinero() > 0) {
                modelo.agregarJugadorActivoRonda(j);
            } else {
                vista.agregarLog(j.getNombre() + " no tiene dinero para jugar.");
            }
        }
        
        if (modelo.getCantidadJugadoresActivosRonda() < 2) {
            vista.agregarLog("Fin del juego - no hay suficientes jugadores.");
            finalizarJuegoCompleto();
            return false;
        }

        //  Apuestas
        modelo.setPozoRonda(0);
        for (Jugador j : modelo.getJugadoresActivosRonda()) {
            int apuesta = j.calcularApuesta();
            j.setApuestaActual(apuesta);
            j.perder(apuesta);
            modelo.agregarAlPozo(apuesta);
            modelo.getEstadisticas().registrarApuesta(j.getApodo(), apuesta);
            vista.agregarLog(j.getNombre() + " apuesta $" + apuesta);
        }
        vista.agregarLog("Pozo de la ronda: $" + modelo.getPozoRonda());
        vista.actualizarPanelSuperior(modelo.getPartidaActual(), modelo.getCantidadPartidas(), modelo.getRondaActual(), modelo.getPozoRonda());

        // Resetear marcadores de ronda
        modelo.setMejorPuntajeRonda(0);
        modelo.limpiarGanadoresRonda();

        return true;
    }

    private void procesarResultado(Jugador j, ResultadoDados resultado) {
        int puntaje = resultado.getSuma();
        j.setResultadoDados(new int[]{resultado.getDado1(), resultado.getDado2()});

        vista.agregarLog(j.getNombre() + " tiró: [" + resultado.getDado1() + "] + [" + resultado.getDado2() + "] = " + puntaje);
        modelo.getEstadisticas().registrarPuntaje(j.getApodo(), puntaje);

        if (puntaje > modelo.getMejorPuntajeRonda()) {
            modelo.setMejorPuntajeRonda(puntaje);
            modelo.limpiarGanadoresRonda();
            modelo.agregarGanadorRonda(j);
        } else if (puntaje == modelo.getMejorPuntajeRonda()) {
            modelo.agregarGanadorRonda(j);
        }

        actualizarPanelesJugadores();
    }

    private void finalizarRonda() {
        if (modelo.getGanadoresRonda().isEmpty()) {
            vista.agregarLog("No hubo ganadores esta ronda.");
        } else {
            int premio = modelo.getPozoRonda() / modelo.getGanadoresRonda().size();
            vista.agregarLog("Ganador(es) de la ronda (Puntaje " + modelo.getMejorPuntajeRonda() + "):");
            for (Jugador g : modelo.getGanadoresRonda()) {
                g.ganar(premio);
                vista.agregarLog("¡" + g.getNombre() + " gana $" + premio + "!");
            }
        }

        actualizarPanelesJugadores();
        modelo.incrementarRondaActual();
        modelo.setJugadorTurnoIndex(0);
        
        if (modelo.getRondaActual() > PartidaModelo.RONDAS_POR_PARTIDA) {
            
            vista.agregarLog("--- FIN DE LA PARTIDA " + modelo.getPartidaActual() + " ---");
            guardarResultadoPartida();
            modelo.setRondaActual(1);
            modelo.incrementarPartidaActual();

            if (modelo.getPartidaActual() > modelo.getCantidadPartidas()) {
                vista.agregarLog("¡Juego terminado! Presiona 'Siguiente' para ver el reporte.");
                vista.getBtnSiguienteRonda().setText("Ver Reporte Final");
            } else {
                vista.getBtnSiguienteRonda().setText("Iniciar Partida " + modelo.getPartidaActual());
            }
        } else {
            vista.getBtnSiguienteRonda().setText("Iniciar Ronda " + modelo.getRondaActual());
        }

        //guardado automatico
        try {
            // Llama al gestor que ya existe y guarda el modelo actual
            // con un nombre de archivo predefinido.
            gestorPersistencia.guardarPartida(this.modelo, "autosave.sav");
            vista.agregarLog("...Partida guardada automáticamente...");
        } catch (Exception e) {
            vista.agregarLog("Error en el guardado automático: " + e.getMessage());
        }
        
        vista.getBtnSiguienteRonda().setEnabled(true);
    }

    private void actualizarPanelesJugadores() {
        for (PanelJugador panel : panelesJugadores) {
            panel.actualizarDatos();
        }
    }

    private void finalizarJuegoCompleto() {
        vista.agregarLog("Mostrando reporte final...");
        vista.getBtnSiguienteRonda().setEnabled(false);
        vista.getBtnReRoll().setEnabled(false);

        gestorPersistencia.guardarHistorial(this.modelo.getHistorial());
        
        VentanaReporte vistaReporte = new VentanaReporte();
        List<Jugador> jugadores = this.modelo.getJugadores();
        Estadisticas estadisticas = this.modelo.getEstadisticas();
        ArrayList<String> historial = this.modelo.getHistorial();

        ControladorVentanaReporte controladorReporte = new ControladorVentanaReporte(
                vistaReporte,
                jugadores,
                this.modelo.getCantidadPartidas(), 
                estadisticas,
                historial
        );

        vistaReporte.setVisible(true);
        this.vista.dispose();
    }

    private void salirDelJuego() {
        int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro que desea salir?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void mostrarRanking() {
        StringBuilder sb = new StringBuilder("--- Ranking Actual ---\n");
        List<Jugador> ranking = new ArrayList<>(modelo.getJugadores());
        Collections.sort(ranking, new Comparator<Jugador>() {
            @Override
            public int compare(Jugador j1, Jugador j2) {
                return Integer.compare(j2.getDinero(), j1.getDinero());
            }
        });
        for (int i = 0; i < ranking.size(); i++) {
            Jugador j = ranking.get(i);
            sb.append(String.format("%d. %s (%s) - $%d\n",
                    i + 1,
                    j.getNombre(),
                    j.obtenerTipoJugador(),
                    j.getDinero()
            ));
        }
        JOptionPane.showMessageDialog(vista, sb.toString(), "Ranking", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarHistorial() {
        StringBuilder sb = new StringBuilder("--- Historial de Partidas ---\n");
        ArrayList<String> historial = modelo.getHistorial();
        if (historial.isEmpty()) {
            sb.append("Aún no se ha completado ninguna partida.");
        } else {
            for (String linea : historial) {
                sb.append(linea).append("\n");
            }
        }
        JOptionPane.showMessageDialog(vista, sb.toString(), "Historial", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarEstadisticas() {
        Estadisticas est = modelo.getEstadisticas();
        StringBuilder sb = new StringBuilder("--- Estadísticas Generales ---\n");
        sb.append("Mayor Apuesta: $").append(est.getMayorApuesta()).append(" (").append(est.getJugadorMayorApuesta()).append(")\n");
        sb.append("Mejor Puntaje: ").append(est.getMejorPuntaje()).append(" (").append(est.getJugadorMejorPuntaje()).append(")\n");
        sb.append("Víctimas de Trampas: ").append(est.getVictimasDelCasino()).append("\n");
        JOptionPane.showMessageDialog(vista, sb.toString(), "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void guardarResultadoPartida() {
        modelo.incrementarContadorPartidas();
        String jugadoresCSV = modelo.obtenerNombresJugadoresCSV();
        String ganador = modelo.obtenerGanador();
        int rondasJugadas = 3;
        String sb = "PARTIDA #" + modelo.getContadorPartidas() + " - Jugadores: " + jugadoresCSV + " | Ganador: " + ganador + " | Rondas: " + rondasJugadas;
        modelo.guardarEnHistorial(sb);
    }
}
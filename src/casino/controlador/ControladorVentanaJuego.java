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
import java.util.stream.Collectors;
import casino.vista.VentanaReporte;
import casino.controlador.ControladorVentanaReporte;

/**
 *
 * @author BANGHO
 */
public class ControladorVentanaJuego {

    private final VentanaJuego vista;
    private final PartidaModelo modelo;
    private final int cantidadPartidas;
    private final List<PanelJugador> panelesJugadores;

    // Variables de Estado del Juego
    private int partidaActual = 1;
    private int rondaActual = 1;
    private final int RONDAS_POR_PARTIDA = 3;

    // Variables de Estado de la Ronda
    private int jugadorTurnoIndex = 0;
    private ArrayList<Jugador> jugadoresActivosRonda;
    private int pozoRonda = 0;
    private int mejorPuntajeRonda = 0;
    private ArrayList<Jugador> ganadoresRonda;

    // Variables de Estado del Re-Roll
    private JugadorVIP jugadorVIPActual = null;
    private ResultadoDados resultadoVIPActual = null;

    public ControladorVentanaJuego(VentanaJuego vista, List<Jugador> jugadores, int cantidadPartidas) {
        this.vista = vista;
        this.cantidadPartidas = cantidadPartidas;
        this.modelo = new PartidaModelo();
        this.panelesJugadores = new ArrayList<>();

        for (Jugador j : jugadores) {
            this.modelo.agregarJugador(j);
        }

    }

    public void iniciar() {
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        vista.actualizarPanelSuperior(1, cantidadPartidas, 1, 0);

        crearPanelesJugadores();

        vista.agregarLog("¡Comienza el juego! Partida 1 de " + cantidadPartidas);

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

        if (partidaActual > cantidadPartidas) {
            finalizarJuegoCompleto();
            return;
        }

        if (jugadorVIPActual != null) {
            vista.agregarLog(jugadorVIPActual.getNombre() + " decide NO usar su re-roll.");

            procesarResultado(jugadorVIPActual, resultadoVIPActual);

            jugadorVIPActual = null;
            resultadoVIPActual = null;
            vista.getBtnReRoll().setEnabled(false);

            jugadorTurnoIndex++;
            vista.getBtnSiguienteRonda().setText("Siguiente Turno");

            if (jugadorTurnoIndex > jugadoresActivosRonda.size()) {
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
        if (jugadorVIPActual == null) {
            return;
        }

        vista.getBtnReRoll().setEnabled(false);
        vista.getBtnSiguienteRonda().setEnabled(false);

        jugadorVIPActual.usarReroll();
        vista.agregarLog("¡" + jugadorVIPActual.getNombre() + " usa su re-roll!");

        JugadorCasino casino = modelo.getJugadores().stream()
                .filter(j -> j instanceof JugadorCasino).map(j -> (JugadorCasino) j).findFirst().orElse(null);

        ResultadoDados nuevoResultado = modelo.getCasino().getJuego().lanzarDados(
                jugadorVIPActual, casino,
                modelo.getCasino().getRegistroTrampas(),
                modelo.getEstadisticas()
        );

        for (String msg : nuevoResultado.getMensajes()) {
            vista.agregarLog(msg);
        }

        procesarResultado(jugadorVIPActual, nuevoResultado);

        jugadorVIPActual = null;
        resultadoVIPActual = null;
        vista.getBtnSiguienteRonda().setText("Siguiente Turno");
        vista.getBtnSiguienteRonda().setEnabled(true);

        jugadorTurnoIndex++;
        if (jugadorTurnoIndex > jugadoresActivosRonda.size()) {
            finalizarRonda();
        } else {
            vista.agregarLog("Turno finalizado. Continúa.");
        }
    }

    private void procesarTurnoJugador() {

        vista.getBtnSiguienteRonda().setEnabled(false);
        vista.getBtnReRoll().setEnabled(false);

        if (jugadorTurnoIndex == 0) {

            if (!prepararNuevaRonda()) {
                return;
            }

            jugadorTurnoIndex = 1;

            vista.getBtnSiguienteRonda().setText("Siguiente Turno");
            vista.getBtnSiguienteRonda().setEnabled(true);
            return;
        }

        if (jugadorTurnoIndex > 0 && jugadorTurnoIndex <= jugadoresActivosRonda.size()) {

            Jugador j = jugadoresActivosRonda.get(jugadorTurnoIndex - 1);
            vista.agregarLog("Turno de: " + j.getNombre());

            JugadorCasino casino = modelo.getJugadores().stream()
                    .filter(p -> p instanceof JugadorCasino).map(p -> (JugadorCasino) p).findFirst().orElse(null);

            ResultadoDados resultado = modelo.getCasino().getJuego().lanzarDados(
                    j, casino,
                    modelo.getCasino().getRegistroTrampas(),
                    modelo.getEstadisticas()
            );

            // Loguear trampas
            for (String msg : resultado.getMensajes()) {
                vista.agregarLog(msg);
            }

            // LÓGICA DE RE-ROLL 
            if (j instanceof JugadorVIP vip && vip.puedeReroll()) {
                vista.agregarLog("¡" + j.getNombre() + " (VIP) puede usar su re-roll! (Resultado: " + resultado.getSuma() + ")");

                this.jugadorVIPActual = vip;
                this.resultadoVIPActual = resultado;
                vista.getBtnReRoll().setEnabled(true);
                vista.getBtnSiguienteRonda().setText("Saltar Re-Roll");
                vista.getBtnSiguienteRonda().setEnabled(true);

                return;
            }

            procesarResultado(j, resultado);
            jugadorTurnoIndex++;
            vista.getBtnSiguienteRonda().setText("Siguiente Turno");
            vista.getBtnSiguienteRonda().setEnabled(true);

            if (jugadorTurnoIndex > jugadoresActivosRonda.size()) {
                finalizarRonda();
            }
            return;
        }
    }

    private boolean prepararNuevaRonda() {
        // Comprobar fin de juego (si se llama al inicio de una partida inexistente)
        if (partidaActual > cantidadPartidas) {
            finalizarJuegoCompleto();
            return false;
        }

        vista.agregarLog("\n--- PARTIDA " + partidaActual + " / RONDA " + rondaActual + " ---");

        for (Jugador j : modelo.getJugadores()) {
            if (j instanceof JugadorVIP vip) {
                vip.resetReroll(); // Llama al método que creaste en JugadorVIP
            }
        }

        //  Obtener jugadores activos
        jugadoresActivosRonda = new ArrayList<>();
        for (Jugador j : modelo.getJugadores()) {
            if (j.getDinero() > 0) {
                jugadoresActivosRonda.add(j);
            } else {
                vista.agregarLog(j.getNombre() + " no tiene dinero para jugar.");
            }
        }

        if (jugadoresActivosRonda.size() < 2) {
            vista.agregarLog("Fin del juego - no hay suficientes jugadores.");
            finalizarJuegoCompleto();
            return false;
        }

        //  Apuestas
        pozoRonda = 0;
        for (Jugador j : jugadoresActivosRonda) {
            int apuesta = j.calcularApuesta();
            j.setApuestaActual(apuesta);
            j.perder(apuesta);
            pozoRonda += apuesta;
            modelo.getEstadisticas().registrarApuesta(j.getApodo(), apuesta);
            vista.agregarLog(j.getNombre() + " apuesta $" + apuesta);
        }
        vista.agregarLog("Pozo de la ronda: $" + pozoRonda);
        vista.actualizarPanelSuperior(partidaActual, cantidadPartidas, rondaActual, pozoRonda);

        // Resetear marcadores de ronda
        mejorPuntajeRonda = 0;
        ganadoresRonda = new ArrayList<>();

        return true;
    }

    /**
     * Guarda el puntaje de un jugador y actualiza el ranking de la ronda
     */
    private void procesarResultado(Jugador j, ResultadoDados resultado) {
        int puntaje = resultado.getSuma();

        j.setResultadoDados(new int[]{resultado.getDado1(), resultado.getDado2()});

        vista.agregarLog(j.getNombre() + " tiró: [" + resultado.getDado1() + "] + [" + resultado.getDado2() + "] = " + puntaje);
        modelo.getEstadisticas().registrarPuntaje(j.getApodo(), puntaje);

        if (puntaje > mejorPuntajeRonda) {
            mejorPuntajeRonda = puntaje;
            ganadoresRonda.clear();
            ganadoresRonda.add(j);
        } else if (puntaje == mejorPuntajeRonda) {
            ganadoresRonda.add(j);
        }

        actualizarPanelesJugadores();
    }

    private void finalizarRonda() {

        if (ganadoresRonda.isEmpty()) {
            vista.agregarLog("No hubo ganadores esta ronda.");
        } else {
            int premio = pozoRonda / ganadoresRonda.size();
            vista.agregarLog("Ganador(es) de la ronda (Puntaje " + mejorPuntajeRonda + "):");
            for (Jugador g : ganadoresRonda) {
                g.ganar(premio);
                vista.agregarLog("¡" + g.getNombre() + " gana $" + premio + "!");
            }
        }

        actualizarPanelesJugadores();

        rondaActual++;
        jugadorTurnoIndex = 0;

        if (rondaActual > RONDAS_POR_PARTIDA) {

            vista.agregarLog("--- FIN DE LA PARTIDA " + partidaActual + " ---");
            guardarResultadoPartida();
            rondaActual = 1;
            partidaActual++;

            if (partidaActual > cantidadPartidas) {
                vista.agregarLog("¡Juego terminado! Presiona 'Siguiente' para ver el reporte.");
                vista.getBtnSiguienteRonda().setText("Ver Reporte Final");
            } else {
                vista.getBtnSiguienteRonda().setText("Iniciar Partida " + partidaActual);
            }
        } else {
            vista.getBtnSiguienteRonda().setText("Iniciar Ronda " + rondaActual);
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

// --- INICIO DE LA LÓGICA CORREGIDA ---
        // 1. Crear la nueva vista de reporte
        VentanaReporte vistaReporte = new VentanaReporte();

        // 2. Obtener las partes que SÍ necesita el constructor desde el modelo
        List<Jugador> jugadores = this.modelo.getJugadores();
        Estadisticas estadisticas = this.modelo.getEstadisticas();
        ArrayList<String> historial = this.modelo.getHistorial();

        // 3. Crear el controlador pasándole los argumentos correctos
        ControladorVentanaReporte controladorReporte = new ControladorVentanaReporte(
                vistaReporte,
                jugadores,
               this.cantidadPartidas, // <--- AÑADIDO AQUÍ
                estadisticas,
                historial
        );

        vistaReporte.setVisible(true);
        this.vista.dispose();

        // --- FIN DE LA LÓGICA CORREGIDA ---
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

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

/**
 *
 * @author BANGHO
 */
public class ControladorVentanaJuego {

    private final VentanaJuego vista;
    private final PartidaModelo modelo;
    private final int cantidadPartidas;
    private final List<PanelJugador> panelesJugadores;

    private int partidaActual = 1;
    private int rondaActual = 1;
    private final int RONDAS_POR_PARTIDA = 3;

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
        vista.getBtnSiguienteRonda().addActionListener(e -> jugarSiguienteRonda());
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

    private void jugarSiguienteRonda() {
        if (partidaActual > cantidadPartidas) {
            finalizarJuegoCompleto();
            return;
        }

        vista.agregarLog("\n--- PARTIDA " + partidaActual + " / RONDA " + rondaActual + " ---");

        ArrayList<Jugador> jugadoresActivos = new ArrayList<>();
        for (Jugador j : modelo.getJugadores()) {
            if (j.getDinero() > 0) {
                jugadoresActivos.add(j);
            } else {
                vista.agregarLog(j.getNombre() + " no tiene dinero para jugar.");
            }
        }

        if (jugadoresActivos.size() < 2) {
            vista.agregarLog("Fin del juego - no hay suficientes jugadores con dinero.");
            finalizarJuegoCompleto();
            return;
        }

        int pozo = 0;
        for (Jugador j : jugadoresActivos) {
            int apuesta = j.calcularApuesta();
            j.setApuestaActual(apuesta);
            j.perder(apuesta);
            pozo += apuesta;

            modelo.getEstadisticas().registrarApuesta(j.getApodo(), apuesta);

            vista.agregarLog(j.getNombre() + " apuesta $" + apuesta);
        }
        vista.agregarLog("Pozo de la ronda: $" + pozo);

        vista.actualizarPanelSuperior(partidaActual, cantidadPartidas, rondaActual, pozo);

        int mejorPuntaje = 0;
        ArrayList<Jugador> ganadores = new ArrayList<>();

        JugadorCasino casino = null;
        for (Jugador j : modelo.getJugadores()) {
            if (j instanceof JugadorCasino) {
                casino = (JugadorCasino) j;
                break;
            }
        }
        for (Jugador j : jugadoresActivos) {

            int puntaje = modelo.getCasino().getJuego().lanzarDados(
                    j,
                    casino,
                    modelo.getCasino().getRegistroTrampas(),
                    modelo.getEstadisticas()
            );

            modelo.getEstadisticas().registrarPuntaje(j.getApodo(), puntaje);

            if (puntaje > mejorPuntaje) {
                mejorPuntaje = puntaje;
                ganadores.clear();
                ganadores.add(j);
            } else if (puntaje == mejorPuntaje) {
                ganadores.add(j);
            }
        }

        int premio = pozo / ganadores.size();
        vista.agregarLog("Ganador(es) de la ronda (Puntaje " + mejorPuntaje + "):");
        for (Jugador g : ganadores) {
            g.ganar(premio);
            vista.agregarLog("¡" + g.getNombre() + " gana $" + premio + "!");
        }

        actualizarPanelesJugadores();
        rondaActual++;
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

            vista.getBtnSiguienteRonda().setText("Siguiente Ronda (" + rondaActual + ")");
        }
    }

    private void actualizarPanelesJugadores() {
        for (PanelJugador panel : panelesJugadores) {
            panel.actualizarDatos();
        }
    }

    private void finalizarJuegoCompleto() {
        vista.agregarLog("Mostrando reporte final...");
        vista.getBtnSiguienteRonda().setEnabled(false);

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
                // Ordenar de mayor a menor
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

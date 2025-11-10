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

/**
 *
 * @author BANGHO
 */
public class ControladorVentanaJuego {

    private VentanaJuego vista;
    private PartidaModelo modelo;
    private int cantidadPartidas;
    private List<PanelJugador> panelesJugadores;

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

        vista.actualizarPanelSuperior(1, cantidadPartidas, 1, 0.0);

        crearPanelesJugadores();

        vista.agregarLog("¡Comienza el juego! Partida 1 de " + cantidadPartidas);
        vista.getBtnSiguienteRonda().addActionListener(e -> jugarSiguienteRonda());
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

        // 1. Verificar jugadores con dinero
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
}

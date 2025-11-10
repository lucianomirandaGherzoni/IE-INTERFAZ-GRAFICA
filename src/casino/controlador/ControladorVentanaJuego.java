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
    //private List<Jugador> jugadores;
    private List<PanelJugador> panelesJugadores;
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
}

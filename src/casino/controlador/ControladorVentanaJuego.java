/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.controlador;

import casino.modelo.*;
import casino.vista.VentanaJuego;
import casino.vista.PanelJugador;
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
    private List<PanelJugador> panelesJugadores; //Lista para guardar los paneles de jugador y poder actualizarlos

    public ControladorVentanaJuego(VentanaJuego vista, List<Jugador> jugadores, int cantidadPartidas) {
        this.vista = vista;
        this.jugadores = jugadores;
        this.cantidadPartidas = cantidadPartidas;
        this.modelo = new PartidaModelo();
    }
    
    //Implementar lógica del juego
    
    
}

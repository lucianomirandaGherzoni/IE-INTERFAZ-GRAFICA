/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.controlador;


import casino.modelo.*;
import casino.vista.VentanaReporte;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author BANGHO
 */
public class ControladorVentanaReporte {
    private VentanaReporte vista;
    private List<Jugador> jugadores;
    private int totalPartidas;
    private Estadisticas estadisticas;
    private ArrayList<String> historial;

    public ControladorVentanaReporte(VentanaReporte vista, List<Jugador> jugadores, 
                                     int totalPartidas, Estadisticas estadisticas, 
                                     ArrayList<String> historial) {
        this.vista = vista;
        this.jugadores = jugadores;
        this.totalPartidas = totalPartidas;
        this.estadisticas = estadisticas;
        this.historial = historial;        
    }
    
    // Resto del código para mostrar reporte
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.controlador;

import casino.modelo.Estadisticas;
import casino.modelo.Jugador;
import casino.vista.VentanaConfiguracion;
import casino.vista.VentanaReporte;

import java.util.ArrayList;
import java.util.List;

public class ControladorVentanaReporte {
    private final VentanaReporte vista;
    private final List<Jugador> jugadores;
    private final int totalPartidas;
    private final Estadisticas estadisticas;
    private final ArrayList<String> historial;

    public ControladorVentanaReporte(VentanaReporte vista,
                                     List<Jugador> jugadores,
                                     int totalPartidas,
                                     Estadisticas estadisticas,
                                     ArrayList<String> historial) {
        this.vista = vista;
        this.jugadores = jugadores;
        this.totalPartidas = totalPartidas;
        this.estadisticas = estadisticas;
        this.historial = historial;

        // Cargar datos en la vista
        this.vista.cargarRanking(this.jugadores);
        this.vista.mostrarEstadisticas(this.estadisticas);
        this.vista.cargarHistorial(this.historial);

        // Conectar botón "Volver al inicio"
        this.vista.onVolver(this::volverAlInicio);

        // Centrar y dejar lista
        this.vista.setLocationRelativeTo(null);
    }

    private void volverAlInicio() {
        // Cierra el reporte
        this.vista.dispose();

        // Abre configuración (el controlador de configuración se crea adentro, como ya venías haciendo)
        VentanaConfiguracion vConfig = new VentanaConfiguracion();
        vConfig.setVisible(true);
    }
}

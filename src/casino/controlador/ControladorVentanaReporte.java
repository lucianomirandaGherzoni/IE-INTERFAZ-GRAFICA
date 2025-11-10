/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.controlador;

import casino.modelo.Estadisticas;
import casino.modelo.Jugador;
import casino.modelo.PartidaModelo;
import casino.vista.VentanaReporte;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Controlador de VentanaReporte sin DTOs.
 * Usa directamente las clases del paquete casino.modelo.
 */
public class ControladorVentanaReporte {

    private final VentanaReporte vista;
    private final List<Jugador> jugadores;
    private final int totalPartidas;
    private final Estadisticas estadisticas;
    private final List<String> historial;
    private final Runnable callbackVolver;

    /* ========= CONSTRUCTORES RECOMENDADOS ========= */

    /** Atajo: construir con el modelo completo. */
    public ControladorVentanaReporte(PartidaModelo modelo) {
        this(new VentanaReporte(), modelo, () -> {});
    }

    /** Atajo: vista + modelo + acción de volver (por ejemplo, abrir configuración). */
    public ControladorVentanaReporte(VentanaReporte vista, PartidaModelo modelo, Runnable onVolver) {
        this(
            vista,
            (modelo != null) ? modelo.getJugadores()        : List.of(),
            (modelo != null) ? modelo.getContadorPartidas() : 0,
            (modelo != null) ? modelo.getEstadisticas()     : null,
            (modelo != null) ? modelo.getHistorial()        : List.of(),
            onVolver
        );
    }

    /* ========= CONSTRUCTOR COMPATIBLE (lo que te dejaron tus compañeros) ========= */

    public ControladorVentanaReporte(VentanaReporte vista,
                                     List<Jugador> jugadores,
                                     int totalPartidas,
                                     Estadisticas estadisticas,
                                     ArrayList<String> historial) {
        this(vista, jugadores, totalPartidas, estadisticas, (Collection<String>) historial, () -> {});
    }

    /* ========= IMPLEMENTACIÓN BASE (con null-safety) ========= */

    public ControladorVentanaReporte(VentanaReporte vista,
                                     Collection<? extends Jugador> jugadores,
                                     Integer totalPartidas,
                                     Estadisticas estadisticas,
                                     Collection<String> historial,
                                     Runnable onVolver) {

        this.vista         = (vista != null) ? vista : new VentanaReporte();
        this.jugadores     = (jugadores != null) ? new ArrayList<>(jugadores) : List.of();
        this.totalPartidas = (totalPartidas != null) ? totalPartidas : 0;
        this.estadisticas  = estadisticas;
        this.historial     = (historial != null) ? new ArrayList<>(historial) : List.of();
        this.callbackVolver = (onVolver != null) ? onVolver : () -> {};

        init();
    }

    /* ========= LÓGICA ========= */

    private void init() {
        // Título simpático (opcional)
        try { vista.setTitle("Reporte del Juego — Partidas: " + totalPartidas); } catch (Exception ignored) {}

        // 1) Ranking (la vista ordena por dinero desc internamente)
        vista.cargarRanking(jugadores);

        // 2) Estadísticas
        vista.mostrarEstadisticas(estadisticas);

        // 3) Historial (tu PartidaModelo ya limita a 5 entradas; la vista vuelve a acotar a 3–5)
        vista.cargarHistorial(historial);

        // 4) Botón Volver
        vista.onVolver(() -> {
            vista.dispose();
            callbackVolver.run();
        });
    }

    /** Muestra la ventana en el EDT. */
    public void mostrar() {
        if (SwingUtilities.isEventDispatchThread()) {
            vista.setVisible(true);
        } else {
            SwingUtilities.invokeLater(() -> vista.setVisible(true));
        }
    }
}

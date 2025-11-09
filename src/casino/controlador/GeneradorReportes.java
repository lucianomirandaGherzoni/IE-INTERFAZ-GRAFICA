/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.controlador;

import casino.modelo.Estadisticas;
import casino.modelo.Jugador;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BANGHO
 */
public class GeneradorReportes {

    public GeneradorReportes() {
        
    }
    
    // Muestra historial por consola
    public void mostrarHistorial(ArrayList<String> historial) {
        System.out.println("\n--- HISTORIAL RECIENTE ---");
        if (historial.isEmpty()) {
            System.out.println("(vacío)");
            return;
        }
        for (String entrada : historial) {
            System.out.println(entrada);
        }
    }
    
    // Muestra estadísticas actuales
    public void mostrarEstadisticas(Estadisticas estadisticas) {
        System.out.println("\n--- ESTADÍSTICAS GENERALES ---");
        System.out.println("Mayor apuesta realizada: $" + estadisticas.getMayorApuesta() 
                + " (" + estadisticas.getJugadorMayorApuesta() + ")");
        System.out.println("Mejor puntaje de dados: " + estadisticas.getMejorPuntaje() 
                + " (" + estadisticas.getJugadorMejorPuntaje() + ")");
        System.out.println("Jugadores afectados por trampas del casino: " 
                + estadisticas.getVictimasDelCasino());
    }
    
    // Imprime reporte final completo
    public void imprimirReporteFinal(List<Jugador> jugadores, int partidas, 
                                     Estadisticas estadisticas, ArrayList<String> historial) {
        StringBuilder rep = new StringBuilder();

        rep.append("\n=======================================\n");
        rep.append(" REPORTE FINAL DEL CASINO\n");
        rep.append("=======================================\n");

        rep.append("Jugadores participantes: ").append(jugadores.size()).append("\n");
        rep.append("Total de partidas jugadas: ").append(partidas).append("\n");

        // --- RANKING FINAL ---
        rep.append("--- RANKING FINAL ---\n");
        List<Jugador> ranking = new ArrayList<>(jugadores);
        ranking.sort((a, b) -> {
            int cmp = Integer.compare(b.getDinero(), a.getDinero());
            return (cmp != 0) ? cmp : Integer.compare(b.getPartidasGanadas(), a.getPartidasGanadas());
        });

        int pos = 1;
        for (Jugador j : ranking) {
            rep.append(pos++).append(". ")
                    .append(j.getApodo()).append(" (").append(j.obtenerTipoJugador()).append(") - $")
                    .append(j.getDinero()).append(" - ")
                    .append(j.getPartidasGanadas()).append(" victorias\n");
        }

        // --- ESTADÍSTICAS GENERALES ---
        rep.append("--- ESTADÍSTICAS GENERALES ---\n");
        rep.append("Mayor apuesta realizada: $").append(estadisticas.getMayorApuesta())
                .append(" (").append(estadisticas.getJugadorMayorApuesta()).append(")\n");
        rep.append("Mejor puntaje de dados: ").append(estadisticas.getMejorPuntaje())
                .append(" (").append(estadisticas.getJugadorMejorPuntaje()).append(")\n");
        rep.append("Jugadores afectados por trampas del casino: ")
                .append(estadisticas.getVictimasDelCasino()).append("\n");

        // --- HISTORIAL RECIENTE ---
        rep.append("--- HISTORIAL RECIENTE ---\n");
        if (historial.isEmpty()) {
            rep.append("(vacío)\n");
        } else {
            rep.append("[Últimas ").append(historial.size()).append(" partidas]\n");
            for (String h : historial) {
                rep.append(h).append("\n");
            }
        }

        rep.append("=======================================\n");
        System.out.println(rep.toString());
    }
}

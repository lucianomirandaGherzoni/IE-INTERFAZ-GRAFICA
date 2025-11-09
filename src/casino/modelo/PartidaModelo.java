/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BANGHO
 */
public class PartidaModelo {
    private ArrayList <String> historial;
    private int contadorPartidas;
    private Estadisticas estadisticas;
    private List <Jugador> jugadores;
    private CasinoAdministrador casino;
    
    //constructor
    public PartidaModelo() {
        this.historial = historial;
        this.contadorPartidas = contadorPartidas;
        this.estadisticas = estadisticas;
        this.jugadores = jugadores;
        this.casino = casino;
    }
    
    //Métodos para manejar el historial
    public void guardarEnHistorial(String detalle){
        if (historial.size() == 5){
            historial.remove(0);
        }
        historial.add(detalle);
    }
    
    public ArrayList<String> getHistorial(){
        return historial;
    }
    
    //Métodos de partidas
    public void incrementarContadorPartidas(){
        contadorPartidas++;
    }

    public int getContadorPartidas() {
        return contadorPartidas;
    }
    
    //Métodos de estadísticas
    public Estadisticas getEstadisticas() {
        return estadisticas;
    }
    
    //Métodos de jugadores 
    public void agregarJugador(Jugador jugador) {
        jugadores.add(jugador);
        casino.agregarJugador(jugador);
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }
    
    public void limpiarJugadores() {
        jugadores.clear();
    }
    
    //Método para obtener el casino
    public CasinoAdministrador getCasino() {
        return casino;
    }
    
    // Método para reiniciar casino (nueva partida)
    public void reiniciarCasino() {
        this.casino = new CasinoAdministrador();
    }
    
    //Método para definir el ganador de la partida
    public String obtenerGanador(){
        int maxWins = -1;
        List<String> ganadores = new ArrayList<>();
        
        for (Jugador j : jugadores) {
            int w = j.getPartidasGanadas();
            if (w > maxWins) {
                maxWins = w;
                ganadores.clear();
                ganadores.add(j.getNombre());
            } else if (w == maxWins) {
                ganadores.add(j.getNombre());
            }
        }
        
        return (ganadores.size() == 1) ? ganadores.get(0) : "Empate";       
    }
        
    //Método para calcular el total de rondas jugadas
    public int calcularTotalRondas() {
        int rondas = 0;
        for (Jugador j : jugadores) {
            rondas += j.getPartidasGanadas();
        }
        return rondas;
    }
    
    // Método para crear string de jugadores (CSV)
    public String obtenerNombresJugadoresCSV() {
        List<String> nombres = new ArrayList<>();
        for (Jugador j : jugadores) {
            nombres.add(j.getNombre());
        }
        return String.join(",", nombres);
    }
    
    
    
}

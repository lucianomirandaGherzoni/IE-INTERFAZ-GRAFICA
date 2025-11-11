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
// Asegúrate de que todas tus clases de modelo implementen java.io.Serializable
public class PartidaModelo implements java.io.Serializable {

    private ArrayList<String> historial;
    private int contadorPartidas;
    private Estadisticas estadisticas;
    private List<Jugador> jugadores;
    private CasinoAdministrador casino;
    
    private int cantidadPartidas; 

    private int partidaActual;
    private int rondaActual;
    public static final int RONDAS_POR_PARTIDA = 3; 

    // Estado de la Ronda
    private int jugadorTurnoIndex;
    private ArrayList<Jugador> jugadoresActivosRonda;
    private int pozoRonda;
    private int mejorPuntajeRonda;
    private ArrayList<Jugador> ganadoresRonda;

    // Estado de Re-Roll
    private JugadorVIP jugadorVIPActual;
    private ResultadoDados resultadoVIPActual;

    //constructor
    public PartidaModelo() {
        this.historial = new ArrayList<>();
        this.contadorPartidas = 0;
        this.estadisticas = new Estadisticas();
        this.jugadores = new ArrayList<>();
        this.casino = new CasinoAdministrador();

        // --- INICIALIZACIÓN DE CAMPOS ---
        this.cantidadPartidas = 0; // Inicializado
        this.partidaActual = 1;
        this.rondaActual = 1;
        this.jugadorTurnoIndex = 0;
        this.jugadoresActivosRonda = new ArrayList<>();
        this.pozoRonda = 0;
        this.mejorPuntajeRonda = 0;
        this.ganadoresRonda = new ArrayList<>();
        this.jugadorVIPActual = null;
        this.resultadoVIPActual = null;
    }

    //Métodos para manejar el historial
    public void guardarEnHistorial(String detalle) {
        if (historial.size() == 5) {
            historial.remove(0);
        }
        historial.add(detalle);
    }

    public ArrayList<String> getHistorial() {
        return historial;
    }

    //Métodos de partidas
    public void incrementarContadorPartidas() {
        contadorPartidas++;
    }

    public int getContadorPartidas() {
        return contadorPartidas;
    }
    
   
    public int getCantidadPartidas() {
        return cantidadPartidas;
    }

    public void setCantidadPartidas(int cantidadPartidas) {
        this.cantidadPartidas = cantidadPartidas;
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
    public String obtenerGanador() {
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

    public int getPartidaActual() {
        return partidaActual;
    }

    public void setPartidaActual(int partidaActual) {
        this.partidaActual = partidaActual;
    }
    
    public void incrementarPartidaActual() {
        this.partidaActual++;
    }

    public int getRondaActual() {
        return rondaActual;
    }

    public void setRondaActual(int rondaActual) {
        this.rondaActual = rondaActual;
    }
    
    public void incrementarRondaActual() {
        this.rondaActual++;
    }

    public int getJugadorTurnoIndex() {
        return jugadorTurnoIndex;
    }

    public void setJugadorTurnoIndex(int jugadorTurnoIndex) {
        this.jugadorTurnoIndex = jugadorTurnoIndex;
    }
    
    public void incrementarJugadorTurnoIndex() {
        this.jugadorTurnoIndex++;
    }

    // --- Métodos para Jugadores Activos ---
    public ArrayList<Jugador> getJugadoresActivosRonda() {
        return jugadoresActivosRonda;
    }

    public void setJugadoresActivosRonda(ArrayList<Jugador> jugadoresActivosRonda) {
        this.jugadoresActivosRonda = jugadoresActivosRonda;
    }
    
    public void limpiarJugadoresActivosRonda() {
        this.jugadoresActivosRonda.clear();
    }
    
    public void agregarJugadorActivoRonda(Jugador j) {
        this.jugadoresActivosRonda.add(j);
    }
    
    public int getCantidadJugadoresActivosRonda() {
        return this.jugadoresActivosRonda.size();
    }
    
    public Jugador getJugadorActivoRonda(int index) {
        return this.jugadoresActivosRonda.get(index);
    }

    // --- Métodos para Pozo ---
    public int getPozoRonda() {
        return pozoRonda;
    }

    public void setPozoRonda(int pozoRonda) {
        this.pozoRonda = pozoRonda;
    }
    
    public void agregarAlPozo(int monto) {
        this.pozoRonda += monto;
    }

    // --- Métodos para Ganadores de Ronda ---
    public int getMejorPuntajeRonda() {
        return mejorPuntajeRonda;
    }

    public void setMejorPuntajeRonda(int mejorPuntajeRonda) {
        this.mejorPuntajeRonda = mejorPuntajeRonda;
    }

    public ArrayList<Jugador> getGanadoresRonda() {
        return ganadoresRonda;
    }

    public void setGanadoresRonda(ArrayList<Jugador> ganadoresRonda) {
        this.ganadoresRonda = ganadoresRonda;
    }
    
    public void limpiarGanadoresRonda() {
        this.ganadoresRonda.clear();
    }
    
    public void agregarGanadorRonda(Jugador j) {
        this.ganadoresRonda.add(j);
    }

    // --- Métodos para Estado Re-Roll ---
    public JugadorVIP getJugadorVIPActual() {
        return jugadorVIPActual;
    }

    public void setJugadorVIPActual(JugadorVIP jugadorVIPActual) {
        this.jugadorVIPActual = jugadorVIPActual;
    }

    public ResultadoDados getResultadoVIPActual() {
        return resultadoVIPActual;
    }

    public void setResultadoVIPActual(ResultadoDados resultadoVIPActual) {
        this.resultadoVIPActual = resultadoVIPActual;
    }
    
    public void limpiarEstadoReRoll() {
        this.jugadorVIPActual = null;
        this.resultadoVIPActual = null;
    }

}
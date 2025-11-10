/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.modelo;

import casino.modelo.Jugador;
import casino.modelo.JugadorNovato;
import casino.modelo.RegistroTrampas;
import casino.modelo.juegoDados;
import casino.modelo.Estadisticas;
import casino.modelo.JugadorVIP;
import casino.modelo.JugadorExperto;
import casino.modelo.JugadorCasino;
import java.util.ArrayList;

public class CasinoAdministrador  implements java.io.Serializable{
    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private juegoDados juego;
    private RegistroTrampas registroTrampas = new RegistroTrampas();
    
    public CasinoAdministrador() {
        this.juego = new juegoDados(); 
    }
    
    
    public Jugador crearJugador(String nombre, int tipo) {
        return switch (tipo) {
            case 1 -> new JugadorNovato(nombre);
            case 2 -> new JugadorExperto(nombre);
            case 3 -> new JugadorVIP(nombre);
            case 4 -> new JugadorCasino(nombre);
            default -> new JugadorNovato(nombre);
        };
    }
    
    public void agregarJugador (Jugador jugador){
        jugadores.add(jugador);
    }
    
    public juegoDados getJuego() {
        return juego;
    }
    
    public void jugar (Estadisticas estadisticas){
        // Buscar si hay JugadorCasino
        JugadorCasino casino = null;
        for (Jugador j : jugadores) {
            if (j instanceof JugadorCasino) {
                casino = (JugadorCasino) j;
                break;
            }
        }
        
        // Crear JuegoDados con los datos necesarios
        juego = new juegoDados();
        
        for (int ronda = 1; ronda <= 3; ronda++) {
            System.out.println("\nRONDA " + ronda);
            
            // Verificar jugadores con dinero
            ArrayList<Jugador> jugadoresActivos = new ArrayList<>();
            for (Jugador j : jugadores) {
                if (j.getDinero() > 0) {
                    jugadoresActivos.add(j);
                }
            }
            
            if (jugadoresActivos.size() < 2) {
                System.out.println("Fin del juego - no hay suficientes jugadores");
                break;
            }
            
            // Acá empiezan las apuestas
            int pozo = 0;
            for (Jugador j : jugadoresActivos) {
                int apuesta = j.calcularApuesta();
                j.perder(apuesta);
                pozo += apuesta;
                estadisticas.registrarApuesta(j.getApodo(), apuesta);
                System.out.println(j.getNombre() + " apuesta $" + apuesta);
            }
            System.out.println("Pozo: $" + pozo);
            
            // Ahora se lanzan los dados
            int mejorPuntaje = 0;
            ArrayList<Jugador> ganadores = new ArrayList<>();
            
            for (Jugador j : jugadoresActivos) {
   
                ResultadoDados resultado = juego.lanzarDados(j, casino, registroTrampas, estadisticas);
                
                for(String msg : resultado.getMensajes()){
                    System.out.println(msg);
                }
                
     
                int puntaje = resultado.getSuma(); 
   
                estadisticas.registrarPuntaje(j.getApodo(), puntaje);
                
                if (puntaje > mejorPuntaje) {
                    mejorPuntaje = puntaje;
                    ganadores.clear();
                    ganadores.add(j);
                } else if (puntaje == mejorPuntaje) {
                    ganadores.add(j);
                }
            }
            
            // Premio
            int premio = pozo / ganadores.size();
            for (Jugador g : ganadores) {
                g.ganar(premio);
                System.out.println("¡" + g.getNombre() + " gana $" + premio + "!");
            }
            
            // Estado actual
            System.out.println("\nEstado:");
            for (Jugador j : jugadores) {
                System.out.println(j.getNombre() + ": $" + j.getDinero());
            }
        }
        
        System.out.println("\n=== JUEGO TERMINADO ===");
        for (Jugador j : jugadores) {
            System.out.println(j.getNombre() + " (" + j.obtenerTipoJugador() + "): $" + j.getDinero() + " - Ganadas: " + j.getPartidasGanadas());
        }
    }
    
    public RegistroTrampas getRegistroTrampas (){
        return registroTrampas;
    } 
}

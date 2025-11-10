/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.modelo;

import java.util.ArrayList;

public class Estadisticas  implements java.io.Serializable{
    private int mejorPuntaje = 0;
    private String jugadorMejorPuntaje = "";
    private int mayorApuesta = 0;
    private String jugadorMayorApuesta = "";
    private ArrayList<String> victimasDelCasino = new ArrayList<>();
    
    public void registrarApuesta(String jugador, int apuesta){
        if (apuesta > mayorApuesta){
            mayorApuesta = apuesta;
            jugadorMayorApuesta = jugador;
        }
    }
    
    public void registrarPuntaje(String jugador, int puntaje){
        if (puntaje > mejorPuntaje){
            mejorPuntaje = puntaje;
            jugadorMejorPuntaje = jugador;
        }
    }
    
    public void registrarVictimas (String nombreJugador){
        victimasDelCasino.add(nombreJugador);
    }

    public int getMejorPuntaje() {
        return mejorPuntaje;
    }

    public String getJugadorMejorPuntaje() {
        return jugadorMejorPuntaje;
    }

    public int getMayorApuesta() {
        return mayorApuesta;
    }

    public String getJugadorMayorApuesta() {
        return jugadorMayorApuesta;
    }
    
    public String getVictimasDelCasino(){
        if (victimasDelCasino.isEmpty()) return "Ninguna";
        
        return String.join(",", victimasDelCasino) + "(Total: " + victimasDelCasino.size() + ")";
    }
    
    public void reset() {
        this.mejorPuntaje = 0;
        this.jugadorMejorPuntaje = "";
        this.mayorApuesta = 0;
        this.jugadorMayorApuesta = "";
        this.victimasDelCasino.clear();
    }
}

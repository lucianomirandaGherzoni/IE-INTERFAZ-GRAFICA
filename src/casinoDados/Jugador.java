/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casinoDados;

//import java.util.*;

abstract class Jugador {
    public String nombre;

    public int dinero;
    public int partidasGanadas;
    
    public Jugador(String nombre){
        this.nombre = nombre;
   
        this.dinero = 100;
        this.partidasGanadas = 0;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDinero(int dinero) {
        this.dinero = dinero;
    }

    public void setPartidasGanadas(int partidasGanadas) {
        this.partidasGanadas = partidasGanadas;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDinero() {
        return dinero;
    }

    public int getPartidasGanadas() {
        return partidasGanadas;
    }
    
    //Métodos comunes
    public void ganar (int cantidad){
        this.dinero += cantidad;
        partidasGanadas++;
    }
    
    public void perder (int cantidad){
        this.dinero -= cantidad;
    }
    
    //Métodos abstractos
    public abstract int calcularApuesta();
    public abstract String obtenerTipoJugador(); 
    
    @Override
    public String toString() {
        return nombre + " (" + obtenerTipoJugador() + ") - $" + dinero + " - Partidas ganadas: " + partidasGanadas;
    }
    
}

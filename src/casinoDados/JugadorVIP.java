/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casinoDados;


class JugadorVIP extends Jugador{

    private boolean reRoll;
    
            
    public JugadorVIP(String nombre) {
        super(nombre);
        this.reRoll = false;
    }
    
    @Override
    public int calcularApuesta() {
        return (int) (getDinero() * 0.30); // Apuesta 30% 
    }
    
    @Override
    public String obtenerTipoJugador() {
        return "VIP";
    }
    
    public boolean puedeReroll() {
        return !reRoll;
    }
    
    public void usarReroll() {
        this.reRoll = true;
    }
    
    public void resetReroll() {
        this.reRoll = false;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.modelo;

class JugadorExperto extends Jugador{

    public JugadorExperto(String nombre) {
        super(nombre);
    }
    
    @Override
    public int calcularApuesta() {
        return (int) (getDinero() * 0.20); // Apuesta 20% de su dinero
    }
    
    @Override
    public String obtenerTipoJugador() {
        return "Experto";
    }
    
}

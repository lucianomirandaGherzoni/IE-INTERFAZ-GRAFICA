/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casinoDados;


class JugadorCasino extends Jugador{

    public JugadorCasino(String nombre) {
        super(nombre);
    }
        
    @Override
    public int calcularApuesta() {
        return (int) (getDinero()); 
    }
    
    @Override
    public String obtenerTipoJugador() {
        return "Jugador Casino";
    }
}

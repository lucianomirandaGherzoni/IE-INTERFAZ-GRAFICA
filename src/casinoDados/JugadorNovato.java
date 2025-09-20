/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casinoDados;

class JugadorNovato extends Jugador{

    public JugadorNovato(String nombre) {
        super(nombre);
    }
    
    @Override
    public int calcularApuesta() {
        return Math.min(50, getDinero());
    }
    
    @Override
    public String obtenerTipoJugador() {
        return "Novato";
    }
}

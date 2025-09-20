/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casinoDados;

import java.util.Random;


class JugadorCasino extends Jugador{

    private double PROBABILIDAD_DADOS_CARGADOS = 0.3;
    private double PROBABILIDAD_CONFUNDIR = 0.3;    
    private Random random;

    public JugadorCasino(String nombre) {
        super(nombre);
        this.random = new Random();
    }
        
    @Override
    public int calcularApuesta() {
        return (int) (getDinero() * 0.40); //40% así tiene una "apuesta agresiva"
    }
    
    @Override
    public String obtenerTipoJugador() {
        return "Casino";
    }
    
    //Habilidades tramposas
    public boolean intentarDadosCargados(){
        return random.nextDouble() < PROBABILIDAD_DADOS_CARGADOS;
    }
    
    public boolean intentarConfundirJugador() {
        return random.nextDouble() < PROBABILIDAD_CONFUNDIR;
    }
}

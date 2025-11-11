/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.modelo;

import java.util.*;

public class juegoDados implements java.io.Serializable {

    private final Dado dado = new Dado();

    public ResultadoDados lanzarDados(Jugador jugador, JugadorCasino casino, RegistroTrampas registro, Estadisticas estadisticas) {
        int dado1 = dado.tirar();
        int dado2 = dado.tirar();

        ResultadoDados resultado = new ResultadoDados(dado1, dado2);

        if (casino != null && jugador != casino) {
            if (casino.intentarConfundirJugador()) {
                dado1 = Math.max(1, dado1 - 1);
                dado2 = Math.max(1, dado2 - 1);
                resultado = new ResultadoDados(dado1, dado2);

                registro.registrarTrampa("Confusión", jugador.getNombre());
                estadisticas.registrarVictimas(jugador.getApodo());

                resultado.agregarMensaje("¡El casino confundió a " + jugador.getNombre() + "!");
            }
        }

        if (jugador instanceof JugadorCasino casinoJugador) {
            if (casinoJugador.intentarDadosCargados()) {
   
                resultado = new ResultadoDados(6, 6);
                registro.registrarTrampa("Dados cargados", "El Casino");

                resultado.agregarMensaje("¡El casino usó dados cargados!");
            }
        }

        jugador.setResultadoDados(new int[]{resultado.getDado1(), resultado.getDado2()});


        return resultado;
    }
}

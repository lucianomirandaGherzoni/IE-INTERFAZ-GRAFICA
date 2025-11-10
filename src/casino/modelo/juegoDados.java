/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.modelo;
import java.util.*;

public class juegoDados {
    private final Dado dado = new Dado();
    
    public int lanzarDados(Jugador jugador, JugadorCasino casino, RegistroTrampas registro, Estadisticas estadisticas ) {
        int dado1 = dado.tirar();
        int dado2 = dado.tirar();
        int suma = dado1 + dado2;
        
        if (casino != null && jugador != casino) {
            // Trampa: Confundir jugador (reducir dados)
            if (casino.intentarConfundirJugador()) {
                dado1 = Math.max(1, dado1 - 1);
                dado2 = Math.max(1, dado2 - 1);
                suma = dado1 + dado2;
                registro.registrarTrampa("Confusión", jugador.getNombre());
                estadisticas.registrarVictimas(jugador.getApodo());
                System.out.println("¡El casino confundió a " + jugador.getNombre() + "!");
            }
        }
        
        //En el caso de ser el casino, puede usar dados cargados
        if (jugador instanceof JugadorCasino casinoJugador) {
            if (casinoJugador.intentarDadosCargados()) {
                dado1 = 6;
                dado2 = 6;
                suma = dado1 + dado2;
                registro.registrarTrampa("Dados cargados", "El Casino");
                System.out.println("¡El casino usó dados cargados!");
            }
        }
        
        System.out.println(jugador.getNombre() + " tiró: " + dado1 + " + " + dado2 + " = " + suma);
        
        //En caso de que el VIP haga el re-roll
        if (jugador instanceof JugadorVIP vip) {
            if (vip.puedeReroll()) {
                System.out.print("¿Usar re-roll? (s/n): ");
                String respuesta = new Scanner(System.in).nextLine();
                if (respuesta.equals("s")) {
                    vip.usarReroll();
                    dado1 = dado.tirar();
                    dado2 = dado.tirar();
                    suma = dado1 + dado2;
                    System.out.println("Re-roll: " + dado1 + " + " + dado2 + " = " + suma);
                }
            }
        }
        
        return suma;
    }
}

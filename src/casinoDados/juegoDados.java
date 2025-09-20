/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casinoDados;
import java.util.*;

class juegoDados {
    //private Dado dado = new Dado();
    private final Dado dado = new Dado();
    
    public int lanzarDados(Jugador jugador) {
        int dado1 = dado.tirar();
        int dado2 = dado.tirar();
        int suma = dado1 + dado2;
        
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package casinoDados;

import java.util.*;

public class CasinoDados {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CasinoAdministrador casino = new CasinoAdministrador();
        
        System.out.println("¡Bienvenidos al Casino de Dados!");
        
        // Crear jugadores
        System.out.print("¿Cuántos jugadores participarán? (2-4): ");
        int n = scanner.nextInt();
        scanner.nextLine();
        
        for (int i = 1; i <= n; i++) {
            System.out.print("Nombre del jugador " + i + ": ");
            String nombre = scanner.nextLine();
            System.out.print("Tipo (1=Novato, 2=Experto, 3=VIP): ");
            int tipo = scanner.nextInt();
            scanner.nextLine();
            
            Jugador jugador = casino.crearJugador(nombre, tipo);
            casino.agregarJugador(jugador);
        }
        
        casino.jugar();
        scanner.close();
    }
    

}

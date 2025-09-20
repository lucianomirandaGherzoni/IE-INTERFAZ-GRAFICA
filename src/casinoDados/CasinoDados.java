/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package casinoDados;

import java.util.*;

public class CasinoDados {
    
        // 🔹 Historial de las últimas 5 partidas
    private static final Deque<String> HISTORIAL = new ArrayDeque<>(5);

// Contador para numerar partidas
    private static int contadorPartidas = 0;
    
    // Guarda una línea en el historial con tope 5
    private static void guardarPartida(String detalle) {
        if (HISTORIAL.size() == 5) {
            HISTORIAL.removeFirst();
        }
        HISTORIAL.addLast(detalle);
    }

// Muestra historial por consola
    private static void mostrarHistorial() {
        System.out.println("\n--- HISTORIAL RECIENTE ---");
        if (HISTORIAL.isEmpty()) {
            System.out.println("(vacío)");
            return;
        }
        for (String entrada : HISTORIAL) {
            System.out.println(entrada);
        }
    }


 public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    boolean seguir = true;

    while (seguir) {
        CasinoAdministrador casino = new CasinoAdministrador();

        System.out.println("¡Bienvenidos al Casino de Dados!");
        System.out.print("¿Cuántos jugadores participarán? (2-4): ");
        int n = scanner.nextInt();
        scanner.nextLine();

        List<String> nombres = new ArrayList<>();
        List<Jugador> jugadoresLocal = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            System.out.print("Nombre del jugador " + i + ": ");
            String nombre = scanner.nextLine();
            System.out.print("Tipo (1=Novato, 2=Experto, 3=VIP): ");
            int tipo = scanner.nextInt();
            scanner.nextLine();

            Jugador jugador = casino.crearJugador(nombre, tipo);
            casino.agregarJugador(jugador);

            nombres.add(nombre);
            jugadoresLocal.add(jugador);
        }

        casino.jugar();

        // ===== Guardar en historial =====
        contadorPartidas++;
        String jugadoresCSV = String.join(",", nombres);

        int maxWins = -1;
        List<String> ganadores = new ArrayList<>();
        for (Jugador j : jugadoresLocal) {
            int w = j.getPartidasGanadas();
            if (w > maxWins) { maxWins = w; ganadores.clear(); ganadores.add(j.getNombre()); }
            else if (w == maxWins) { ganadores.add(j.getNombre()); }
        }
        String ganador = (ganadores.size() == 1) ? ganadores.get(0) : "Empate";

        int rondas = 0;
        for (Jugador j : jugadoresLocal) rondas += j.getPartidasGanadas();

        StringBuilder sb = new StringBuilder()
            .append("PARTIDA #").append(contadorPartidas)
            .append(" - Jugadores: ").append(jugadoresCSV)
            .append(" | Ganador: ").append(ganador)
            .append(" | Rondas: ").append(rondas);

        guardarPartida(sb.toString());
        mostrarHistorial();
        // ================================

        // 🔹 Preguntar si seguir
        System.out.print("\n¿Quieren jugar otra partida? (s/n): ");
        String respuesta = scanner.nextLine().trim().toLowerCase();
        if (!respuesta.equals("s")) {
            seguir = false;
        }
    }

    scanner.close();
}
  
    
}

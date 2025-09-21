/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package casinoDados;

import java.util.*;

public class CasinoDados {


    private static final Deque<String> HISTORIAL = new ArrayDeque<>(5);


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
    
    private static void imprimirReporteFinal(List<Jugador> jugadores, int partidas) {
        StringBuilder rep = new StringBuilder();

        rep.append("\n=======================================\n");
        rep.append(" REPORTE FINAL DEL CASINO\n");
        rep.append("=======================================\n");

        rep.append("Jugadores participantes: ").append(jugadores.size()).append("\n");
        rep.append("Total de partidas jugadas: ").append(partidas).append("\n");

        // --- RANKING FINAL ---
        rep.append("--- RANKING FINAL ---\n");
        List<Jugador> ranking = new ArrayList<>(jugadores);
        ranking.sort((a, b) -> {
            int cmp = Integer.compare(b.getDinero(), a.getDinero());
            return (cmp != 0) ? cmp : Integer.compare(b.getPartidasGanadas(), a.getPartidasGanadas());
        });

        int pos = 1;
        for (Jugador j : ranking) {
            rep.append(pos++).append(". ")
                    .append(j.getApodo()).append(" (").append(j.obtenerTipoJugador()).append(") - $")
                    .append(j.getDinero()).append(" - ")
                    .append(j.getPartidasGanadas()).append(" victorias\n");
        }

        // --- Estadísticas (pendientes) ---
        rep.append("--- ESTADÍSTICAS GENERALES ---\n");
        rep.append("Mayor apuesta realizada: (pendiente)\n");
        rep.append("Mejor puntaje de dados: (pendiente)\n");
        rep.append("Víctimas del casino: (pendiente)\n");

        // --- Historial (pendiente) ---
        rep.append("--- HISTORIAL RECIENTE ---\n");
        rep.append("(pendiente)\n");

        rep.append("=======================================\n");
        System.out.println(rep.toString());
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean seguir = true;
            List<Jugador> ultimoPlantel = Collections.emptyList();

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

                //------- Agregar Apodo ---------
                String apodo;
                boolean apodoValido = false;
                Jugador jugador = null;
                
                //bucle while para validar el apodo.
                while (!apodoValido) {
                    System.out.print("Ingrese su Apodo (entre 3-10 caracteres, solo letras y espacios): ");
                    apodo = scanner.nextLine();

                    if (validarApodo(apodo)) {
                        System.out.print("Tipo (1=Novato, 2=Experto, 3=VIP): ");
                        int tipo = scanner.nextInt();
                        scanner.nextLine();
                        
                        // Se asigna el objeto jugador cuando el apodo es válido.
                        jugador = casino.crearJugador(nombre, tipo);
                        jugador.setApodo(apodo);
                        casino.agregarJugador(jugador);
                        apodoValido = true;
                    } else {
                        System.out.println("¡Error! El apodo no es válido. Inténtalo de nuevo.");
                    }
                }
                                  
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
                if (w > maxWins) {
                    maxWins = w;
                    ganadores.clear();
                    ganadores.add(j.getNombre());
                } else if (w == maxWins) {
                    ganadores.add(j.getNombre());
                }
            }
            String ganador = (ganadores.size() == 1) ? ganadores.get(0) : "Empate";

            int rondas = 0;
            for (Jugador j : jugadoresLocal) {
                rondas += j.getPartidasGanadas();
            }

            StringBuilder sb = new StringBuilder()
                    .append("PARTIDA #").append(contadorPartidas)
                    .append(" - Jugadores: ").append(jugadoresCSV)
                    .append(" | Ganador: ").append(ganador)
                    .append(" | Rondas: ").append(rondas);

            guardarPartida(sb.toString());
            mostrarHistorial();
            // ================================
                  ultimoPlantel = new ArrayList<>(jugadoresLocal);

            // 🔹 Preguntar si seguir
            System.out.print("\n¿Quieren jugar otra partida? (s/n): ");
            String respuesta = scanner.nextLine().trim().toLowerCase();
            if (!respuesta.equals("s")) {
                seguir = false;
            }
        }
        // Al final del juego, mostrar el reporte final
          imprimirReporteFinal(ultimoPlantel, contadorPartidas);


        scanner.close();
    }

    //----Validacion de Apodo Metodo para validar si un apodo cumple con los requisitos.
    public static boolean validarApodo(String apodo) {
        if (apodo == null || apodo.isEmpty()) {
            return false;
        }
        return apodo.length() >= 3 && apodo.length() <= 10 && apodo.matches("[a-zA-Z\\s]+");
    }

}

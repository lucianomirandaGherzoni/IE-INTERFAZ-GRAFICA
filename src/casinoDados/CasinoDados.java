/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package casinoDados;

import java.util.*;

public class CasinoDados {

    private static final ArrayList<String> HISTORIAL = new ArrayList<>(5);
    private static int contadorPartidas = 0;
    private static Estadisticas estadisticas = new Estadisticas();

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

        // --- Estadísticas---
        rep.append("--- ESTADÍSTICAS GENERALES ---\n");
        rep.append("Mayor apuesta realizada: $").append(estadisticas.getMayorApuesta())
                .append(" (").append(estadisticas.getJugadorMayorApuesta()).append(")\n");
        rep.append("Mejor puntaje de dados: ").append(estadisticas.getMejorPuntaje())
                .append(" (").append(estadisticas.getJugadorMejorPuntaje()).append(")\n");
        rep.append("Jugadores afectados por trampas del casino: ")
                .append(estadisticas.getVictimasDelCasino()).append("\n");

        // --- HISTORIAL RECIENTE ---
        rep.append("--- HISTORIAL RECIENTE ---\n");
        if (HISTORIAL.isEmpty()) {
            rep.append("(vacío)\n");
        } else {
            rep.append("[Últimas ").append(HISTORIAL.size()).append(" partidas]\n");
            for (String h : HISTORIAL) {
                rep.append(h).append("\n");
            }
        }

        rep.append("=======================================\n");
        System.out.println(rep.toString());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean seguir = true;
        List<Jugador> ultimoPlantel = new ArrayList<>();

        while (seguir) {
            CasinoAdministrador casino = new CasinoAdministrador();

            System.out.println("¡Bienvenidos al Casino de Dados!");

            // Crear jugadores
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
                        System.out.print("Tipo (1=Novato, 2=Experto, 3=VIP, 4=Casino): ");
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

            casino.jugar(estadisticas);

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

            // --- Lógica de comandos ---
            while (seguir) {

                System.out.println("\n  (Escribe 's' para jugar otra partida o algun COMANDO :(STATS, HISTORY, RANKING, TRAMPAS, SAVE [TU NOMBRE], QUIT))");
                String comando = scanner.nextLine().trim().toUpperCase();

                switch (comando) {
                    case "S":
                        seguir = true;
                        break;
                    case "STATS":
                        //Logica para mostrar estadisticas 
                        System.out.println("\n--- ESTADÍSTICAS GENERALES ---");
                        System.out.println("Mayor apuesta realizada: $" + estadisticas.getMayorApuesta() + " (" + estadisticas.getJugadorMayorApuesta() + ")");
                        System.out.println("Mejor puntaje de dados: " + estadisticas.getMejorPuntaje() + " (" + estadisticas.getJugadorMejorPuntaje() + ")");
                        System.out.println("Jugadores afectados por trampas del casino: " + estadisticas.getVictimasDelCasino());
                        break;
                    case "HISTORY":
                        //Mostrar Historial de partidas
                        mostrarHistorial();
                        break;
                    case "RANKING":
                        //Mostrar rankinkg actual
                        imprimirReporteFinal(ultimoPlantel, contadorPartidas);

                        break;
                    case "TRAMPAS":
                        //Mostrar registros trampas
                        casino.getRegistroTrampas().mostrarTrampas();
                        break;
                    case "QUIT":
                        seguir = false;
                        System.out.println("¡Gracias por jugar!");
                        break;
                    default:
                        if (comando.startsWith("SAVE ")) {
                            // Logica para guardar partida

                        } else {
                            System.out.println("Comando no reconocido. Saliendo del juego.");
                            seguir = false;
                        }
                        break;
                }

            }
        }

        //  Reporte final
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

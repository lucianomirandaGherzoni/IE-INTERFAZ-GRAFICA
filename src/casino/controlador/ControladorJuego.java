/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.controlador;

import casino.modelo.*;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author BANGHO
 */
public class ControladorJuego {
    private PartidaModelo modelo;
    private Scanner scanner;
    private GeneradorReportes generadorReportes;

    public ControladorJuego() {
        this.modelo = new PartidaModelo();
        this.scanner = new Scanner(System.in);
        this.generadorReportes = new GeneradorReportes();
    }
    
    public void iniciar(){
        boolean seguir = true;
        
        while (seguir) {
            // Reiniciar casino para nueva partida
            modelo.reiniciarCasino();
            modelo.limpiarJugadores();
            
            System.out.println("¡Bienvenidos al Casino de Dados!");
            
            // Registrar jugadores
            registrarJugadores();
            
            // Jugar la partida
            modelo.getCasino().jugar(modelo.getEstadisticas());
            
            // Guardar resultado en historial
            guardarResultadoPartida();
            
            // Mostrar historial
            generadorReportes.mostrarHistorial(modelo.getHistorial());
            
            // Procesar comandos
            seguir = procesarComandos();
        }
        
        //Reporte final al terminar
        generadorReportes.imprimirReporteFinal(modelo.getJugadores(), modelo.getContadorPartidas(), modelo.getEstadisticas(), modelo.getHistorial());
        scanner.close();
    }
    
    private void registrarJugadores(){
        System.out.println("¿Cuántos jugadores participarán? (2-4):");
        int n = scanner.nextInt();
        scanner.nextLine();
        
        for (int i = 1; i <= n; i++) {
            System.out.print("Nombre del jugador " + i + ": ");
            String nombre = scanner.nextLine();
            
            // Validar apodo
            String apodo;
            boolean apodoValido = false;
            Jugador jugador = null;
            
            while (!apodoValido) {
                System.out.print("Ingrese su Apodo (entre 3-10 caracteres, solo letras y espacios): ");
                apodo = scanner.nextLine();
                
                if (validarApodo(apodo)) {
                    System.out.print("Tipo (1=Novato, 2=Experto, 3=VIP, 4=Casino): ");
                    int tipo = scanner.nextInt();
                    scanner.nextLine();
                    
                    jugador = modelo.getCasino().crearJugador(nombre, tipo);
                    jugador.setApodo(apodo);
                    modelo.agregarJugador(jugador);
                    apodoValido = true;
                } else {
                    System.out.println("¡Error! El apodo no es válido. Inténtalo de nuevo.");
                }
            }
        }
    }
    
    private void guardarResultadoPartida() {
        modelo.incrementarContadorPartidas();
        
        String jugadoresCSV = modelo.obtenerNombresJugadoresCSV();
        String ganador = modelo.obtenerGanador();
        int rondas = modelo.calcularTotalRondas();
        
        StringBuilder sb = new StringBuilder()
                .append("PARTIDA #").append(modelo.getContadorPartidas())
                .append(" - Jugadores: ").append(jugadoresCSV)
                .append(" | Ganador: ").append(ganador)
                .append(" | Rondas: ").append(rondas);
        
        modelo.guardarEnHistorial(sb.toString());
    }
    
    private boolean procesarComandos() {
        boolean continuarJugando = true;
        
        while (true) {
            System.out.println("\n(Escribe 's' para jugar otra partida o algún COMANDO: STATS, HISTORY, RANKING, TRAMPAS, SAVE [NOMBRE], QUIT)");
            String comando = scanner.nextLine().trim().toUpperCase();
            
            switch (comando) {
                case "S":
                    return true; // Continuar con otra partida
                    
                case "STATS":
                    generadorReportes.mostrarEstadisticas(modelo.getEstadisticas());
                    break;
                    
                case "HISTORY":
                    generadorReportes.mostrarHistorial(modelo.getHistorial());
                    break;
                    
                case "RANKING":
                    generadorReportes.imprimirReporteFinal(modelo.getJugadores(), 
                            modelo.getContadorPartidas(), 
                            modelo.getEstadisticas(), 
                            modelo.getHistorial());
                    break;
                    
                case "TRAMPAS":
                    modelo.getCasino().getRegistroTrampas().mostrarTrampas();
                    break;
                    
                case "QUIT":
                    System.out.println("¡Gracias por jugar!");
                    return false; // Salir del juego
                    
                default:
                    if (comando.startsWith("SAVE ")) {
                        String nombreArchivo = comando.substring(5).trim();
                        System.out.println("Guardando partida como: " + nombreArchivo);
                        // TODO: Implementar guardado con GestorPersistencia
                    } else {
                        System.out.println("Comando no reconocido.");
                    }
                    break;
            }
        }
    }
    
    private boolean validarApodo(String apodo) {
        if (apodo == null || apodo.isEmpty()) {
            return false;
        }
        return apodo.length() >= 3 && apodo.length() <= 10 && apodo.matches("[a-zA-Z\\s]+");
    }
}

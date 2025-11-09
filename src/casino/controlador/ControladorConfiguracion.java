package casino.controlador;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import casino.modelo.*;
import casino.vista.VentanaConfiguracion;
import casino.vista.VentanaJuego;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BANGHO
 */
public class ControladorConfiguracion {
    private VentanaConfiguracion vista;
    private DefaultListModel<String> modeloLista;
    private List<Jugador> jugadores;
    
    public ControladorConfiguracion(VentanaConfiguracion vista) {
        this.vista = vista;
        this.modeloLista = new DefaultListModel<>();
        this.jugadores = new ArrayList<>();
        inicializarVista();
    }
    
    
    private void inicializarVista() {
        // Asignar el modelo a la lista
        vista.getLstJugadores().setModel(modeloLista);
    }
    
    public void agregarJugador(String nombre, String apodo, String tipo) {
        // Validar nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            vista.mostrarError("El nombre no puede estar vacío");
            return;
        }
        
        // Validar apodo (3-10 caracteres, solo letras y espacios)
        if (!validarApodo(apodo)) {
            vista.mostrarError("El apodo debe tener entre 3-10 caracteres, solo letras y espacios");
            return;
        }
        
        // Validar que no haya más de 4 jugadores
        if (jugadores.size() >= 4) {
            vista.mostrarError("Máximo 4 jugadores permitidos");
            return;
        }
        
        // Crear jugador según tipo
        Jugador jugador = crearJugador(nombre, tipo);
        jugador.setApodo(apodo);
        
        // Agregar a la lista
        jugadores.add(jugador);
        modeloLista.addElement((jugadores.size()) + ". " + nombre + " (" + apodo + ") - " + tipo);
        
        // Limpiar campos
        vista.limpiarCampos();
        
        vista.mostrarMensaje("Jugador agregado correctamente");
    }
    
    public void eliminarJugador(int indice) {
        if (indice == -1) {
            vista.mostrarError("Selecciona un jugador para eliminar");
            return;
        }
        
        jugadores.remove(indice);
        modeloLista.remove(indice);
        
        // Reordenar números
        for (int i = 0; i < modeloLista.size(); i++) {
            String elemento = modeloLista.get(i);
            // Quitar el número viejo y poner el nuevo
            String sinNumero = elemento.substring(elemento.indexOf(".") + 2);
            modeloLista.set(i, (i + 1) + ". " + sinNumero);
        }
        
        vista.mostrarMensaje("Jugador eliminado");
    }
    
    public void iniciarJuego(int dineroInicial, int cantidadPartidas) {
        // Validar cantidad de jugadores
        if (jugadores.size() < 2) {
            vista.mostrarError("Se necesitan al menos 2 jugadores para comenzar");
            return;
        }
        
        if (jugadores.size() > 4) {
            vista.mostrarError("Máximo 4 jugadores permitidos");
            return;
        }
        
        // Validar dinero inicial
        if (dineroInicial <= 0) {
            vista.mostrarError("El dinero inicial debe ser mayor a 0");
            return;
        }
        
        // Configurar dinero inicial de todos los jugadores
        for (Jugador j : jugadores) {
            j.setDinero(dineroInicial);
        }
        
        // Crear y abrir ventana de juego
        VentanaJuego ventanaJuego = new VentanaJuego(jugadores, cantidadPartidas);
        ventanaJuego.setVisible(true);
        
        // Cerrar ventana de configuración
        vista.dispose();
    }
    
    public void salir() {
        int confirmacion = JOptionPane.showConfirmDialog(
            vista,
            "¿Está seguro que desea salir?",
            "Confirmar salida",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    private boolean validarApodo(String apodo) {
        if (apodo == null || apodo.isEmpty()) {
            return false;
        }
        return apodo.length() >= 3 && apodo.length() <= 10 && apodo.matches("[a-zA-Z\\s]+");
    }
    
    private Jugador crearJugador(String nombre, String tipo) {
        return switch (tipo) {
            case "Novato" -> new JugadorNovato(nombre);
            case "Experto" -> new JugadorExperto(nombre);
            case "VIP" -> new JugadorVIP(nombre);
            case "Casino" -> new JugadorCasino(nombre);
            default -> new JugadorNovato(nombre);
        };
    }
}

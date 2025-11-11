/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package casino.controlador;

import casino.modelo.*;
import casino.vista.VentanaConfiguracion;
import casino.vista.VentanaJuego;
import casino.controlador.ControladorVentanaJuego;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;


/**
 *
 * @author BANGHO
 */

public class ControladorConfiguracion {
    private VentanaConfiguracion vista;
    private DefaultListModel<String> modeloLista;
    private List<Jugador> jugadores;
    
    private GestorPersistencia gestorPersistencia;
    
    // Constructor: Inicializa el controlador, listas y gestor.
    public ControladorConfiguracion(VentanaConfiguracion vista) {
        this.vista = vista;
        this.modeloLista = new DefaultListModel<>();
        this.jugadores = new ArrayList<>();
        
        this.gestorPersistencia = new GestorPersistencia();
        
        inicializarVista();
    }
    
    
    // Configura la JList de la vista.
    private void inicializarVista() {
        vista.getLstJugadores().setModel(modeloLista);
        vista.getBtnCargarPartida().addActionListener(e -> clicCargarPartida());
    }
    
    // Maneja el clic para cargar una partida guardada.
    private void clicCargarPartida() {
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        chooser.setDialogTitle("Cargar partida");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Partida Guardada (.sav)", "sav"));

        if (chooser.showOpenDialog(vista) == javax.swing.JFileChooser.APPROVE_OPTION) {
            String archivo = chooser.getSelectedFile().getAbsolutePath();
            
            PartidaModelo modeloCargado = gestorPersistencia.cargarPartida(archivo);
            
            if (modeloCargado != null) {
                
                vista.dispose();
                
                VentanaJuego vistaJuego = new VentanaJuego();
                
                ControladorVentanaJuego ctrlJuego = new ControladorVentanaJuego(
                        vistaJuego, 
                        modeloCargado
                ); 
                
                ctrlJuego.iniciarPartidaCargada(); 
            }
        }
    }
    
    // Valida y agrega un nuevo jugador a la lista.
    public void agregarJugador(String nombre, String apodo, String tipo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            vista.mostrarError("El nombre no puede estar vacío");
            return;
        }
        if (!validarApodo(apodo)) {
            vista.mostrarError("El apodo debe tener entre 3-10 caracteres, solo letras y espacios");
            return;
        }
        if (jugadores.size() >= 4) {
            vista.mostrarError("Máximo 4 jugadores permitidos");
            return;
        }
        Jugador jugador = crearJugador(nombre, tipo);
        jugador.setApodo(apodo);
        jugadores.add(jugador);
        modeloLista.addElement((jugadores.size()) + ". " + nombre + " (" + apodo + ") - " + tipo);
        vista.limpiarCampos();
        vista.mostrarMensaje("Jugador agregado correctamente");
    }
    
    // Elimina un jugador seleccionado de la lista.
    public void eliminarJugador(int indice) {
        if (indice == -1) {
            vista.mostrarError("Selecciona un jugador para eliminar");
            return;
        }
        jugadores.remove(indice);
        modeloLista.remove(indice);
        for (int i = 0; i < modeloLista.size(); i++) {
            String elemento = modeloLista.get(i);
            String sinNumero = elemento.substring(elemento.indexOf(".") + 2);
            modeloLista.set(i, (i + 1) + ". " + sinNumero);
        }
        vista.mostrarMensaje("Jugador eliminado");
    }
    
    // Valida e inicia una nueva partida.
    public void iniciarJuego(int dineroInicial, int cantidadPartidas) {
        if (jugadores.size() < 2) {
            vista.mostrarError("Se necesitan al menos 2 jugadores para comenzar");
            return;
        }
        if (jugadores.size() > 4) {
            vista.mostrarError("Máximo 4 jugadores permitidos");
            return;
        }
        if (dineroInicial <= 0) {
            vista.mostrarError("El dinero inicial debe ser mayor a 0");
            return;
        }
        for (Jugador j : jugadores) {
            j.setDinero(dineroInicial);
        }
        VentanaJuego ventanaJuego = new VentanaJuego();
        ControladorVentanaJuego controlJuego = new ControladorVentanaJuego(
            ventanaJuego, 
            this.jugadores,
            cantidadPartidas
        );
        controlJuego.iniciar();
        vista.dispose();
    }
    
    // Muestra confirmación y cierra la aplicación.
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
    
    // Valida el formato del apodo (longitud y caracteres).
    private boolean validarApodo(String apodo) {
        if (apodo == null || apodo.isEmpty()) {
            return false;
        }
        return apodo.length() >= 3 && apodo.length() <= 10 && apodo.matches("[a-zA-Z\\s]+");
    }
    
    // Crea una instancia del tipo de Jugador (Novato, VIP, etc.).
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
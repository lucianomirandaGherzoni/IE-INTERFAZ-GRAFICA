/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.controlador;


import casino.modelo.PartidaModelo;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
/**
 *
 * @author BANGHO
 */
public class GestorPersistencia {
    
    // Define el nombre del archivo para el historial
    private static final String ARCHIVO_HISTORIAL = "historial.txt";

    /**
     * Guarda el ESTADO COMPLETO de la partida usando Serialización.
     * @param modelo El objeto PartidaModelo a guardar.
     * @param archivo El nombre del archivo (ej: "partida1.sav")
     */
    public void guardarPartida(PartidaModelo modelo, String archivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(modelo);
        } catch (IOException e) {
            // Manejar la excepción apropiadamente
            System.err.println("Error al guardar la partida: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "No se pudo guardar la partida.", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga un ESTADO COMPLETO de partida.
     * @param archivo El nombre del archivo (ej: "partida1.sav")
     * @return El PartidaModelo restaurado, o null si falla.
     */
    public PartidaModelo cargarPartida(String archivo) {
        PartidaModelo modelo = null;
        File file = new File(archivo);

        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "El archivo de guardado no existe.", "Error al Cargar", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            modelo = (PartidaModelo) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar la partida: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "No se pudo cargar la partida. El archivo puede estar corrupto.", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
        }
        return modelo;
    }

    /**
     * Guarda la lista del historial en "historial.txt".
     * Esto cumple el requisito de usar archivos de texto.
     */
    public void guardarHistorial(ArrayList<String> historial) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_HISTORIAL))) {
            for (String linea : historial) {
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el historial: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "No se pudo guardar el historial.", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga la lista del historial desde "historial.txt".
     */
    public ArrayList<String> cargarHistorial() {
        ArrayList<String> historial = new ArrayList<>();
        File file = new File(ARCHIVO_HISTORIAL);

        if (!file.exists()) {
            return historial;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                historial.add(linea);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar el historial: " + e.getMessage());
        }
        return historial;
    }
    
}

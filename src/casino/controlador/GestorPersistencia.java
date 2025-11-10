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
    
    private static final String ARCHIVO_HISTORIAL = "historial.txt";

    // Guarda el estado completo de la partida (Serialización)
    public void guardarPartida(PartidaModelo modelo, String archivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(modelo);
        } catch (IOException e) {
            System.err.println("Error al guardar la partida: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "No se pudo guardar la partida.", "Error de Persistencia", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Carga el estado completo de una partida (Serialización)
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

    // Guarda el historial de partidas en un .txt
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

    // Carga el historial de partidas desde un .txt
    public ArrayList<String> cargarHistorial() {
        ArrayList<String> historial = new ArrayList<>();
        File file = new File("historial.txt");

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
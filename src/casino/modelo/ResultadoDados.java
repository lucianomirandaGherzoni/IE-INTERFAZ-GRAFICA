/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.modelo;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author luciano
 */
public class ResultadoDados implements java.io.Serializable {
    
    private final int dado1;
    private final int dado2;
    private final int suma;
    private final List<String> mensajes = new ArrayList<>();

    public ResultadoDados(int dado1, int dado2) {
        this.dado1 = dado1;
        this.dado2 = dado2;
        this.suma = dado1 + dado2;
    }
    
    public void agregarMensaje(String mensaje) {
        mensajes.add(mensaje);
    }

    // Getters
    public int getDado1() { return dado1; }
    public int getDado2() { return dado2; }
    public int getSuma() { return suma; }
    public List<String> getMensajes() { return mensajes; }
    
}

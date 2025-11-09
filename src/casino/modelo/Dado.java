/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.modelo;
import java.util.Random;

public class Dado {  
    //private Random random;
    private final Random random;
    
    public Dado() {
        this.random = new Random();
    }
    
    public int tirar() {
        return random.nextInt(6) + 1; // Número entre 1 y 6
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casinoDados;

import java.util.ArrayList;


class RegistroTrampas {
    private ArrayList <String> trampas;

    public RegistroTrampas() {
        this.trampas = new ArrayList<>();
    }
    
    public void registrarTrampa(String tipoDeTrampa, String victima){
        String registro = tipoDeTrampa + " aplicada a " + victima;
        trampas.add(registro);
    }
    
    public ArrayList<String> obtenerTrampas(){
        return trampas;
    }
    
    public void mostrarTrampas(){
        System.out.println("REGISTRO DE TRAMPAS");
        for(String trampa : trampas){
            System.out.println("- " + trampa);
        }
    }
    
}

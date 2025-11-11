/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.dao;

import casino.modelo.Jugador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BANGHO
 */
public class JugadorDAO {
    public void guardarJugador(Jugador jugador) throws SQLException {
        String sql = "INSERT INTO jugadores (nombre, apodo, tipo_jugador, dinero_total_ganado, victorias_totales) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, jugador.getNombre());
            stmt.setString(2, jugador.getApodo());
            stmt.setString(3, jugador.obtenerTipoJugador());
            stmt.setInt(4, jugador.getDinero());
            stmt.setInt(5, jugador.getPartidasGanadas());
            
            stmt.executeUpdate();
        }
    }
    
    public List<Jugador> obtenerRanking() throws SQLException {
        List<Jugador> ranking = new ArrayList<>();
        String sql = "SELECT * FROM jugadores ORDER BY dinero_total_ganado DESC, victorias_totales DESC";
        
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                // TODO: Reconstruir objetos Jugador desde la BD
                // Necesitarás adaptar según tu modelo
            }
        }
        
        return ranking;
    }
}

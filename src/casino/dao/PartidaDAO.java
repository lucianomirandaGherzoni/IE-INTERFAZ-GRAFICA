/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package casino.dao;

import java.sql.*;
/**
 *
 * @author BANGHO
 */
public class PartidaDAO {
    public void guardarPartida(Integer ganadorId, int rondasJugadas, int pozoFinal) throws SQLException {
        String sql = "INSERT INTO partidas (ganador_id, rondas_jugadas, pozo_final) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (ganadorId != null) {
                stmt.setInt(1, ganadorId);
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setInt(2, rondasJugadas);
            stmt.setInt(3, pozoFinal);
            
            stmt.executeUpdate();
        }
    }
    
    public int contarPartidas() throws SQLException {
        String sql = "SELECT COUNT(*) FROM partidas";
        
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}

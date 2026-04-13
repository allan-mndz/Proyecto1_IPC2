package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.Destino;
import com.mysql.cj.xdevapi.PreparableStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DestinoDAO {
    public boolean insertarDestino(Destino destino){
        String sql = "INSERT INTO Destinos (nombre, pais, descripcion, clima, imagen_url) VALUES (?, ?, ?, ?, ?)";

        try(Connection con = Conexion.getConnection();
            PreparedStatement ps  = con.prepareStatement(sql)) {

            ps.setString(1, destino.getNombre());
            ps.setString(2, destino.getPais());
            ps.setString(3, destino.getDescripcion());
            ps.setString(4, destino.getClima());
            ps.setString(5, destino.getImagen());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        }catch (SQLException e){
            System.out.println("Error al insertar destino: " + e.getMessage());
            return false;
        }
    }

    public List<Destino> obtenerTodosLosDestinos() {
        List<Destino> listaDestinos = new ArrayList<>();

        String sql = "SELECT * FROM Destinos";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Destino d = new Destino();

                // Llenamos el objeto con los datos de la fila actual
                d.setNombre(rs.getString("nombre"));
                d.setPais(rs.getString("pais"));
                d.setDescripcion(rs.getString("descripcion"));
                d.setClima(rs.getString("clima"));
                d.setImagen(rs.getString("imagen_url"));

                listaDestinos.add(d);
            }

        } catch (java.sql.SQLException e) {
            System.out.println("Error al obtener el catálogo de destinos: " + e.getMessage());
        }

        return listaDestinos;
    }

    public boolean eliminarDestino(String nombre) {
        String sql = "DELETE FROM Destinos WHERE nombre = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);

            int filasAfectadas = pstmt.executeUpdate();

            return filasAfectadas > 0;

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarDestino(Destino destino) {
        String sql = "UPDATE Destinos SET pais = ?, descripcion = ?, clima = ?, imagen_url = ? WHERE nombre = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, destino.getPais());
            pstmt.setString(2, destino.getDescripcion());
            pstmt.setString(3, destino.getClima());
            pstmt.setString(4, destino.getImagen());

            pstmt.setString(5, destino.getNombre());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

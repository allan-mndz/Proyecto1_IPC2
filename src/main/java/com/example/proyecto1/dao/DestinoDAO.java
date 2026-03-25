package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.Destino;
import com.mysql.cj.xdevapi.PreparableStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DestinoDAO {
    public boolean insertarDestino(Destino destino){
        String sql = "INSERT INTO Destinos (nombre, pais, descripcion, clima, imagen_url) VALUES (?, ?, ?, ?, ?)";

        try(Connection con = Conexion.getConnection();
            PreparedStatement ps  = con.prepareStatement(sql)) {

            ps.setString(1, destino.getNombre());
            ps.setString(2, destino.getPais());
            ps.setString(3, destino.getDescripcion());
            ps.setString(4, destino.getClima());
            ps.setString(5, destino.getImagenUrl());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        }catch (SQLException e){
            System.out.println("Error al insertar destino: " + e.getMessage());
            return false;
        }
    }

    public java.util.List<com.example.proyecto1.modelos.Destino> obtenerTodosLosDestinos() {
        java.util.List<com.example.proyecto1.modelos.Destino> listaDestinos = new java.util.ArrayList<>();

        String sql = "SELECT * FROM Destinos";

        try (java.sql.Connection con = com.example.proyecto1.config.Conexion.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                com.example.proyecto1.modelos.Destino d = new com.example.proyecto1.modelos.Destino();

                // Llenamos el objeto con los datos de la fila actual
                d.setNombre(rs.getString("nombre"));
                d.setPais(rs.getString("pais"));
                d.setDescripcion(rs.getString("descripcion"));
                d.setClima(rs.getString("clima"));
                d.setImagenUrl(rs.getString("imagen_url"));

                listaDestinos.add(d);
            }

        } catch (java.sql.SQLException e) {
            System.out.println("Error al obtener el catálogo de destinos: " + e.getMessage());
        }

        return listaDestinos;
    }
}

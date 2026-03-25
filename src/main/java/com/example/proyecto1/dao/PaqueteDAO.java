package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.Paquete;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PaqueteDAO {
    public boolean insertarPaquete(Paquete paquete){
        String sql = "INSERT INTO Paquetes (nombre, destino_nombre, duracion, descripcion, precio, capacidad, estado) VALUES (?,?,?,?,?,?,?')";
        try(Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, paquete.getNombre());
            ps.setString(2, paquete.getDestinoNombre());
            ps.setInt(3, paquete.getDuracion());
            ps.setString(4, paquete.getDescripcion());
            ps.setDouble(5, paquete.getPrecio());
            ps.setInt(6, paquete.getCapacidad());
            ps.setInt(7, paquete.getEstado());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (Exception e) {
            System.out.println("Error al insertar paquete: " + e.getMessage());
            return false;
        }
    }

    public java.util.List<com.example.proyecto1.modelos.Paquete> obtenerTodosLosPaquetes() {
        java.util.List<com.example.proyecto1.modelos.Paquete> listaPaquetes = new java.util.ArrayList<>();

        String sql = "SELECT * FROM Paquetes WHERE estado = 1";

        try (java.sql.Connection con = com.example.proyecto1.config.Conexion.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                com.example.proyecto1.modelos.Paquete p = new com.example.proyecto1.modelos.Paquete();

                // Llenamos el objeto con los datos de la fila actual
                p.setNombre(rs.getString("nombre"));
                p.setDestinoNombre(rs.getString("destino_nombre"));
                p.setDuracion(rs.getInt("duracion"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setCapacidad(rs.getInt("capacidad"));
                p.setEstado(rs.getInt("estado"));

                // Agregamos el paquete armado a nuestra caja (lista)
                listaPaquetes.add(p);
            }

        } catch (java.sql.SQLException e) {
            System.out.println("Error al obtener el catálogo de paquetes: " + e.getMessage());
        }

        return listaPaquetes;
    }
}

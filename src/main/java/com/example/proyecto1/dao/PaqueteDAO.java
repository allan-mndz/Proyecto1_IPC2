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
}

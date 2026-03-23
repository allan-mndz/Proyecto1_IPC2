package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.ServicioPaquete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServicioPaqueteDAO {
    public boolean insertarServicioPaquete(ServicioPaquete servicioPaquete){
        String sql = "INSERT INTO Servicio_Paquete (paquete_nombre, proveedor_nombre, descripcion, costo) VALUES (?, ?, ?, ?)";

        try(Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, servicioPaquete.getPaqueteNombre());
            ps.setString(2, servicioPaquete.getProveedorNombre());
            ps.setString(3, servicioPaquete.getDescripcion());
            ps.setDouble(4, servicioPaquete.getCosto());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }catch(SQLException e) {
            System.out.println("Error al insertar servicio de paquete: " + e.getMessage());
            return false;
        }
    }
}

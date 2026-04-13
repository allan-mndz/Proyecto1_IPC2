package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.ServicioPaquete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<ServicioPaquete> obtenerServiciosPorPaquete(String nombreDelPaquete) {
        List<ServicioPaquete> listaServicios = new ArrayList<>();

        String sql = "SELECT * FROM Servicio_Paquete WHERE paquete_nombre = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
             ps.setString(1, nombreDelPaquete);

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    com.example.proyecto1.modelos.ServicioPaquete sp = new com.example.proyecto1.modelos.ServicioPaquete();
                    sp.setPaqueteNombre(rs.getString("paquete_nombre"));
                    sp.setProveedorNombre(rs.getString("proveedor_nombre"));
                    sp.setDescripcion(rs.getString("descripcion"));
                    sp.setCosto(rs.getDouble("costo"));
                    listaServicios.add(sp);
                }
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error al obtener servicios por paquete: " + e.getMessage());
        }

        return listaServicios;
    }
}

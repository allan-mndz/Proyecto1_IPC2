package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.Proveedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProveedorDAO {

    public boolean insertarProveedor(Proveedor proveedor) {
        String sql = "INSERT INTO Proveedores (nombre, tipo, pais, contacto) VALUES (?, ?,?, ?)";
        try(Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, proveedor.getNombre());
            ps.setInt(2, proveedor.getTipo());
            ps.setString(3, proveedor.getPais());
            ps.setString(4, proveedor.getContacto());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        }catch (SQLException e){
            System.out.println("Error al insertar proveedor: " + e.getMessage());
            return false;
        }
    }

    public java.util.List<com.example.proyecto1.modelos.Proveedor> obtenerTodosLosProveedores() {
        java.util.List<Proveedor> listaProveedores = new java.util.ArrayList<>();

        String sql = "SELECT * FROM Proveedores";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Proveedor p = new Proveedor();

                // Llenamos el objeto con los datos de la fila actual
                p.setNombre(rs.getString("nombre"));
                p.setTipo(rs.getInt("tipo"));
                p.setPais(rs.getString("pais"));
                p.setContacto(rs.getString("contacto"));

                listaProveedores.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener el catálogo de proveedores: " + e.getMessage());
        }

        return listaProveedores;
    }
}

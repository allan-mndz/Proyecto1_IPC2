package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.Proveedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Proveedor> obtenerTodosLosProveedores() {
        List<Proveedor> listaProveedores = new ArrayList<>();

        String sql = "SELECT * FROM Proveedores";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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

    public boolean actualizarProveedor(Proveedor proveedor) {
        String sql = "UPDATE Proveedores SET tipo = ?, pais = ?, contacto = ? WHERE nombre = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, proveedor.getTipo());
            pstmt.setString(2, proveedor.getPais());
            pstmt.setString(3, proveedor.getContacto());
            pstmt.setString(4, proveedor.getNombre());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarProveedor(String nombre) {
        String sql = "DELETE FROM Proveedores WHERE nombre = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}

package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public boolean insertarCliente(Cliente cliente) {
        String sql = "INSERT INTO Clientes (dpi, nombre, fecha_nacimiento, telefono, email, nacionalidad) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getDpi());
            ps.setString(2, cliente.getNombre());
            ps.setDate(3, Date.valueOf(cliente.getFechaNacimiento()));
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getEmail());
            ps.setString(6, cliente.getNacionalidad());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        }catch (SQLException e){
            System.out.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }

    public List<Cliente> obtenerTodosLosClientes() {
        List<Cliente> listaClientes = new ArrayList<>();

        String sql = "SELECT * FROM Clientes";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
               Cliente c = new Cliente();

                c.setDpi(rs.getString("dpi"));
                c.setNombre(rs.getString("nombre"));
                java.sql.Date fechaSQL = rs.getDate("fecha_nacimiento");
                if (fechaSQL != null) {
                    c.setFechaNacimiento(fechaSQL.toLocalDate());
                }
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                c.setNacionalidad(rs.getString("nacionalidad"));

                listaClientes.add(c);
            }

        } catch (java.sql.SQLException e) {
            System.out.println("Error al obtener la lista de clientes: " + e.getMessage());
        }

        return listaClientes;
    }

    public Cliente obtenerClientePorDpi(String dpi) {
        String sql = "SELECT * FROM Clientes WHERE dpi = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dpi);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setDpi(rs.getString("dpi"));
                    c.setNombre(rs.getString("nombre"));
                    java.sql.Date fechaSQL = rs.getDate("fecha_nacimiento");
                    if (fechaSQL != null) {
                        c.setFechaNacimiento(fechaSQL.toLocalDate());
                    }
                    c.setTelefono(rs.getString("telefono"));
                    c.setEmail(rs.getString("email"));
                    c.setNacionalidad(rs.getString("nacionalidad"));
                    return c; // Retornamos el cliente si lo encontramos
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener cliente por DPI: " + e.getMessage());
        }
        return null; // Retornamos nulo si no existe
    }

    public boolean actualizarCliente(Cliente cliente) {
        String sql = "UPDATE Clientes SET nombre=?, fecha_nacimiento=?, telefono=?, email=?, nacionalidad=? WHERE dpi=?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setDate(2, java.sql.Date.valueOf(cliente.getFechaNacimiento()));
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getNacionalidad());
            ps.setString(6, cliente.getDpi()); // El DPI va al final para buscarlo

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }
}

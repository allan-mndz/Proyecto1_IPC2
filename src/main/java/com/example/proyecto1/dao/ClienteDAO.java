package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.Cliente;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public java.util.List<com.example.proyecto1.modelos.Cliente> obtenerTodosLosClientes() {
        java.util.List<com.example.proyecto1.modelos.Cliente> listaClientes = new java.util.ArrayList<>();

        String sql = "SELECT * FROM Clientes";

        try (java.sql.Connection con = com.example.proyecto1.config.Conexion.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                com.example.proyecto1.modelos.Cliente c = new com.example.proyecto1.modelos.Cliente();

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
}

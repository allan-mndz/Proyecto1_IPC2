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
}

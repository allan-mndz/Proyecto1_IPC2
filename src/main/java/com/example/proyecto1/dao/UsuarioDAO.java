package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    // metodo para guardar un nuevo usuario en la base de datos

    public boolean insertarUsuario(Usuario usuario) {
        // La consulta SQL con signos de interrogación para evitar inyección SQL
        String sql = "INSERT INTO Usuarios (nombre, password, tipo) VALUES (?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Reemplazamos los signos de interrogación con los datos del objeto Java
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getPassword());
            ps.setInt(3, usuario.getTipo()); // 1: Atención, 2: Operaciones, 3: Admin

            // Ejecutamos la inserción. Si retorna mayor a 0, significa que se insertó la fila.
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    // Metodo para buscar un usuario por su nombre

    public Usuario obtenerUsuario(String nombreUsuario) {
        String sql = "SELECT * FROM Usuarios WHERE nombre = ?";
        Usuario usuario = null;

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);

            // ResultSet guarda la tabla que nos devuelve MySQL
            try (ResultSet rs = ps.executeQuery()) {
                // Si rs.next() es true, significa que sí encontró al usuario
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setTipo(rs.getInt("tipo"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener usuario: " + e.getMessage());
        }

        return usuario;
    }
}

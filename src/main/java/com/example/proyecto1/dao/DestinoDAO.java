package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.Destino;
import com.mysql.cj.xdevapi.PreparableStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DestinoDAO {
    public boolean insertarDestino(Destino destino){
        String sql = "INSERT INTO Destinos (nombre, pais, descripcion, clima, imagen_url) VALUES (?, ?, ?, ?, ?)";

        try(Connection con = Conexion.getConnection();
            PreparedStatement ps  = con.prepareStatement(sql)) {

            ps.setString(1, destino.getNombre());
            ps.setString(2, destino.getPais());
            ps.setString(3, destino.getDescripcion());
            ps.setString(4, destino.getClima());
            ps.setString(5, destino.getImagenUrl());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        }catch (SQLException e){
            System.out.println("Error al insertar destino: " + e.getMessage());
            return false;
        }
    }
}

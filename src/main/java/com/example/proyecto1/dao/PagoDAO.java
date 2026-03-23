package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.Pago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PagoDAO {
    public boolean procesarPago(Pago pago) {
       String sql = "INSERT INTO Pagos (numero_reservacion, monto, metodo, fecha) VALUES (?, ?, ?, ?)";
       try(Connection con = Conexion.getConnection();
           PreparedStatement ps = con.prepareStatement(sql)) {

           ps.setString(1, pago.getNumeroReservacion());
           ps.setDouble(2, pago.getMonto());
           ps.setInt(3, pago.getMetodo());
           ps.setDate(4, java.sql.Date.valueOf(pago.getFecha()));
           int filasAfectadas = ps.executeUpdate();
           return filasAfectadas > 0;

       } catch (SQLException e) {
           System.out.println("Error al procesar el pago: " + e.getMessage());
           return false;
       }
    }
}

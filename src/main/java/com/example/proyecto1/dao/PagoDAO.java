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

    public java.util.List<com.example.proyecto1.modelos.Pago> obtenerPagosPorReservacion(String numeroReservacion) {
        String sql = "SELECT id_pago, numero_reservacion, monto, metodo, fecha FROM Pagos WHERE numero_reservacion = ?";
        java.util.List<com.example.proyecto1.modelos.Pago> pagos = new java.util.ArrayList<>();

        try(Connection con = Conexion.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, numeroReservacion);
            try(java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    com.example.proyecto1.modelos.Pago pago = new com.example.proyecto1.modelos.Pago();
                    pago.setIdPago(rs.getInt("id_pago"));
                    pago.setNumeroReservacion(rs.getString("numero_reservacion"));
                    pago.setMonto(rs.getDouble("monto"));
                    pago.setMetodo(rs.getInt("metodo"));

                    java.sql.Date fechaSQL = rs.getDate("fecha");
                    if (fechaSQL != null) {
                        pago.setFecha(fechaSQL.toLocalDate());
                    }
                    pagos.add(pago);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los pagos: " + e.getMessage());
        }
        return pagos;
    }
}

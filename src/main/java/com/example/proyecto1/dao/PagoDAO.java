package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.Pago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PagoDAO {
    public int procesarPago(Pago pago) {
        String sqlInsert = "INSERT INTO Pagos (numero_reservacion, monto, metodo, fecha) VALUES (?, ?, ?, ?)";
        String sqlCheck = "SELECT r.costo_total, (SELECT SUM(monto) FROM Pagos WHERE numero_reservacion = ?) AS total_pagado FROM Reservaciones r WHERE r.numero_reservacion = ?";
        String sqlUpdate = "UPDATE Reservaciones SET estado = 'Confirmada' WHERE numero_reservacion = ?";

        Connection con = null;
        try {
            con = Conexion.getConnection();
            con.setAutoCommit(false); // Iniciamos la transacción

            // Insertar el pago
            try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                psInsert.setString(1, pago.getNumeroReservacion());
                psInsert.setDouble(2, pago.getMonto());
                psInsert.setInt(3, pago.getMetodo());
                psInsert.setDate(4, java.sql.Date.valueOf(pago.getFecha()));
                psInsert.executeUpdate();
            }

            // Verificar los montos
            boolean confirmada = false;
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
                psCheck.setString(1, pago.getNumeroReservacion());
                psCheck.setString(2, pago.getNumeroReservacion());
                try (java.sql.ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        double costoTotal = rs.getDouble("costo_total");
                        double totalPagado = rs.getDouble("total_pagado");

                        // Si ya cubrió el costo completo, encendemos la bandera
                        if (totalPagado >= costoTotal) {
                            confirmada = true;
                        }
                    }
                }
            }

            // Actualizar el estado a Confirmada si aplica
            if (confirmada) {
                try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {
                    psUpdate.setString(1, pago.getNumeroReservacion());
                    psUpdate.executeUpdate();
                }
            }

            con.commit(); // Confirmar y guardar todos los cambios
            return confirmada ? 2 : 1;

        } catch (SQLException e) {
            System.out.println("Error al procesar el pago: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return 0; // 0 = Error
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
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

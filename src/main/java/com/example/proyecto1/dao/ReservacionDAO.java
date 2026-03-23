package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.Reservacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReservacionDAO {
    public boolean insertarReservacion(Reservacion reservacion){
        String sqlReservacion = "INSERT INTO Reservaciones (numero_reservacion, fecha_creacion, fecha_viaje, paquete_nombre, cantidad_pasajeros, agente_nombre, costo_total, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlPasajero = "INSERT INTO Reservacion_Pasajero (numero_reservacion, dpi_cliente) VALUES (?, ?)";

        Connection con = null;

        try{
            con = Conexion.getConnection();
            con.setAutoCommit(false);

            try(PreparedStatement psRes = con.prepareStatement(sqlReservacion)){
                psRes.setString(1, reservacion.getNumeroReservacion());
                psRes.setDate(2, java.sql.Date.valueOf(reservacion.getFechaCreacion()));
                psRes.setDate(3, java.sql.Date.valueOf(reservacion.getFechaViaje()));
                psRes.setString(4, reservacion.getPaqueteNombre());
                psRes.setInt(5, reservacion.getCantidadPasajeros());
                psRes.setString(6, reservacion.getAgenteNombre());
                psRes.setDouble(7, reservacion.getCostoTotal());
                psRes.setString(8, reservacion.getEstado());

                psRes.executeUpdate();
            }

            java.util.List<String> dpis = reservacion.getDpisPasajeros();

            if (dpis != null && !dpis.isEmpty()) {
                try(PreparedStatement psPas = con.prepareStatement(sqlPasajero)){
                    for (String dpi : dpis) {
                        psPas.setString(1, reservacion.getNumeroReservacion());
                        psPas.setString(2, dpi);
                        psPas.executeUpdate();
                    }
                }
            }

            con.commit();
            return true;
        }catch(SQLException e){
            System.out.println("Error al procesar reservación: " + e.getMessage());

            if (con != null) {
                try {
                    con.rollback();
                    System.out.println("Se cancelo la Transacción de la reservacion.");
                } catch (SQLException ex) {
                   ex.printStackTrace();
                }
            }
            return false;
        }finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

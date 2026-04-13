package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.Reservacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

            List<String> dpis = reservacion.getDpisPasajeros();

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

    public List<Reservacion> obtenerReservaciones(String numeroReservacion){
        List<Reservacion> listaReservaciones = new ArrayList<>();

        String sql = "SELECT * FROM Reservaciones WHERE numero_reservacion = ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, numeroReservacion);

            try(java.sql.ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    Reservacion r = new Reservacion();

                    r.setNumeroReservacion(rs.getString("numero_reservacion"));
                    java.sql.Date fechaCreacionSQL = rs.getDate("fecha_creacion");
                    if(fechaCreacionSQL != null) {
                        r.setFechaCreacion(fechaCreacionSQL.toLocalDate());
                    }

                    java.sql.Date fechaViajeSQL = rs.getDate("fecha_viaje");
                    if(fechaViajeSQL != null) {
                        r.setFechaViaje(fechaViajeSQL.toLocalDate());
                    }

                    r.setPaqueteNombre(rs.getString("paquete_nombre"));
                    r.setCantidadPasajeros(rs.getInt("cantidad_pasajeros"));
                    r.setAgenteNombre(rs.getString("agente_nombre"));
                    r.setCostoTotal(rs.getDouble("costo_total"));
                    r.setEstado(rs.getString("estado"));

                    String sqlPasajeros = "SELECT dpi_cliente FROM Reservacion_Pasajero WHERE numero_reservacion = ?";

                    try (java.sql.PreparedStatement psPasajeros = con.prepareStatement(sqlPasajeros)) {
                        psPasajeros.setString(1, r.getNumeroReservacion());
                        try (java.sql.ResultSet rsPasajeros = psPasajeros.executeQuery()) {
                            java.util.List<String> dpis = new java.util.ArrayList<>();
                            while(rsPasajeros.next()){
                                dpis.add(rsPasajeros.getString("dpi_cliente"));
                            }
                            r.setDpisPasajeros(dpis);
                        }
                    }

                    listaReservaciones.add(r);
                }
            }
        }catch (Exception e){
            System.out.println("Error al obtener las reservaciones: " + e.getMessage());
        }
        return listaReservaciones;
    }
}

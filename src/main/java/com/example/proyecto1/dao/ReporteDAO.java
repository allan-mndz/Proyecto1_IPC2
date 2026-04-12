package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReporteDAO {

    // Esta función convierte CUALQUIER resultado de SQL en una lista fácil de leer para Angular
    private List<Map<String, Object>> convertirAMapa(ResultSet rs) throws Exception {
        List<Map<String, Object>> lista = new ArrayList<>();
        ResultSetMetaData md = rs.getMetaData();
        int columnas = md.getColumnCount();

        while (rs.next()) {
            Map<String, Object> fila = new HashMap<>();
            for (int i = 1; i <= columnas; ++i) {
                fila.put(md.getColumnLabel(i), rs.getObject(i));
            }
            lista.add(fila);
        }
        return lista;
    }

    // REPORTE 1: Ventas en un intervalo de tiempo
    public List<Map<String, Object>> reporteVentas(String fechaInicio, String fechaFin) {
        // Asumo que tu tabla se llama Reservaciones y cruza datos básicos
        String sql = "SELECT numero_reservacion, paquete_nombre, cantidad_pasajeros, agente_nombre, costo_total, fecha_creacion " +
                "FROM Reservaciones " +
                "WHERE fecha_creacion BETWEEN ? AND ?";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {
                return convertirAMapa(rs);
            }
        } catch (Exception e) {
            System.out.println("Error en Reporte 1: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Map<String, Object>> reporteCancelaciones(String inicio, String fin) {
        // Asumimos un 80% de reembolso y 20% de pérdida (ajusta la fórmula según tus reglas de negocio)
        String sql = "SELECT numero_reservacion, fecha_creacion as fecha_cancelacion, " +
                "(costo_total * 0.80) as monto_reembolsado, (costo_total * 0.20) as perdida_agencia " +
                "FROM Reservaciones WHERE estado = 'Cancelada' AND fecha_creacion BETWEEN ? AND ?";
        return ejecutarConsulta(sql, inicio, fin);
    }

    public List<Map<String, Object>> reporteGanancias(String inicio, String fin) {
        String sql = "SELECT SUM(costo_total) as ganancias_brutas, " +
                "SUM(CASE WHEN estado = 'Cancelada' THEN costo_total * 0.80 ELSE 0 END) as total_reembolsos, " +
                "SUM(CASE WHEN estado != 'Cancelada' THEN costo_total ELSE (costo_total * 0.20) END) as ganancia_neta " +
                "FROM Reservaciones WHERE fecha_creacion BETWEEN ? AND ?";
        return ejecutarConsulta(sql, inicio, fin);
    }

    public List<Map<String, Object>> reporteAgenteMasVentas(String inicio, String fin) {
        String sql = "SELECT agente_nombre, numero_reservacion, paquete_nombre, costo_total " +
                "FROM Reservaciones WHERE agente_nombre = (" +
                "  SELECT agente_nombre FROM Reservaciones WHERE fecha_creacion BETWEEN ? AND ? " +
                "  GROUP BY agente_nombre ORDER BY COUNT(*) DESC LIMIT 1" +
                ") AND fecha_creacion BETWEEN ? AND ?";
        return ejecutarConsultaDoble(sql, inicio, fin);
    }

    public List<Map<String, Object>> reporteAgenteMasGanancias(String inicio, String fin) {
        String sql = "SELECT agente_nombre, SUM(costo_total) as total_ganancia " +
                "FROM Reservaciones WHERE fecha_creacion BETWEEN ? AND ? " +
                "GROUP BY agente_nombre ORDER BY total_ganancia DESC LIMIT 1";
        return ejecutarConsulta(sql, inicio, fin);
    }

    public List<Map<String, Object>> reportePaqueteMasVendido(String inicio, String fin) {
        String sql = "SELECT paquete_nombre, numero_reservacion, agente_nombre, cantidad_pasajeros, costo_total " +
                "FROM Reservaciones WHERE paquete_nombre = (" +
                "  SELECT paquete_nombre FROM Reservaciones WHERE fecha_creacion BETWEEN ? AND ? " +
                "  GROUP BY paquete_nombre ORDER BY COUNT(*) DESC LIMIT 1" +
                ") AND fecha_creacion BETWEEN ? AND ?";
        return ejecutarConsultaDoble(sql, inicio, fin);
    }

    public List<Map<String, Object>> reportePaqueteMenosVendido(String inicio, String fin) {
        String sql = "SELECT paquete_nombre, numero_reservacion, agente_nombre, cantidad_pasajeros, costo_total " +
                "FROM Reservaciones WHERE paquete_nombre = (" +
                "  SELECT paquete_nombre FROM Reservaciones WHERE fecha_creacion BETWEEN ? AND ? " +
                "  GROUP BY paquete_nombre ORDER BY COUNT(*) ASC LIMIT 1" +
                ") AND fecha_creacion BETWEEN ? AND ?";
        return ejecutarConsultaDoble(sql, inicio, fin);
    }

    public List<Map<String, Object>> reporteOcupacionDestino(String inicio, String fin) {
        // Cruza Reservaciones con Paquetes para saber el destino
        String sql = "SELECT p.destino_nombre as destino, COUNT(r.numero_reservacion) as total_reservaciones, SUM(r.cantidad_pasajeros) as total_pasajeros " +
                "FROM Reservaciones r JOIN Paquetes p ON r.paquete_nombre = p.nombre " +
                "WHERE r.fecha_creacion BETWEEN ? AND ? GROUP BY p.destino_nombre";
        return ejecutarConsulta(sql, inicio, fin);
    }

    private List<Map<String, Object>> ejecutarConsulta(String sql, String inicio, String fin) {
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, inicio); ps.setString(2, fin);
            try (ResultSet rs = ps.executeQuery()) { return convertirAMapa(rs); }
        } catch (Exception e) { System.out.println("Error Reporte: " + e.getMessage()); return new ArrayList<>(); }
    }

    private List<Map<String, Object>> ejecutarConsultaDoble(String sql, String inicio, String fin) {
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, inicio); ps.setString(2, fin); ps.setString(3, inicio); ps.setString(4, fin);
            try (ResultSet rs = ps.executeQuery()) { return convertirAMapa(rs); }
        } catch (Exception e) { System.out.println("Error Reporte: " + e.getMessage()); return new ArrayList<>(); }
    }
}
package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.Paquete;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PaqueteDAO {

    public boolean insertarPaqueteConServicios(com.example.proyecto1.modelos.Paquete paquete) {
        String sqlPaquete = "INSERT INTO Paquetes (nombre, destino_nombre, duracion, descripcion, precio, capacidad, estado) VALUES (?,?,?,?,?,?,?)";
        String sqlServicio = "INSERT INTO Servicio_Paquete (paquete_nombre, proveedor_nombre, descripcion, costo) VALUES (?,?,?,?)";

        java.sql.Connection conn = null;

        try {
            conn = com.example.proyecto1.config.Conexion.getConnection();

            // INICIAMOS LA TRANSACCIÓN
            // Esto le dice a MySQL que no guarde nada definitivamente hasta que le demos permiso.
            conn.setAutoCommit(false);

            // GUARDAMOS EL PAQUETE PRINCIPAL
            try (java.sql.PreparedStatement psPaquete = conn.prepareStatement(sqlPaquete)) {
                psPaquete.setString(1, paquete.getNombre());
                psPaquete.setString(2, paquete.getDestinoNombre());
                psPaquete.setInt(3, paquete.getDuracion());
                psPaquete.setString(4, paquete.getDescripcion());
                psPaquete.setDouble(5, paquete.getPrecio());
                psPaquete.setInt(6, paquete.getCapacidad());
                psPaquete.setInt(7, 1); // Asumimos que 1 es Activo

                psPaquete.executeUpdate();
            }

            // GUARDAMOS TODOS LOS SERVICIOS DE LA LISTA
            if (paquete.getServicios() != null && !paquete.getServicios().isEmpty()) {
                try (java.sql.PreparedStatement psServicio = conn.prepareStatement(sqlServicio)) {
                    for (com.example.proyecto1.modelos.ServicioPaquete servicio : paquete.getServicios()) {
                        psServicio.setString(1, paquete.getNombre()); // Lo enlazamos al paquete
                        psServicio.setString(2, servicio.getProveedorNombre());
                        psServicio.setString(3, servicio.getDescripcion());
                        psServicio.setDouble(4, servicio.getCosto());
                        psServicio.executeUpdate();
                    }
                }
            }

            // CONFIRMAMOS LA TRANSACCIÓN
            // le decimos a MySQL que salio todo bien, que ahora sí guarde todo definitivamente.
            conn.commit();
            return true;

        } catch (java.sql.SQLException e) {
            if (conn != null) {
                try {
                    // hacemos un ROLLBACK. Esto deshace todo, como si nada hubiera pasado.
                    conn.rollback();
                } catch (java.sql.SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("Error en transacción al insertar paquete: " + e.getMessage());
            return false;
        } finally {
            // devolvemos la conexión a su estado normal antes de cerrarla
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public java.util.List<com.example.proyecto1.modelos.Paquete> obtenerTodosLosPaquetes() {
        java.util.List<com.example.proyecto1.modelos.Paquete> listaPaquetes = new java.util.ArrayList<>();

        String sql = "SELECT * FROM Paquetes WHERE estado = 1";

        try (java.sql.Connection con = com.example.proyecto1.config.Conexion.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                com.example.proyecto1.modelos.Paquete p = new com.example.proyecto1.modelos.Paquete();

                // Llenamos el objeto con los datos de la fila actual
                p.setNombre(rs.getString("nombre"));
                p.setDestinoNombre(rs.getString("destino_nombre"));
                p.setDuracion(rs.getInt("duracion"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio"));
                p.setCapacidad(rs.getInt("capacidad"));
                p.setEstado(rs.getInt("estado"));

                // Agregamos el paquete armado a nuestra lista de paquetes
                listaPaquetes.add(p);
            }

        } catch (java.sql.SQLException e) {
            System.out.println("Error al obtener el catálogo de paquetes: " + e.getMessage());
        }

        return listaPaquetes;
    }
}

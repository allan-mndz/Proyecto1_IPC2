package com.example.proyecto1.dao;

import com.example.proyecto1.config.Conexion;
import com.example.proyecto1.modelos.Paquete;
import com.example.proyecto1.modelos.ServicioPaquete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PaqueteDAO {

    public boolean insertarPaqueteConServicios(Paquete paquete) {
        String sqlPaquete = "INSERT INTO Paquetes (nombre, destino_nombre, duracion, descripcion, precio, capacidad, estado) VALUES (?,?,?,?,?,?,?)";
        String sqlServicio = "INSERT INTO Servicio_Paquete (paquete_nombre, proveedor_nombre, descripcion, costo) VALUES (?,?,?,?)";

        Connection conn = null;

        try {
            conn = Conexion.getConnection();

            // Esto hace que MySQL no guarde nada hasta que le demos permiso.
            conn.setAutoCommit(false);

            // GUARDAMOS EL PAQUETE PRINCIPAL
            try (PreparedStatement psPaquete = conn.prepareStatement(sqlPaquete)) {
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
                try (PreparedStatement psServicio = conn.prepareStatement(sqlServicio)) {
                    for (ServicioPaquete servicio : paquete.getServicios()) {
                        psServicio.setString(1, paquete.getNombre()); // Lo enlazamos al paquete
                        psServicio.setString(2, servicio.getProveedorNombre());
                        psServicio.setString(3, servicio.getDescripcion());
                        psServicio.setDouble(4, servicio.getCosto());
                        psServicio.executeUpdate();
                    }
                }
            }

            // confirmamos la transaccion
            conn.commit();
            return true;

        } catch (java.sql.SQLException e) {
            if (conn != null) {
                try {
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

    public List<Paquete> obtenerTodosLosPaquetes() {
       List<Paquete> listaPaquetes = new ArrayList<>();

        String sql = "SELECT * FROM Paquetes WHERE estado = 1";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paquete p = new Paquete();

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

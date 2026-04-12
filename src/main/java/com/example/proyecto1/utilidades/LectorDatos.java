package com.example.proyecto1.utilidades;

import com.example.proyecto1.dao.*;
import com.example.proyecto1.modelos.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class LectorDatos {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private DestinoDAO destinoDAO = new DestinoDAO();
    private PagoDAO pagoDAO = new PagoDAO();
    private PaqueteDAO paqueteDAO = new PaqueteDAO();
    private ProveedorDAO proveedorDAO = new ProveedorDAO();
    private ReservacionDAO reservacionDAO = new ReservacionDAO();
    private ServicioPaqueteDAO servicioPaqueteDAO = new ServicioPaqueteDAO();

    private DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private int contadorRes = 1;

    public void procesarArchivo(InputStream archivo){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(archivo, "UTF-8"))){
            String linea;

            while((linea = br.readLine()) != null){
                linea = linea.trim();
                if(linea.isEmpty()){
                    continue;
                }

                if (linea.startsWith("USUARIO(")) {
                    procesarUsuario(linea);
                } else if (linea.startsWith("CLIENTE(")) {
                    procesarCliente(linea);
                } else if (linea.startsWith("DESTINO(")) {
                    procesarDestino(linea);
                } else if (linea.startsWith("PAGO(")) {
                    procesarPago(linea);
                } else if (linea.startsWith("PAQUETE(")) {
                    procesarPaquete(linea);
                } else if (linea.startsWith("PROVEEDOR(")) {
                    procesarProveedor(linea);
                } else if (linea.startsWith("RESERVACION(")) {
                    procesarReservacion(linea);
                } else if (linea.startsWith("SERVICIO_PAQUETE(")) {
                    procesarServicioPaquete(linea);
                } else {
                    System.out.println("Línea ignorada o no reconocida: " + linea);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al procesar el archivo: " + e.getMessage());
        }
    }

    private void procesarUsuario(String linea) {
        try {
            String[] datos = extraerDatos(linea);
            Usuario u = new Usuario(limpiar(datos[0]), limpiar(datos[1]), Integer.parseInt(limpiar(datos[2])));
            usuarioDAO.insertarUsuario(u);
        } catch (Exception e) {
            System.out.println("Error al procesar USUARIO: " + linea);
        }
    }

    private void procesarCliente(String linea){
        try{
            String[] datos = extraerDatos(linea);
            Cliente c = new Cliente();
            c.setDpi(limpiar(datos[0]));
            c.setNombre(limpiar(datos[1]));
            c.setFechaNacimiento(LocalDate.parse(limpiar(datos[2]), formatoFecha));
            c.setTelefono(limpiar(datos[3]));
            c.setEmail(limpiar(datos[4]));
            c.setNacionalidad(limpiar(datos[5]));
            clienteDAO.insertarCliente(c);
        }catch (Exception e){
            System.out.println("Error al procesar CLIENTE: " + linea);
        }
    }

    private void procesarDestino(String linea){
        try {
            String[] datos = extraerDatos(linea);
            Destino d = new Destino();
            d.setNombre(limpiar(datos[0]));
            d.setPais(limpiar(datos[1]));
            d.setDescripcion(limpiar(datos[2]));
            d.setClima("ND");
            d.setImagen("ND");
            destinoDAO.insertarDestino(d);
        }catch (Exception e){
            System.out.println("Error al procesar DESTINO: " + linea);
        }
    }

    private void procesarProveedor(String linea){
        try{
            String[] datos = extraerDatos(linea);
            Proveedor p = new Proveedor();
            p.setNombre(limpiar(datos[0]));
            p.setTipo(Integer.parseInt(limpiar(datos[1])));
            p.setPais(limpiar(datos[2]));
            p.setContacto("ND");
            proveedorDAO.insertarProveedor(p);
        }catch (Exception e){
            System.out.println("Error al procesar PROVEEDORE: " + linea);
        }
    }

    private void procesarPaquete(String linea) {
        try {
            String[] datos = extraerDatos(linea);
            Paquete p = new Paquete();
            p.setNombre(limpiar(datos[0]));
            p.setDestinoNombre(limpiar(datos[1]));
            p.setDuracion(Integer.parseInt(limpiar(datos[2])));
            p.setPrecio(Double.parseDouble(limpiar(datos[3])));
            p.setCapacidad(Integer.parseInt(limpiar(datos[4])));
            p.setDescripcion("ND");
            p.setEstado(1); // 1 = Activo por defecto
            paqueteDAO.insertarPaqueteConServicios(p);
        } catch (Exception e) { System.out.println("Error Formato PAQUETE: " + linea); }
    }

    private void procesarServicioPaquete(String linea) {
        try {
            String[] datos = extraerDatos(linea);
            ServicioPaquete sp = new ServicioPaquete();
            sp.setPaqueteNombre(limpiar(datos[0]));
            sp.setProveedorNombre(limpiar(datos[1]));
            sp.setDescripcion(limpiar(datos[2]));
            sp.setCosto(Double.parseDouble(limpiar(datos[3])));
            servicioPaqueteDAO.insertarServicioPaquete(sp);
        } catch (Exception e) { System.out.println("Error Formato SERVICIO_PAQUETE: " + linea); }
    }

    private void procesarReservacion(String linea) {
        try {
            String[] datos = extraerDatos(linea);
            Reservacion r = new Reservacion();

            r.setNumeroReservacion(String.format("RES-%05d", contadorRes++));
            r.setPaqueteNombre(limpiar(datos[0]));
            r.setAgenteNombre(limpiar(datos[1]));
            r.setFechaViaje(LocalDate.parse(limpiar(datos[2]), formatoFecha));
            r.setFechaCreacion(LocalDate.now());
            r.setEstado("Pendiente");
            r.setCostoTotal(0.0);

            String pasajerosSueltos = limpiar(datos[3]);
            List<String> listaDpis = Arrays.asList(pasajerosSueltos.split("[|/]"));
            r.setDpisPasajeros(listaDpis);
            r.setCantidadPasajeros(listaDpis.size());

            reservacionDAO.insertarReservacion(r);


        } catch (Exception e) {
            System.out.println("Error Formato RESERVACIONE: " + linea);
        }
    }

    private void procesarPago(String linea) {
        try {
            String[] datos = extraerDatos(linea);
            Pago p = new Pago();
            p.setNumeroReservacion(limpiar(datos[0]));
            p.setMonto(Double.parseDouble(limpiar(datos[1])));
            p.setMetodo(Integer.parseInt(limpiar(datos[2])));
            p.setFecha(LocalDate.parse(limpiar(datos[3]), formatoFecha));
            p.setIdPago(0);
            p.setMetodo(p.getMetodo());
            pagoDAO.procesarPago(p);
        } catch (Exception e) { System.out.println("Error Formato PAGO: " + linea); }
    }


    private String[] extraerDatos(String linea) {
        int inicio = linea.indexOf("(") + 1;
        int fin = linea.lastIndexOf(")");
        String contenido = linea.substring(inicio, fin);
        return contenido.split(",");
    }

    private String limpiar(String dato) {
        return dato.replace("\"", "").trim();
    }
}

package modelos;

import java.time.LocalDate;
import java.util.List;

public class Reservacion {
    private String numeroReservacion;
    private LocalDate fechaCreacion;
    private LocalDate fechaViaje;
    private String paqueteNombre;
    private int cantidadPasajeros;
    private String agenteNombre;
    private double costoTotal;
    private String estado;
    private List<String> dpisPasajeros; // Para manejar los múltiples clientes

    public Reservacion() {
    }

    public Reservacion(String numeroReservacion, LocalDate fechaCreacion, LocalDate fechaViaje, String paqueteNombre, int cantidadPasajeros, String agenteNombre, double costoTotal, String estado, List<String> dpisPasajeros) {
        this.numeroReservacion = numeroReservacion;
        this.fechaCreacion = fechaCreacion;
        this.fechaViaje = fechaViaje;
        this.paqueteNombre = paqueteNombre;
        this.cantidadPasajeros = cantidadPasajeros;
        this.agenteNombre = agenteNombre;
        this.costoTotal = costoTotal;
        this.estado = estado;
        this.dpisPasajeros = dpisPasajeros;
    }

    public String getNumeroReservacion() {
        return numeroReservacion;
    }

    public void setNumeroReservacion(String numeroReservacion) {
        this.numeroReservacion = numeroReservacion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaViaje() {
        return fechaViaje;
    }

    public void setFechaViaje(LocalDate fechaViaje) {
        this.fechaViaje = fechaViaje;
    }

    public String getPaqueteNombre() {
        return paqueteNombre;
    }

    public void setPaqueteNombre(String paqueteNombre) {
        this.paqueteNombre = paqueteNombre;
    }

    public int getCantidadPasajeros() {
        return cantidadPasajeros;
    }

    public void setCantidadPasajeros(int cantidadPasajeros) {
        this.cantidadPasajeros = cantidadPasajeros;
    }

    public String getAgenteNombre() {
        return agenteNombre;
    }

    public void setAgenteNombre(String agenteNombre) {
        this.agenteNombre = agenteNombre;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<String> getDpisPasajeros() {
        return dpisPasajeros;
    }

    public void setDpisPasajeros(List<String> dpisPasajeros) {
        this.dpisPasajeros = dpisPasajeros;
    }
}

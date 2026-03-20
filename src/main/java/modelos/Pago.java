package modelos;

import java.time.LocalDate;

public class Pago {
    private int idPago;
    private String numeroReservacion;
    private double monto;
    private int metodo; // 1: Efectivo, 2: Tarjeta, 3: Transferencia
    private LocalDate fecha;

    public Pago() {
    }

    public Pago(int idPago, String numeroReservacion, double monto, int metodo, LocalDate fecha) {
        this.idPago = idPago;
        this.numeroReservacion = numeroReservacion;
        this.monto = monto;
        this.metodo = metodo;
        this.fecha = fecha;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public String getNumeroReservacion() {
        return numeroReservacion;
    }

    public void setNumeroReservacion(String numeroReservacion) {
        this.numeroReservacion = numeroReservacion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getMetodo() {
        return metodo;
    }

    public void setMetodo(int metodo) {
        this.metodo = metodo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}

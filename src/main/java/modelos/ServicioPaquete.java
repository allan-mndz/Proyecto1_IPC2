package modelos;

public class ServicioPaquete {
    private int idServicio;
    private String paqueteNombre;
    private String proveedorNombre;
    private String descripcion;
    private double costo;

    public ServicioPaquete() {
    }

    public ServicioPaquete(int idServicio, String paqueteNombre, String proveedorNombre, String descripcion, double costo) {
        this.idServicio = idServicio;
        this.paqueteNombre = paqueteNombre;
        this.proveedorNombre = proveedorNombre;
        this.descripcion = descripcion;
        this.costo = costo;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public String getPaqueteNombre() {
        return paqueteNombre;
    }

    public void setPaqueteNombre(String paqueteNombre) {
        this.paqueteNombre = paqueteNombre;
    }

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }
}

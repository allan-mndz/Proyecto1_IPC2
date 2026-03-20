package modelos;

public class Paquete {
    private String nombre;
    private String destinoNombre;
    private int duracion;
    private String descripcion;
    private double precio;
    private int capacidad;
    private int estado; // 1 = Activo, 0 = Inactivo

    public Paquete() {
    }

    public Paquete(String nombre, String destinoNombre, int duracion, String descripcion, double precio, int capacidad, int estado) {
        this.nombre = nombre;
        this.destinoNombre = destinoNombre;
        this.duracion = duracion;
        this.descripcion = descripcion;
        this.precio = precio;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDestinoNombre() {
        return destinoNombre;
    }

    public void setDestinoNombre(String destinoNombre) {
        this.destinoNombre = destinoNombre;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}

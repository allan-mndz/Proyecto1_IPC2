package modelos;

public class Proveedor {
    private String nombre;
    private int tipo; // 1: Aerolínea, 2: Hotel, 3: Tour, 4: Traslado, 5: Otro [cite: 209]
    private String pais;
    private String contacto;

    public Proveedor() {
    }

    public Proveedor(String nombre, int tipo, String pais, String contacto) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.pais = pais;
        this.contacto = contacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
}

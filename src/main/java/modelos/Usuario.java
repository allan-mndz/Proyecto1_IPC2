package modelos;

public class Usuario {
    private String nombre;
    private String password;
    private int tipo;

    public Usuario(){

    }

    public Usuario(String nombre, String password, int tipo) {
        this.nombre = nombre;
        this.password = password;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}

package com.example.proyecto1.modelos;

public class Destino {
    private String nombre;
    private String pais;
    private String descripcion;
    private String clima;
    private String imagen;

    public Destino() {
    }

    public Destino(String nombre, String pais, String descripcion, String clima, String imagen) {
        this.nombre = nombre;
        this.pais = pais;
        this.descripcion = descripcion;
        this.clima = clima;
        this.imagen= imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagenUrl) {
        this.imagen = imagenUrl;
    }
}

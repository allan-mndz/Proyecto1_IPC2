package com.example.proyecto1.modelos;

import java.time.LocalDate;

public class Cliente {
    private String dpi;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String email;
    private String nacionalidad;

    public Cliente() {
    }

    public Cliente(String dpi, String nombre, LocalDate fechaNacimiento, String telefono, String email, String nacionalidad) {
        this.dpi = dpi;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.nacionalidad = nacionalidad;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
}

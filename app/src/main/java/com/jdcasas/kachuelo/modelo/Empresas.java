package com.jdcasas.kachuelo.modelo;

public class Empresas {
    private String nombre;
    private String descripcion;
    private String celular;
    private String coorx;
    private String coory;

    public Empresas(String nombre, String descripcion, String celular, String coorx, String coory, String imagenEmpresas) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.celular = celular;
        this.coorx = coorx;
        this.coory = coory;
        this.imagenEmpresas = imagenEmpresas;
    }

    private String imagenEmpresas;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCoorx() {
        return coorx;
    }

    public void setCoorx(String coorx) {
        this.coorx = coorx;
    }

    public String getCoory() {
        return coory;
    }

    public void setCoory(String coory) {
        this.coory = coory;
    }

    public String getImagenEmpresas() {
        return imagenEmpresas;
    }

    public void setImagenEmpresas(String imagenEmpresas) {
        this.imagenEmpresas = imagenEmpresas;
    }
}

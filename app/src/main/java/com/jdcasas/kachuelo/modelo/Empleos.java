package com.jdcasas.kachuelo.modelo;
public class Empleos {
    private String fecha;
    private String descripcion;
    private String sueldo;
    private String contacto;
    private String imagenEmpleos;
    public Empleos(String fecha, String contacto, String sueldo, String descripcion, String imagenEmpleos) {
        this.fecha = fecha;
        this.contacto = contacto;
        this.sueldo = sueldo;
        this.descripcion = descripcion;
        this.imagenEmpleos = imagenEmpleos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSueldo() {
        return sueldo;
    }

    public void setSueldo(String sueldo) {
        this.sueldo = sueldo;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getImagenEmpleos() {
        return imagenEmpleos;
    }

    public void setImagenEmpleos(String imagenEmpleos) {
        this.imagenEmpleos = imagenEmpleos;
    }
}



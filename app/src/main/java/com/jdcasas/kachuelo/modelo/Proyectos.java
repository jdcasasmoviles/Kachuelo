package com.jdcasas.kachuelo.modelo;
import android.content.Context;
import com.jdcasas.kachuelo.vista.DialogKachuelo;
public class Proyectos {
    private String nombreProyecto;
    private String descripcion;
    private String imagen1;
    private String imagen2;
    private String creadoPor;//id del usuario
    Context context;
    DialogKachuelo dialogo;

    public Proyectos(String nombreProyecto,String descripcion,String creadoPor, String imagen1, String imagen2) {
        this.nombreProyecto = nombreProyecto;
        this.descripcion = descripcion;
        this.creadoPor = creadoPor;
        this.imagen1 = imagen1;
        this.imagen2 = imagen2;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen1() {
        return imagen1;
    }

    public void setImagen1(String imagen1) {
        this.imagen1 = imagen1;
    }

    public String getImagen2() {
        return imagen2;
    }

    public void setImagen2(String imagen2) {
        this.imagen2 = imagen2;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }
}

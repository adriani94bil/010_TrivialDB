package com.trivialdb.model;

public class Pregunta {
    public long id;
    public String descripcion;
    public boolean isCorrecto;
    public String mensaje;

    public Pregunta(long id, String descripcion, boolean isCorrecto, String mensaje) {
        this.id = id;
        this.descripcion = descripcion;
        this.isCorrecto = isCorrecto;
        this.mensaje = mensaje;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isCorrecto() {
        return isCorrecto;
    }

    public void setCorrecto(boolean correcto) {
        isCorrecto = correcto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}

package com.example.lab_gsi.Dominio;

import android.net.Uri;

import java.io.Serializable;

public class Recomendacion implements Serializable {
    private String nombre;
    private String comentario;
    private String latitud;
    private String longitud;
    private String imagenBit;
    private String tipo; //0=restaurante, 1=lugar


    public Recomendacion(String nombre, String comentario, String latitud, String longitud, String imagenBit, String tipo) {
        this.nombre = nombre;

        this.comentario = comentario;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagenBit = imagenBit;
        this.tipo = tipo;
    }

    public Recomendacion(){
        this.nombre = "";
        this.comentario = "";
        this.latitud = "";
        this.longitud = "";
        this.imagenBit = "";
        this.tipo = "";
    }


    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getImagenBit() {
        return imagenBit;
    }

    public void setImagenBit(String imagenBit) {
        this.imagenBit = imagenBit;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

   /* @Override
    public String toString() {
        return "Recomendacion{" +
                "nombreTarjeta='" + nombre + '\'' +
                ", comentario='" + comentario + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                ", imagenBit='" + imagenBit + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }*/
}

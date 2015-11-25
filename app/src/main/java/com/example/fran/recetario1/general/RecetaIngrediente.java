package com.example.fran.recetario1.general;

/**
 * Created by Carmen on 12/11/2015.
 */
public class RecetaIngrediente {

    private int idRI;
    private int idReceta;
    private int idIngrediente;
    private String cantidad;

    public RecetaIngrediente(/*int idRI, */int idReceta, int idIngrediente, String cantidad) {
//        this.idRI = idRI;
        this.idReceta = idReceta;
        this.idIngrediente = idIngrediente;
        this.cantidad = cantidad;
    }

    public RecetaIngrediente(){

    }

    public int getIdRI() {
        return idRI;
    }

    public void setIdRI(int idRI) {
        this.idRI = idRI;
    }

    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "RecetaIngrediente{" +
                "idRI=" + idRI +
                ", idReceta=" + idReceta +
                ", idIngrediente=" + idIngrediente +
                ", cantidad='" + cantidad + '\'' +
                '}';
    }
}

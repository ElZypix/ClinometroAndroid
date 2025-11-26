package com.ElZypix.myapplication;

public class Logica_registros {
    private String Nombre;
    private double distancia;
    private double angulo;
    private double alturaOjos;
    private double alturaTotal;

    public Logica_registros(String nombre, double distancia, double angulo, double alturaOjos, double alturaTotal) {
        this.Nombre = nombre;
        this.distancia = distancia;
        this.angulo = angulo;
        this.alturaOjos = alturaOjos;
        this.alturaTotal = alturaTotal;
    }

    public String getNombre() {
        return Nombre;
    }

    public double getDistancia() {
        return distancia;
    }

    public double getAngulo() {
        return angulo;
    }

    public double getAlturaTotal() {
        return alturaTotal;
    }

    public double getAlturaOjos() {
        return alturaOjos;
    }
}

package com.example.recetascocina.dto;

import java.io.Serializable;

public class PlatoIngredientes implements Serializable {
    private int cantidadComensales;
    private float cantidadIngrediente;
    private String createdAt;
    private String updatedAt;
    private IngredienteJson idIngrediente;
    private Plato idPlato;

    public IngredienteJson getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(IngredienteJson idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public Plato getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(Plato idPlato) {
        this.idPlato = idPlato;
    }

    public int getCantidadComensales() {
        return cantidadComensales;
    }

    public void setCantidadComensales(int cantidadComensales) {
        this.cantidadComensales = cantidadComensales;
    }

    public float getCantidadIngrediente() {
        return cantidadIngrediente;
    }

    public void setCantidadIngrediente(float cantidadIngrediente) {
        this.cantidadIngrediente = cantidadIngrediente;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


}

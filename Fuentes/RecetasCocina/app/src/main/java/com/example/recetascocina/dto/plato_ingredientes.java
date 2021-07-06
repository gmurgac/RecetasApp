package com.example.recetascocina.dto;

import java.io.Serializable;

public class plato_ingredientes implements Serializable {
    private int cantidadComensales;
    private float cantidadIngrediente;
    private String createdAt;
    private String updatedAt;
    private int ingredienteId;
    private int platoId;

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

    public int getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(int ingredienteId) {
        this.ingredienteId = ingredienteId;
    }

    public int getPlatoId() {
        return platoId;
    }

    public void setPlatoId(int platoId) {
        this.platoId = platoId;
    }
}

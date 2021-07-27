package com.example.recetascocina.dto;

import java.io.Serializable;

public class TragoIngredientes implements Serializable {
    private float cantidadIngrediente;
    private String createdAt;
    private String updatedAt;
    private int ingredienteId;
    private int tragoId;

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

    public int getTragoId() {
        return tragoId;
    }

    public void setTragoId(int tragoId) {
        this.tragoId = tragoId;
    }
}

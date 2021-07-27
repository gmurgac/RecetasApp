package com.example.recetascocina.dto;

import java.io.Serializable;

public class IngredienteJson implements Serializable{
    private int ingredienteId;
    private String nombre;
    private String unidadMedida;
    private String createdAt;
    private String updatedAt;
    private PlatoIngredientes plato_ingredientes;
    private TragoIngredientes trago_ingredientes;

    public PlatoIngredientes getPlato_ingredientes() {
        return plato_ingredientes;
    }

    public void setPlato_ingredientes(PlatoIngredientes plato_ingredientes) {
        this.plato_ingredientes = plato_ingredientes;
    }

    public TragoIngredientes getTrago_ingredientes() {
        return trago_ingredientes;
    }

    public void setTrago_ingredientes(TragoIngredientes trago_ingredientes) {
        this.trago_ingredientes = trago_ingredientes;
    }

    @Override
    public String toString() {
        return "" + nombre;
    }

    public int getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(int ingredienteId) {
        this.ingredienteId = ingredienteId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
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

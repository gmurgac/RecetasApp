package com.example.recetascocina.dto;

import java.io.Serializable;
import java.sql.Date;

public class IngredienteJson implements Serializable{
    private int ingredienteId;
    private String nombre;
    private String unidadMedida;
    private String createdAt;
    private String updatedAt;
    private plato_ingredientes plato_ingredientes;
    private trago_ingredientes trago_ingredientes;

    public com.example.recetascocina.dto.trago_ingredientes getTrago_ingredientes() {
        return trago_ingredientes;
    }

    public void setTrago_ingredientes(com.example.recetascocina.dto.trago_ingredientes trago_ingredientes) {
        this.trago_ingredientes = trago_ingredientes;
    }

    public plato_ingredientes getPlatoIngredientes() {
        return plato_ingredientes;
    }

    public void setPlatoIngredientes(plato_ingredientes platoIngredientes) {
        this.plato_ingredientes = platoIngredientes;
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

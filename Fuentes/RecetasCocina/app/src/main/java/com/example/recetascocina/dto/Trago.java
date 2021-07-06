package com.example.recetascocina.dto;

import java.io.Serializable;
import java.util.List;

public class Trago implements Serializable {
    private int tragoId;
    private String nombre;
    private String descripcion;
    private String tipo;
    private String createdAt;
    private String updatedAt;
    private List<IngredienteJson> ingredientes;

    public int getTragoId() {
        return tragoId;
    }

    public void setTragoId(int tragoId) {
        this.tragoId = tragoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public List<IngredienteJson> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<IngredienteJson> ingredientes) {
        this.ingredientes = ingredientes;
    }
}

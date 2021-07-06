package com.example.recetascocina.dao;

import com.example.recetascocina.dto.Ingrediente;

import java.util.List;


//Interfaz DAO para ingredientes
public interface IngredientesDAO {
    Ingrediente save(Ingrediente ingrediente);
    List<Ingrediente> getAll();
    Ingrediente erase(Ingrediente ingrediente);
    Ingrediente modify(Ingrediente ingrediente);
    boolean findIngrediente(String nombre);
}

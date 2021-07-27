package com.example.recetascocina.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.recetascocina.R;
import com.example.recetascocina.dao.IngredientesDAO;
import com.example.recetascocina.dao.IngredientesDAOSqLite;
import com.example.recetascocina.dto.Ingrediente;
import com.example.recetascocina.dto.IngredienteJson;

import java.util.List;

//Clase adaptador de listview de ingredientes de plato
public class IngredientesPlatoListAdapter extends ArrayAdapter<IngredienteJson> {

    private List<IngredienteJson> ingredientesJsons;
    private Activity contexto;



        //Constructor
    public IngredientesPlatoListAdapter(@NonNull Activity context, int resource, @NonNull List<IngredienteJson> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.ingredientesJsons = objects;
    }

    //Metodo para llenar cada item de listview
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        IngredientesDAO stockDAO = new IngredientesDAOSqLite(this.contexto);
        List<Ingrediente> ingredientesStock = stockDAO.getAll();


        LayoutInflater inflater = this.contexto.getLayoutInflater();
        View fila = inflater.inflate(R.layout.ingredientes_plato_list,null,true);
        TextView nombreTv = fila.findViewById(R.id.nombre_ingrediente_pl);
        TextView cantidadTv = fila.findViewById(R.id.cantidad_ingrediente_plato_tv);
        TextView cantidadStockTv = fila.findViewById(R.id.cantidad_ingrediente_stock_tv);
        IngredienteJson actual = ingredientesJsons.get(position);
        nombreTv.setText(actual.getNombre());
        cantidadTv.setText(""+actual.getPlato_ingredientes().getCantidadIngrediente()+" "+actual.getUnidadMedida());
        float cantidadEnStock = 0;
        for (Ingrediente i: ingredientesStock
             ) {
            if(i.getNombre().equalsIgnoreCase(actual.getNombre())){
                cantidadEnStock = i.getCantidad();
            }
        }
        if(cantidadEnStock < actual.getPlato_ingredientes().getCantidadIngrediente()){
            cantidadStockTv.setBackgroundResource(R.color.red_pokeball);
        }else if(cantidadEnStock == actual.getPlato_ingredientes().getCantidadIngrediente()){
            cantidadStockTv.setBackgroundResource(R.color.caution);
        }else{
            cantidadStockTv.setBackgroundResource(R.color.pass);
        }
        cantidadStockTv.setText("Tienes "+cantidadEnStock+" "+actual.getUnidadMedida());





        return fila;
    }
}


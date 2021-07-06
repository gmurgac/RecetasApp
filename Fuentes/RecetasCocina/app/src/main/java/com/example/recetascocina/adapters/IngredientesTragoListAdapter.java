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

public class IngredientesTragoListAdapter extends ArrayAdapter<IngredienteJson> {

    private Activity context;
    private List<IngredienteJson> ingredientesJson;

    public IngredientesTragoListAdapter(@NonNull Activity context, int resource, @NonNull List<IngredienteJson> objects) {
        super(context, resource, objects);
        this.context = context;
        this.ingredientesJson = objects;
    }

    //Metodo para llenar cada item de listview
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        IngredientesDAO stockDAO = new IngredientesDAOSqLite(this.context);
        List<Ingrediente> stock = stockDAO.getAll();

        LayoutInflater inflater = this.context.getLayoutInflater();
        View fila = inflater.inflate(R.layout.ingredientes_trago_list,null,true);
        TextView nombreTv = fila.findViewById(R.id.nombre_ingrediente_trago_tv);
        TextView cantidadIngredienteTragoTv = fila.findViewById(R.id.cantidad_ingrediente_trago_tv);
        TextView stockTv = fila.findViewById(R.id.cantidad_ingrediente_stock_tragos_tv);
        IngredienteJson actual = ingredientesJson.get(position);
        nombreTv.setText(actual.getNombre());
        cantidadIngredienteTragoTv.setText(""+actual.getTrago_ingredientes().getCantidadIngrediente()+""+actual.getUnidadMedida());
        float cantidadEnStock = 0;
        for (Ingrediente i: stock
        ) {
            if(i.getNombre().equalsIgnoreCase(actual.getNombre())){
                cantidadEnStock = i.getCantidad();
            }
        }
        if(cantidadEnStock < actual.getTrago_ingredientes().getCantidadIngrediente()){
            stockTv.setBackgroundResource(R.color.red_pokeball);
        }else if(cantidadEnStock == actual.getTrago_ingredientes().getCantidadIngrediente()){
            stockTv.setBackgroundResource(R.color.caution);
        }else{
            stockTv.setBackgroundResource(R.color.pass);
        }
        stockTv.setText("Tienes "+cantidadEnStock+" "+actual.getUnidadMedida());



        return fila;
    }
}

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
import com.example.recetascocina.dto.Ingrediente;

import java.util.List;

//Clase adaptador de listview de ingredientes

public class IngredientesListAdapter extends ArrayAdapter<Ingrediente> {

    private List<Ingrediente> ingredientes;
    private Activity activity;

    //Constructor
    public IngredientesListAdapter(@NonNull Activity context, int resource, @NonNull List<Ingrediente> objects) {
        super(context, resource, objects);
        this.ingredientes = objects;
        this.activity = context;
    }

    //Metodo para llenar cada item de listview
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = this.activity.getLayoutInflater();
        View fila = inflater.inflate(R.layout.ingredientes_list,null,true);
        TextView nombreTv = fila.findViewById(R.id.nombre_ingrediente_pl);
        TextView cantidadTv = fila.findViewById(R.id.cantidad_ingrediente_pl);
        TextView unidadMedida= fila.findViewById(R.id.unidad_medida_txv);
        Ingrediente actual = ingredientes.get(position);
        nombreTv.setText(actual.getNombre());
        cantidadTv.setText(""+actual.getCantidad());
        unidadMedida.setText(actual.getUnidadMedida());



        return fila;
    }
}

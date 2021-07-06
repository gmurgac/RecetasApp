package com.example.recetascocina.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.recetascocina.R;
import com.example.recetascocina.dao.IngredientesDAO;
import com.example.recetascocina.dao.IngredientesDAOSqLite;
import com.example.recetascocina.dto.IngredienteJson;

import java.util.List;


public class SpinerIngredientesAdapter extends BaseAdapter {
    Activity context;

    private List<IngredienteJson> ingredientesJsons;



    public SpinerIngredientesAdapter(Activity context, List<IngredienteJson> nombres) {
        this.context = context;
        this.ingredientesJsons = nombres;
    }

    @Override
    public int getCount() {
        return ingredientesJsons.size();
    }

    @Override
    public IngredienteJson getItem(int position) {
        return ingredientesJsons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //Metodo para llenar cada item de spinner
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(R.layout.custom_spiner_item,null,false);
        TextView nombreIngrTxt = item.findViewById(R.id.nombre_ingrediente_spiner);
        IngredienteJson actual = ingredientesJsons.get(position);
        nombreIngrTxt.setText(actual.getNombre());
        return item;


    }


}

package com.example.recetascocina.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.recetascocina.R;
import com.example.recetascocina.dao.IngredientesDAO;
import com.example.recetascocina.dao.IngredientesDAOSqLite;
import com.example.recetascocina.dto.Ingrediente;
import com.example.recetascocina.dto.IngredienteJson;
import com.example.recetascocina.dto.Trago;

import java.util.List;

public class TragosListAdapter extends ArrayAdapter<Trago> {

    private Activity context;
    private List<Trago> list;

    public TragosListAdapter(@NonNull Activity context, int resource, @NonNull List<Trago> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
    }
    //Metodo para llenar cada item de listview
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        IngredientesDAO ingrDAO = new IngredientesDAOSqLite(this.context);
        List<Ingrediente> ingredientesStock = ingrDAO.getAll();

        LayoutInflater inflater = this.context.getLayoutInflater();
        View fila = inflater.inflate(R.layout.tragos_list,null,true);
        TextView nombreTv = fila.findViewById(R.id.nombre_trago_tv);
        ImageView alertaIv = fila.findViewById(R.id.alerta_trago_ingredientes_iv);
        TextView mensajeAlertaTv = fila.findViewById(R.id.alerta_trago_mensaje_tv);
        Trago actual = this.list.get(position);
        nombreTv.setText(actual.getNombre());
        List<IngredienteJson> ingredientesDelTrago = actual.getIngredientes();
        if(verificarStock(ingredientesDelTrago, ingredientesStock)){
            alertaIv.setImageResource(R.drawable.ic_baseline_tag_faces_24);
            mensajeAlertaTv.setText("Tienes todos los ingredientes");
            alertaIv.setBackgroundResource(R.color.pass);
        }else{
            alertaIv.setImageResource(R.drawable.ic_baseline_report_24);
            mensajeAlertaTv.setText("Te faltan algunos ingredientes");
            alertaIv.setBackgroundResource(R.color.red_pokeball);
        }
        return fila;
    }

    public boolean verificarStock(List<IngredienteJson> ingredientesDelTrago, List<Ingrediente> ingredientesStock){
        boolean hayStock;
        boolean hayCantidad;
        boolean[] verifier = new boolean[ingredientesDelTrago.size()];
        int k = 0;
        for (IngredienteJson i: ingredientesDelTrago
        ) {
            hayCantidad = false;
            hayStock = false;
            //buscar ingrediente en stock
            for (Ingrediente ig: ingredientesStock
            ) {
                if(ig.getNombre().equalsIgnoreCase( i.getNombre())) {
                    //TIENE EL INGREDIENTE...VERIFICAR CANTIDAD
                    hayStock = true;
                    float cantidadStock = ig.getCantidad();
                    float cantidadPlato = i.getTrago_ingredientes().getCantidadIngrediente();
                    if (cantidadStock >= cantidadPlato) {
                        hayCantidad = true;
                        break;
                    }

                }
            }
            if(hayStock == true && hayCantidad == true){
                verifier[k] = true;
            }else{
                verifier[k] = false;
            }
            k++;
        }
        //verificar el verifie
        boolean verificadorFinal=true;
        for (boolean b:verifier
        ) {
            if (b == false){
                verificadorFinal = b;
                break;
            }
        }
        return verificadorFinal;

    }
}

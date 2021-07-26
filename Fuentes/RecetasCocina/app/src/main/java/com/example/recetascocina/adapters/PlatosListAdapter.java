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
import com.example.recetascocina.dto.Plato;

import java.util.List;

public class PlatosListAdapter extends ArrayAdapter<Plato> {

    private Activity context;
    private List<Plato> list;


    public PlatosListAdapter(@NonNull Activity context, int resource, @NonNull List<Plato> objects) {
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
        View fila = inflater.inflate(R.layout.platos_list,null,true);
        TextView nombreTv = fila.findViewById(R.id.nombre_plato_tv);
        TextView comensalesTv = fila.findViewById(R.id.cantidad_comensales_tv);
        ImageView alertaIv = fila.findViewById(R.id.alerta_ingredientes_iv);
        TextView mensajeAlertaTv = fila.findViewById(R.id.alerta_mensaje_tv);

        Plato actual = this.list.get(position);
        List<IngredienteJson> ingredientesDelPlato = actual.getIngredientes();
        //VERIFICAR SI TIENE TODOS LOS INGREDIENTES

                nombreTv.setText(actual.getNombre());
                int cantidadComensalesPlato = 0;
                try {
                    cantidadComensalesPlato = actual.getIngredientes().get(0).getPlatoIngredientes().getCantidadComensales();

                } catch (Exception e) {

                }
                comensalesTv.setText("Receta para: " + cantidadComensalesPlato + " personas");

                //Verificacion de stock, si tiene cantidades suficientes asigna colores verde y OK, si tiene menos toma color Rojo.(Acompañados de mensaje)

                if (verificarStock(ingredientesDelPlato, ingredientesStock)) {
                    //image view OK
                    alertaIv.setImageResource(R.drawable.ic_baseline_tag_faces_24);
                    mensajeAlertaTv.setText("Tienes todos los ingredientes");
                    alertaIv.setBackgroundResource(R.color.pass);

                } else {
                    //imgae view caution, mas textview faltan algunos ingredientes
                    alertaIv.setImageResource(R.drawable.ic_baseline_report_24);
                    mensajeAlertaTv.setText("Te faltan algunos ingredientes");
                    alertaIv.setBackgroundResource(R.color.red_pokeball);

                }


       return fila;

    }

    //Metodo para verificar si existe ingrediente del plato en el stock del usuario.
    //Devuelve verdadero si posee todos las cantidades de ingredientes suficientes para realizar plato
    public boolean verificarStock(List<IngredienteJson> ingredientesDelPlato,List<Ingrediente> ingredientesStock){
        boolean hayStock;
        boolean hayCantidad;
        boolean[] verifier = new boolean[ingredientesDelPlato.size()];
        int k = 0;
        for (IngredienteJson i: ingredientesDelPlato
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
                    float cantidadPlato = i.getPlatoIngredientes().getCantidadIngrediente();
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


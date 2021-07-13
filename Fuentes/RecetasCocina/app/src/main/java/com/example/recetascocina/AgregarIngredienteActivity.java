package com.example.recetascocina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.recetascocina.adapters.SpinerIngredientesAdapter;
import com.example.recetascocina.dao.IngredientesDAO;
import com.example.recetascocina.dao.IngredientesDAOSqLite;
import com.example.recetascocina.dto.Ingrediente;
import com.example.recetascocina.dto.IngredienteJson;
import com.example.recetascocina.helpers.Globales;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AgregarIngredienteActivity extends AppCompatActivity {

    //Declaracion atributos de activity
    private RequestQueue queue;
    private List<IngredienteJson> ingredientes = new ArrayList<>();
    private IngredientesDAO ingrDAO = new IngredientesDAOSqLite(this);
    private Spinner spinnerIngredientes;
    private EditText cantidadIngrET;
    private Button agregar;
    private SpinerIngredientesAdapter adapterSpinerIngr;
    private TextView unidadMedidaTv;
    private Toolbar toolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Inicializacion de elementos graficos como objetos
        this.cantidadIngrET = findViewById(R.id.cantidad_ingrediente_et);
        this.agregar = findViewById(R.id.agregar_ingrediente_inventario_btn);
        this.unidadMedidaTv = findViewById(R.id.unidad_medida_txv2);
        this.spinnerIngredientes = findViewById(R.id.spinner_ingr_id);
        this.adapterSpinerIngr = new SpinerIngredientesAdapter(this,ingredientes);//Inicializacion adaptador de spiner, carga ingredientes existentes en API
        this.spinnerIngredientes.setAdapter(this.adapterSpinerIngr);
        this.spinnerIngredientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //Funcionalidad al seleccionar ingrediente de spiner se obtiene su unidad de medida
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                IngredienteJson igJson = (IngredienteJson) spinnerIngredientes.getAdapter().getItem(position);
                unidadMedidaTv.setText(igJson.getUnidadMedida());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        queue = Volley.newRequestQueue(AgregarIngredienteActivity.this);//Inicializa cola de peticiones
        //Generacion de peticion JSON
        JsonArrayRequest jsonReq = new JsonArrayRequest(
                Request.Method.GET
                , Globales.urlIngredientes
                , null
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    List<IngredienteJson> lista = new ArrayList<IngredienteJson>(){};

                    ingredientes.clear();
                    IngredienteJson[] ingredientesObj = new Gson().fromJson(response.toString() , IngredienteJson[].class);
                    //ingredientes.addAll(Arrays.asList(ingredientesObj));
                    lista.addAll(Arrays.asList(ingredientesObj));
                    for (IngredienteJson ij: lista
                         ) {
                        if(!ingrDAO.findIngrediente(ij.getNombre())){
                            ingredientes.add(ij);
                        }
                    }


                } catch (Exception e) {
                    ingredientes = null;

                }finally {

                    adapterSpinerIngr.notifyDataSetChanged();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ingredientes = null;
                adapterSpinerIngr.notifyDataSetChanged();
            }
        });
        //FIN peticion JSON
        queue.add(jsonReq);//Se añade peticion a cola de peticiones


        //Funcionalidad de boton agregar
        this.agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> errores = new ArrayList<String>();
                float cantidad =0;
                IngredienteJson ijson = new IngredienteJson();
                String nombreIngr = "";
                String unidadMedida = "";
                try {
                    ijson = (IngredienteJson) spinnerIngredientes.getAdapter().getItem(spinnerIngredientes.getSelectedItemPosition());
                    nombreIngr = ijson.getNombre();
                    unidadMedida = ijson.getUnidadMedida();
                }catch (Exception e){
                    errores.add("No hay ingrediente");
                }


                try {
                    cantidad = Float.parseFloat(cantidadIngrET.getText().toString().trim());
                }catch (Exception e){
                    errores.add("Cantidad no es numero");
                }
                if(cantidad <= 0){
                    errores.add("Cantidad debe ser numero mayor que cero");
                }

                if(errores.isEmpty()){
                    Ingrediente i = new Ingrediente();
                    i.setNombre(nombreIngr);
                    i.setCantidad(cantidad);
                    i.setUnidadMedida(unidadMedida);
                    ingrDAO.save(i);
                    Toast.makeText(AgregarIngredienteActivity.this,"Añadido a Dispensa",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else{
                    String cadenaErrores="";
                    for(String e:errores){
                        cadenaErrores=cadenaErrores+" "+e;
                    }
                    Toast.makeText(AgregarIngredienteActivity.this,cadenaErrores,Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ingrediente);
        this.toolbar = findViewById(R.id.idTolbar);
        this.setSupportActionBar(this.toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
}
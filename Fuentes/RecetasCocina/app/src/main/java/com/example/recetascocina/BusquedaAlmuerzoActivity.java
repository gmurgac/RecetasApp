package com.example.recetascocina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.recetascocina.adapters.PlatosListAdapter;
import com.example.recetascocina.dto.IngredienteJson;
import com.example.recetascocina.dto.Plato;
import com.example.recetascocina.helpers.Globales;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Activity class, de busqueda de almuerzo

public class BusquedaAlmuerzoActivity extends AppCompatActivity {

    //Declaracion de atributos del activity
    private EditText platoEdTxt; //Campo de texto editable de busqueda, aplica a filtro de nombre de platos
    private ListView platosEncontradosLv; //Vista de lista de platos que muestra la busqueda al oprimir boton buscar
    private Button buscarPlatosBtn; //Boton que realiza busqueda de platos.
    private List<Plato> platosJsonList = new ArrayList<Plato>(){}; //Listado de platos donde se guardaran platos encontrados por busqueda
    private RequestQueue queue; //Objeto necesario para el consumo de la API, para abrir cola de peticiones
    private PlatosListAdapter adapterPlatosLv; //Adaptador personalizado de Vista de lista.
    private Toolbar toolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        this.adapterPlatosLv.notifyDataSetChanged(); //Se notifica al adaptador si es que hubo cambios en la lista de platos
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_almuerzo);
        //Inicializacion de elementos graficos
        this.toolbar = findViewById(R.id.idTolbar);
        this.setSupportActionBar(this.toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.platoEdTxt = findViewById(R.id.plato_buscar_edtxt);
        this.platosEncontradosLv = findViewById(R.id.platos_encontrados_lv);
        this.buscarPlatosBtn = findViewById(R.id.btn_buscar_almuerzo);
        //Inicializacion de adaptadores
        this.adapterPlatosLv = new PlatosListAdapter(this,R.layout.platos_list,this.platosJsonList);
        this.platosEncontradosLv.setAdapter(this.adapterPlatosLv); //Se carga Vista de listado con adaptador

        //Se crea una nueva cola de peticiones a traves de la libreria Volley, en este activity.
        queue = Volley.newRequestQueue(BusquedaAlmuerzoActivity.this);
        //Se agrega funcionalidad de click en boton de buscar platos
        this.buscarPlatosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombrePlato = platoEdTxt.getText().toString(); //Se crea y asigna texto de busqueda ingresao en input de texto. (Filtro para busqueda)
                if(nombrePlato.isEmpty()){ //CASO QUE CAMPO DE TEXTO VACIO == SIN FILTRAR
                    //Peticion JSON
                    JsonArrayRequest jsonReq = new JsonArrayRequest(
                            Request.Method.GET //Tipo de metodo GET
                            , Globales.urlPlatos //URL de API Sin Filtro
                            , null
                            , new Response.Listener<JSONArray>() { //Listener en caso de que haya respuesta Valida
                        @Override
                        public void onResponse(JSONArray response) {
                            try {

                                platosJsonList.clear(); //se limpia lista de platos
                                Plato[] platosObj = new Gson().fromJson(response.toString() , Plato[].class);//Se crea y asigna array de platos, con libreria GSON traduce el texto entregado por la API en formato JSON.
                                platosJsonList.addAll(Arrays.asList(platosObj));//Se añade array de platos a Listado de platos en formato Lista.

                            } catch (Exception e) {
                                platosJsonList = null;

                            }finally {
                                //Se notifica al adaptador de la Lista que sus datos cambiaron (Listado de platos)
                                adapterPlatosLv.notifyDataSetChanged();


                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            platosJsonList = null;
                            //Caso de error lista se deja nula
                        }
                    });
                    queue.add(jsonReq); //Se añade peticion a la cola
                }else{
                    //CASO CAMPO DE TEXTO DE BUSQUEDA NO ES VACIO , SE APLICA FILTRO
                    JsonArrayRequest jsonReq = new JsonArrayRequest(
                            Request.Method.GET
                            , Globales.urlPlatos+"/"+nombrePlato //URL API CON FILTRO
                            , null
                            , new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {

                                platosJsonList.clear(); //se limpia lista de platos
                                Plato[] platosObj = new Gson().fromJson(response.toString() , Plato[].class);//Se crea y asigna array de platos, con libreria GSON traduce el texto entregado por la API en formato JSON.
                                platosJsonList.addAll(Arrays.asList(platosObj));//Se añade array de platos a Listado de platos en formato Lista.

                            } catch (Exception e) {
                                platosJsonList = null;

                            }finally {
                                //Se notifica al adaptador de la Lista que sus datos cambiaron (Listado de platos)
                                adapterPlatosLv.notifyDataSetChanged();


                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            platosJsonList = null;

                        }
                    });
                    queue.add(jsonReq); //Se añade peticion a la cola
                }





            }
        });

        //Funcionalidad al hacer click a un elemento de listado (Platos)
        this.platosEncontradosLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Plato plato = platosJsonList.get(position); //Se obtiene el objeto plato
                Intent intent = new Intent(BusquedaAlmuerzoActivity.this, VerRecetaPlatoActivity.class); //Se crea e inicializa el intent para lanzar activity Ver receta del plato seleccionado
                intent.putExtra("plato",plato); //Se añade el objeto plato seleccionado para el intent
                startActivity(intent); //Se lanza el activity Ver Receta del plato seleccionado
            }
        });

    }
}
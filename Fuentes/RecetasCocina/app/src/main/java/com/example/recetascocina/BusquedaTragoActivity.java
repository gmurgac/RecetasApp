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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.recetascocina.adapters.TragosListAdapter;
import com.example.recetascocina.dto.IngredienteJson;
import com.example.recetascocina.dto.Trago;
import com.example.recetascocina.helpers.Globales;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BusquedaTragoActivity extends AppCompatActivity {

    //Declaracion de atributos del activity
    private EditText tragoEd;//Campo de texto editable de busqueda, aplica a filtro de nombre de tragos
    private Button buscarTragoBtn;//Boton que realiza busqueda de tragos
    private ListView tragosEncontradosLv;//Vista de lista de tragos que muestra la busqueda
    private RequestQueue queue;//Objeto cola de peticiones
    private TragosListAdapter adapterListTragos;//Adaptador personalizado de vista de lista para tragos
    private List<Trago> tragosJsonList = new ArrayList<Trago>(){};//Listado de tragos
    private Toolbar toolbar;
    private TextView tituloToolbar;

    @Override
    protected void onResume() {
        super.onResume();
        this.adapterListTragos.notifyDataSetChanged();//Se notifica al adaptador si es que hubo cambios en elementos y en sus componentes del listado de tragos
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_trago);
        //Inicializacion de elementos graficos
        this.toolbar = findViewById(R.id.idTolbar);
        this.setSupportActionBar(this.toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.tituloToolbar = findViewById(R.id.titulo_toolbar_txt);
        this.tituloToolbar.setText("Solo para mayores de 18 ;)");
        this.tragoEd = findViewById(R.id.trago_buscar_edtxt);
        this.buscarTragoBtn = findViewById(R.id.btn_buscar_trago);
        this.tragosEncontradosLv = findViewById(R.id.tragos_encontrados_lv);
        this.adapterListTragos = new TragosListAdapter(this,R.layout.tragos_list,this.tragosJsonList);//Inicializacion de adaptador se pasan 3 parametros, Contexto Activity, layout de items de lista, listado de tragos.
        this.tragosEncontradosLv.setAdapter(this.adapterListTragos);//Se carga vista de listado con adaptador
        //Se inicializa cola de peticiones.
        queue = Volley.newRequestQueue(BusquedaTragoActivity.this);
        //Se agrega funcionalidad click al boton de buscar tragos
        this.buscarTragoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreTrago = tragoEd.getText().toString().trim();//Se crea y asigna texto de busqueda ingresado en input de texto.(Filtro para busqueda)
                if(nombreTrago.isEmpty()){//Caso campo texto vacio, sin filtro
                    //Peticion JSON
                    JsonArrayRequest jsonReq = new JsonArrayRequest(Request.Method.GET//Tipo de metodo GET
                            , Globales.urlTragos//URL DE API SIN FILTRO
                            , null
                            , new Response.Listener<JSONArray>() {//Listener cuando haya respuesta validad de la peticion
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                tragosJsonList.clear();//Se limpia lista de tragos
                                Trago[] tragosObj = new Gson().fromJson(response.toString(),Trago[].class);//Se crea y asigna array de tragos, con libreria GSON traduce el texto entregado por la peticion en formato JSON
                                tragosJsonList.addAll(Arrays.asList(tragosObj));//Se añaden todos los elementos tipo Trago del array como lista a Lista de tragos
                            }catch (Exception e){
                                tragosJsonList = null;
                            }finally {
                                adapterListTragos.notifyDataSetChanged();//Se notifica adaptador de Listado que datos cambiaron
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            tragosJsonList = null;
                        }
                    });
                    queue.add(jsonReq);//Se añade peticion a la cola en el activity
                }else{
                    JsonArrayRequest jsonReq = new JsonArrayRequest(Request.Method.GET//Tipo metodo GET
                            , Globales.urlTragos+"/"+nombreTrago//URL API CON FILTRO
                            , null
                            , new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                tragosJsonList.clear();//Se limpia lista de tragos
                                Trago[] tragosObj = new Gson().fromJson(response.toString(),Trago[].class);//Se crea y asigna array de tragos, con libreria GSON traduce el texto entregado por la peticion en formato JSON
                                tragosJsonList.addAll(Arrays.asList(tragosObj));//Se añaden todos los elementos tipo Trago del array como lista a Lista de tragos
                            }catch (Exception e){
                                tragosJsonList = null;
                            }finally {
                                adapterListTragos.notifyDataSetChanged();//Se notifica adaptador de Listado que datos cambiaron
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            tragosJsonList = null;
                        }
                    });
                    queue.add(jsonReq);//Se añade peticion a la cola del activity
                }
            }
        });
        //Funcionalidad al hacker click en un elemento del listado (Tragos)
        this.tragosEncontradosLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trago trago = tragosJsonList.get(position);//Se obtiene el objeto seleccionado
                Intent intent = new Intent(BusquedaTragoActivity.this//Se inicializa Intent de activities
                        ,VerRecetaTragoActivity.class);
                intent.putExtra("trago",trago);//Se añade objeto trago como extra del intent, para cargarlo luego en activity de ver receta
                startActivity(intent);//Se lanza activity VER RECETA TRAGO.
            }
        });
    }
}
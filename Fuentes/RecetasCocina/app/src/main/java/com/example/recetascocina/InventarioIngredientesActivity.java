package com.example.recetascocina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.recetascocina.adapters.IngredientesListAdapter;
import com.example.recetascocina.dao.IngredientesDAO;
import com.example.recetascocina.dao.IngredientesDAOSqLite;
import com.example.recetascocina.dto.Ingrediente;
import com.example.recetascocina.dto.Plato;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class InventarioIngredientesActivity extends AppCompatActivity {

    private ListView ingredientesLv;//Vista de listado de ingredientes en stock.
    private IngredientesListAdapter adapter;//Adaptador personalizado de vista de listado de ingredientes.
    private List<Ingrediente> ingredientes;//Lista de ingredientes
    private IngredientesDAO ingrDAO = new IngredientesDAOSqLite(this);//Dao de ingredientes, conecta con Base de datos local
    private FloatingActionButton agregarIngrBtn;//Boton flotante, redirigire a Activity de agregar ingredientes a la base de datos
    private Toolbar toolbar;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Metodo on resume
    @Override
    protected void onResume() {
        super.onResume();


        this.ingredientes = this.ingrDAO.getAll();//Se asigna listado de ingredientes a los que estan en base de datos local
        //Inicializacion de objetos con elementos graficos.
        this.agregarIngrBtn = findViewById(R.id.agregar_ingrediente_btn);
        this.ingredientesLv = findViewById(R.id.ingredientes_lv);
        //fin inicializacion de objetos.
        this.adapter = new IngredientesListAdapter(this,R.layout.ingredientes_list,this.ingredientes);//Se inicializa adaptador de lista de ingredientes, se le pasan 3 parametros: contexto,layout, lista de ingredientes
        this.ingredientesLv.setAdapter(this.adapter);//Se agrega adaptador de vista de lista.
        //Se agrega funcionalidad al hacer click en item de listado de ingredientes, para modificar o eliminar de bbdd
        this.ingredientesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ingrediente ingrediente = ingredientes.get(position);//Se asigna ingrediente seleccionado para enviar como extra
                Intent intent = new Intent(InventarioIngredientesActivity.this, ModificarStockIngredienteActivity.class);//Se genera intent para activity modificar stock
                intent.putExtra("ingrediente",ingrediente);//Se añade ingrediente como extra del intent
                startActivity(intent);//Se lanza actvity con extra
            }
        });
        //Fin funcionalidad items de lista
        //Se agrega funcionalidad al boton flotante de ir a activity agregar ingrediente
        this.agregarIngrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InventarioIngredientesActivity.this,AgregarIngredienteActivity.class));//Se lanza Activity agregar ingredientes
            }
        });

    }
    private TextView tituloToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario_ingredientes);
        this.toolbar = findViewById(R.id.idTolbar);
        this.setSupportActionBar(this.toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.tituloToolbar = findViewById(R.id.titulo_toolbar_txt);
        this.tituloToolbar.setText("Tú Despensa");


    }
}
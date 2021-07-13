package com.example.recetascocina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.recetascocina.adapters.IngredientesTragoListAdapter;
import com.example.recetascocina.dao.IngredientesDAO;
import com.example.recetascocina.dao.IngredientesDAOSqLite;
import com.example.recetascocina.dto.Ingrediente;
import com.example.recetascocina.dto.IngredienteJson;
import com.example.recetascocina.dto.Trago;

import java.util.ArrayList;
import java.util.List;

public class VerRecetaTragoActivity extends AppCompatActivity {

    //Atributos de activity
    private Button realizarTragoBtn;//Boton de realizar trago
    private TextView preparacionTragoTv;//Campo de texto fijo, se carga descripcion del plato para su preparacion
    private ListView ingredientesTragosLv;//Vista de listado, se carga listado de ingredientes del trago
    private TextView nombreTragoTv;//Campo de texto fijo, se carga el nombre del trago
    private Trago trago;//Objeto de clase trago
    private List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();//Listado de ingredientes
    private IngredientesDAO ingredientesStock = new IngredientesDAOSqLite(this);//Dao ingredientes en base de datos local
    private IngredientesTragoListAdapter adapterIngrTragoLv;//Adaptador de vista de listado de ingredientes del trago
    private Toolbar toolbar;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    //Metodo de creacion de activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_receta_trago);//Carga de elementos graficos de activity
        this.toolbar = findViewById(R.id.idTolbar);
        this.setSupportActionBar(this.toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Recibir extras enviados por activity de busqueda de trago
        if(getIntent().getExtras() != null){
            this.trago = (Trago) getIntent().getSerializableExtra("trago");//Se recibe objeto trago enviado desde activity busqueda de trago
        }
        this.ingredientes = ingredientesStock.getAll();//Se asigna ingredientes de stock de usuario.
        //Carga de objetos de elementos graficos para su modificacion
        this.nombreTragoTv = findViewById(R.id.nombre_trago_tv);
        this.preparacionTragoTv = findViewById(R.id.preparacion_trago_tv);
        this.ingredientesTragosLv = findViewById(R.id.ingredientes_trago_lv);
        this.realizarTragoBtn = findViewById(R.id.realizar_trago_btn);
        //Cargar atributos del trago
        this.nombreTragoTv.setText(this.trago.getNombre());//Carga nombre
        this.preparacionTragoTv.setText(this.trago.getDescripcion());//Carga descripcion
        this.adapterIngrTragoLv = new IngredientesTragoListAdapter( //Se inicializa adaptador de listado de ingredientes del trago, 3 parametros contexto,recurso de layout,Listado de ingredientes del trago.
                this,
                R.layout.ingredientes_trago_list,
                this.trago.getIngredientes());
        this.ingredientesTragosLv.setAdapter(this.adapterIngrTragoLv);//Se carga adaptador del Listado de vista.
        //Funcionalidad de realizar trago en el boton, al hacer click, se restan ingredientes del trago utilizados de ingredientes en stock de base de datos local de usuario.
        this.realizarTragoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<IngredienteJson> ingredientesAEliminar = trago.getIngredientes();//Lista de ingredientes a restar de stock
                List<Ingrediente> ingredientesSt = ingredientes; //Lista de ingredientes en stock
                for (IngredienteJson iTrago:ingredientesAEliminar//Pasa por Todos los ingredientes que hay que restar en stock
                ) {
                    for (Ingrediente iStock:ingredientesSt //Por cada ingrediente del trago busca el mismo en stock para restar cantidad
                    ) {
                        if(iTrago.getNombre().equalsIgnoreCase(iStock.getNombre())){//Encuentra el nombre de ingrediente
                            if(iStock.getCantidad() != 0){
                                float resta = iStock.getCantidad()-iTrago.getTrago_ingredientes().getCantidadIngrediente();//Resta cantidades
                                if(resta<0){//Si es menor a 0, modifica stock a 0
                                    Ingrediente modificable = new Ingrediente();
                                    modificable = iStock;
                                    modificable.setCantidad(0);
                                    ingredientesStock.modify(modificable);
                                }else{//Si es mayor o igual a 0, modifica a cantidad igual a la resta
                                    Ingrediente modificable = new Ingrediente();
                                    modificable = iStock;
                                    modificable.setCantidad(resta);
                                    ingredientesStock.modify(modificable);
                                }
                            }
                        }
                    }
                }


                realizarTragoBtn.setEnabled(false);//deshabilitar boton.

                realizarTragoBtn.setBackgroundResource(R.drawable.roundedbuttondisabled);//Cambiar color
                adapterIngrTragoLv.notifyDataSetChanged();//Notificar adaptador de ingredientes del trago que datos cambiaron.
            }
        });

    }
}
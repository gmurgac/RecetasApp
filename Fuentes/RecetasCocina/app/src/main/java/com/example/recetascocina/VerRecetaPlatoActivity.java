package com.example.recetascocina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.recetascocina.adapters.IngredientesPlatoListAdapter;
import com.example.recetascocina.dao.IngredientesDAO;
import com.example.recetascocina.dao.IngredientesDAOSqLite;
import com.example.recetascocina.dto.Ingrediente;
import com.example.recetascocina.dto.IngredienteJson;
import com.example.recetascocina.dto.Plato;

import java.util.ArrayList;
import java.util.List;

public class VerRecetaPlatoActivity extends AppCompatActivity {

    //Atributos de activity
    private Plato plato; //Plato que se esta viendo
    private IngredientesDAO ingredientesStock = new IngredientesDAOSqLite(this); //Listado de objetos ingredientes que posee en base de datos Local.
    private TextView nombrePlatoTv; //Campo de texto fijo, se carga el nombre del plato
    private TextView cantidadComenTv;//Campo de texto fijo, se carga la cantidad de comensales del plato
    private ListView ingredientesPlatoLv;//Vista de listado, se cargan ingredientes del plato
    private TextView preparacionTv;//Campo de texto fijo, se carga descripcion del plato para su preparacion.
    private Button realizarPlatoBtn;//Boton de realizar plato, se descuentan ingredientes utilizados de stock de la base de datos local de usuario.
    private IngredientesPlatoListAdapter adapterIngredientesPlato;//Adaptador de vista de listado de Ingredientes del plato
    List<Ingrediente> ingredientes = new ArrayList<Ingrediente>();//Listado de ingredientes,
    private Toolbar toolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    //METODO CUANDO SE CREA ACTIVITY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_receta_plato);//Carga de elementos graficos del activity
        this.toolbar = findViewById(R.id.idTolbar);
        this.setSupportActionBar(this.toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        //RECIBIR SI SE ENVIARON EXTRA EN INTENT QUE VIENE DE BUSQUEDA ALMUERZO
        if(getIntent().getExtras() != null){ // En caso de que vengan extras
            this.plato = (Plato) getIntent().getSerializableExtra("plato"); //Se carga plato enviado al haber dado clik en item del listado de platos de Busqueda de platos Activity.
        }
        this.ingredientes = ingredientesStock.getAll(); //Se cargan ingredientes q hay en stock usuario
        //Carga de objetos de elementos graficos para su modificacion
        this.nombrePlatoTv = findViewById(R.id.nombre_plato_tv);
        this.cantidadComenTv = findViewById(R.id.cantidad_comensales_tv);
        this.ingredientesPlatoLv = findViewById(R.id.ingredientes_plato_lv);
        this.preparacionTv = findViewById(R.id.preparacion_tv);
        this.realizarPlatoBtn = findViewById(R.id.realizar_plato_btn);
        this.realizarPlatoBtn = findViewById(R.id.realizar_plato_btn);
        //CARGAR ELEMENTOS DEL PLATO
        this.nombrePlatoTv.setText(this.plato.getNombre());//Se carga nombre.
        try {
            this.cantidadComenTv.setText("Receta para "+this.plato.getIngredientes().get(0).getPlatoIngredientes().getCantidadComensales()+" personas");//Se carga cantidad de comensales
        }catch (Exception e){

        }

        this.preparacionTv.setText(this.plato.getDescripcion());//Se carga preparacion con descripcion del plato

        this.adapterIngredientesPlato = new IngredientesPlatoListAdapter(
                this,R.layout.ingredientes_plato_list,this.plato.getIngredientes());//Se inicializa adaptador de listado de ingredientes. 3 parametros, contexto (Activity), layout del adaptador y listado de ingredientes del plato
        this.ingredientesPlatoLv.setAdapter(this.adapterIngredientesPlato);//Se carga vista del listado con adaptador

        //FUNCIONALIDAD DE BOTON REALIZAR PLATO: RESTA INGREDIENTES UTILIZADOS DE STOCK USUARIO
        this.realizarPlatoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<IngredienteJson> ingredientesAEliminar = plato.getIngredientes();//Lista de ingredientes a restar, son los ingredientes del plato
                List<Ingrediente> ingredientesSt = ingredientes;//Listado de ingredientes en Stock
                //Logica para encontrar ingrediente y restar
                for (IngredienteJson iPlato:ingredientesAEliminar //Pasa por todos los ingredientes a restar
                     ) {
                    for (Ingrediente iStock:ingredientesSt //Pasa por todos los ingredientes en Stock por cada ingrediente del plato
                         ) {
                        if(iPlato.getNombre().equalsIgnoreCase(iStock.getNombre())){//Si nombres de ingredientes son iguales, resta las cantidades utilizadas al stock
                            if(iStock.getCantidad() != 0){ //Solo resta si cantidad es distinta de 0
                                float resta = iStock.getCantidad()-iPlato.getPlatoIngredientes().getCantidadIngrediente(); //Resta de Stock menos cantidad utilizada del plato
                                if(resta<0){ //Si la resta da menor que 0, deja stock en 0.
                                    Ingrediente modificable = new Ingrediente();//Ingrediente a modificar
                                    modificable = iStock;//Ingrediente que esta en stock
                                    modificable.setCantidad(0);//Se reasigna cantidad a 0
                                    ingredientesStock.modify(modificable);//Se llama a DAO ingredientes, modifica en base de datos local.
                                }else{//Caso que resta sea > o = a 0, Se modifica cantidad igual a la resta
                                    Ingrediente modificable = new Ingrediente();
                                    modificable = iStock;//Ingrediente asignado igual al de Stock
                                    modificable.setCantidad(resta);//Modifica atributo cantidad igual a resta
                                    ingredientesStock.modify(modificable);//Se llama a DAO Ingrediente, modifica en base de datos
                                }
                            }
                        }
                    }
                }
                realizarPlatoBtn.setEnabled(false);//Se modifica boton a deshabilitado
                realizarPlatoBtn.setBackgroundResource(R.drawable.roundedbuttondisabled);//Se cambia color del boton mas oscuro
                adapterIngredientesPlato.notifyDataSetChanged();//Se notifica al adaptador de ingredientes del plato, ya que cambiaron las cantidades en Stock del usuario.
            }
        });
    }
}
package com.example.recetascocina;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.recetascocina.dao.IngredientesDAO;
import com.example.recetascocina.dao.IngredientesDAOSqLite;
import com.example.recetascocina.dto.Ingrediente;
import com.example.recetascocina.dto.Plato;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ModificarStockIngredienteActivity extends AppCompatActivity {

    //Declaracion de atributos de activity
    private TextView nombreIngrediente;//Campo de texto fijo nombre de ingrediente
    private FloatingActionButton modificarBtn;//Boton flotante para modificar ingrediente
    private FloatingActionButton eliminarBtn;//Boton flotante para eliminar de ingredientes en bbdd
    private NumberPicker decimalesPicker;//Seleccionador de numeros decimales de cantidad de ingrediente
    private TextView unidadMedida;//Texto fijo de unidad de medida del ingrediente
    private IngredientesDAO ingreDAO = new IngredientesDAOSqLite(this);//Dao que conecta bbdd
    private Ingrediente ingrediente;//Objeto de clase ingrediente
    private NumberPicker cantidadPicker;//Seleccionador de numeros enteros para cantidad de stock
    private Toolbar toolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_stock_ingrediente);
        //Inicializacion de elementos graficos
        this.toolbar = findViewById(R.id.idTolbar);
        this.setSupportActionBar(this.toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.unidadMedida = findViewById(R.id.unidad_medida_tv3);
        this.cantidadPicker = findViewById(R.id.cantidad_np);
        this.decimalesPicker = findViewById(R.id.cantidad_decimal_np);
        this.nombreIngrediente = findViewById(R.id.nombre_ingrediente_modificar_tv);
        this.eliminarBtn = findViewById(R.id.eliminar_ingrediente_fb);
        //Se asignan valores maximos y minimos de seleccionadores de numeros
        this.cantidadPicker.setMaxValue(9999);
        this.cantidadPicker.setMinValue(0);
        this.decimalesPicker.setMaxValue(9);
        this.decimalesPicker.setMinValue(0);
        //recibir extras
        if(getIntent().getExtras() != null){
            this.ingrediente = (Ingrediente) getIntent().getSerializableExtra("ingrediente");//Se recibe extra desde inventario, ingrediente que se selecciono en activity de inventario.
        }
        this.unidadMedida.setText(this.ingrediente.getUnidadMedida());//Se asigna texto de unidad de medida de ingrediente
        this.nombreIngrediente.setText(this.ingrediente.getNombre());//Se asigna texto de nombre de ingrediente
        //SEPARAR CANTIDAD INGREDIENTE EN PARTE ENTERA Y DECIMAL
        String cantidad = ""+this.ingrediente.getCantidad();
        String[] partesCantidad = cantidad.split("[.]");
        //FIN SEPARAR PARTE ENTERA Y DECIMAL DE CANTIDAD DE INGREDIENTE
        //ASIGNAR ENTERA Y DECIMAL A NUMBERS PICKERS
        int parteEntera=0;
        int parteDecimal=0;
        try {
            parteDecimal = Integer.parseInt(partesCantidad[1]);
            parteEntera = Integer.parseInt(partesCantidad[0]);
        }catch (Exception e){

        }
        this.cantidadPicker.setValue(parteEntera);
        this.decimalesPicker.setValue(parteDecimal);
        //FIN ASIGNAR A SELECCIONADOR DE CANTIDAD
        //FUNCIONALIDAD BOTON MODIFICAR GUARDAR
        this.modificarBtn = findViewById(R.id.modificar_ingrediente_fb);
        //Funcionalidad de click al boton modificar
        this.modificarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se lanza un dialogo alerta para confirmar modificacion
                AlertDialog dialogo = new AlertDialog
                        .Builder(ModificarStockIngredienteActivity.this)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String cantidadModifica = ""+cantidadPicker.getValue()+"."+decimalesPicker.getValue();
                                float cantidadFModifica = Float.parseFloat(cantidadModifica);
                                ingrediente.setCantidad(cantidadFModifica);
                                ingreDAO.modify(ingrediente);
                                onBackPressed();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setTitle("Confirmar")
                        .setMessage("Seguro que deseas modificar cantidad?")
                        .create();
                dialogo.show();

            }
        });

        //FUNCIONALIDAD BOTON BORRAR

        this.eliminarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se lanza dialogo de alerta para confirmar eliminacion de ingrediente del stock de usuario
                AlertDialog dialogo = new AlertDialog
                        .Builder(ModificarStockIngredienteActivity.this)
                        .setPositiveButton("Sí, eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ingreDAO.erase(ingrediente);
                                onBackPressed();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setTitle("Confirmar")
                        .setMessage("Seguro que deseas eliminar ?")
                        .create();
                dialogo.show();

            }
        });
    }
}
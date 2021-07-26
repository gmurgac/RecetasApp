package com.example.recetascocina;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.recetascocina.dto.Ingrediente;
import com.example.recetascocina.dto.Usuario;

public class MenuInicioActivity extends AppCompatActivity {

    //declaracion de atributos de activity
    private Button inventarioBtn; //Boton que lleva a la activity de inventario
    private Button buscarAlmuerzoBtn; //Boton que lleva a la activity de buscar almuerzo
    private Button buscarTragosBtn; //boton que lleva a la activity de buscar tragos
    private Toolbar toolbar;
    private TextView tituloToolbar;
    private Usuario user;


    @Override
    public void onBackPressed() {
        AlertDialog dialogo = new AlertDialog
                .Builder(MenuInicioActivity.this)
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setTitle("Saliendo de aplicacion")
                .setMessage("Seguro que deseas salir?")
                .create();
        dialogo.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio);

        this.toolbar = findViewById(R.id.idTolbar);
        this.tituloToolbar = findViewById(R.id.titulo_toolbar_txt);
        if(getIntent().getExtras() != null){
            this.user = (Usuario) getIntent().getSerializableExtra("usuario");//Se recibe extra desde inventario, ingrediente que se selecciono en activity de inventario.
        }
        this.tituloToolbar.setText("Bienvenido "+this.user.getNickname());

        final LinearLayout lil = findViewById(R.id.id_linearLayout_menuInicio);
        //final SpringAnimation sprinAnim = new SpringAnimation(lil, DynamicAnimation.TRANSLATION_Y,0);

        this.buscarAlmuerzoBtn = findViewById(R.id.btn_buscar_almuerzo);

        this.buscarAlmuerzoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se se lanza y envia a Activity Buscar Almuerzo
                startActivity(new Intent(MenuInicioActivity.this,BusquedaAlmuerzoActivity.class));
            }
        });
        this.inventarioBtn = findViewById(R.id.btn_inventario_ingresar);
        this.inventarioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se lanza y envia a Activity Inventario
                startActivity(new Intent(MenuInicioActivity.this,
                        InventarioIngredientesActivity.class));
            }
        });
        this.buscarTragosBtn = findViewById(R.id.btn_buscar_trago);
        this.buscarTragosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se lanza y envia a Activity Buscar Tragos
                startActivity(new Intent(MenuInicioActivity.this,
                        BusquedaTragoActivity.class));
            }
        });
    }
}
package com.example.recetascocina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.recetascocina.R;
import com.example.recetascocina.dao.UsuariosDAO;
import com.example.recetascocina.dao.UsuariosDAOSqLite;

//Activity cuando se lanza aplicacion, es la pantalla de inicio

public class MainActivity extends AppCompatActivity {

    //Declaracion de atributos propios del activity, elementos graficos del activity.
    private Button menuInicioBtn;
    private UsuariosDAO userDAO = new UsuariosDAOSqLite(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.menuInicioBtn = findViewById(R.id.menu_inicio_btn);
        //Funcionalidad al dar click al botòn de inicio
        this.menuInicioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Se lanza la Activity Menu de inicio
                if(userDAO.getUser()!=null){
                    Intent intent = new Intent(MainActivity.this,MenuInicioActivity.class);
                    intent.putExtra("usuario",userDAO.getUser());
                    startActivity(intent);
                }else{
                    startActivity(new Intent(MainActivity.this,RegistroUsuarioActivity.class));
                }


            }
        });

//TODO: verificar que usuario en bbdd local existe en bbdd nube
    }
}


package com.example.recetascocina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.recetascocina.dao.UsuariosDAO;
import com.example.recetascocina.dao.UsuariosDAOSqLite;
import com.example.recetascocina.dto.Usuario;
import com.example.recetascocina.helpers.Globales;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class RegistroUsuarioActivity extends AppCompatActivity {

    private RequestQueue queue;
    private UsuariosDAO userDAO = new UsuariosDAOSqLite(this);

    private EditText nicknameEd;
    private Button registrarNickBtn;
    private Usuario usuario;

    private EditText correoEd;
    private EditText passEd;
    private EditText pass2Ed;

    private Button iniciarSesion;
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        //Declaracion de variables
        this.iniciarSesion = findViewById(R.id.btn_iniciar_sesion_yaposee);
        this.iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistroUsuarioActivity.this,InicioSesionActivity.class));
            }
        });
        this.usuario = new Usuario();
        this.nicknameEd = findViewById(R.id.ed_nickname);
        this.nicknameEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    nicknameEd.setBackgroundResource(R.drawable.rounded_etxt);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.correoEd = findViewById(R.id.ed_txt_correo);
        this.correoEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!validarEmail(s.toString())){
                    correoEd.setBackgroundResource(R.drawable.rounded_etxt_error);
                }else{
                    correoEd.setBackgroundResource(R.drawable.rounded_etxt);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.passEd = findViewById(R.id.ed_txt_crear_pass);
        this.passEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<5){
                    passEd.setBackgroundResource(R.drawable.rounded_etxt_error);
                }else{
                    passEd.setBackgroundResource(R.drawable.rounded_etxt);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.pass2Ed = findViewById(R.id.ed_txt_repetir_pass);
        this.pass2Ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!passEd.getText().toString().equals(pass2Ed.getText().toString())){
                    pass2Ed.setBackgroundResource(R.drawable.rounded_etxt_error);
                }else{
                    pass2Ed.setBackgroundResource(R.drawable.rounded_etxt);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.registrarNickBtn = findViewById(R.id.btn_registrar_nick);
        this.queue = Volley.newRequestQueue(RegistroUsuarioActivity.this);
        //Funcionalidad Boton registrar
        this.registrarNickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = nicknameEd.getText().toString().trim();
                String correo = correoEd.getText().toString().trim();
                String pass = passEd.getText().toString();
                String pass2 = pass2Ed.getText().toString();
                List<String> errores = new ArrayList<String>(){};
                if(nickname.isEmpty()){
                    errores.add("Nombre vacio");
                    nicknameEd.setBackgroundResource(R.drawable.rounded_etxt_error);
                }
                if(!validarEmail(correo)){
                    errores.add("Correo inválido");
                    correoEd.setBackgroundResource(R.drawable.rounded_etxt_error);
                }
                if(pass.length()<5){
                    errores.add("Contraseña debe tener al menos 5 caracteres");

                }else if(!pass.equals(pass2)){
                    errores.add("Las contraseñas no coinciden");

                }
                if(pass.trim().isEmpty()){
                    errores.add("Contraseña vacia");
                    passEd.setBackgroundResource(R.drawable.rounded_etxt_error);
                    pass2Ed.setBackgroundResource(R.drawable.rounded_etxt_error);
                }

                if(errores.isEmpty()){

                    registrarNickBtn.setEnabled(false);//Desactiva boton
                    usuario.setNickname(nickname);
                    JSONObject jsonBody = new JSONObject();
                    try {
                        jsonBody.put("nombreUsuario", usuario.getNickname());
                        jsonBody.put("correoUsuario",correo);
                        jsonBody.put("passwordUsuario",pass);


                    final String requestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST
                            , Globales.urlUsuarios, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);

                            if(response.equalsIgnoreCase("200")){
                                //Guardar en bbdd
                                userDAO.save(usuario);
                                //Lanzar activity

                                Intent intent = new Intent(RegistroUsuarioActivity.this,MenuInicioActivity.class);
                                intent.putExtra("usuario",usuario);
                                startActivity(intent);
                            }else{
                                registrarNickBtn.setEnabled(true);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEYERROR", error.toString());
                            registrarNickBtn.setEnabled(true);
                            Toast.makeText(RegistroUsuarioActivity.this,"No se pudo registrar, intentelo nuevamente",Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;
                            }
                        }

                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            String responseString = "";
                            if (response != null) {
                                responseString = String.valueOf(response.statusCode);
                                // can get more details such as response.headers
                            }
                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                        }
                    };
                        queue.add(stringRequest);
                }catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }


                }

            }
        });

    }
}
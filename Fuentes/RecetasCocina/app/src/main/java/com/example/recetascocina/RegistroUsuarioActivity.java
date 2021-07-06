package com.example.recetascocina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;

public class RegistroUsuarioActivity extends AppCompatActivity {

    private RequestQueue queue;
    private UsuariosDAO userDAO = new UsuariosDAOSqLite(this);

    private EditText nicknameEd;
    private Button registrarNickBtn;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        //Declaracion de variables
        this.usuario = new Usuario();
        this.nicknameEd = findViewById(R.id.ed_nickname);
        this.registrarNickBtn = findViewById(R.id.btn_registrar_nick);
        this.queue = Volley.newRequestQueue(RegistroUsuarioActivity.this);
        //Funcionalidad Boton registrar
        this.registrarNickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = nicknameEd.getText().toString().trim();
                if(nickname.isEmpty()){
                    Toast.makeText(RegistroUsuarioActivity.this,"Ingrese un nickname",Toast.LENGTH_SHORT).show();
                }else if(nickname.length()<5){
                    Toast.makeText(RegistroUsuarioActivity.this,"Nickname de al menos 5 caracteres de largo",Toast.LENGTH_LONG).show();
                }
                else {
                    registrarNickBtn.setEnabled(false);//Desactiva boton
                    usuario.setNickname(nickname);
                    JSONObject jsonBody = new JSONObject();
                    try {
                        jsonBody.put("nickname", usuario.getNickname());


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
                                startActivity(new Intent(RegistroUsuarioActivity.this,MenuInicioActivity.class));
                            }else{
                                registrarNickBtn.setEnabled(true);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEYERROR", error.toString());
                            registrarNickBtn.setEnabled(true);
                            Toast.makeText(RegistroUsuarioActivity.this,"Intente nuevamente, pruebe otro nickname",Toast.LENGTH_SHORT).show();
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
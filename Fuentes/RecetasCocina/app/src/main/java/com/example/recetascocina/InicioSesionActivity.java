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
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.recetascocina.dao.UsuariosDAO;
import com.example.recetascocina.dao.UsuariosDAOSqLite;
import com.example.recetascocina.dto.Usuario;
import com.example.recetascocina.helpers.Globales;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class InicioSesionActivity extends AppCompatActivity {
    private RequestQueue queue;
    private EditText nickEd;
    private EditText pass;
    private Button iniciarSesion;
    private UsuariosDAO userDAO = new UsuariosDAOSqLite(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        this.queue = Volley.newRequestQueue(InicioSesionActivity.this);
        this.nickEd = findViewById(R.id.ed_nickname);
        this.pass = findViewById(R.id.ed_txt_crear_pass);
        this.iniciarSesion = findViewById(R.id.btn_registrar_nick);
        this.iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombreUsuario = nickEd.getText().toString().trim();
                String password = pass.getText().toString();

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("nombreUsuario", nombreUsuario);
                        //jsonBody.put("correoUsuario",correo);
                        jsonBody.put("passwordUsuario", password);


                    final String requestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST
                            , Globales.urlLogin, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);

                            if(response.equalsIgnoreCase("200")){
                                //INICIAR SESION LANZAR MENU INICIO
                                Usuario usuario = new Usuario();
                                usuario.setNickname(nombreUsuario);
                                userDAO.save(usuario);

                                Intent intent = new Intent(InicioSesionActivity.this,MenuInicioActivity.class);
                                intent.putExtra("usuario",usuario);
                                startActivity(intent);
                                //Toast.makeText(InicioSesionActivity.this,"EXCELENTE",Toast.LENGTH_SHORT).show();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEYERROR", error.toString());
                            iniciarSesion.setEnabled(true);
                            Toast.makeText(InicioSesionActivity.this,"Nombre de usuario o contraseña incorrecta",Toast.LENGTH_SHORT).show();
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
        });

    }
}
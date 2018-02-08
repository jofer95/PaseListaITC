package com.example.lyfs.pasedelistaitc.Actividades;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.TextureView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lyfs.pasedelistaitc.BaseDeDatos.DataBase;
import com.example.lyfs.pasedelistaitc.Objetos.Respuestas.Grupo;
import com.example.lyfs.pasedelistaitc.R;
import com.example.lyfs.pasedelistaitc.Servicios.Servicios;
import com.example.lyfs.pasedelistaitc.Utilerias.Opciones;
import com.example.lyfs.pasedelistaitc.Utilerias.Utilerias;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GrupoDetalleActivity extends AppCompatActivity {

    private Context context;
    private DataBase baseDeDatos;
    private TextView tvNombreMateria,tvGrupo,tvClaveMateria,tvLunes,tvMartes,tvMiercoles,tvJueves,tvViernes;
    private ProgressDialog progressDialog;
    private TextView totalAsistencias,totalFaltas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo_detalle);
        context = GrupoDetalleActivity.this;
        tvNombreMateria = (TextView) findViewById(R.id.idNombreMateria);
        tvGrupo = (TextView) findViewById(R.id.idGrupo);
        tvClaveMateria = (TextView) findViewById(R.id.idClaveMateria);
        tvLunes = (TextView) findViewById(R.id.idLunes);
        tvMartes = (TextView) findViewById(R.id.idMartes);
        tvMiercoles = (TextView) findViewById(R.id.idMiercoles);
        tvJueves = (TextView) findViewById(R.id.idJueves);
        tvViernes = (TextView) findViewById(R.id.idViernes);
        totalAsistencias = (TextView) findViewById(R.id.idTotalAsistencias);
        totalFaltas = (TextView) findViewById(R.id.idTotalFaltas);
        //INICIALIZANDO LA BASE DE DATOS:
        baseDeDatos = new DataBase(this);
        Grupo grupo = baseDeDatos.obtenerGrupoPorClaveMateria(Utilerias.getPreference(context, Opciones.MATERIA));
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(grupo != null){
            actionBar.setTitle(grupo.getMateria());
            tvNombreMateria.setText(grupo.getMateria());
            tvGrupo.setText(grupo.getGrupo());
            tvClaveMateria.setText(grupo.getClavemateria());
            tvLunes.setText("Lunes de: "+grupo.getHoralunes()+" Hrs.");
            tvMartes.setText("Martes de: "+grupo.getHoramartes()+" Hrs.");
            tvMiercoles.setText("Miercoles de: "+grupo.getHoramiercoles()+" Hrs.");
            tvJueves.setText("Jueves de: "+grupo.getHorajueves()+" Hrs.");
            tvViernes.setText("Viernes de: "+grupo.getHoraviernes()+" Hrs.");
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setSubtitle("Detalles");
        consultarTotalAsistencias();
        consultarTotalFaltas();
    }

    private void consultarTotalAsistencias() {
        /*progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Obteniendo asistencias...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();*/
        RequestQueue queue = Volley.newRequestQueue(this);
        String usuario = Utilerias.getPreference(context, Opciones.USUARIO);
        String usuarioValida = Utilerias.getPreference(context, Opciones.USUARIO_VALIDA);
        String periodoActual = Utilerias.getPreference(context, Opciones.PERIODO_ACTUAL);
        String materia = Utilerias.getPreference(context, Opciones.MATERIA);
        String grupo = Utilerias.getPreference(context, Opciones.GRUPO);
        String url = Servicios.asistenciasPorGrupo(usuario, usuarioValida, periodoActual,materia,grupo);
        //jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        //request.add(jsonObjectRequest);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("respuesta").equals("true")) {
                                String total = jsonObject.getString("cantidad");
                                totalAsistencias.setText(total);
                                if(progressDialog != null){
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                }
                            } else {
                                Toast.makeText(context, "Datos no disponibles", Toast.LENGTH_LONG).show();
                                if(progressDialog != null){
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Ocurrio un error al procesar los datos", Toast.LENGTH_LONG).show();
                            if(progressDialog != null){
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                        }
                        //showProgress(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //showProgress(false);
                if(progressDialog != null){
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void consultarTotalFaltas() {
        /*progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Obteniendo asistencias...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();*/
        RequestQueue queue = Volley.newRequestQueue(this);
        String usuario = Utilerias.getPreference(context, Opciones.USUARIO);
        String usuarioValida = Utilerias.getPreference(context, Opciones.USUARIO_VALIDA);
        String periodoActual = Utilerias.getPreference(context, Opciones.PERIODO_ACTUAL);
        String materia = Utilerias.getPreference(context, Opciones.MATERIA);
        String grupo = Utilerias.getPreference(context, Opciones.GRUPO);
        String url = Servicios.faltasPorGrupo(usuario, usuarioValida, periodoActual,materia,grupo);
        //jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        //request.add(jsonObjectRequest);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("respuesta").equals("true")) {
                                String total = jsonObject.getString("cantidad");
                                totalFaltas.setText(total);
                                if(progressDialog != null){
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                }
                            } else {
                                Toast.makeText(context, "Datos no disponibles", Toast.LENGTH_LONG).show();
                                if(progressDialog != null){
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Ocurrio un error al procesar los datos", Toast.LENGTH_LONG).show();
                            if(progressDialog != null){
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                        }
                        //showProgress(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //showProgress(false);
                if(progressDialog != null){
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

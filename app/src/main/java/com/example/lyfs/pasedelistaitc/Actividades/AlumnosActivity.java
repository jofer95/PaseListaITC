package com.example.lyfs.pasedelistaitc.Actividades;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lyfs.pasedelistaitc.Adapters.AlumnosAdapter;
import com.example.lyfs.pasedelistaitc.Adapters.GruposAdapter;
import com.example.lyfs.pasedelistaitc.BaseDeDatos.DataBase;
import com.example.lyfs.pasedelistaitc.Objetos.Respuestas.Alumno;
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

public class AlumnosActivity extends AppCompatActivity {

    private Context context;
    private DataBase baseDeDatos;
    private ProgressDialog progressDialog;
    private ArrayList<Alumno> listaAlumnos;
    private RecyclerView alumnosRecyclerView;
    private RequestQueue request;
    private Button btnCapturarAsistencias;
    private AlumnosAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos);
        context = AlumnosActivity.this;
        alumnosRecyclerView = (RecyclerView) findViewById(R.id.idRecyclerAlumnos);
        btnCapturarAsistencias = findViewById(R.id.btnCapturar);

        alumnosRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //INICIALIZANDO LA BASE DE DATOS:
        baseDeDatos = new DataBase(this);
        request = Volley.newRequestQueue(context);
        Grupo grupo = baseDeDatos.obtenerGrupoPorClaveMateria(Utilerias.getPreference(context,Opciones.MATERIA));
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //myToolbar.setTitle(grupo.getMateria()+"2");
        //myToolbar.setSubtitle("Alumnos del grupo");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(grupo != null){
            actionBar.setTitle(grupo.getMateria());
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setSubtitle("Alumnos del grupo");
        if(MainActivity.asistenciaPorAlumno){
            btnCapturarAsistencias.setVisibility(View.GONE);
        }

        consultarAlumnos();
        btnCapturarAsistencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarAsistencias();
            }
        });

    }

    private void guardarAsistencias() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Obteniendo alumnos...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String usuario = Utilerias.getPreference(context, Opciones.USUARIO);
        String usuarioValida = Utilerias.getPreference(context, Opciones.USUARIO_VALIDA);
        String periodoActual = Utilerias.getPreference(context, Opciones.PERIODO_ACTUAL);
        String materia = Utilerias.getPreference(context, Opciones.MATERIA);
        String grupo = Utilerias.getPreference(context, Opciones.GRUPO);
        for(final Alumno obj : listaAlumnos){
            if(obj.getEstatus() < 1){
                continue;
            }
            String url = Servicios.insertarIncidencia(usuario, usuarioValida, periodoActual,materia,grupo,obj.getNcontrol(),obj.getEstatus());

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("respuesta").equals("true")) {
                                    /*JSONArray jsonArray = jsonObject.optJSONArray("alumnos");
                                    listaAlumnos = new ArrayList<Alumno>();
                                    Gson gson = new GsonBuilder().create();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Alumno alumno = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), Alumno.class);
                                        listaAlumnos.add(alumno);
                                        baseDeDatos.insertOrReplaceAlumnos(alumno);
                                    }
                                    AlumnosAdapter adapter = new AlumnosAdapter(listaAlumnos);
                                    alumnosRecyclerView.setAdapter(adapter);*/
                                    listaAlumnos.remove(obj);
                                    adapter = new AlumnosAdapter(listaAlumnos);
                                    alumnosRecyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    //Toast.makeText(context, "No se registro la indicencia", Toast.LENGTH_LONG).show();
                                    //progressDialog.dismiss();
                                    //progressDialog = null;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Ocurrio un error al procesar los datos", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            //showProgress(false);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //showProgress(false);
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
        progressDialog.dismiss();
        progressDialog = null;
        if(listaAlumnos.size() != 0){
            adapter = new AlumnosAdapter(listaAlumnos);
            alumnosRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void consultarAlumnos() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Obteniendo alumnos...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String usuario = Utilerias.getPreference(context, Opciones.USUARIO);
        String usuarioValida = Utilerias.getPreference(context, Opciones.USUARIO_VALIDA);
        String periodoActual = Utilerias.getPreference(context, Opciones.PERIODO_ACTUAL);
        String materia = Utilerias.getPreference(context, Opciones.MATERIA);
        String grupo = Utilerias.getPreference(context, Opciones.GRUPO);
        String url = Servicios.alumnosPorGrupo(usuario, usuarioValida, periodoActual,materia,grupo);
        //jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        //request.add(jsonObjectRequest);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("respuesta").equals("true")) {
                                JSONArray jsonArray = jsonObject.optJSONArray("alumnos");
                                listaAlumnos = new ArrayList<Alumno>();
                                Gson gson = new GsonBuilder().create();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Alumno alumno = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), Alumno.class);
                                    listaAlumnos.add(alumno);
                                    baseDeDatos.insertOrReplaceAlumnos(alumno);
                                }
                                adapter = new AlumnosAdapter(listaAlumnos);
                                alumnosRecyclerView.setAdapter(adapter);
                                progressDialog.dismiss();
                                progressDialog = null;
                            } else {
                                Toast.makeText(context, "Alumnos no disponibles", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Ocurrio un error al procesar los alumnos", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        //showProgress(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //showProgress(false);
                progressDialog.dismiss();
                progressDialog = null;
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

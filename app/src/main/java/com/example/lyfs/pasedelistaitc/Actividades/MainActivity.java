package com.example.lyfs.pasedelistaitc.Actividades;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lyfs.pasedelistaitc.Adapters.GruposAdapter;
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
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener/*, Response.ErrorListener, Response.Listener<JSONObject>*/ {

    private Context context;
    private DataBase baseDeDatos;
    private ProgressDialog progressDialog;
    private ArrayList<Grupo> listaGrupos;
    private RecyclerView gruposRecyclerView;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue request;
    public static boolean tomaDeLista;
    public static boolean asistenciaPorGrupo;
    public static boolean asistenciaPorAlumno;
    private android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        gruposRecyclerView = (RecyclerView) findViewById(R.id.idRecyclergrupos);
        gruposRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Toma de asistencia");
        actionBar.setSubtitle("Seleccione un grupo");
        context = MainActivity.this;
        //INICIALIZANDO LA BASE DE DATOS:
        baseDeDatos = new DataBase(this);
        request = Volley.newRequestQueue(context);

        //BOTON FLOTANTE
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String sesion = Utilerias.getPreference(this, Opciones.SESION_ACTIVA);
        if (sesion.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            consultarGrupos();
        }
    }

    public void consultarGrupos() {
        //showProgress(true);
        // Instantiate the RequestQueue.
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Obteniendo grupos...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        String usuario = Utilerias.getPreference(context, Opciones.USUARIO);
        String usuarioValida = Utilerias.getPreference(context, Opciones.USUARIO_VALIDA);
        String periodoActual = Utilerias.getPreference(context, Opciones.PERIODO_ACTUAL);
        String url = Servicios.gruposPorUsuario(usuario, usuarioValida, periodoActual);
        //jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        //request.add(jsonObjectRequest);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("respuesta").equals("true")) {
                                JSONArray jsonArray = jsonObject.optJSONArray("grupos");
                                listaGrupos = new ArrayList<Grupo>();
                                Gson gson = new GsonBuilder().create();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Grupo grupo = gson.fromJson(String.valueOf(jsonArray.getJSONObject(i)), Grupo.class);
                                    listaGrupos.add(grupo);
                                    baseDeDatos.insertOrReplaceGrupos(grupo);
                                }
                                GruposAdapter adapter = new GruposAdapter(listaGrupos);
                                gruposRecyclerView.setAdapter(adapter);
                                progressDialog.dismiss();
                                progressDialog = null;
                            } else {
                                Toast.makeText(context, "Grupos no disponibles", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Ocurrio un error al procesar los grupos", Toast.LENGTH_LONG).show();
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tomaAsistencia) {
            asistenciaPorAlumno = false;
            asistenciaPorGrupo = false;
            actionBar.setTitle("Toma de asistencia");
            actionBar.setSubtitle("Seleccione un grupo");

        } else if (id == R.id.nav_asistenciasAlumno) {
            asistenciaPorAlumno = true;
            asistenciaPorGrupo = false;
            actionBar.setTitle("Asistencias por alumno");
            actionBar.setSubtitle("Seleccione un grupo");

        } else if (id == R.id.nav_asistenciasGrupo) {
            asistenciaPorGrupo = true;
            asistenciaPorAlumno = false;
            actionBar.setTitle("Asistencias por grupo");
            actionBar.setSubtitle("Seleccione un grupo");

        } else if (id == R.id.nav_buscarAlumno) {
            asistenciaPorAlumno = false;
            asistenciaPorGrupo = false;
            Intent intent = new Intent(context,AlumnoDetalle.class);
            startActivity(intent);

        } else if (id == R.id.nav_acercaDe) {
            Intent intent = new Intent(context,AcercaDeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_cerrarSesion) {
            Utilerias.savePreference(context, Opciones.SESION_ACTIVA, "");
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

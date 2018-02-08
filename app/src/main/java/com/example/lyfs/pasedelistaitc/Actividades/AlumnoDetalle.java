package com.example.lyfs.pasedelistaitc.Actividades;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lyfs.pasedelistaitc.BaseDeDatos.DataBase;
import com.example.lyfs.pasedelistaitc.R;
import com.example.lyfs.pasedelistaitc.Servicios.Servicios;
import com.example.lyfs.pasedelistaitc.Utilerias.Opciones;
import com.example.lyfs.pasedelistaitc.Utilerias.Utilerias;

import org.json.JSONException;
import org.json.JSONObject;

public class AlumnoDetalle extends AppCompatActivity {

    private Context context;
    private DataBase baseDeDatos;
    private TextView tvNombre,tvTotalAsistencias,tvTotalFaltas;
    private EditText etNumeroControl;
    private Button btnBuscar;
    private ImageView foto;
    private RequestQueue request;
    private LinearLayout linearBuscar,linearDatos;
    private ProgressDialog progressDialog;
    private String numeroContro,nombreCompleto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_detalle);
        foto = (ImageView)findViewById(R.id.idFoto);
        etNumeroControl = (EditText) findViewById(R.id.edNumeroControl);
        linearBuscar = (LinearLayout) findViewById(R.id.layBuscar);
        linearDatos = (LinearLayout) findViewById(R.id.layDatos);
        tvNombre = (TextView) findViewById(R.id.Nombre);
        tvTotalAsistencias = (TextView) findViewById(R.id.idAsistencias);
        tvTotalFaltas = (TextView) findViewById(R.id.idFaltas);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        context = AlumnoDetalle.this;
        request = Volley.newRequestQueue(context);
        Intent intent = getIntent();
        nombreCompleto = intent.getStringExtra("nombre");
        numeroContro = intent.getStringExtra("numeroControl");
        tvNombre.setText(nombreCompleto);
        //INICIALIZANDO LA BASE DE DATOS:
        baseDeDatos = new DataBase(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detalles del alumno");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setSubtitle("Información");
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asignarFoto(Servicios.obtenerFotografia(etNumeroControl.getText().toString()));
                hideKeyboard(AlumnoDetalle.this);
            }
        });
        if(!MainActivity.asistenciaPorAlumno){
            linearDatos.setVisibility(View.GONE);
        }else{
            linearBuscar.setVisibility(View.GONE);
        }
        asignarFoto(Servicios.obtenerFotografia(numeroContro));
        consultarTotalAsistencias();
        consultarTotalFaltas();
    }

    private void asignarFoto(String foto_url) {
        ImageRequest imageRequest = new ImageRequest(foto_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                foto.setImageBitmap(response);

            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No se pudo obtenr la foto", Toast.LENGTH_SHORT).show();
                Log.e("error","error al obtener la foto");
            }
        });
        request.add(imageRequest);
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
        String url = Servicios.asistenciasPorAlumno(usuario, usuarioValida, periodoActual,materia,grupo,numeroContro);
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
                                tvTotalAsistencias.setText(total);
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
        String url = Servicios.faltasPorAlumno(usuario, usuarioValida, periodoActual,materia,grupo,numeroContro);
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
                                tvTotalFaltas.setText(total);
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

package com.example.lyfs.pasedelistaitc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.lyfs.pasedelistaitc.Actividades.AlumnoDetalle;
import com.example.lyfs.pasedelistaitc.Actividades.AlumnosActivity;
import com.example.lyfs.pasedelistaitc.Actividades.MainActivity;
import com.example.lyfs.pasedelistaitc.Objetos.Respuestas.Alumno;
import com.example.lyfs.pasedelistaitc.Objetos.Respuestas.Grupo;
import com.example.lyfs.pasedelistaitc.R;
import com.example.lyfs.pasedelistaitc.Servicios.Servicios;

import java.util.ArrayList;

/**
 * Created by lyfs on 10/12/2017.
 */

public class AlumnosAdapter extends RecyclerView.Adapter<AlumnosAdapter.ViewHolderAlumnos> {
    private ArrayList<Alumno> listaAlumnos;
    private RequestQueue request;

    public AlumnosAdapter(ArrayList<Alumno> listaDatos) {
        this.listaAlumnos = listaDatos;
        setHasStableIds(true);
    }

    @Override
    public ViewHolderAlumnos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alumno_item, null);
        return new AlumnosAdapter.ViewHolderAlumnos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderAlumnos holder, int position) {
        holder.asignarAlumnos(listaAlumnos.get(position));
    }

    @Override
    public int getItemCount() {
        return listaAlumnos.size();
    }

    public class ViewHolderAlumnos extends RecyclerView.ViewHolder {
        TextView nombre, numeroControl;
        ImageView foto;
        View estatus;
        Context contexto;

        public ViewHolderAlumnos(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.idNombre);
            numeroControl = (TextView) itemView.findViewById(R.id.idNcontrol);
            foto = (ImageView) itemView.findViewById(R.id.idFoto);
            estatus = (View) itemView.findViewById(R.id.idEstatus);
            contexto = itemView.getContext();
            request = Volley.newRequestQueue(contexto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();
                    if(MainActivity.asistenciaPorAlumno){
                        Intent intent = new Intent(contexto, AlumnoDetalle.class);
                        intent.putExtra("nombre",listaAlumnos.get(itemPosition).nombreCompleto());
                        intent.putExtra("numeroControl",listaAlumnos.get(itemPosition).getNcontrol());
                        contexto.startActivity(intent);
                    }else{
                        if(listaAlumnos.get(itemPosition).getEstatus() == 1){
                            estatus.setBackgroundColor(Color.RED);
                            listaAlumnos.get(itemPosition).setEstatus(2);
                        }else{
                            estatus.setBackgroundColor(Color.GREEN);
                            listaAlumnos.get(itemPosition).setEstatus(1);
                        }
                    }
                }
            });
        }

        public void asignarAlumnos(Alumno alumno) {
            String nombreCompleto = alumno.getApellidopaterno() + " " + alumno.getApellidomaterno() + " " + alumno.getNombre();
            nombreCompleto = nombreCompleto.replace("null"," ");
            nombre.setText(nombreCompleto);
            numeroControl.setText(alumno.getNcontrol());
            /*if (!grupoObj.getFoto().isEmpty()) {
                asignarFoto(usuarios.getFoto());
            }*/
            asignarFoto(Servicios.obtenerFotografia(alumno.getNcontrol()));
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
                    //Toast.makeText(contexto, "Error al obtener fotografia", Toast.LENGTH_SHORT).show();
                }
            });
            request.add(imageRequest);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

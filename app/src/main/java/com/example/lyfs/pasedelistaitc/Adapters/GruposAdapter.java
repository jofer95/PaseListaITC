package com.example.lyfs.pasedelistaitc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.lyfs.pasedelistaitc.Actividades.AlumnosActivity;
import com.example.lyfs.pasedelistaitc.Actividades.GrupoDetalleActivity;
import com.example.lyfs.pasedelistaitc.Actividades.MainActivity;
import com.example.lyfs.pasedelistaitc.Objetos.Respuestas.Grupo;
import com.example.lyfs.pasedelistaitc.R;
import com.example.lyfs.pasedelistaitc.Utilerias.Opciones;
import com.example.lyfs.pasedelistaitc.Utilerias.Utilerias;

import java.util.ArrayList;

/**
 * Created by lyfs on 09/12/2017.
 */

public class GruposAdapter extends RecyclerView.Adapter<GruposAdapter.ViewHolderListaGrupos> {
    private ArrayList<Grupo> listaGrupos;
    private RequestQueue request;


    public GruposAdapter(ArrayList<Grupo> listaDatos) {
        this.listaGrupos = listaDatos;
    }

    @Override
    public ViewHolderListaGrupos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grupo_item, null);
        return new ViewHolderListaGrupos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderListaGrupos holder, int position) {
        holder.asignarDatos(listaGrupos.get(position));
    }

    @Override
    public int getItemCount() {
        return listaGrupos.size();
    }

    public class ViewHolderListaGrupos extends RecyclerView.ViewHolder {
        TextView nombre, grupo;
        ImageView foto;
        Context contexto;


        public ViewHolderListaGrupos(final View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.idNombre);
            grupo = (TextView) itemView.findViewById(R.id.idGrupo);
            foto = (ImageView) itemView.findViewById(R.id.idFoto);
            contexto = itemView.getContext();
            request = Volley.newRequestQueue(contexto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();
                    Utilerias.savePreference(contexto, Opciones.MATERIA,listaGrupos.get(itemPosition).getClavemateria());
                    Utilerias.savePreference(contexto, Opciones.GRUPO,listaGrupos.get(itemPosition).getGrupo());
                    if(MainActivity.asistenciaPorGrupo){
                        Intent intent = new Intent(contexto, GrupoDetalleActivity.class);
                        contexto.startActivity(intent);
                    }else{
                        Intent intent = new Intent(contexto, AlumnosActivity.class);
                        contexto.startActivity(intent);
                    }
                }
            });
        }

        public void asignarDatos(Grupo grupoObj) {
            nombre.setText(grupoObj.getMateria());
            grupo.setText(grupoObj.getGrupo());
            /*if (!grupoObj.getFoto().isEmpty()) {
                asignarFoto(usuarios.getFoto());
            }*/
        }

        /*private void asignarFoto(String foto_url) {
            ImageRequest imageRequest = new ImageRequest(foto_url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    foto.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(contexto, "Error al obtener fotografia", Toast.LENGTH_SHORT).show();
                }
            });
            request.add(imageRequest);
        }*/

    }
}

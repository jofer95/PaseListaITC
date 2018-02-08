package com.example.lyfs.pasedelistaitc.Objetos.Respuestas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lyfs on 08/12/2017.
 */

public class Alumno {

    @SerializedName("0")
    @Expose
    public String _0;
    @SerializedName("1")
    @Expose
    public String _1;
    @SerializedName("2")
    @Expose
    public String _2;
    @SerializedName("3")
    @Expose
    public String _3;
    @SerializedName("ncontrol")
    @Expose
    public String ncontrol;
    @SerializedName("apellidopaterno")
    @Expose
    public String apellidopaterno;
    @SerializedName("apellidomaterno")
    @Expose
    public String apellidomaterno;
    @SerializedName("nombre")
    @Expose
    public String nombre;
    public int estatus;

    public String getNcontrol() {
        return ncontrol;
    }

    public void setNcontrol(String ncontrol) {
        this.ncontrol = ncontrol;
    }

    public String getApellidopaterno() {
        return apellidopaterno;
    }

    public void setApellidopaterno(String apellidopaterno) {
        this.apellidopaterno = apellidopaterno;
    }

    public String getApellidomaterno() {
        return apellidomaterno;
    }

    public void setApellidomaterno(String apellidomaterno) {
        this.apellidomaterno = apellidomaterno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public String nombreCompleto(){
        String nombreCompleto = apellidopaterno + " "+ apellidomaterno + " "+nombre;
        nombreCompleto = nombreCompleto.replace("null"," ");
        return nombreCompleto;
    }
}
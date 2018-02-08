package com.example.lyfs.pasedelistaitc.Objetos.Respuestas;

/**
 * Created by lyfs on 07/12/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Grupo {

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
    @SerializedName("4")
    @Expose
    public String _4;
    @SerializedName("5")
    @Expose
    public String _5;
    @SerializedName("6")
    @Expose
    public String _6;
    @SerializedName("7")
    @Expose
    public String _7;
    @SerializedName("8")
    @Expose
    public String _8;
    @SerializedName("clavemateria")
    @Expose
    public String clavemateria;
    @SerializedName("grupo")
    @Expose
    public String grupo;
    @SerializedName("usuario")
    @Expose
    public String usuario;
    @SerializedName("materia")
    @Expose
    public String materia;
    @SerializedName("horalunes")
    @Expose
    public String horalunes;
    @SerializedName("horamartes")
    @Expose
    public String horamartes;
    @SerializedName("horamiercoles")
    @Expose
    public String horamiercoles;
    @SerializedName("horajueves")
    @Expose
    public String horajueves;
    @SerializedName("horaviernes")
    @Expose
    public String horaviernes;

    public String getClavemateria() {
        return clavemateria;
    }

    public void setClavemateria(String clavemateria) {
        this.clavemateria = clavemateria;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getHoralunes() {
        return horalunes;
    }

    public void setHoralunes(String horalunes) {
        this.horalunes = horalunes;
    }

    public String getHoramartes() {
        return horamartes;
    }

    public void setHoramartes(String horamartes) {
        this.horamartes = horamartes;
    }

    public String getHoramiercoles() {
        return horamiercoles;
    }

    public void setHoramiercoles(String horamiercoles) {
        this.horamiercoles = horamiercoles;
    }

    public String getHorajueves() {
        return horajueves;
    }

    public void setHorajueves(String horajueves) {
        this.horajueves = horajueves;
    }

    public String getHoraviernes() {
        return horaviernes;
    }

    public void setHoraviernes(String horaviernes) {
        this.horaviernes = horaviernes;
    }
}
package com.example.lyfs.pasedelistaitc.BaseDeDatos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lyfs.pasedelistaitc.Objetos.Respuestas.Alumno;
import com.example.lyfs.pasedelistaitc.Objetos.Respuestas.Grupo;

/**
 * Created by lyfs on 04/12/2017.
 */

public class DataBase extends SQLiteOpenHelper {

    public static String BASE_DE_DATOS = "ITCPaseLista.db";

    public static String TABLE_ALUMNOS = "Alumnos";
    public static String TABLE_GRUPOS = "Grupos";
    public SQLiteDatabase database;

    public DataBase(Context context) {
        super(context, BASE_DE_DATOS, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        database = db;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_ALUMNOS +" ("+
                "nControl INTEGER PRIMARY KEY NOT NULL, " +
                "ApellidoPaterno NVARCHAR(100) NULL, " +
                "ApellidoMaterno NVARCHAR(100) NULL, " +
                "Nombre NVARCHAR(100) NULL)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_GRUPOS +" ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "clavemateria NVARCHAR(100) NULL, " +
                "grupo NVARCHAR(100) NULL, " +
                "usuario NVARCHAR(100) NULL, " +
                "materia NVARCHAR(100) NULL, " +
                "horalunes NVARCHAR(100) NULL, " +
                "horamartes NVARCHAR(100) NULL, " +
                "horamiercoles NVARCHAR(100) NULL, " +
                "horajueves NVARCHAR(100) NULL, " +
                "horaviernes NVARCHAR(100) NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /***
     * METODO PARA INSERTAR LOS GRUPOS EN LA BASE DE DATOS
     * @param grupo
     */
    public void insertOrReplaceGrupos(Grupo grupo){

        database.execSQL("INSERT OR REPLACE INTO "+TABLE_GRUPOS+ " (clavemateria,grupo,usuario,materia,horalunes,horamartes,horamiercoles,horajueves,horaviernes) VALUES" +
                "('"+grupo.clavemateria+"','"+grupo.grupo+"','"+grupo.usuario+"','"+grupo.materia+"'" +
                ",'"+grupo.horalunes+"','"+grupo.horamartes+"','"+grupo.horamiercoles+"','"+grupo.horajueves+"'," +
                "'"+grupo.horaviernes+"')");
    }

    /***
     * METODO PARA INSERTAR LOS ALUMNOS EN LA BASE DE DATOS
     * @param alumno
     */
    public void insertOrReplaceAlumnos(Alumno alumno){
        database.execSQL("INSERT OR REPLACE INTO "+TABLE_ALUMNOS+ "(nControl,ApellidoPaterno,ApellidoMaterno,Nombre) VALUES" +
                "("+alumno.ncontrol+",'"+alumno.apellidopaterno+"','"+alumno.apellidomaterno+"',"+"'"+alumno.nombre+"')");
    }

    public Grupo obtenerGrupoPorClaveMateria(String claveMateria){
        String selectQuery = "SELECT * FROM "+TABLE_GRUPOS+" WHERE clavemateria like '%"+claveMateria+"%'";
        Cursor c = database.rawQuery(selectQuery, new String[] {});
        Grupo grupo = new Grupo();
        if (c.moveToFirst()) {
            grupo.setClavemateria(c.getString(c.getColumnIndex("clavemateria")));
            grupo.setGrupo(c.getString(c.getColumnIndex("grupo")));
            grupo.setUsuario(c.getString(c.getColumnIndex("usuario")));
            grupo.setMateria(c.getString(c.getColumnIndex("materia")));
            grupo.setHoralunes(c.getString(c.getColumnIndex("horalunes")));
            grupo.setHoramartes(c.getString(c.getColumnIndex("horamartes")));
            grupo.setHoramiercoles(c.getString(c.getColumnIndex("horamiercoles")));
            grupo.setHorajueves(c.getString(c.getColumnIndex("horajueves")));
            grupo.setHoraviernes(c.getString(c.getColumnIndex("horaviernes")));
        }
        c.close();
        return grupo;
    }
}

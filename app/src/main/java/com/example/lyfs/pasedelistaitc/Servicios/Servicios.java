package com.example.lyfs.pasedelistaitc.Servicios;

/**
 * Created by lyfs on 03/12/2017.
 */

public class Servicios {

    //SERVICIOS DE LA APP
    static String baseURL = "http://itculiacan.edu.mx/dadm/apipaselista/data/";
    static String fotosURL = "http://189.202.197.43/fotos/";

    public static String validaUsuario(String usuario, String clave){
        return baseURL + "validausuario.php?usuario="+usuario+"&clave="+clave;
    }
    public static String gruposPorUsuario(String usuario, String usuarioValida, String periodoActual){
        return baseURL + "obtienegrupos.php?usuario="+usuario+"&usuariovalida="+usuarioValida+"&periodoactual="+periodoActual;
    }
    public static String alumnosPorGrupo(String usuario, String usuarioValida, String periodoActual, String materia, String grupo){
        return baseURL + "obtienealumnos.php?usuario="+usuario+"&usuariovalida="+usuarioValida+"&periodoactual="+periodoActual+"&materia="+materia+"&grupo="+grupo;
    }
    public static String insertarIncidencia(String usuario, String usuarioValida, String periodoActual, String materia, String grupo,String numeroControl, int incidencia){
        return baseURL + "asignaincidencia.php?usuario="+usuario+"&usuariovalida="+usuarioValida+"&periodoactual="+periodoActual+"&materia="+materia+"&grupo="+grupo+"&ncontrol="+numeroControl+"&incidencia="+incidencia;
    }
    public static String obtenerFotografia(String numeroControl){
        return fotosURL + numeroControl+".jpg";
    }
    public static String asistenciasPorGrupo(String usuario, String usuarioValida, String periodoActual, String materia, String grupo){
        return baseURL + "cantidadasistenciasgrupo.php?usuario="+usuario+"&usuariovalida="+usuarioValida+"&periodoactual="+periodoActual+"&materia="+materia+"&grupo="+grupo;
    }
    public static String faltasPorGrupo(String usuario, String usuarioValida, String periodoActual, String materia, String grupo){
        return baseURL + "cantidadfaltasgrupo.php?usuario="+usuario+"&usuariovalida="+usuarioValida+"&periodoactual="+periodoActual+"&materia="+materia+"&grupo="+grupo;
    }
    public static String asistenciasPorAlumno(String usuario, String usuarioValida, String periodoActual, String materia, String grupo,String numeroControl){
        return baseURL + "cantidadasistenciasalumno.php?usuario="+usuario+"&usuariovalida="+usuarioValida+"&periodoactual="+periodoActual+"&materia="+materia+"&grupo="+grupo+"&ncontrol="+numeroControl;
    }
    public static String faltasPorAlumno(String usuario, String usuarioValida, String periodoActual, String materia, String grupo,String numeroControl){
        return baseURL + "cantidadfaltasalumno.php?usuario="+usuario+"&usuariovalida="+usuarioValida+"&periodoactual="+periodoActual+"&materia="+materia+"&grupo="+grupo+"&ncontrol="+numeroControl;
    }
}

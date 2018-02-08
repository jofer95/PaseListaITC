package com.example.lyfs.pasedelistaitc.Utilerias;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.lyfs.pasedelistaitc.Actividades.MainActivity;

/**
 * Created by lyfs on 04/12/2017.
 */

public class Utilerias {
    public static Context context;

    public Utilerias(Context contexto) {
        context = contexto;
    }

    public static String getPreference(Context context,String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }

    public static boolean savePreference(Context context, String key, String value){
        try {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putString(key,value);
            editor.apply();
            editor = null;
            return true;
        } catch(Exception ex) {
            return false;
        }
    }

    public static boolean savePreference(Context context, String key, boolean value) {
        try {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putBoolean(key,value);
            editor.apply();
            editor = null;
            return true;
        } catch(Exception ex) {
            return false;
        }
    }
}

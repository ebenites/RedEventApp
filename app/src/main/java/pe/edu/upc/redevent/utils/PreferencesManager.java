package pe.edu.upc.redevent.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by ebenites on 05/08/2016.
 */
public class PreferencesManager {

    private static final String PREFERENCEID =  "RedEventPreferences";

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private PreferencesManager(Context context){
        this.sharedPreferences = context.getSharedPreferences(PREFERENCEID, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    /**
     * Preferences attributes
     */
    public static final String PREF_EMAIL = "user-email";

    public static final String PREF_ISLOGGED = "user-islogged";

    /**
     * Crea una instancia de PreferencesManager para administrar los parámetros de preferencias
     * @param context
     * @return
     */
    public static PreferencesManager getInstance(Context context){
        return new PreferencesManager(context);
    }

    public void savePreference(String param, String email){
        editor.putString(param, email);
        editor.commit();
        Log.d(PreferencesManager.class.getSimpleName(), "savePreference: "+param+"-"+email);
    }

    public String loadPreference(String param){
        String email = sharedPreferences.getString(param, null);
        Log.d(PreferencesManager.class.getSimpleName(), "loadPreference: "+param+"-"+email);
        return email;
    }

    private void removePreference(String param) {
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(param, null);
        e.commit();
        Log.d(PreferencesManager.class.getSimpleName(), "removePreference: "+param);
    }

}

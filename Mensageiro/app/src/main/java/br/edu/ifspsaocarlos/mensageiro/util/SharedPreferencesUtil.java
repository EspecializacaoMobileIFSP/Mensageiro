package br.edu.ifspsaocarlos.mensageiro.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author maiko.trindade
 * @since 28/06/2016
 */
public class SharedPreferencesUtil {

    private static final String PREFERENCE_NAME = "MESSENGER_PREFS";

    public static void saveString(final String key, final String value) {
        final Context context = MessengerApplication.getInstance().getApplicationContext();
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(final String key) {
        final Context context = MessengerApplication.getInstance().getApplicationContext();
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return prefs.getString(key, null);
    }

}

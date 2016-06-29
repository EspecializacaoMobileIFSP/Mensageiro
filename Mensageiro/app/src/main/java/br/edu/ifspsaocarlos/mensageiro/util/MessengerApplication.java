package br.edu.ifspsaocarlos.mensageiro.util;

import android.app.Application;

/**
 * @author maiko.trindade
 * @since 28/06/2016
 */
public class MessengerApplication extends Application {

    private static MessengerApplication sInstance;

    public static MessengerApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
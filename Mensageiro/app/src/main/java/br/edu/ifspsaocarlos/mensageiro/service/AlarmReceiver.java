package br.edu.ifspsaocarlos.mensageiro.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author maiko.trindade
 * @since 06/07/2016
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (MessageService.service != null &&
                MessageService.service.isRunning()) {
            // TODO finish this implementation
            // MessageService.service.dotheMagic(intent);

            MessageService.service.getContacts();
            MessageService.service.getMessages();
        }
    }
}
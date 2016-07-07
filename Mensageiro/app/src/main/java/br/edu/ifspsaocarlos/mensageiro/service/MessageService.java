package br.edu.ifspsaocarlos.mensageiro.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.ui.MainActivity;

/**
 * @author maiko.trindade
 * @since 06/07/2016
 */
public class MessageService extends Service {

    //after Android 5.1 the minimum interval time is one minute = 60 * 1000 milisec
    private final static int DELAY_TIME = 60 * 1000;
    private final static int NOTIFICATION_ID = 123123;
    private AlarmManager alarmManager;
    private PendingIntent pendingAlarm;
    public static MessageService service;
    private static boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        MessageService.service = this;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        pendingAlarm = PendingIntent.getBroadcast(this, 0, new Intent(this, AlarmReceiver.class),
                0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(), DELAY_TIME, pendingAlarm);
        isRunning = true;
    }

    public void dotheMagic(Intent intent) {

        //TODO firstly do the magic then show notification
        showNotification("1", "1");

    }

    private void showNotification(String contactId, String messageId) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setPriority(Notification.PRIORITY_MAX)
                        //TODO customize it
                        .setContentText("New Messages");

        final Intent resultIntent = new Intent(this, MainActivity.class);

        //resultIntent.putExtra("contactId", contactId);
        //resultIntent.putExtra("messageId", messageId);

        //TODO remove mock
        resultIntent.putExtra("contactId", "CONTATO ID 123123");
        resultIntent.putExtra("messageId", "MENSAGEM ID 123123");

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pendingAlarm != null) {
            alarmManager.cancel(pendingAlarm);
        }
        isRunning = false;
    }

    public static boolean isRunning() {
        return isRunning;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
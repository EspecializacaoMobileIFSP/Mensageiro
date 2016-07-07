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
import android.util.Log;

import java.util.List;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.model.Account;
import br.edu.ifspsaocarlos.mensageiro.model.Contact;
import br.edu.ifspsaocarlos.mensageiro.model.Message;
import br.edu.ifspsaocarlos.mensageiro.model.MessageContact;
import br.edu.ifspsaocarlos.mensageiro.networking.BaseNetworkConfig;
import br.edu.ifspsaocarlos.mensageiro.networking.ContactsInterface;
import br.edu.ifspsaocarlos.mensageiro.networking.ContactsList;
import br.edu.ifspsaocarlos.mensageiro.networking.MessagesInterface;
import br.edu.ifspsaocarlos.mensageiro.networking.MessagesList;
import br.edu.ifspsaocarlos.mensageiro.ui.MainActivity;
import br.edu.ifspsaocarlos.mensageiro.util.MessengerApplication;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * @author maiko.trindade
 * @since 06/07/2016
 */
public class MessageService extends Service {

    //after Android 5.1 the minimum interval time is one minute = 60 * 1000 milisec
    private final static int DELAY_TIME = 60 * 1000;
    private final static int NOTIFICATION_ID = 123123;
    private final static String TAG = "MESSENGER_TAG";
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

//    public void dotheMagic(Intent intent) {
//        showNotification("1", "1");
//    }

    public void getContacts() {
        ContactsInterface service = BaseNetworkConfig.createService(ContactsInterface.class);
        Call<ContactsList> call = service.getContactsList();
        call.enqueue(
                new Callback<ContactsList>() {
                    @Override
                    public void onResponse(retrofit.Response<ContactsList> response,
                                           Retrofit retrofit) {
                        if (response.isSuccess()) {
                            ContactsList contactsList = response.body();
                            List<Contact> contacts = contactsList.getContacts();

                            Realm realm = MessengerApplication.getInstance().getRealmInstance();
                            for (Contact contact : contacts) {
                                realm.beginTransaction();
                                realm.insertOrUpdate(contact);
                                realm.commitTransaction();
                            }
                        } else {
                            Log.e(TAG, "Unsuccessful download contacts list");
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(TAG, "Fail to download contacts list");
                    }
                }
        );
    }

    public void getMessages() {
        Account account = MessengerApplication.getInstance().getAccount();
        if (account != null) {
            MessagesInterface service = BaseNetworkConfig.createService(MessagesInterface.class);

            Realm realm = MessengerApplication.getInstance().getRealmInstance();
            RealmQuery<Contact> query = realm.where(Contact.class);
            RealmResults<Contact> results = query.findAll();

            for (Contact contact : results) {
                RealmQuery<MessageContact> queryLastMessage = realm.where(MessageContact.class);
                MessageContact messageContact = queryLastMessage
                        .equalTo("from", account.getId())
                        .findFirst();

                Call<MessagesList> call = service.getMessagesList(
                        (messageContact == null) ? "0" : String.valueOf(messageContact.getLastMessageId()),
                        String.valueOf(account.getId()),
                        String.valueOf(contact.getId())
                );
                call.enqueue(
                        new Callback<MessagesList>() {
                            @Override
                            public void onResponse(Response<MessagesList> response,
                                                   Retrofit retrofit) {
                                if (response.isSuccess()) {
                                    MessagesList messagesList = response.body();
                                    List<Message> messages = messagesList.getMessages();

                                    Realm realm = MessengerApplication.getInstance().getRealmInstance();
                                    for (Message message : messages) {
                                        realm.beginTransaction();
                                        realm.insertOrUpdate(message);
                                        realm.commitTransaction();
                                    }

                                    if (messages.size() > 1) {
                                        Message message = messages.get(messages.size() - 1);
                                        MessageContact messageContact = new MessageContact(message);

                                        realm.beginTransaction();
                                        realm.insertOrUpdate(messageContact);
                                        realm.commitTransaction();
                                    }
                                } else {
                                    Log.e(TAG, response.code() + " - " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.e(TAG, t.getMessage());
                            }
                        }
                );

                queryLastMessage = realm.where(MessageContact.class);
                messageContact = queryLastMessage
                        .equalTo("from", contact.getId())
                        .findFirst();

                call = service.getMessagesList(
                        (messageContact == null) ? "0" : String.valueOf(messageContact.getLastMessageId()),
                        String.valueOf(contact.getId()),
                        String.valueOf(account.getId())
                );
                call.enqueue(
                        new Callback<MessagesList>() {
                            @Override
                            public void onResponse(Response<MessagesList> response,
                                                   Retrofit retrofit) {
                                if (response.isSuccess()) {
                                    MessagesList messagesList = response.body();
                                    List<Message> messages = messagesList.getMessages();

                                    Realm realm = MessengerApplication.getInstance().getRealmInstance();
                                    for (Message message : messages) {
                                        realm.beginTransaction();
                                        realm.insertOrUpdate(message);
                                        realm.commitTransaction();
                                    }

                                    if (messages.size() > 1) {
                                        Message message = messages.get(messages.size() - 1);
                                        MessageContact messageContact = new MessageContact(message);

                                        realm.beginTransaction();
                                        realm.insertOrUpdate(messageContact);
                                        realm.commitTransaction();

                                        showNotification(
                                                message.getFrom(),
                                                String.valueOf(message.getId()),
                                                message.getBody()
                                        );
                                    }
                                } else {
                                    Log.e(TAG, response.code() + " - " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.e(TAG, t.getMessage());
                            }
                        }
                );
            }
        }
    }

    private void showNotification(String contactId, String messageId, String message) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setPriority(Notification.PRIORITY_MAX)
                        //TODO customize it
                        .setContentText(message);

        final Intent resultIntent = new Intent(this, MainActivity.class);

        resultIntent.putExtra("contactId", contactId);
        resultIntent.putExtra("messageId", messageId);

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
package br.edu.ifspsaocarlos.mensageiro.util;

import android.app.Application;
import android.text.TextUtils;

import br.edu.ifspsaocarlos.mensageiro.model.Account;
import br.edu.ifspsaocarlos.mensageiro.model.Contact;

/**
 * @author maiko.trindade
 * @since 28/06/2016
 */
public class MessengerApplication extends Application {

    private static MessengerApplication sInstance;
    private static Account sAccount;

    public static MessengerApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        setAccount();
    }

    private void setAccount() {
        final String accountId = SharedPreferencesUtil.getString("id");
        final String accountFullname = SharedPreferencesUtil.getString("fullname");
        final String accountNickname = SharedPreferencesUtil.getString("nickname");

        if (!TextUtils.isEmpty(accountId) && !TextUtils.isEmpty(accountNickname) &&
                !TextUtils.isEmpty(accountFullname)) {
            sAccount = new Account(Long.parseLong(accountId), accountFullname, accountNickname);
        }
    }

    public void setAccount(Contact contact) {
        SharedPreferencesUtil.saveString("id", String.valueOf(contact.getId()));
        SharedPreferencesUtil.saveString("fullname", contact.getFullName());
        SharedPreferencesUtil.saveString("nickname", contact.getNickName());
        sAccount = new Account(contact.getId(), contact.getFullName(), contact.getNickName());
    }

    public Account getAccount() {
        return sAccount;
    }
}
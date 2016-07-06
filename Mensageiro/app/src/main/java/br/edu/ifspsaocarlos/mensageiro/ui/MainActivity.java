package br.edu.ifspsaocarlos.mensageiro.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.model.Account;
import br.edu.ifspsaocarlos.mensageiro.service.MessageService;
import br.edu.ifspsaocarlos.mensageiro.ui.contract.BaseActivityView;
import br.edu.ifspsaocarlos.mensageiro.util.MessengerApplication;

/**
 * @author maiko.trindade
 * @since 19/06/2016
 */
public class MainActivity extends AppCompatActivity implements BaseActivityView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startService();
        onNewIntent(getIntent());
        verifyUserSession();
    }

    private void verifyUserSession() {
        final Account account = MessengerApplication.getInstance().getAccount();
        if (account != null) {
            changeFragment(new ContactsListFragment(this), getString(R.string.contacts_list));
        } else {
            changeFragment(new NewContactFragment(this), getString(R.string.new_account));
        }
    }

    @Override
    public void changeFragment(final Fragment fragment, final String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .addToBackStack(fragment.getTag())
                .commit();
        setTitle(title);
    }

    @Override
    public void showMessage(View view, int messageResourceId) {
        Snackbar.make(view, getString(messageResourceId), Snackbar.LENGTH_SHORT).show();
    }

    public void startService() {
        startService(new Intent(MainActivity.this, MessageService.class));
    }

    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey("contactId") && extras.containsKey("messageId")) {
            String contactId = extras.getString("contactId");
            String messageId = extras.getString("messageId");

            //TODO goTo somewhere according to contactId and messageId

            Toast.makeText(this, "ContactId = " + contactId +
                    " | messageId = " + messageId, Toast.LENGTH_LONG).show();
        }
    }
}

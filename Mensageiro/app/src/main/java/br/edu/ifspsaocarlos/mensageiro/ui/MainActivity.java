package br.edu.ifspsaocarlos.mensageiro.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.model.Account;
import br.edu.ifspsaocarlos.mensageiro.model.Contact;
import br.edu.ifspsaocarlos.mensageiro.service.MessageService;
import br.edu.ifspsaocarlos.mensageiro.ui.contract.BaseActivityView;
import br.edu.ifspsaocarlos.mensageiro.util.MessengerApplication;
import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * @author maiko.trindade
 * @since 19/06/2016
 */
public class MainActivity extends AppCompatActivity implements BaseActivityView {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("contactId") && extras.containsKey("messageId")) {
            String contactId = extras.getString("contactId");
            String messageId = extras.getString("messageId");

            Realm realm = MessengerApplication.getInstance().getRealmInstance();
            RealmQuery<Contact> query = realm.where(Contact.class);
            Contact contact = query.equalTo("id", Long.parseLong(contactId)).findFirst();

            MessagesListFragment messagesListFragment = new MessagesListFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("contact_parcel", contact);
            messagesListFragment.setArguments(bundle);
            initFragment(messagesListFragment, contact.getNickName());
        } else {
            startService();
            onNewIntent(getIntent());
            verifyUserSession();
        }
    }

    private void verifyUserSession() {
        final Account account = MessengerApplication.getInstance().getAccount();
        if (account != null) {
            initFragment(new ContactsListFragment(this), getString(R.string.contacts_list));
        } else {
            initFragment(new LoginFragment(this), getString(R.string.new_account));
        }
    }

    public void initFragment(final Fragment fragment, final String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        setTitle(title);
    }

    @Override
    public void changeFragment(final Fragment fragment, final String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .addToBackStack(fragment.getTag())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        setTitle(title);
    }

    @Override
    public void showMessage(View view, int messageResourceId) {
        Snackbar.make(view, messageResourceId, Snackbar.LENGTH_SHORT).show();
    }

    public void startService() {
        startService(new Intent(MainActivity.this, MessageService.class));
    }

    @Override
    public void onNewIntent(Intent intent) {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_profile) {
            initFragment(new EditContactFragment(this), getString(R.string.edit_account));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

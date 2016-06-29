package br.edu.ifspsaocarlos.mensageiro.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.util.SharedPreferencesUtil;

/**
 * @author maiko.trindade
 * @since 19/06/2016
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        verifyUserSession();
    }

    private void verifyUserSession() {
        final String userId = SharedPreferencesUtil.getString("id");
        if (TextUtils.isEmpty(userId)) {
            changeFragment(new NewContactFragment(), getString(R.string.new_account));
        } else {
            changeFragment(new ContactsListFragment(), getString(R.string.contacts_list));
        }
    }

    protected void changeFragment(final Fragment fragment, final String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .addToBackStack(fragment.getTag())
                .commit();
        setTitle(title);
    }

}

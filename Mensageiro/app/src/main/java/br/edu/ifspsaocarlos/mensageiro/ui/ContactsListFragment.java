package br.edu.ifspsaocarlos.mensageiro.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.model.Contact;
import br.edu.ifspsaocarlos.mensageiro.ui.adapter.ContactsListAdapter;
import br.edu.ifspsaocarlos.mensageiro.ui.callback.MessageListCallback;
import br.edu.ifspsaocarlos.mensageiro.ui.contract.BaseActivityView;
import br.edu.ifspsaocarlos.mensageiro.util.MessengerApplication;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * @author maiko.trindade
 * @since 19/06/2016
 */
public class ContactsListFragment extends Fragment {

    private static final String TAG = ContactsListFragment.class.getSimpleName();
    private View mRootView;
    private BaseActivityView mBaseView;

    public ContactsListFragment(BaseActivityView view) {
        mBaseView = view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRootView = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        downloadContacts();
        return mRootView;
    }

    private void configureRecyclerView(List<Contact> contacts) {
        RecyclerView recyclerView = (RecyclerView)
                mRootView.findViewById(R.id.contacts_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mRootView.getContext()));
        final ContactsListAdapter adapter = new ContactsListAdapter(contacts,
                new MessageListCallback() {
                    @Override
                    public void openContactMessages(Contact contact) {
                        final MessagesListFragment messagesListFragment = new
                                MessagesListFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("contact_parcel", contact);
                        messagesListFragment.setArguments(bundle);
                        mBaseView.changeFragment(messagesListFragment, contact.getNickName());
                    }
                });
        recyclerView.setAdapter(adapter);
    }

    private void downloadContacts() {
        Realm realm = MessengerApplication.getInstance().getRealmInstance();
        RealmQuery<Contact> query = realm.where(Contact.class);
        RealmResults<Contact> results = query.findAll();
        List<Contact> contacts = results.subList(0, results.size());
        configureRecyclerView(contacts);
    }
}

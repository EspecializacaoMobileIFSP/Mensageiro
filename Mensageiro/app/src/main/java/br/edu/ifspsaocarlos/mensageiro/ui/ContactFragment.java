package br.edu.ifspsaocarlos.mensageiro.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.model.Contact;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private View mRootView;

    public ContactFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_contact, container, false);
        handleContact();
        return mRootView;
    }

    private void handleContact() {
        if (getArguments() != null) {
            Contact contact = getArguments().getParcelable("contact_parcel");
            setupContact(contact);
        }
    }

    private void setupContact(Contact contact) {
        TextView tv = (TextView) mRootView.findViewById(R.id.full_name_text_view);
        tv.setText(contact.getFullName());

        tv = (TextView) mRootView.findViewById(R.id.nick_name_text_view);
        tv.setText(contact.getNickName());
    }
}

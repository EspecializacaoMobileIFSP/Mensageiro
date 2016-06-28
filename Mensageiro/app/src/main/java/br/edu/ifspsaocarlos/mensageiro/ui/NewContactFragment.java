package br.edu.ifspsaocarlos.mensageiro.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import br.edu.ifspsaocarlos.mensageiro.R;

public class NewContactFragment extends Fragment {

    private static final String TAG = NewContactFragment.class.getSimpleName();

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRootView = inflater.inflate(R.layout.fragment_new_contact, container, false);

        configureElements();
        return mRootView;
    }

    private void configureElements() {
        final EditText mFullnameEdt = (EditText) mRootView.findViewById(R.id.fullname_edt_view);
        final EditText mNicknameEdt = (EditText) mRootView.findViewById(R.id.nickname_edt_view);
        final Button mConfirmButton = (Button) mRootView.findViewById(R.id.confirm_button);

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullname = mFullnameEdt.getText().toString();
                final String nickname = mNicknameEdt.getText().toString();

                if (!TextUtils.isEmpty(fullname) && !TextUtils.isEmpty(nickname)) {
                    saveAccount(fullname, nickname);
                }
            }
        });
    }

    private void saveAccount(final String fullname, final String nickname) {
        //TODO after login
        ((MainActivity) getActivity()).changeFragment(new ContactsListFragment(), getString(R
                .string.contacts_list));
    }
}

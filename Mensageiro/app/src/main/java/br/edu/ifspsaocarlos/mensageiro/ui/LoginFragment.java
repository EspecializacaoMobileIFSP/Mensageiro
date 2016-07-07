package br.edu.ifspsaocarlos.mensageiro.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.model.Contact;
import br.edu.ifspsaocarlos.mensageiro.networking.BaseNetworkConfig;
import br.edu.ifspsaocarlos.mensageiro.networking.ContactsInterface;
import br.edu.ifspsaocarlos.mensageiro.ui.contract.BaseActivityView;
import br.edu.ifspsaocarlos.mensageiro.util.MessengerApplication;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();
    private View mRootView;
    private BaseActivityView mBaseView;

    public LoginFragment(BaseActivityView view) {
        mBaseView = view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRootView = inflater.inflate(R.layout.fragment_login, container, false);

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

        Contact contact = new Contact(fullname, nickname);
        final ContactsInterface service = BaseNetworkConfig.createService(ContactsInterface.class);
        Call<Contact> response = service.newContact(contact);

        response.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Response<Contact> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    final Contact contact = response.body();
                    if (contact != null) {
                        MessengerApplication.getInstance().setAccount(contact);
                        mBaseView.changeFragment(new ContactsListFragment(mBaseView),
                                getString(R.string.contacts_list));
                    }
                } else {
                    mBaseView.showMessage(getView(), R.string.generic_error);
                    Log.d(TAG, "unsuccessful response");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mBaseView.showMessage(getView(), R.string.generic_error);
                Log.d(TAG, "failure to add new contact");
            }
        });
    }

}

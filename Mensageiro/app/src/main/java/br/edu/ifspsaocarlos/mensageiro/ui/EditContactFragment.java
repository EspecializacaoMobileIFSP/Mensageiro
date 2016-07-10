package br.edu.ifspsaocarlos.mensageiro.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.model.Account;
import br.edu.ifspsaocarlos.mensageiro.model.Contact;
import br.edu.ifspsaocarlos.mensageiro.networking.BaseNetworkConfig;
import br.edu.ifspsaocarlos.mensageiro.networking.ContactsInterface;
import br.edu.ifspsaocarlos.mensageiro.ui.contract.BaseActivityView;
import br.edu.ifspsaocarlos.mensageiro.util.MessengerApplication;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * @author maiko.trindade
 * @since 07/07/2016
 */
public class EditContactFragment extends Fragment {

    private static final String TAG = EditContactFragment.class.getSimpleName();

    private View mRootView;
    private BaseActivityView mBaseView;
    private String mSelectedImagePath;
    private ImageView mAddPicImageView;
    private static final int SELECT_PICTURE = 1;

    public EditContactFragment(BaseActivityView view) {
        mBaseView = view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRootView = inflater.inflate(R.layout.fragment_edit_contact, container, false);

        configureElements();
        return mRootView;
    }

    private void configureElements() {
        mAddPicImageView = (ImageView) mRootView.findViewById(R.id
                .new_picture_image_view);
        final EditText fullnameEdt = (EditText) mRootView.findViewById(R.id.fullname_edt_view);
        final EditText nicknameEdt = (EditText) mRootView.findViewById(R.id.nickname_edt_view);
        final Button confirmButton = (Button) mRootView.findViewById(R.id.edit_button);

        mAddPicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        getActivity().getString(R.string.select_image)), SELECT_PICTURE);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullname = fullnameEdt.getText().toString();
                final String nickname = nicknameEdt.getText().toString();

                if (!TextUtils.isEmpty(fullname) && !TextUtils.isEmpty(nickname)) {
                    editAccount(fullname, nickname);
                }
            }
        });

        final Account account = MessengerApplication.getInstance().getAccount();
        if (account != null && !TextUtils.isEmpty(account.getFullName())
                && !TextUtils.isEmpty(account.getNickName())) {
            fullnameEdt.setText(account.getFullName());
            nicknameEdt.setText(account.getNickName());
        }

    }

    private void editAccount(final String newFullname, final String newNickname) {

        final ContactsInterface service = BaseNetworkConfig.createService(ContactsInterface.class);
        final Account account = MessengerApplication.getInstance().getAccount();
        Contact contact = new Contact(account.getId(), newFullname, newNickname);
        Call<Contact> response = service.editContact(contact, String.valueOf(account.getId()));

        response.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Response<Contact> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    final Contact contact = response.body();
                    if (contact != null) {
                        mBaseView.showMessage(getView(), R.string.edited_successfully);
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
                Log.d(TAG, "failure to update new contact");
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    mAddPicImageView.setImageURI(selectedImageUri);
                }
            }
        }
    }
}

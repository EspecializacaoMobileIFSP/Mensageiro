package br.edu.ifspsaocarlos.mensageiro.ui.adapter;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.model.Contact;
import br.edu.ifspsaocarlos.mensageiro.networking.BaseNetworkConfig;
import br.edu.ifspsaocarlos.mensageiro.networking.ContactsInterface;
import br.edu.ifspsaocarlos.mensageiro.ui.callback.MessageListCallback;
import br.edu.ifspsaocarlos.mensageiro.util.MessengerApplication;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * @author maiko.trindade
 * @since 19/06/2016
 */
public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ViewHolder> {

    private final static String TAG = ContactsListAdapter.class.getSimpleName();

    private List<Contact> mContacts;
    private MessageListCallback mMessageCallback;
    private int mPosition;

    public ContactsListAdapter(List<Contact> contacts, MessageListCallback callback) {
        mContacts = contacts;
        mMessageCallback = callback;
    }

    public void setItens(List<Contact> contacts) {
        this.mContacts = contacts;
    }

    public Contact getSelectedItem() {
        return mContacts.get(mPosition);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View view = layoutInflater.inflate(R.layout.item_contact_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final View contextView = holder.itemView;
        final Contact contact = mContacts.get(position);

        holder.mFullNameTxtView.setText(contact.getFullName());
        holder.mNickNameTxtView.setText(contact.getNickName());
        holder.mContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMessageCallback != null) {
                    mMessageCallback.openContactMessages(contact);
                }
            }
        });
        holder.mMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view, contextView, contact);
            }
        });
    }

    private void showPopupMenu(final View view, final View contextView, final Contact contact) {
        PopupMenu popup = new PopupMenu(contextView.getContext(), view);
        popup.getMenuInflater().inflate(R.menu.menu_contact_list_popup, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionDelete:
                        deleteContact(contextView, contact);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void deleteContact(final View contextView, final Contact contact) {
        final ContactsInterface service = BaseNetworkConfig.createService(ContactsInterface.class);
        Call<Contact> response = service.deleteContact(String.valueOf(contact.getId()));

        response.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Response<Contact> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Realm realm = MessengerApplication.getInstance().getRealmInstance();
                    realm.beginTransaction();
                    realm.insertOrUpdate(contact);
                    realm.commitTransaction();
                    Snackbar.make(contextView, R.string.deleted_contact, Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    Log.d(TAG, "unsuccessful response");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "failure to delete contact");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mFullNameTxtView;
        private TextView mNickNameTxtView;
        private RelativeLayout mContainerLayout;
        private ImageView mMenuImageView;

        public ViewHolder(View view) {
            super(view);
            mFullNameTxtView = (TextView) view.findViewById(R.id.full_name_text_view);
            mNickNameTxtView = (TextView) view.findViewById(R.id.nick_name_text_view);
            mContainerLayout = (RelativeLayout) view.findViewById(R.id
                    .item_container_linear_layout);
            mMenuImageView = (ImageView) view.findViewById(R.id.menu_image_view);
        }
    }
}
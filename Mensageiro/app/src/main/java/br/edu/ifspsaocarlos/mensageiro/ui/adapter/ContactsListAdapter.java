package br.edu.ifspsaocarlos.mensageiro.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.model.Contact;
import br.edu.ifspsaocarlos.mensageiro.ui.callback.MessageListCallback;

/**
 * @author maiko.trindade
 * @since 19/06/2016
 */
public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ViewHolder> {

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
    public void onBindViewHolder(ViewHolder holder, int position) {
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
package br.edu.ifspsaocarlos.mensageiro.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.model.Contact;

/**
 * @author maiko.trindade
 * @since 19/06/2016
 */
public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ViewHolder> {

    private List<Contact> mContacts;
    private int mPosition;

    public ContactsListAdapter(List<Contact> contacts) {
        mContacts = contacts;
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
        holder.fullNameTxtView.setText(contact.getFullName());
        holder.nickNameTxtView.setText(contact.getNickName());
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fullNameTxtView;
        private TextView nickNameTxtView;

        public ViewHolder(View view) {
            super(view);
            fullNameTxtView = (TextView) view.findViewById(R.id.full_name_text_view);
            nickNameTxtView = (TextView) view.findViewById(R.id.nick_name_text_view);
        }
    }
}
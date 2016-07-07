package br.edu.ifspsaocarlos.mensageiro.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.model.Contact;
import br.edu.ifspsaocarlos.mensageiro.model.Message;
import br.edu.ifspsaocarlos.mensageiro.networking.BaseNetworkConfig;
import br.edu.ifspsaocarlos.mensageiro.networking.MessagesInterface;
import br.edu.ifspsaocarlos.mensageiro.networking.MessagesList;
import br.edu.ifspsaocarlos.mensageiro.ui.adapter.MessagesListAdapter;
import br.edu.ifspsaocarlos.mensageiro.util.MessengerApplication;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesListFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MessagesListFragment.class.getSimpleName();
    private View mRootView;

    private String from;
    private String to;

    public MessagesListFragment() {
        from = String.valueOf(
                MessengerApplication.getInstance().getAccount().getId()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRootView = inflater.inflate(R.layout.fragment_messages_list, container, false);
        mRootView.findViewById(R.id.send_image_button).setOnClickListener(this);
        handleMessages();
        return mRootView;
    }

    private void handleMessages() {
        if (getArguments() != null) {
            final Contact contact = getArguments().getParcelable("contact_parcel");
            to = String.valueOf(contact.getId());
            downloadMessages(to);
        }
    }

    private void downloadMessages(String toContact) {
        final MessagesInterface service = BaseNetworkConfig.createService(MessagesInterface.class);

        final Call<MessagesList> call = service.getMessagesList("0", from, toContact);
        call.enqueue(
                new Callback<MessagesList>() {
                    @Override
                    public void onResponse(retrofit.Response<MessagesList> response,
                                           Retrofit retrofit) {
                        if (response.isSuccess()) {
                            final MessagesList messagesList = response.body();
                            List<Message> messages = messagesList.getMessages();
                            configureRecyclerView(messages);
                        } else {
                            Log.e(TAG, response.code() + " - " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                }
        );
    }

    private void configureRecyclerView(List<Message> messages) {
        RecyclerView recyclerView = (RecyclerView)
                mRootView.findViewById(R.id.messages_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mRootView.getContext()));

        MessagesListAdapter adapter = new MessagesListAdapter(messages);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        EditText messageEditText = (EditText) mRootView.findViewById(R.id.message_edit_text);
        String body = messageEditText.getText().toString();

        Message message = new Message(from, to, "", body);
        final MessagesInterface service = BaseNetworkConfig.createService(MessagesInterface.class);
        Call<Message> response = service.newMessage(message);

        response.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Response<Message> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    final Message message = response.body();
                    if (message != null) {
//                        mBaseView.changeFragment(new ContactsListFragment(mBaseView),
//                                getString(R.string.contacts_list));
                        downloadMessages(to);
                    }
                } else {
//                    mRootView.showMessage(getView(), R.string.generic_error);
                    Log.d(TAG, "unsuccessful response");
                }
            }

            @Override
            public void onFailure(Throwable t) {
//                mBaseView.showMessage(getView(), R.string.generic_error);
                Log.d(TAG, "failure to add new contact");
            }
        });
    }
}
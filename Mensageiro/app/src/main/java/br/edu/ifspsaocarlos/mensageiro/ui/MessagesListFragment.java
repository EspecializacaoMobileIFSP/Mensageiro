package br.edu.ifspsaocarlos.mensageiro.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.model.Contact;
import br.edu.ifspsaocarlos.mensageiro.model.Message;
import br.edu.ifspsaocarlos.mensageiro.model.MessageContact;
import br.edu.ifspsaocarlos.mensageiro.networking.BaseNetworkConfig;
import br.edu.ifspsaocarlos.mensageiro.networking.MessagesInterface;
import br.edu.ifspsaocarlos.mensageiro.networking.MessagesList;
import br.edu.ifspsaocarlos.mensageiro.ui.adapter.MessagesListAdapter;
import br.edu.ifspsaocarlos.mensageiro.ui.contract.BaseActivityView;
import br.edu.ifspsaocarlos.mensageiro.util.MessengerApplication;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
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
    private BaseActivityView mBaseView;
    private String mTitle;
    private String from;
    private String to;
    private String mSelectedImagePath;
    private static final int SELECT_PICTURE = 1;
    private static final int mInterval = 3000;
    private Handler mHandler;

    public MessagesListFragment(BaseActivityView view, String title) {
        mBaseView = view;
        mTitle = title;
        from = String.valueOf(
                MessengerApplication.getInstance().getAccount().getId()
        );
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = inflater.inflate(R.layout.fragment_messages_list, container, false);
        mRootView.findViewById(R.id.send_image_view).setOnClickListener(this);
        handleMessages();
        handleTask();
        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_messages, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_contact) {
            ContactFragment fragment = new ContactFragment();
            fragment.setArguments(getArguments());
            mBaseView.changeFragment(fragment, getString(R.string.action_contact));
            return true;
        } else if (id == R.id.action_get_picture) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    getActivity().getString(R.string.select_image)), SELECT_PICTURE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(mTitle);
    }

    private void handleMessages() {
        if (getArguments() != null) {
            final Contact contact = getArguments().getParcelable("contact_parcel");
            to = String.valueOf(contact.getId());
            retrieveMessages();
        }
    }

    private void handleTask() {
        mHandler = new Handler();
        startRepeatingTask();
    }

    private void retrieveMessages() {
        Realm realm = MessengerApplication.getInstance().getRealmInstance();
        RealmQuery<Message> query = realm.where(Message.class);
        RealmResults<Message> results = query.beginGroup()
                .equalTo("to", to).equalTo("from", from)
                .or()
                .equalTo("to", from).equalTo("from", to)
                .endGroup()
                .findAll()
                .sort("id");
        List<Message> messages = results.subList(0, results.size());
        configureRecyclerView(messages);
    }

    private void configureRecyclerView(List<Message> messages) {
        RecyclerView recyclerView = (RecyclerView)
                mRootView.findViewById(R.id.messages_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mRootView.getContext()));

        MessagesListAdapter adapter = new MessagesListAdapter(messages, from);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_image_view) {
            EditText messageEditText = (EditText) mRootView.findViewById(R.id.message_edit_text);
            String body = messageEditText.getText().toString();
            messageEditText.setText("");

            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE
                );
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            Message message = new Message(from, to, "", body);
            final MessagesInterface service = BaseNetworkConfig.createService(MessagesInterface
                    .class);

            Call<Message> response = service.newMessage(message);

            response.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Response<Message> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        final Message message = response.body();
                        if (message != null) {
                            Realm realm = MessengerApplication.getInstance().getRealmInstance();
                            realm.beginTransaction();
                            realm.insertOrUpdate(message);
                            realm.commitTransaction();
                            retrieveMessages();
                        }
                    } else {
                        Log.d(TAG, "Unsuccessful send message");
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(TAG, "Fail to send a new message");
                }
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                //TODO do the magic
            }
        }
    }

    @Override
    public void onDetach() {
        stopRepeatingTask();
        super.onDetach();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateMessages();
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    void updateMessages() {
        Realm realm = MessengerApplication.getInstance().getRealmInstance();
        MessagesInterface service = BaseNetworkConfig.createService(MessagesInterface.class);

        RealmQuery<MessageContact> queryLastMessage = realm.where(MessageContact.class);
        MessageContact messageContact = queryLastMessage
                .equalTo("from", Long.parseLong(from))
                .findFirst();

        Call<MessagesList> call = service.getMessagesList(
                (messageContact == null) ? "0" : String.valueOf(messageContact.getLastMessageId()),
                from, to
        );
        call.enqueue(
                new Callback<MessagesList>() {
                    @Override
                    public void onResponse(Response<MessagesList> response,
                                           Retrofit retrofit) {
                        if (response.isSuccess()) {
                            MessagesList messagesList = response.body();
                            List<Message> messages = messagesList.getMessages();

                            Realm realm = MessengerApplication.getInstance().getRealmInstance();
                            for (Message message : messages) {
                                realm.beginTransaction();
                                realm.insertOrUpdate(message);
                                realm.commitTransaction();
                            }

                            if (messages.size() > 1) {
                                Message message = messages.get(messages.size() - 1);
                                MessageContact messageContact = new MessageContact(message);

                                realm.beginTransaction();
                                realm.insertOrUpdate(messageContact);
                                realm.commitTransaction();
                            }
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

        queryLastMessage = realm.where(MessageContact.class);
        messageContact = queryLastMessage
                .equalTo("from", Long.parseLong(to))
                .findFirst();

        call = service.getMessagesList(
                (messageContact == null) ? "0" : String.valueOf(messageContact.getLastMessageId()),
                to, from
        );
        call.enqueue(
                new Callback<MessagesList>() {
                    @Override
                    public void onResponse(Response<MessagesList> response,
                                           Retrofit retrofit) {
                        if (response.isSuccess()) {
                            MessagesList messagesList = response.body();
                            List<Message> messages = messagesList.getMessages();

                            Realm realm = MessengerApplication.getInstance().getRealmInstance();
                            for (Message message : messages) {
                                realm.beginTransaction();
                                realm.insertOrUpdate(message);
                                realm.commitTransaction();
                            }

                            if (messages.size() > 1) {
                                Message message = messages.get(messages.size() - 1);
                                MessageContact messageContact = new MessageContact(message);

                                realm.beginTransaction();
                                realm.insertOrUpdate(messageContact);
                                realm.commitTransaction();
                            }
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
        retrieveMessages();
    }

}

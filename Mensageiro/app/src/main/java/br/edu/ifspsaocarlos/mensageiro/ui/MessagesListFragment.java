package br.edu.ifspsaocarlos.mensageiro.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.model.Message;
import br.edu.ifspsaocarlos.mensageiro.networking.BaseNetworkConfig;
import br.edu.ifspsaocarlos.mensageiro.networking.MessagesInterface;
import br.edu.ifspsaocarlos.mensageiro.networking.MessagesList;
import br.edu.ifspsaocarlos.mensageiro.ui.adapter.MessagesListAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesListFragment extends Fragment {

    private static final String TAG = MessagesListFragment.class.getSimpleName();

    private View mRootView;


    public MessagesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRootView = inflater.inflate(R.layout.fragment_messages_list, container, false);

        downloadMessages();
        return mRootView;
    }

    private void downloadMessages() {
        final MessagesInterface service = BaseNetworkConfig.createService(MessagesInterface.class);

        final Call<MessagesList> call = service.getMessagesList("0", "1", "1");
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
}
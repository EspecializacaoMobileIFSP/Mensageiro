package br.edu.ifspsaocarlos.mensageiro.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.mensageiro.R;
import br.edu.ifspsaocarlos.mensageiro.model.Message;

/**
 * Created by denis on 23/06/16.
 */
public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.ViewHolder> {

    private List<Message> mMessages;
    private int mPosition;
    private String mOwn;

    public MessagesListAdapter(List<Message> messages, String own) {
        mMessages = messages;
        mOwn = own;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View view = layoutInflater.inflate(R.layout.item_message_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mMessages.get(position);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                holder.messageLytView.getLayoutParams();

        if (message.getFrom().equals(mOwn)) {
            holder.messageLytView.setBackgroundResource(R.drawable.message_background_from);
            params.gravity = Gravity.START;
        } else {
            holder.messageLytView.setBackgroundResource(R.drawable.message_background_to);
            params.gravity = Gravity.END;
        }
        holder.messageLytView.setLayoutParams(params);

        if (!TextUtils.isEmpty(message.getSubject())) {
            holder.subjectTxtView.setText(message.getSubject());
        } else {
            holder.subjectTxtView.setVisibility(View.GONE);
        }

        holder.bodyTxtView.setText(message.getBody());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout messageLytView;
        private TextView subjectTxtView;
        private TextView bodyTxtView;

        public ViewHolder(View view) {
            super(view);
            messageLytView = (LinearLayout) view.findViewById(R.id.message_layout_view);
            subjectTxtView = (TextView) view.findViewById(R.id.subject_text_view);
            bodyTxtView = (TextView) view.findViewById(R.id.body_text_view);
        }
    }
}

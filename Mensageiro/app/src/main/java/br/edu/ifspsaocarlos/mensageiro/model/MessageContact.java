package br.edu.ifspsaocarlos.mensageiro.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by denis on 06/07/16.
 */
public class MessageContact extends RealmObject {

    @PrimaryKey
    private long from;
    private long lastMessageId;

    public MessageContact() {
    }

    public MessageContact(Message message) {
        from = Long.parseLong(message.getFrom());
        lastMessageId = message.getId();
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }
}

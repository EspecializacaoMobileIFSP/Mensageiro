package br.edu.ifspsaocarlos.mensageiro.networking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.edu.ifspsaocarlos.mensageiro.model.Message;

/**
 * Created by denis on 23/06/16.
 */
public class MessagesList {

    @SerializedName("mensagens")
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }
}

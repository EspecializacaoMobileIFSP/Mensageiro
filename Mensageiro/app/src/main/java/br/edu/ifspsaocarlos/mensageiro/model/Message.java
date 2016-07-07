package br.edu.ifspsaocarlos.mensageiro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by denis on 23/06/16.
 */
public class Message extends RealmObject {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("origem_id")
    @Expose
    private String from;

    @SerializedName("destino_id")
    @Expose
    private String to;

    @SerializedName("assunto")
    @Expose
    private String subject;

    @SerializedName("corpo")
    @Expose
    private String body;

    public Message() {
    }

    public Message(String from, String to, String subject, String body) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

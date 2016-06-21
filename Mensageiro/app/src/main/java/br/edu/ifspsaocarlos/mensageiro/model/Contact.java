package br.edu.ifspsaocarlos.mensageiro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author maiko.trindade
 * @since 19/06/2016
 */
public class Contact {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("nome_completo")
    @Expose
    private String fullName;

    @SerializedName("apelido")
    @Expose
    private String nickName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}

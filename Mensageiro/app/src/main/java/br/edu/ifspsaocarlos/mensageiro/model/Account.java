package br.edu.ifspsaocarlos.mensageiro.model;

/**
 * @author maiko.trindade
 * @since 29/06/2016
 */
public class Account {

    private long id;
    private String fullName;
    private String nickName;

    public Account(long id, String fullName, String nickName) {
        this.id = id;
        this.fullName = fullName;
        this.nickName = nickName;
    }

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

package br.edu.ifspsaocarlos.mensageiro.networking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.edu.ifspsaocarlos.mensageiro.model.Contact;

/**
 * @author maiko.trindade
 * @since 19/06/2016
 */
public class ContactsList {

    @SerializedName("contatos")
    private List<Contact> contacts;

    public List<Contact> getContacts() {
        return contacts;
    }
}

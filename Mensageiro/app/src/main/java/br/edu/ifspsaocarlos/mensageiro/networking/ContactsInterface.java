package br.edu.ifspsaocarlos.mensageiro.networking;

import br.edu.ifspsaocarlos.mensageiro.model.Contact;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * @author maiko.trindade
 * @since 19/06/2016
 */
public interface ContactsInterface {

    @GET("/sdm/mensageiro/contato")
    Call<ContactsList> getContactsList();

    @POST("/sdm/mensageiro/contato")
    Call<Contact> newContact(@Body Contact contact);

}

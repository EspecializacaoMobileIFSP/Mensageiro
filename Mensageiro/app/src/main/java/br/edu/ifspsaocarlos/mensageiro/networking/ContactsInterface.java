package br.edu.ifspsaocarlos.mensageiro.networking;

import br.edu.ifspsaocarlos.mensageiro.model.Contact;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * @author maiko.trindade
 * @since 19/06/2016
 */
public interface ContactsInterface {

    @GET("/sdm/mensageiro/contato")
    Call<ContactsList> getContactsList();

    @POST("/sdm/mensageiro/contato")
    Call<Contact> newContact(@Body Contact contact);

    @POST("/sdm/mensageiro/contato/{id}")
    Call<Contact> editContact(@Body Contact contact, @Path("id") String id);

    @DELETE("/sdm/mensageiro/contato/{id}")
    Call<Contact> deleteContact(@Path("id") String id);
}

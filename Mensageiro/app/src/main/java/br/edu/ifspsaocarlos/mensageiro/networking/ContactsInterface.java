package br.edu.ifspsaocarlos.mensageiro.networking;

import retrofit.Call;
import retrofit.http.GET;

/**
 * @author maiko.trindade
 * @since 19/06/2016
 */
public interface ContactsInterface {

    @GET("/sdm/mensageiro/contato")
    Call<ContactsList> getContactsList();
}

package br.edu.ifspsaocarlos.mensageiro.networking;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by denis on 23/06/16.
 */
public interface MessagesInterface {

    @GET("/sdm/mensageiro/mensagem/{start}/{from}/{to}")
    Call<MessagesList> getMessagesList(@Path("start") String start,
                                       @Path("from") String from,
                                       @Path("to") String to);
}

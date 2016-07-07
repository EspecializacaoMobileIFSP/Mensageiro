package br.edu.ifspsaocarlos.mensageiro.networking;

import br.edu.ifspsaocarlos.mensageiro.model.Message;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by denis on 23/06/16.
 */
public interface MessagesInterface {

    @GET("/sdm/mensageiro/mensagem/{start}/{from}/{to}")
    Call<MessagesList> getMessagesList(@Path("start") String start,
                                       @Path("from") String from,
                                       @Path("to") String to);

    @POST("/sdm/mensageiro/mensagem")
    Call<Message> newMessage(@Body Message contact);
}

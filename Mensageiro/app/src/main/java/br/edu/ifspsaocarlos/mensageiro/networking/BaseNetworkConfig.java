package br.edu.ifspsaocarlos.mensageiro.networking;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * @author maiko.trindade
 * @since 19/06/2016
 */
public class BaseNetworkConfig {

    public static final String BASE_URL = "http://www.nobile.pro.br";
    private static final int TIMEOUT = 30;

    public static <S> S createService(Class<S> serviceClass) {

        //logging retrofit
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.interceptors().add(interceptor);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit.create(serviceClass);
    }

}

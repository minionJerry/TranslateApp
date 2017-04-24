package com.test.translateapp;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static Network instance;
    private final TranslateYandexApi translateApi;
    private Network() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.addInterceptor(interceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://translate.yandex.net/")
                .build();

        translateApi = retrofit.create(TranslateYandexApi.class);
    }

    public static synchronized TranslateYandexApi getInterface(){
        if(instance == null){
            instance = new Network();
        }
        return instance.getApi();
    }
    public TranslateYandexApi getApi(){
        return translateApi;
    }
}

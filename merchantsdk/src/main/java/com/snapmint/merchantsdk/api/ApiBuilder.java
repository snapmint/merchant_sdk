package com.snapmint.merchantsdk.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.snapmint.merchantsdk.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilder {
    private final static int TIMEOUT_IN_SECONDS = 60;

    public static <T> T create(final Class<T> serviceInterface) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        client.readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://merchant-js.snapmint.com/")
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(serviceInterface);
    }

}

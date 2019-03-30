package com.example.dogsworld.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService service;
    private Retrofit retrofit;

    private NetworkService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ServerDogsConstant.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getService() {
        if (service == null) {
            service = new NetworkService();
        }
        return service;
    }

    public PlaceHolderApi getJsonApi() {
        return retrofit.create(PlaceHolderApi.class);
    }
}

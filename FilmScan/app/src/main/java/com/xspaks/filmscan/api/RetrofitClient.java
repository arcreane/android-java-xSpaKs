package com.xspaks.filmscan.api;

import retrofit2.Retrofit;

public class RetrofitClient {

    private final Retrofit.Builder retrofitBuilder;

    public RetrofitClient(String baseUrl) {
        retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl);
    }

    public Retrofit getRetrofitInstance() {
        return retrofitBuilder.build();
    }
}

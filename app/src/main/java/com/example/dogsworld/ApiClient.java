package com.example.dogsworld;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiClient {

    private final OkHttpClient okHttpClient;
    private final Gson gson;

    public ApiClient() {
        this.okHttpClient = createOkHttpClient();
        this.gson = new Gson();
    }

    private static OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public void getDogs(DogResult callback) {
        Request request = new Request.Builder()
                .url("dogs")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //gson.fromJson(response.body().string(), String.class);

                List<DogInfo> dogs = null;
                new Handler(Looper.getMainLooper()).post(() -> callback.onResult(dogs));
            }
        });
    }

}

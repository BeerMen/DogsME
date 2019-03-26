package com.example.dogsworld;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class CreateVoice {

    static void post(String json, String url, Context context) {

        OkHttpClient mClient;
        mClient = new OkHttpClient().newBuilder().build();

        RequestBody body = RequestBody.create(ServerDogsConstant.JSON_MEDIA_TYPE, json);
        Request request = new Request.Builder()
                .url(url)
                .header(ServerDogsConstant.API_KAY_NAME, ServerDogsConstant.API_KAY)
                .post(body)
                .build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call,@NotNull Response response) throws IOException {

                if (response.body() != null) {
                    String resp = response.body().string();
                    boolean isComEnding = resp.contains("400,");
                    if (isComEnding) {
                        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context,
                                R.string.already_added_favorites,
                                Toast.LENGTH_LONG).show());
                    }

                }
            }
        });
    }
}

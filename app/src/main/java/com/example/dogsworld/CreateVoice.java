package com.example.dogsworld;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateVoice {

    public static void post(String json, String url, MediaType JSON,Context context){

        OkHttpClient mClient;
        mClient = new OkHttpClient().newBuilder().build();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .header("x-api-key", "a00fade5-8415-4580-8fec-5fee491ce7ce")
                .post(body)
                .build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String resp = response.body().string();
                boolean isComEnding = resp.contains("400,");
                if (isComEnding){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"Вы уже добавили в избранное",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}

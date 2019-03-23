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
    private static final String DBAG = "DBagTest";
    private static Context mContext;
    public CreateVoice (Context context){
        mContext = context;
    }

    public static void post(String json, String url, MediaType JSON){

        OkHttpClient mClient;
        mClient = new OkHttpClient().newBuilder().build();

        Log.d(DBAG,"post " +json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .header("x-api-key", "a00fade5-8415-4580-8fec-5fee491ce7ce")
                .post(body)
                .build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(DBAG,"нажатие  # 5.1" );
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String resp = response.body().string();
                boolean isComEnding = resp.contains("400,");
                Log.d(DBAG,"Ответ от сервера" + resp);
                if (isComEnding){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext,"Вы уже добавили в избранное",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}

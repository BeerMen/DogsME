package com.example.dogsworld;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadImage {


    public static void uploadImage(String sub_id, File file, Context context) {


        try {
            File sourceFile = file;
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
            RequestBody requestBody1 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("SUB_ID", sub_id)
                    .addFormDataPart("file", "file", RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                    .build();

            Request request = new Request.Builder()
                    .url("https://api.thedogapi.com/v1/images/upload")
                    .header("x-api-key", "a00fade5-8415-4580-8fec-5fee491ce7ce")
                    .header("Content-Type", "multipart/form-data")
                    .post(requestBody1)
                    .build();

            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("TegDBag",e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String resp = response.body().string();
                    //Log.d("TegDBag",resp);
                    boolean isComEnding = resp.contains("400,");
                    if (isComEnding){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context,"На Фото нет собак!",Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context,"Фото успешно загружено на сервер",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });

        } catch (Exception e) {
            Log.e("TegDBag", "Other Error: " + e.getLocalizedMessage());
        }
    }
}


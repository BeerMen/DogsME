package com.example.dogsworld;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

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
import timber.log.Timber;

class UploadImage {


    static void uploadImage(File file, Context context) {

        try {
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
            RequestBody requestBody1 = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("SUB_ID", ServerDogsConstant.SUB_ID)
                    .addFormDataPart("file", "file", RequestBody.create(MEDIA_TYPE_PNG, file))
                    .build();

            Request request = new Request.Builder()
                    .url(ServerDogsConstant.URL_UPLOAD_IMAGE)
                    .header(ServerDogsConstant.API_KAY_NAME, ServerDogsConstant.API_KAY)
                    .header("Content-Type", "multipart/form-data")
                    .post(requestBody1)
                    .build();

            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Timber.d(e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    if (response.body() != null) {
                        String resp = response.body().string();
                        boolean isComEnding = resp.contains("400,");
                        if (isComEnding) {
                            new Handler(Looper.getMainLooper()).post(() ->
                                    Toast.makeText(context, R.string.no_dog
                                            , Toast.LENGTH_LONG).show());
                        } else {
                            new Handler(Looper.getMainLooper()).post(() ->
                                    Toast.makeText(context, R.string.good_upload,
                                            Toast.LENGTH_LONG).show());
                        }
                    }

                }
            });

        } catch (Exception e) {
            Timber.d(e.getLocalizedMessage());
        }
    }
}


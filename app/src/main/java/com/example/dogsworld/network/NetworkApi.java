package com.example.dogsworld.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.dogsworld.DogInfo;
import com.example.dogsworld.DogPost;
import com.example.dogsworld.DogPostFavorites;
import com.example.dogsworld.R;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkApi {

    public void postUploadImage(File file, Context context){
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("SUB_ID", ServerDogsConstant.SUB_ID)
                .addFormDataPart("file", "file", RequestBody.create(MEDIA_TYPE_PNG, file))
                .build();

        NetworkService.getService().getJsonApi().postUploadImage(requestBody).enqueue(new Callback<ApiAnswer>() {
            @Override
            public void onResponse(Call<ApiAnswer> call, Response<ApiAnswer> response) {
                if (response.body() != null) {
                    ApiAnswer apiAnswer = response.body();
                    if (apiAnswer.status == 400) {
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

            @Override
            public void onFailure(Call<ApiAnswer> call, Throwable t) {

            }
        });
    }

    public void postFavorite(DogPostFavorites data, Context context){
        NetworkService.getService().getJsonApi().postFavorite(data,ServerDogsConstant.SUB_ID_INT).enqueue(new Callback<ApiAnswer>() {
            @Override
            public void onResponse(Call<ApiAnswer> call, Response<ApiAnswer> response) {
                if (response.body() != null) {
                    ApiAnswer apiAnswer = response.body();
                    if (apiAnswer.status == 400) {
                        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context,
                                R.string.already_added_favorites,
                                Toast.LENGTH_LONG).show());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiAnswer> call, Throwable t) {

            }
        });
    }

    public void postLike(DogPost data) {
        NetworkService.getService().getJsonApi().postLike(data, ServerDogsConstant.SUB_ID_INT).enqueue(new Callback<DogInfo>() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public void getDownloadImage(NotificationGetListDogInfo notificationGetListDogInfo){
        NetworkService.getService().getJsonApi().getDownloadImage(100,
                100,
                "DESK",
                ServerDogsConstant.SUB_ID_INT,
                "json",
                1,
                1).enqueue(new Callback<List<DogInfo>>() {
            @Override
            public void onResponse(Call<List<DogInfo>> call, Response<List<DogInfo>> response) {
                new Handler(Looper.getMainLooper()).post(() -> notificationGetListDogInfo.getManyOfDogs(response.body()));
            }

            @Override
            public void onFailure(Call<List<DogInfo>> call, Throwable t) {

            }
        });
    }

    public void getFavoriteList(NotificationGetListDogInfo notificationGetListDogInfo){

        NetworkService.getService().getJsonApi().getFavoriteList(ServerDogsConstant.SUB_ID_INT).enqueue(new Callback<List<DogInfo>>() {
            @Override
            public void onResponse(Call<List<DogInfo>> call, Response<List<DogInfo>> response) {
                if (response.body() != null){
                    new Handler(Looper.getMainLooper()).post(() ->notificationGetListDogInfo.getManyOfDogs(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<DogInfo>> call, Throwable t) {

            }
        });
    }

    public void getRandomDogs(NotificationGetListDogInfo notificationGetListDogInfo) {
        NetworkService.getService().getJsonApi().getRandomDog().enqueue(new Callback<List<DogInfo>>() {
            @Override
            public void onResponse(Call<List<DogInfo>> call, Response<List<DogInfo>> response) {
                if (response.body() != null) {
                    new Handler(Looper.getMainLooper()).post(() -> notificationGetListDogInfo.getManyOfDogs(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<DogInfo>> call, Throwable t) {
            }
        });
    }

    public interface NotificationGetListDogInfo {
        void getManyOfDogs(List<DogInfo> manyOfDogs);
    }
}

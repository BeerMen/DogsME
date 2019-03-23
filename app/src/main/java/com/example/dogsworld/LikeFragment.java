package com.example.dogsworld;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LikeFragment extends Fragment implements View.OnClickListener {
    ImageView likeImage;
    TextView textName;
    TextView textCharacteristic;
    ImageView btnLike;
    ImageView btnDisLike;
    ImageButton btnFavorite;
    List<DogInfo> postDogInfo;


    FrameLayout bigLike;
    FrameLayout like;
    Gson gson = new Gson();
    private final String SaveLike = "https://api.thedogapi.com/v1/votes?sub_id=" + MainActivity.sub_id;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getDogsFromApi();
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.like_fragment,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        likeImage = view.findViewById(R.id.image_like);
        btnFavorite = view.findViewById(R.id.btn_favorite);
        btnFavorite.setOnClickListener(this::onClick);
        btnLike = view.findViewById(R.id.like);
        btnLike.setOnClickListener(this::onClick);
        btnDisLike = view.findViewById(R.id.dislike);
        btnDisLike.setOnClickListener(this::onClick);
        textName = view.findViewById(R.id.text_name_dog);
        textCharacteristic = view.findViewById(R.id.text_characteristic_dog);

        bigLike = view.findViewById(R.id.bog_like_fragnebt);
        like = view.findViewById(R.id.like_fragment);
        super.onViewCreated(view, savedInstanceState);
    }

    public void getDogsFromApi() {
        Gson gson = new Gson();
        OkHttpClient mClient = new OkHttpClient().newBuilder().build();

        Request mRequest = new Request.Builder()
                .header("x-api-key", "a00fade5-8415-4580-8fec-5fee491ce7ce")
                .url("https://api.thedogapi.com/v1/images/search")
                .build();

        mClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code" + response);

                    String resp = responseBody.string();
                    Type listType = new TypeToken<ArrayList<DogInfo>>(){}.getType();
                    List<DogInfo> manyOfDogs = gson.fromJson(resp, listType);
                    postDogInfo = manyOfDogs;
                    assignmentView(manyOfDogs);

                }
            }
        });

    }
    public void assignmentView(List<DogInfo> manyOfDogs){

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                if (manyOfDogs.get(0).breeds.isEmpty()){
                    getDogsFromApi();
                }else {
                    Picasso.get().load(manyOfDogs.get(0).url).into(likeImage);
                    textName.setText("Порода " + manyOfDogs.get(0).breeds.get(0).name);
                    textCharacteristic.setText("Длина жизни " + manyOfDogs.get(0).breeds.get(0).life_span + "\n"
                            + "Темперамент " + manyOfDogs.get(0).breeds.get(0).temperament + "\n"
                            + "Разводят для " + manyOfDogs.get(0).breeds.get(0).bred_for);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.like:
            if (postDogInfo != null ){
                CreateVoice.post(gson.toJson(new DogPost(postDogInfo.get(0).id,1,MainActivity.sub_id)),SaveLike,JSON,getActivity());
                getDogsFromApi();
                break;
            }
            break;

            case R.id.btn_favorite:
                if (postDogInfo != null ) {
                    CreateVoice.post(gson.toJson(new DogPostFavorites(postDogInfo.get(0).id,MainActivity.sub_id)),
                            "https://api.thedogapi.com/v1/favourites", JSON,getActivity());
                    break;
                }
                break;
            case R.id.dislike:
                getDogsFromApi();
                break;
        }
    }
}

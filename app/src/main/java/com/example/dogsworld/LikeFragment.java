package com.example.dogsworld;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LikeFragment extends Fragment implements View.OnClickListener {
    private ImageView likeImage;
    private TextView textName;
    private TextView textCharacteristic;

    private List<DogInfo> postDogInfo;

    private final Gson gson = new Gson();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getDogsFromApi();
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_like, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        textName = view.findViewById(R.id.text_name_dog);
        textCharacteristic = view.findViewById(R.id.text_characteristic_dog);
        likeImage = view.findViewById(R.id.image_like);

        ImageButton btnFavorite;
        btnFavorite = view.findViewById(R.id.btn_favorite);
        btnFavorite.setOnClickListener(this);

        ImageView btnLike;
        btnLike = view.findViewById(R.id.like);
        btnLike.setOnClickListener(this);

        ImageView btnDisLike;
        btnDisLike = view.findViewById(R.id.dislike);
        btnDisLike.setOnClickListener(this);

        super.onViewCreated(view, savedInstanceState);
    }

    public void getDogsFromApi() {
        Gson gson = new Gson();
        OkHttpClient mClient = new OkHttpClient().newBuilder().build();

        Request mRequest = new Request.Builder()
                .header(ServerDogsConstant.API_KAY_NAME, ServerDogsConstant.API_KAY)
                .url(ServerDogsConstant.URL_RANDOM_DOGS)
                .build();

        mClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call,@NotNull IOException e) {

                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call,@NotNull Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code" + response);

                    if (responseBody != null) {
                        String resp = responseBody.string();
                        Type listType = new TypeToken<ArrayList<DogInfo>>() {
                        }.getType();
                        List<DogInfo> manyOfDogs = gson.fromJson(resp, listType);
                        postDogInfo = manyOfDogs;
                        assignmentView(manyOfDogs);
                    }
                }
            }
        });

    }

    public void assignmentView(List<DogInfo> manyOfDogs) {

        new Handler(Looper.getMainLooper()).post(() -> {

            if (manyOfDogs.get(0).breeds.isEmpty()) {
                getDogsFromApi();
            } else {
                Picasso.get().load(manyOfDogs.get(0).url).into(likeImage);
                textName.setText(getString(R.string.breed).concat(" " + manyOfDogs.get(0).breeds.get(0).name));

                textCharacteristic.setText(getString(R.string.length_of_life).concat(" " + manyOfDogs.get(0).breeds.get(0).life_span + "\n"
                        + getString(R.string.temperament) + " " + manyOfDogs.get(0).breeds.get(0).temperament + "\n"
                        + getString(R.string.bred_for) + " " + manyOfDogs.get(0).breeds.get(0).bred_for));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.like:
                if (postDogInfo != null) {
                    CreateVoice.post(gson.toJson(new DogPost(postDogInfo.get(0).id,
                                    1,
                                    ServerDogsConstant.SUB_ID)),
                            ServerDogsConstant.URL_POST_LIKE,
                            getActivity());

                    getDogsFromApi();
                    break;
                }
                break;

            case R.id.btn_favorite:
                if (postDogInfo != null) {
                    CreateVoice.post(gson.toJson(new DogPostFavorites(postDogInfo.get(0).id, ServerDogsConstant.SUB_ID)),
                            ServerDogsConstant.URL_POST_FAVORITE, getActivity());
                    break;
                }
                break;
            case R.id.dislike:
                getDogsFromApi();
                break;
        }
    }
}

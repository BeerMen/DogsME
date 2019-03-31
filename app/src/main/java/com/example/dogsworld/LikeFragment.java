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
import android.widget.Toast;

import com.example.dogsworld.network.ApiAnswer;
import com.example.dogsworld.network.NetworkService;
import com.example.dogsworld.network.ServerDogsConstant;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class LikeFragment extends Fragment implements View.OnClickListener {
    private ImageView likeImage;
    private TextView textName;
    private TextView textCharacteristic;

    private List<DogInfo> postDogInfo;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDogsFromApi();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_like, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    }

    public void getDogsFromApi() {
        NetworkService.getService().getJsonApi().getRandomDog().enqueue(new Callback<List<DogInfo>>() {
            @Override
            public void onResponse(Call<List<DogInfo>> call, Response<List<DogInfo>> response) {
                postDogInfo = response.body();
                assignmentView(response.body());
            }

            @Override
            public void onFailure(Call<List<DogInfo>> call, Throwable t) {
                Timber.w(t);
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

    public void postLike() {
        NetworkService.getService().getJsonApi().postLike(new DogPost(postDogInfo.get(0).id,
                        1,
                        ServerDogsConstant.SUB_ID),
                ServerDogsConstant.SUB_ID_INT).enqueue(new Callback<DogInfo>() {
            @Override
            public void onResponse(Call<DogInfo> call, Response<DogInfo> response) {
                Timber.d(response.message());
            }

            @Override
            public void onFailure(Call<DogInfo> call, Throwable t) {
                Timber.w(t);
            }
        });
    }

    public void postFavorite() {
        NetworkService.getService().getJsonApi().postFavorite(new DogPostFavorites(postDogInfo.get(0).id,
                        ServerDogsConstant.SUB_ID),
                ServerDogsConstant.SUB_ID_INT).enqueue(new Callback<ApiAnswer>() {
            @Override
            public void onResponse(Call<ApiAnswer> call, Response<ApiAnswer> response) {
                responseStatus(response.body());
            }

            @Override
            public void onFailure(Call<ApiAnswer> call, Throwable t) {
                Timber.w(t);
            }
        });
    }

    public void responseStatus(ApiAnswer apiAnswer) {
        if (apiAnswer.getStatus() == 400) {
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getActivity(),
                    R.string.already_added_favorites,
                    Toast.LENGTH_LONG).show());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.like:
                if (postDogInfo != null) {
                    postLike();
                    getDogsFromApi();
                    break;
                }
                break;

            case R.id.btn_favorite:
                if (postDogInfo != null) {
                    postFavorite();
                    break;
                }
                break;
            case R.id.dislike:
                getDogsFromApi();
                break;
        }
    }
}

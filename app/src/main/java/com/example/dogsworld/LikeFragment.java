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

import com.example.dogsworld.network.NetworkApi;
import com.example.dogsworld.network.ServerDogsConstant;
import com.squareup.picasso.Picasso;
import java.util.List;

public class LikeFragment extends Fragment implements View.OnClickListener {
    private ImageView likeImage;
    private TextView textName;
    private TextView textCharacteristic;

    private List<DogInfo> postDogInfo;

    private NetworkApi networkApi;

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

        networkApi = new NetworkApi();

        super.onViewCreated(view, savedInstanceState);
    }

    public void getDogsFromApi() {
        networkApi.getRandomDogs(manyOfDogs -> {
            postDogInfo = manyOfDogs;
            assignmentView(manyOfDogs);
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
                    networkApi.postLike(new DogPost(postDogInfo.get(0).id,1, ServerDogsConstant.SUB_ID));
                    getDogsFromApi();
                    break;
                }
                break;

            case R.id.btn_favorite:
                if (postDogInfo != null) {
                    networkApi.postFavorite(new DogPostFavorites(postDogInfo.get(0).id,ServerDogsConstant.SUB_ID),getActivity());
                    break;
                }
                break;
            case R.id.dislike:
                getDogsFromApi();
                break;
        }
    }
}

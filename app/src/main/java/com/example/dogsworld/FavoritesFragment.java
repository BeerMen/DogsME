package com.example.dogsworld;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

public class FavoritesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    DogsAdapter dogsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

//        ApiClient apiClient = new ApiClient();
//
//        apiClient.getDogs(new DogResult() {
//            @Override
//            public void onResult(List<DogInfo> dogs) {
//
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        })


        recyclerView = view.findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        dogsAdapter = new DogsAdapter();
        recyclerView.setAdapter(dogsAdapter);


        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        getFavoriteList();
        super.onActivityCreated(savedInstanceState);
    }

    public void getFavoriteList() {
        Gson gson = new Gson();
        OkHttpClient mClient;
        mClient = new OkHttpClient().newBuilder().build();

        Request mRequest = new Request.Builder()
                .header(ServerDogsConstant.API_KAY_NAME, ServerDogsConstant.API_KAY)
                .url(ServerDogsConstant.URL_GET_FAVORITE_LIST)
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

                    if (responseBody != null){
                        String resp = responseBody.string();
                        Type listType = new TypeToken<ArrayList<DogInfo>>() {
                        }.getType();
                        List<DogInfo> manyOfDogs = gson.fromJson(resp, listType);
                        setListFavorites(manyOfDogs);
                    }

                }
            }
        });
    }

    private void setListFavorites(List<DogInfo> manyOfDogs) {

        List<String> listImageId = new ArrayList<>();
        for (int i = manyOfDogs.size() - 1; i > -1; i--) {
            listImageId.add(manyOfDogs.get(i).image_id);
        }
        recyclerView.post(() -> dogsAdapter.addUrls(listImageId));
    }

    @Override
    public void onRefresh() {
        getFavoriteList();
        swipeRefreshLayout.post(() ->
            swipeRefreshLayout.setRefreshing(false));
    }
}

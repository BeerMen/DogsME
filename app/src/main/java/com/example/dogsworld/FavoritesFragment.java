package com.example.dogsworld;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ApiClient apiClient = new ApiClient();

        apiClient.getDogs(new DogResult() {
            @Override
            public void onResult(List<DogInfo> dogs) {

            }

            @Override
            public void onError(Exception e) {

            }
        })


        mRecyclerView = view.findViewById(R.id.recycler);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        getFavoriteList(MainActivity.SUB_ID);
        super.onActivityCreated(savedInstanceState);
    }

    public void getFavoriteList(String sub_id) {
        Gson gson = new Gson();
        OkHttpClient mClient;
        mClient = new OkHttpClient().newBuilder().build();

        Request mRequest = new Request.Builder()
                .header("x-api-key", "a00fade5-8415-4580-8fec-5fee491ce7ce")
                .url("https://api.thedogapi.com/v1/favourites?SUB_ID=" + sub_id)
                .build();

        mClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code" + response);

                    String resp = responseBody.string();
                    Type listType = new TypeToken<ArrayList<DogInfo>>() {
                    }.getType();
                    List<DogInfo> manyOfDogs = gson.fromJson(resp, listType);
                    setListFavorites(manyOfDogs);
                }
            }
        });
    }

    private void setListFavorites(List<DogInfo> manyOfDogs) {

        List<String> mStringArrayList = new ArrayList<>();
        for (int i = manyOfDogs.size() - 1; i > -1; i--) {
            mStringArrayList.add(manyOfDogs.get(i).image_id);
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(linearLayoutManager);
                mRecyclerView.setHasFixedSize(true);
                DogsAdapter dogsAdapter = new DogsAdapter();
                mRecyclerView.setAdapter(dogsAdapter);
            }
        });
    }

    @Override
    public void onRefresh() {
        getFavoriteList(MainActivity.SUB_ID);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },2000);
    }
}

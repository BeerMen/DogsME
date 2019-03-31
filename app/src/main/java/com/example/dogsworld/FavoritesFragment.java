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

import com.example.dogsworld.network.NetworkService;
import com.example.dogsworld.network.ServerDogsConstant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class FavoritesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    DogsAdapter dogsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        dogsAdapter = new DogsAdapter();
        recyclerView.setAdapter(dogsAdapter);

        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getFavoriteList();
    }

    public void getFavoriteList() {
        NetworkService.getService().getJsonApi().getFavoriteList(ServerDogsConstant.SUB_ID_INT)
                .enqueue(new Callback<List<DogInfo>>() {
                    @Override
                    public void onResponse(Call<List<DogInfo>> call, Response<List<DogInfo>> response) {
                        if (response.body() != null) {
                            setListFavorites(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DogInfo>> call, Throwable t) {
                        Timber.w(t);
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

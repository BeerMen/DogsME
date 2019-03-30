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

import com.example.dogsworld.network.NetworkApi;
import java.util.ArrayList;
import java.util.List;

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
        new NetworkApi().getFavoriteList(this::setListFavorites);
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

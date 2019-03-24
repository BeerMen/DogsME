package com.example.dogsworld;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    public static final String SUB_ID = "44332213";

    private FrameLayout like;
    private FrameLayout upload;
    private FrameLayout favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        like = findViewById(R.id.like_fragment);
        upload = findViewById(R.id.upload_fragment);
        favorites = findViewById(R.id.favorites_fragment);

        setupFragments();

        BottomNavigationView btnNavView = findViewById(R.id.navigation);
        btnNavView.setOnNavigationItemSelectedListener(this::onMenuSelected);
    }

    private void setupFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        LikeFragment likeFragment = new LikeFragment();
        UploadFragment uploadFragment = new UploadFragment();
        FavoritesFragment favoritesFragment = new FavoritesFragment();

        fragmentTransaction.add(R.id.like_fragment, likeFragment, UploadFragment.class.getSimpleName());
        fragmentTransaction.add(R.id.favorites_fragment, favoritesFragment, FavoritesFragment.class.getSimpleName());
        fragmentTransaction.add(R.id.upload_fragment, uploadFragment, UploadFragment.class.getSimpleName());

        fragmentTransaction.commit();
    }

    private boolean onMenuSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.like_menu:
                like.setVisibility(View.VISIBLE);
                upload.setVisibility(View.GONE);
                favorites.setVisibility(View.GONE);
                break;

            case R.id.download:
                like.setVisibility(View.GONE);
                upload.setVisibility(View.VISIBLE);
                favorites.setVisibility(View.GONE);

                UploadFragment uploadFragment = getUploadFragment();
                if (uploadFragment != null) {
                    uploadFragment.downImg(SUB_ID);
                }

                break;

            case R.id.favorite:
                like.setVisibility(View.GONE);
                upload.setVisibility(View.GONE);
                favorites.setVisibility(View.VISIBLE);

                FavoritesFragment favoritesFragment = getFavoritesFragment();
                if (favoritesFragment != null) {
                    favoritesFragment.getFavoriteList(SUB_ID);
                }

                break;
        }

        return false;
    }

    @Nullable
    private UploadFragment getUploadFragment() {
        return (UploadFragment) getSupportFragmentManager().findFragmentByTag(UploadFragment.class.getSimpleName());
    }

    @Nullable
    private FavoritesFragment getFavoritesFragment() {
        return (FavoritesFragment) getSupportFragmentManager().findFragmentByTag(FavoritesFragment.class.getSimpleName());
    }

}

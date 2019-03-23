package com.example.dogsworld;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    FrameLayout like ;
    FrameLayout upload ;
    FrameLayout favorites ;
    BottomNavigationView btnNavView;
    public static final String sub_id = "44332213";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CreateVoice createVoice = new CreateVoice(this);
        setContentView(R.layout.activity_main);
        like = findViewById(R.id.like_fragment);
        upload = findViewById(R.id.upload_fragment);
        favorites = findViewById(R.id.favorites_fragment);
        btnNavView = findViewById(R.id.navigation);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LikeFragment likeFragment = new LikeFragment();
        UploadFragment uploadFragment = new UploadFragment();
        FavoritesFragment favoritesFragment = new FavoritesFragment();

        fragmentTransaction.add(R.id.like_fragment,likeFragment);

        fragmentTransaction.add(R.id.favorites_fragment,favoritesFragment);

        fragmentTransaction.add(R.id.upload_fragment,uploadFragment);
        fragmentTransaction.commit();

        btnNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.like_menu:
                        like.setVisibility(View.VISIBLE);
                        upload.setVisibility(View.GONE);
                        favorites.setVisibility(View.GONE);
                        break;
                    case R.id.download:
                        like.setVisibility(View.GONE);
                        upload.setVisibility(View.VISIBLE);
                        favorites.setVisibility(View.GONE);
                        uploadFragment.downImg(sub_id);
                        break;
                    case R.id.favorite:
                        like.setVisibility(View.GONE);
                        upload.setVisibility(View.GONE);
                        favorites.setVisibility(View.VISIBLE);
                        favoritesFragment.getFavoriteList(sub_id);
                        break;
                }
                return false;
            }
        });
    }
}

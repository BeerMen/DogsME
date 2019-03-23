package com.example.dogsworld;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DogsAdapter extends RecyclerView.Adapter<DogsAdapter.DogViewHolder>{
    int numerItem;
    List<String> urls;
    public DogsAdapter(int numberItem, List<String> urls) {
        this.numerItem = numberItem;
        this.urls = urls;
    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        return new DogViewHolder(inflater.inflate(R.layout.list_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder dogViewHolder, int i) {
        dogViewHolder.bind(urls.get(i));
    }

    @Override
    public int getItemCount() {
        return numerItem;
    }

    class DogViewHolder extends RecyclerView.ViewHolder {

        ImageView imDogs;
        public DogViewHolder(@NonNull View itemView) {
            super(itemView);
            imDogs = itemView.findViewById(R.id.ivImg);
        }
        void bind(String imageUrl){
            boolean isComEnding = imageUrl.contains("https");
            if (isComEnding){
                Picasso.get().load(imageUrl).fit().into(imDogs);
            }else {
                Picasso.get().load("https://cdn2.thedogapi.com/images/" + imageUrl + ".jpg").fit().into(imDogs);
            }
        }
    }
}

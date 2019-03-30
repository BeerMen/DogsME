package com.example.dogsworld;

 public class DogPost {
    public String image_id;
    public int value;
    public String sub_id;

     DogPost(String image_id, int value, String sub_id) {
        this.image_id = image_id;
        this.value = value;
        this.sub_id = sub_id;
    }
}

package com.example.dogsworld;

import java.util.List;

public class DogInfo {
    String id;
    String url;
    String image_id;

    List<Breeds> breeds;

    class Breeds {
        int id;
        String name;
        String bred_for;
        String breed_group;
        String life_span;
        String temperament;
    }
}


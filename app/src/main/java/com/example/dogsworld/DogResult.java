package com.example.dogsworld;

import java.util.List;

public interface DogResult {

    void onResult(List<DogInfo> dogs);

    void onError(Exception e);

}

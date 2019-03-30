package com.example.dogsworld.network;

import com.example.dogsworld.DogInfo;
import com.example.dogsworld.DogPost;
import com.example.dogsworld.DogPostFavorites;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PlaceHolderApi {

    @Headers(ServerDogsConstant.API_KAY_AND_NAME)
    @GET("/v1/images/search")
    Call<List<DogInfo>> getRandomDog();

    @Headers(ServerDogsConstant.API_KAY_AND_NAME)
    @GET("/v1/favourites?")
    Call<List<DogInfo>> getFavoriteList(@Query("SUB_ID") int subId);

    @Headers(ServerDogsConstant.API_KAY_AND_NAME)
    @POST("/v1/votes?")
    Call<DogInfo> postLike(@Body DogPost data, @Query("SUB_ID") int subId);

    @Headers(ServerDogsConstant.API_KAY_AND_NAME)
    @POST("/v1/favourites")
    Call<ApiAnswer> postFavorite(@Body DogPostFavorites data, @Query("SUB_ID") int subId);

    @Headers(ServerDogsConstant.API_KAY_AND_NAME)
    @POST("/v1/images/upload")
    Call<ApiAnswer> postUploadImage(@Body RequestBody body);

    @Headers(ServerDogsConstant.API_KAY_AND_NAME)
    @GET("v1/images")
    Call<List<DogInfo>> getDownloadImage(@Query("limit") int limit,
                                         @Query("page") int page,
                                         @Query("order") String desk,
                                         @Query("SUB_ID") int subId,
                                         @Query("format") String json,
                                         @Query("include_vote") int includeVote,
                                         @Query("include_favourite") int includeFavourite);
}

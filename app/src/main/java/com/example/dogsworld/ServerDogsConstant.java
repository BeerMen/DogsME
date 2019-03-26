package com.example.dogsworld;

import okhttp3.MediaType;

class ServerDogsConstant {
    static final String SUB_ID = "44332213";

    static final String API_KAY_NAME = "x-api-key";
    static final String API_KAY = "a00fade5-8415-4580-8fec-5fee491ce7ce";

    static final String URL_RANDOM_DOGS = "https://api.thedogapi.com/v1/images/search";

    static final String URL_POST_LIKE = "https://api.thedogapi.com/v1/votes?SUB_ID=" + SUB_ID;
    static final String URL_POST_FAVORITE = "https://api.thedogapi.com/v1/favourites";

    static final String URL_GET_UPLOADED_PHOTOS = "https://api.thedogapi.com/v1/images?limit=100&page=100&order=DESK&SUB_ID="
            + SUB_ID
            + "&format=json&include_vote=1&include_favourite=1";

    static final String URL_UPLOAD_IMAGE = "https://api.thedogapi.com/v1/images/upload";

    static final String URL_GET_FAVORITE_LIST = "https://api.thedogapi.com/v1/favourites?SUB_ID=" + SUB_ID;

    static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
}

package com.example.dogsworld;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UploadFragment extends Fragment implements View.OnClickListener {
    Button btnUpload;
    Button btnLoad;
    RecyclerView mRecyclerView;
    private static final int READ_REQUEST_CODE = 42;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    Uri uri = Uri.parse(data.getData().toString());
                    Log.d("TegDBag", "Uri: " + uri);
                    File file = FileUtils.getFileFromUri(getActivity(), uri);
                    Log.d("TegDBag","File: " + file);
                    UploadImage.uploadImage(MainActivity.sub_id,file,getActivity());

                } catch (Exception x){
                    Log.d("TegDBag", "Uri: " + x.toString());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.upload_fragment,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnUpload = view.findViewById(R.id.click_me);
        btnLoad = view.findViewById(R.id.click_me1);
        mRecyclerView = view.findViewById(R.id.recycler_upload);
        btnLoad.setOnClickListener(this::onClick);
        btnUpload.setOnClickListener(this::onClick);
        imgPermissions();
        super.onViewCreated(view, savedInstanceState);
    }

    public void imgPermissions(){
        if (ContextCompat.checkSelfPermission (getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.click_me:
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(i, READ_REQUEST_CODE);
                break;

            case R.id.click_me1:
                downImg(MainActivity.sub_id);
                break;
        }
    }
    public void downImg(String sub_id){

        Request mRequest = new Request.Builder()
                .header("x-api-key", "a00fade5-8415-4580-8fec-5fee491ce7ce")
                .url("https://api.thedogapi.com/v1/images?limit=100&page=100&order=DESK&sub_id="+
                        sub_id + "&format=json&include_vote=1&include_favourite=1")
                .build();

        Gson gson = new Gson();
        OkHttpClient mClient = new OkHttpClient();
        mClient.newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TegDBag",e.toString() );
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code" + response);

                    String resp = responseBody.string();
                    Type listType = new TypeToken<ArrayList<DogInfo>>(){}.getType();
                    List<DogInfo> manyOfDogs = gson.fromJson(resp, listType);
                    if (resp.equals("[]")){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),"Нету загруженных фотографий",Toast.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        installationOfPictures(manyOfDogs);
                    }
                }
            }
        });
    }
    public void installationOfPictures(List<DogInfo> test){

        List<String> mStringArrayList = new ArrayList<>();

        for (int i = 0;i < test.size();i++){
            mStringArrayList.add(test.get(i).url);
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(linearLayoutManager);
                mRecyclerView.setHasFixedSize(true);
                DogsAdapter dogsAdapter = new DogsAdapter(mStringArrayList.size(),mStringArrayList);
                mRecyclerView.setAdapter(dogsAdapter);
            }
        });

    }
}

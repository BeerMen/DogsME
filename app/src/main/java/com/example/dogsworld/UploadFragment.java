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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dogsworld.network.ApiAnswer;
import com.example.dogsworld.network.NetworkService;
import com.example.dogsworld.network.ServerDogsConstant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class UploadFragment extends Fragment {

    private static final int READ_REQUEST_CODE = 42;

    RecyclerView mRecyclerView;
    DogsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recycler_upload);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        adapter = new DogsAdapter();
        mRecyclerView.setAdapter(adapter);

        view.findViewById(R.id.click_me).setOnClickListener(v -> requestImage());
        view.findViewById(R.id.click_me1).setOnClickListener(v -> downImg());

        requestPermissions();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            onFileResult(data);
        }
    }

    private void onFileResult(Intent intent) {
        if (intent.getData() == null) {
            Timber.w("No data from intent for file result.");
            return;
        }

        try {
            Uri uri = Uri.parse(intent.getData().toString());
            File file = FileUtils.getFileFromUri(getActivity(), uri);

            getRequestBody(file);
        } catch (Exception e) {
            Timber.w(e, "Cannot process intent from result.");
        }
    }

    public void getRequestBody(File file){
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        if (file != null) {
            downloadFile(new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("SUB_ID", ServerDogsConstant.SUB_ID)
                    .addFormDataPart("file", "file", RequestBody.create(MEDIA_TYPE_PNG, file))
                    .build());
        }
    }

    public void downloadFile(RequestBody requestBody){
        NetworkService.getService().getJsonApi().postUploadImage(requestBody).enqueue(new Callback<ApiAnswer>() {
                    @Override
                    public void onResponse(Call<ApiAnswer> call, Response<ApiAnswer> response) {
                        if (response.body() != null) {
                            responseStatus(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiAnswer> call, Throwable t) {
                        Timber.w(t);
                    }
                });
    }

    public void responseStatus(ApiAnswer apiAnswer) {
        if (apiAnswer.getStatus() == 400) {
            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(getActivity(), R.string.no_dog
                            , Toast.LENGTH_LONG).show());
        } else {
            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(getActivity(), R.string.good_upload,
                            Toast.LENGTH_LONG).show());
        }
    }

    public void requestPermissions() {
        if (getActivity() == null) {
            return;
        }

        if (isPermissionNotGranted()) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0
            );
        }
    }

    private boolean isPermissionNotGranted() {
        if (getActivity() != null) {
            return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    private void requestImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void downImg() {
        NetworkService.getService().getJsonApi().getDownloadImage(100,
                100,
                "DESK",
                ServerDogsConstant.SUB_ID_INT,
                "json",
                1,
                1).enqueue(new Callback<List<DogInfo>>() {
            @Override
            public void onResponse(Call<List<DogInfo>> call, Response<List<DogInfo>> response) {
                checkingUploadedPhotos(response.body());
            }

            @Override
            public void onFailure(Call<List<DogInfo>> call, Throwable t) {
                Timber.w(t);
            }
        });
    }

    public void checkingUploadedPhotos(List<DogInfo> manyOfDogs){
        if (manyOfDogs.get(0).url.isEmpty()) {
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(getActivity(), R.string.no_download_image, Toast.LENGTH_LONG).show());
            } else {
                installationOfPictures(manyOfDogs);
            }
    }

    public void installationOfPictures(List<DogInfo> dogs) {
        List<String> uploadUrls = new ArrayList<>();

        for (DogInfo dogInfo : dogs) {
            uploadUrls.add(dogInfo.url);
        }

        mRecyclerView.post(() -> adapter.addUrls(uploadUrls));
    }
}

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

import com.example.dogsworld.network.NetworkApi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
            new NetworkApi().postUploadImage(file,getActivity());
        } catch (Exception e) {
            Timber.w(e, "Cannot process intent from result.");
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
        new NetworkApi().getDownloadImage(manyOfDogs -> {
            if (manyOfDogs.get(0).url.isEmpty()){
                new Handler(Looper.getMainLooper()).post(() ->
                               Toast.makeText(getActivity(), R.string.no_download_image, Toast.LENGTH_LONG).show());
            }else {
                installationOfPictures(manyOfDogs);
            }
        });
    }

    public void installationOfPictures(List<DogInfo> dogs) {
        List<String> uploadUrls = new ArrayList<>();

        for (DogInfo dogInfo : dogs) {
            uploadUrls.add(dogInfo.url);
        }

        mRecyclerView.post(() -> adapter.addUrls(uploadUrls));
    }
}

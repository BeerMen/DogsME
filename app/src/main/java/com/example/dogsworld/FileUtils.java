package com.example.dogsworld;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;

import timber.log.Timber;

 class FileUtils {

     static File getFileFromUri(Context context, Uri uri) {
        if (context == null) {
            Timber.d("Context is null.");
            return null;
        }

        if (uri == null) {
            Timber.d("No uri.");
            return null;
        }

        if (!"content".equals(uri.getScheme())) {
            Timber.d("Uri with invalid scheme.");
            return null;
        }

        String documentId = DocumentsContract.getDocumentId(uri);
        String id = documentId.split(":")[1];

        String[] projection = { MediaStore.Images.Media.DATA };
        String filter = MediaStore.Images.Media._ID + " = ?";

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, filter, new String[]{ id }, null
        );

        if (cursor == null || cursor.getCount() == 0) {
            Timber.d("No result found.");
            return null;
        }

        if (!cursor.moveToFirst()) {
            Timber.d("Cannot move cursor for result.");
            return null;
        }

        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
        cursor.close();
        if (TextUtils.isEmpty(path)) {
            Timber.d( "Result is empty.");
            return null;
        }

        Timber.d("file.%s ", path);
        return new File(path);
    }

}

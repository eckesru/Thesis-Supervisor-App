package de.iu.iwmb02_iu_betreuer_app.data.dao;

import android.graphics.Bitmap;
import android.net.Uri;

import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public interface FileStorageDao {
    void downloadImage(String imageUrl, Callback<Bitmap> callback);
    void uploadExpose(Uri file, Callback<Uri> downloadUri);
    void downloadExpose(String downloadUri, Callback<byte[]> callback);
}

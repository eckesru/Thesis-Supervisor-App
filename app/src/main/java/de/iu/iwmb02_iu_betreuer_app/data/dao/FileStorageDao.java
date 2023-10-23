package de.iu.iwmb02_iu_betreuer_app.data.dao;

import android.graphics.Bitmap;
import android.media.Image;

import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public interface FileStorageDao {
    void downloadImage(String imageUrl, Callback<Bitmap> callback);
    void uploadImage(Image image);
}

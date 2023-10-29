package de.iu.iwmb02_iu_betreuer_app.data.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class FirebaseStorageDao implements FileStorageDao{
    private static FirebaseStorageDao instance;
    private static final String TAG = "FirebaseStorageDao";
    private static final long MAX_DOWNLOADSIZE = 1024 * 1024;

    private final StorageReference storageRef;

    public static FirebaseStorageDao getInstance() {
        if (instance == null) {
            instance = new FirebaseStorageDao();
        }
        return instance;
    }

    private FirebaseStorageDao() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    @Override
    public void downloadImage(String imageUrl, Callback<Bitmap> callback) {
        StorageReference imageRef = storageRef.child(imageUrl);
        imageRef.getBytes(MAX_DOWNLOADSIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d(TAG, "Image dowloaded: "+imageUrl);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                callback.onCallback(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to download image: ", e);
            }
        });
    }

    @Override
    public void uploadExpose(Uri file, Callback<Uri> callback) {
        StorageReference ref = storageRef.child("exposes/"+file.getLastPathSegment());
        UploadTask uploadTask = ref.putFile(file);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                callback.onCallback(uri);
                Log.d(TAG, "Expose successfully uploaded");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Not able to generate download uri", e);
            }
        });

    }
}

package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.ThesisStateEnum;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseStorageDao;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.model.Thesis;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class SuccessThesisSubmittedActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private static final String TAG = "SuccessThesisSubmittedActivity";
    private final Context context = SuccessThesisSubmittedActivity.this;
    private FirebaseAuth auth;
    private FirebaseFirestoreDao firebaseFirestoreDao;
    private FirebaseStorageDao firebaseStorageDao;
    private Thesis thesis;
    private Supervisor primarySupervisor;
    private TextView thesisSubmittedTopicTextView;
    private TextView thesisSubmittedExposeTextView;
    private TextView thesisSubmittedStateTextView;
    private TextView thesisSubmittedSupervisorTextView;
    private TextView thesisSubmittedEmailTextView;
    private ImageButton exposeDownloadButton;
    private String exposeTitle;
    private String exposeDownloadUri;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_thesis_submitted);

        auth = FirebaseAuth.getInstance();
        firebaseFirestoreDao = FirebaseFirestoreDao.getInstance();
        firebaseStorageDao = FirebaseStorageDao.getInstance();

        thesisSubmittedTopicTextView = findViewById(R.id.thesisSubmittedTopicTextView);
        thesisSubmittedExposeTextView = findViewById(R.id.thesisSubmittedExposeTextView);
        thesisSubmittedStateTextView = findViewById(R.id.thesisSubmittedStateTextView);
        thesisSubmittedSupervisorTextView = findViewById(R.id.thesisSubmittedSupervisorTextView);
        thesisSubmittedEmailTextView = findViewById(R.id.thesisSubmittedEmailTextView);
        exposeDownloadButton = findViewById(R.id.exposeDownloadButton);

        toolbar = findViewById(R.id.materialToolbar);

        getThesisFromIntentExtra();
        getUsers();
        getExpose();

        setOnClickListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(this);
    }

    private void getThesisFromIntentExtra() {
        Intent intent = getIntent();
        if (intent.hasExtra("thesis")) {
            thesis = (Thesis) intent.getSerializableExtra("thesis");

            thesisSubmittedTopicTextView.setText(getString(R.string.topic_string_placeholder, thesis.getTitle()));
            thesisSubmittedStateTextView.setText(getString(R.string.thesis_state_string_placeholder, ThesisStateEnum.getLocalizedString(context, thesis.getThesisState())));
        }
    }

    private void getUsers() {
        if (!thesis.getPrimarySupervisorId().isEmpty()) {
            firebaseFirestoreDao.getSupervisor(thesis.getPrimarySupervisorId(), new Callback<Supervisor>() {
                @Override
                public void onCallback(Supervisor ps) {
                    primarySupervisor = ps;
                    thesisSubmittedSupervisorTextView.setText(getString(R.string.supervisor_name_string_placeholder, ps.getFullName()));
                    thesisSubmittedEmailTextView.setText(getString(R.string.email_string_placeholder, ps.getEmail()));
                }
            });
        }
    }

    private void getExpose() {
        exposeDownloadUri = thesis.getExposeDownloadUri();
        if (exposeDownloadUri.isEmpty()) {
            thesisSubmittedExposeTextView.setText(getString(R.string.expose_string_placeholder, getString(R.string.empty)));
        } else {
            exposeTitle = thesis.getExposeTitle();
            thesisSubmittedExposeTextView.setText(getString(R.string.expose_string_placeholder,exposeTitle));
        }
    }

    private void setOnClickListeners() {
        exposeDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(exposeDownloadUri.isEmpty()){return;}
                downloadExpose();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menuItem_logout) {
                    logOutUser();
                    return true;
                }
                return false;
            }
        });
    }

    private void downloadExpose() {
        firebaseStorageDao.downloadExpose(exposeDownloadUri, new Callback<byte[]>() {
            @Override
            public void onCallback(byte[] bytes) {
                File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);;
                File data = new File(downloadDir, exposeTitle);
                try {
                    FileOutputStream fos = new FileOutputStream(data);
                    fos.write(bytes);
                    Log.d(TAG, "Download completed");
                } catch (IOException e) {
                    Log.e(TAG, "Writing PDF file failed", e);
                }
                Toast.makeText(context,R.string.expose_download_message,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            Log.d(TAG, "onAuthStateChanged: No user signed in. Starting LoginActivity");
            ActivityStarter.startLoginActivity(context);
            this.finish();
        }
    }

    private void logOutUser(){
        Log.d(TAG, "logOutUser: Logging out");
        AuthUI.getInstance().signOut(this);
        Toast.makeText(context, getString(R.string.logged_out), Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
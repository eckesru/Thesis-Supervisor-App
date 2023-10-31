package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

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

        toolbar = findViewById(R.id.materialToolbar);

        getThesisFromIntentExtra();
        getUsers();
        getExpose();

        setOnClickListeners();
    }

    private void getThesisFromIntentExtra() {
        Intent intent = getIntent();
        if (intent.hasExtra("thesis")) {
            thesis = (Thesis) intent.getSerializableExtra("thesis");

            thesisSubmittedTopicTextView.setText(getString(R.string.topic_string_placeholder, thesis.getTitle()));
            thesisSubmittedStateTextView.setText(getString(R.string.thesis_state_string_placeholder, ThesisStateEnum.getLocalizedString(context, thesis.getThesisState())));
            //TODO: expose title from thesis object
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
        if (thesis.getExposeDownloadUri().isEmpty()) {
            thesisSubmittedExposeTextView.setText(getString(R.string.expose_string_placeholder, getString(R.string.empty)));
        } else {
            thesisSubmittedExposeTextView.setText(getString(R.string.expose_string_placeholder, thesis.getExposeTitle()));
            //TODO: implement download here
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private void setOnClickListeners() {
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
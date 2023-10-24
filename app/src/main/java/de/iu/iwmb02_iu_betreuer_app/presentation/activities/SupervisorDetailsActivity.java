package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseStorageDao;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class SupervisorDetailsActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{

    private static final String TAG = "SupervisorDetailsActivity";
    private final Context context = SupervisorDetailsActivity.this;
    private Supervisor supervisor;
    private FirebaseStorageDao firebaseStorageDao;

    private ImageButton supervisorDetailsBackButton;
    private ImageView menuItem_logout;
    private ImageView supervisorImageView;
    private TextView supervisorNameTextView;
    private TextView supervisorEmailTextView;
    private TextView supervisorDescriptionTextView;
    private TextView studyFieldsListTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_details);
        firebaseStorageDao = FirebaseStorageDao.getInstance();

        supervisorDetailsBackButton = findViewById(R.id.supervisorDetailsBackButton);
        menuItem_logout = findViewById(R.id.menuItem_logout);

        supervisorImageView = findViewById(R.id.supervisorImageView);
        supervisorNameTextView = findViewById(R.id.supervisorNameTextView);
        supervisorEmailTextView = findViewById(R.id.supervisorEmailTextView);
        supervisorDescriptionTextView = findViewById(R.id.supervisorDescriptionTextView);
        studyFieldsListTextView = findViewById(R.id.studyFieldsListTextView);

        setOnClickListeners();

        getTutorFromIntentExtra();
        displayProfileImage();
        fillSupervisorDetails();
    }


    private void getTutorFromIntentExtra() {
        Intent intent = getIntent();
        if(intent.hasExtra("user")){
            supervisor = (Supervisor) intent.getSerializableExtra("user");
        }
    }

    private void displayProfileImage() {
        firebaseStorageDao.downloadImage(supervisor.getProfilePictureUrl(), new Callback<Bitmap>() {
            @Override
            public void onCallback(Bitmap bmp) {
                supervisorImageView.setImageBitmap(bmp);
            }
        });
    }

    private void fillSupervisorDetails() {
        supervisorNameTextView.setText(supervisor.getFullName());
        supervisorEmailTextView.setText(supervisor.getEmail());
        supervisorDescriptionTextView.setText(supervisor.getProfileDescription());

        StringBuilder sb = new StringBuilder();
        for (String studyfield: supervisor.getStudyFields()){
            sb.append(studyfield + "\n");
        }
        studyFieldsListTextView.setText(sb);
    }

    public void setOnClickListeners(){
        menuItem_logout.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutUser();
            }
        });

        supervisorDetailsBackButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

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
package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseStorageDao;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class SupervisorDetailsActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{

    private static final String TAG = "SupervisorDetailsActivity";
    private final Context context = SupervisorDetailsActivity.this;

    private FirebaseAuth auth;
    private FirebaseStorageDao firebaseStorageDao;
    private Supervisor supervisor;
    private ImageButton supervisorDetailsBackButton;
    private Button startThesisRequestButton;
    private ImageView menuItem_logout;
    private ImageView supervisorImageView;
    private TextView supervisorNameTextView;
    private TextView supervisorEmailTextView;
    private TextView supervisorLanguageTextView;
    private TextView supervisorDescriptionTextView;
    private TextView studyFieldsListTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_details);

        auth = FirebaseAuth.getInstance();
        firebaseStorageDao = FirebaseStorageDao.getInstance();

        supervisorDetailsBackButton = findViewById(R.id.supervisorDetailsBackButton);
        startThesisRequestButton = findViewById(R.id.startThesisRequestButton);
        menuItem_logout = findViewById(R.id.menuItem_logout);

        supervisorImageView = findViewById(R.id.supervisorImageView);
        supervisorNameTextView = findViewById(R.id.supervisorNameTextView);
        supervisorEmailTextView = findViewById(R.id.supervisorEmailTextView);
        supervisorLanguageTextView = findViewById(R.id.supervisorLanguageTextView);
        supervisorDescriptionTextView = findViewById(R.id.supervisorDescriptionTextView);
        studyFieldsListTextView = findViewById(R.id.studyFieldsListTextView);

        setOnClickListeners();

        getSupervisorFromIntentExtra();
        displaySupervisorProfileImage();
        fillSupervisorTextViews();
    }



    private void getSupervisorFromIntentExtra() {
        Intent intent = getIntent();
        if(intent.hasExtra("user")){
            supervisor = (Supervisor) intent.getSerializableExtra("user");
        }
    }

    private void displaySupervisorProfileImage() {
        firebaseStorageDao.downloadImage(supervisor.getProfilePictureUrl(), new Callback<Bitmap>() {
            @Override
            public void onCallback(Bitmap bmp) {
                supervisorImageView.setImageBitmap(bmp);
            }
        });
    }

    private void fillSupervisorTextViews() {
        supervisorNameTextView.setText(supervisor.getFullName());
        supervisorEmailTextView.setText(this.getString(R.string.email_string_placeholder,supervisor.getEmail()));

        ArrayList<String> languageList = supervisor.getLanguages();
        StringBuilder sb = new StringBuilder(languageList.get(0));
        for (int i = 1; i<languageList.size();i++){
            sb.append(", " + languageList.get(i));
        }

        supervisorLanguageTextView.setText(this.getString(R.string.supervisor_languages_string_placeholder,sb));
        supervisorDescriptionTextView.setText(this.getString(R.string.supervisor_description_string_placeholder,supervisor.getProfileDescription()));

        sb = new StringBuilder();
        char bulletSymbol='\u2022';
        for (String studyfield: supervisor.getStudyFields()){
            sb.append(bulletSymbol + " " + studyfield + "\n");
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

        startThesisRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityStarter.startThesisRequestActivity(context, supervisor);
            }
        });
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
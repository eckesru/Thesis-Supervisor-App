package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.LanguageEnum;
import de.iu.iwmb02_iu_betreuer_app.data.StudyFieldEnum;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseStorageDao;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.model.Thesis;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class SupervisorDetailsActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{

    private static final String TAG = "SupervisorDetailsActivity";
    private final Context context = SupervisorDetailsActivity.this;

    private FirebaseAuth auth;
    private FirebaseStorageDao firebaseStorageDao;
    private FirebaseFirestoreDao firebaseFirestoreDao;
    private Supervisor supervisor;
    private Button startThesisRequestButton;
    private ImageView supervisorImageView;
    private TextView supervisorNameTextView;
    private TextView supervisorEmailTextView;
    private TextView supervisorLanguageTextView;
    private TextView supervisorDescriptionTextView;
    private TextView studyFieldsListTextView;
    private MaterialToolbar toolbar;
    private String mode;
    private Thesis thesis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_details);

        auth = FirebaseAuth.getInstance();
        firebaseFirestoreDao = FirebaseFirestoreDao.getInstance();
        firebaseStorageDao = FirebaseStorageDao.getInstance();

        startThesisRequestButton = findViewById(R.id.startThesisRequestButton);

        supervisorImageView = findViewById(R.id.supervisorImageView);
        supervisorNameTextView = findViewById(R.id.supervisorNameTextView);
        supervisorEmailTextView = findViewById(R.id.supervisorEmailTextView);
        supervisorLanguageTextView = findViewById(R.id.supervisorLanguageTextView);
        supervisorDescriptionTextView = findViewById(R.id.supervisorDescriptionTextView);
        studyFieldsListTextView = findViewById(R.id.studyFieldsListTextView);

        toolbar = findViewById(R.id.materialToolbar);
        setOnClickListeners();

        getSupervisorFromIntentExtra();
        displaySupervisorProfileImage();
        fillSupervisorTextViews();

        handleMode();

    }


    private void handleMode() {
        Intent intent = getIntent();
        if(intent.hasExtra("MODE") && intent.hasExtra("THESIS_OBJECT")) {
            mode = intent.getStringExtra("MODE");
            if (mode.equals("SELECT_SECONDARY_SUPERVISOR")) {
                thesis = (Thesis) intent.getSerializableExtra("THESIS_OBJECT");
                toolbar.setTitle(R.string.select_second_supervisor);
                startThesisRequestButton.setText(R.string.set_second_supervisor);
            }
        }
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
        StringBuilder sb = new StringBuilder(LanguageEnum.getLocalizedString(context,languageList.get(0)));
        for (int i = 1; i<languageList.size();i++){
            sb.append(", " + LanguageEnum.getLocalizedString(context,languageList.get(i)));
        }
        supervisorLanguageTextView.setText(getString(R.string.supervisor_languages_string_placeholder,sb));

        supervisorDescriptionTextView.setText(this.getString(R.string.supervisor_description_string_placeholder,supervisor.getProfileDescription()));

        sb = new StringBuilder();
        char bulletSymbol='\u2022';
        for (String studyfield: supervisor.getStudyFields()){
            sb.append(bulletSymbol + " " + StudyFieldEnum.getLocalizedString(context,studyfield) + "\n");
        }
        studyFieldsListTextView.setText(sb);
    }

    public void setOnClickListeners() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menuItem_back) {
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });


        startThesisRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode.equals("SELECT_SECONDARY_SUPERVISOR")) {
                    thesis.setSecondarySupervisorId(supervisor.getUserId());
                    firebaseFirestoreDao.updateThesis(thesis.getThesisId(),thesis);
                    ActivityStarter.startThesisDetailsActivity(context, thesis, mode);
                    finish();
                } else {
                    ActivityStarter.startThesisRequestActivity(context, supervisor);
                }
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
}
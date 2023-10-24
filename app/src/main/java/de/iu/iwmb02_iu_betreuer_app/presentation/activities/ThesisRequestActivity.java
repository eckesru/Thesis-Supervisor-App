package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseStorageDao;
import de.iu.iwmb02_iu_betreuer_app.data.dao.UserDao;
import de.iu.iwmb02_iu_betreuer_app.model.Student;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.model.User;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class ThesisRequestActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private static final String TAG = "ThesisRequestActivity";
    private final Context context = ThesisRequestActivity.this;
    private FirebaseAuth auth;
    private FirebaseStorageDao firebaseStorageDao;
    private Supervisor supervisor;
    private Student student;
    private ImageButton thesisRequestBackButton;
    private ImageButton exposeUploadButton;
    private Button submitThesisRequestButton;
    private ImageView menuItem_logout;
    private TextView supervisorNameTextView;
    private TextView studentNameTextView;
    private TextView studentStudyProgramTextView;
    private EditText thesisTitleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thesis_request);

        auth = FirebaseAuth.getInstance();
        firebaseStorageDao = FirebaseStorageDao.getInstance();

        thesisRequestBackButton = findViewById(R.id.thesisRequestBackButton);
        exposeUploadButton = findViewById(R.id.exposeUploadButton);
        submitThesisRequestButton = findViewById(R.id.submitThesisRequestButton);
        menuItem_logout = findViewById(R.id.menuItem_logout);

        supervisorNameTextView = findViewById(R.id.supervisorNameTextView);
        studentNameTextView = findViewById(R.id.studentNameTextView);
        studentStudyProgramTextView = findViewById(R.id.studentStudyProgramTextView);
        thesisTitleEditText = findViewById(R.id.thesisTitleEditText);

        setOnClickListeners();

        getSupervisorData();
        getStudentData();
    }



    private void getSupervisorData() {
        Intent intent = getIntent();
        if(intent.hasExtra("user")){
            supervisor = (Supervisor) intent.getSerializableExtra("user");
            supervisorNameTextView.setText(supervisor.getFullName());
        }
    }

    private void getStudentData() {
        UserDao userDao = FirebaseFirestoreDao.getInstance();
        if (auth.getCurrentUser() != null) {
            userDao.getUser(auth.getCurrentUser().getUid(), new Callback<User>() {
                @Override
                public void onCallback(User user) {
                    student = (Student) user;
                    studentNameTextView.setText(student.getFullName());
                    studentStudyProgramTextView.setText(student.getStudyProgram());
                }
            });
        }
    }

    public void setOnClickListeners(){
        menuItem_logout.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutUser();
            }
        });

        thesisRequestBackButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        submitThesisRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityStarter.startThesisRequestActivity(context, supervisor);
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
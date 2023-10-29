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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.StudyProgramEnum;
import de.iu.iwmb02_iu_betreuer_app.data.ThesisStateEnum;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseStorageDao;
import de.iu.iwmb02_iu_betreuer_app.model.Student;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.model.Thesis;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class ThesisDetailsActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{
    private static final String TAG = "ThesisDetailsActivity";
    private final Context context = ThesisDetailsActivity.this;
    private FirebaseAuth auth;
    private FirebaseFirestoreDao firebaseFirestoreDao;
    private FirebaseStorageDao firebaseStorageDao;
    private Thesis thesis;
    private Student student;
    private Supervisor primarySupervisor;
    private Supervisor secondarySupervisor;
    private TextView thesisDetailsStudentNameTextView;
    private TextView thesisDetailsStudentStudyProgramTextView;
    private TextView thesisDetailsStudentEmailTextView;
    private TextView thesisDetailsTopicTextView;
    private TextView thesisDetailsExposeTextView;
    private TextView thesisDetailsStateTextView;
    private TextView thesisDetailsBillingTextView;
    private TextView thesisDetailsPrimarySupervisorTextView;
    private TextView thesisDetailsSecondarySupervisorTextView;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thesis_details);

        auth = FirebaseAuth.getInstance();
        firebaseFirestoreDao = FirebaseFirestoreDao.getInstance();
        firebaseStorageDao = FirebaseStorageDao.getInstance();

        thesisDetailsStudentNameTextView = findViewById(R.id.thesisDetailsStudentNameTextView);
        thesisDetailsStudentStudyProgramTextView = findViewById(R.id.thesisDetailsStudentStudyProgramTextView);
        thesisDetailsStudentEmailTextView = findViewById(R.id.thesisDetailsStudentEmailTextView);
        thesisDetailsTopicTextView = findViewById(R.id.thesisDetailsTopicTextView);
        thesisDetailsExposeTextView = findViewById(R.id.thesisDetailsExposeTextView);
        thesisDetailsStateTextView = findViewById(R.id.thesisDetailsStateTextView);
        thesisDetailsBillingTextView = findViewById(R.id.thesisDetailsBillingTextView);
        thesisDetailsPrimarySupervisorTextView = findViewById(R.id.thesisDetailsPrimarySupervisorTextView);
        thesisDetailsSecondarySupervisorTextView = findViewById(R.id.thesisDetailsSecondarySupervisorTextView);
        toolbar = findViewById(R.id.materialToolbar);

        toolbar = findViewById(R.id.materialToolbar);

        getThesisFromIntentExtra();
        getUsers();
        getExpose();

        setOnClickListeners();
    }

    private void getThesisFromIntentExtra() {
        Intent intent = getIntent();
        if(intent.hasExtra("thesis")){
            thesis = (Thesis) intent.getSerializableExtra("thesis");

            thesisDetailsTopicTextView.setText(getString(R.string.topic_string_placeholder, thesis.getTitle()));
            thesisDetailsStateTextView.setText(getString(R.string.thesis_state_string_placeholder,ThesisStateEnum.getLocalizedString(context,thesis.getThesisState())));
            thesisDetailsBillingTextView.setText(getString(R.string.thesis_billing_string_placeholder,ThesisStateEnum.getLocalizedString(context,thesis.getBillingState())));
        }
    }

    private void getUsers() {
        if(!thesis.getStudentId().isEmpty()){
            firebaseFirestoreDao.getStudent(thesis.getStudentId(), new Callback<Student>() {
                @Override
                public void onCallback(Student s) {
                    student = s;

                    thesisDetailsStudentNameTextView.setText(getString(R.string.student_name_string_placeholder, s.getFullName()));
                    thesisDetailsStudentStudyProgramTextView.setText(getString(R.string.student_study_program_string_placeholder, StudyProgramEnum.getLocalizedString(context,s.getStudyProgram())+ " " + s.getStudyLevel()));
                    thesisDetailsStudentEmailTextView.setText(getString(R.string.email_string_placeholder, s.getEmail()));
                }
            });
        }else {
            thesisDetailsStudentNameTextView.setText(getString(R.string.student_name_string_placeholder, ""));
            thesisDetailsStudentStudyProgramTextView.setText(getString(R.string.student_study_program_string_placeholder, ""));
            thesisDetailsStudentEmailTextView.setText(getString(R.string.email_string_placeholder, ""));
        }

        if(!thesis.getPrimarySupervisorId().isEmpty()){
            firebaseFirestoreDao.getSupervisor(thesis.getPrimarySupervisorId(), new Callback<Supervisor>() {
                @Override
                public void onCallback(Supervisor ps) {
                    primarySupervisor = ps;

                    thesisDetailsPrimarySupervisorTextView.setText(getString(R.string.primary_supervisor_name_string_placeholder, ps.getFullName()));
                }
            });
        }
        if(!thesis.getSecondarySupervisorId().isEmpty()){
            firebaseFirestoreDao.getSupervisor(thesis.getSecondarySupervisorId(), new Callback<Supervisor>() {
                @Override
                public void onCallback(Supervisor ss) {
                    secondarySupervisor = ss;

                    thesisDetailsSecondarySupervisorTextView.setText(getString(R.string.secondary_supervisor_name_string_placeholder, ss.getFullName()));
                }
            });
        }else {
            thesisDetailsSecondarySupervisorTextView.setText(getString(R.string.secondary_supervisor_name_string_placeholder, " "));
        }
    }

    private void getExpose() {
        thesisDetailsExposeTextView.setText(getString(R.string.expose_string_placeholder, getFileName(Uri.parse(thesis.getexposeDownloadUri()))));
        //TODO: set metadata expose title to display here
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
                if (id == R.id.menuItem_back) {
                    onBackPressed();
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
}
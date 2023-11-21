package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.BillingStateEnum;
import de.iu.iwmb02_iu_betreuer_app.data.StudyProgramEnum;
import de.iu.iwmb02_iu_betreuer_app.data.ThesisStateEnum;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseStorageDao;
import de.iu.iwmb02_iu_betreuer_app.data.dao.UserDao;
import de.iu.iwmb02_iu_betreuer_app.model.Student;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.model.Thesis;
import de.iu.iwmb02_iu_betreuer_app.model.User;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class ThesisRequestActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private static final String TAG = "ThesisRequestActivity";
    private final Context context = ThesisRequestActivity.this;
    private FirebaseAuth auth;
    private FirebaseFirestoreDao firebaseFirestoreDao;
    private FirebaseStorageDao firebaseStorageDao;
    private Supervisor supervisor;
    private Student student;
    private Thesis thesis;
    private ImageButton exposeUploadButton;
    private Button submitThesisRequestButton;
    private TextView supervisorNameTextView;
    private TextView studentNameTextView;
    private TextView studentStudyProgramTextView;
    private TextView exposeFilePathTextView;
    private EditText thesisTitleEditText;
    private ActivityResultLauncher<Intent> pdfPickerLauncher;
    private String mode;
    private String exposeTitle;
    private Uri file;
    private boolean isValidThesisData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thesis_request);

        auth = FirebaseAuth.getInstance();
        firebaseFirestoreDao = FirebaseFirestoreDao.getInstance();
        firebaseStorageDao = FirebaseStorageDao.getInstance();

        exposeUploadButton = findViewById(R.id.exposeUploadButton);
        submitThesisRequestButton = findViewById(R.id.submitThesisRequestButton);

        supervisorNameTextView = findViewById(R.id.supervisorNameTextView);
        studentNameTextView = findViewById(R.id.studentNameTextView);
        studentStudyProgramTextView = findViewById(R.id.studentStudyProgramTextView);
        exposeFilePathTextView = findViewById(R.id.exposeFilePathTextView);
        thesisTitleEditText = findViewById(R.id.thesisTitleEditText);

        handleMode();

        getSupervisorData();
        getStudentData();

        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);
        setOnClickListeners(toolbar);

        initializePdfPicker();


    }

    private void handleMode() {
        Intent intent = getIntent();
        if(intent.hasExtra("MODE") && intent.hasExtra("THESIS_OBJECT")) {
            mode = intent.getStringExtra("MODE");
            if (mode.equals("THESIS_FROM_TOPIC")) {
                thesis = (Thesis) intent.getSerializableExtra("THESIS_OBJECT");
                thesisTitleEditText.setText(thesis.getTitle());
                thesisTitleEditText.setFocusable(false);
                thesisTitleEditText.setFocusableInTouchMode(false);
                thesisTitleEditText.setClickable(false);
                thesisTitleEditText.setInputType(InputType.TYPE_NULL);

                firebaseFirestoreDao.getSupervisor(thesis.getPrimarySupervisorId(), new Callback<Supervisor>() {
                    @Override
                    public void onCallback(Supervisor s) {
                       supervisor = s;
                       supervisorNameTextView.setText(ThesisRequestActivity.this.getString(R.string.supervisor_name_string_placeholder,supervisor.getFullName()));
                    }
                });
            }
        }
    }

    private void getSupervisorData() {
        Intent intent = getIntent();
        if(intent.hasExtra("user")){
            supervisor = (Supervisor) intent.getSerializableExtra("user");
            supervisorNameTextView.setText(this.getString(R.string.supervisor_name_string_placeholder,supervisor.getFullName()));
        }
    }

    private void getStudentData() {
        UserDao userDao = FirebaseFirestoreDao.getInstance();
        if (auth.getCurrentUser() != null) {
            userDao.getUser(auth.getCurrentUser().getUid(), new Callback<User>() {
                @Override
                public void onCallback(User user) {
                    student = (Student) user;
                    studentNameTextView.setText(ThesisRequestActivity.this.getString(R.string.student_name_string_placeholder,student.getFullName()));
                    studentStudyProgramTextView.setText(ThesisRequestActivity.this.getString(R.string.student_study_program_string_placeholder, StudyProgramEnum.getLocalizedString(context,student.getStudyProgram())) + " " + student.getStudyLevel());
                }
            });
        }
    }

    public void setOnClickListeners(MaterialToolbar toolbar) {
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

        exposeUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPdfPicker();
            }
        });

        submitThesisRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateThesisData();
                if(isValidThesisData){
                    if(mode != null && mode.equals("THESIS_FROM_TOPIC")){
                        updateExistingThesis();
                    }else{
                        createNewThesis();
                    }
                }

            }
        });
    }



    private void updateExistingThesis() {
        firebaseStorageDao.uploadExpose(file, new Callback<Uri>() {
            @Override
            public void onCallback(Uri downloadUri) {
                thesis.setStudentId(student.getUserId());
                thesis.setExposeTitle(exposeTitle);
                thesis.setExposeDownloadUri(downloadUri.toString());
                firebaseFirestoreDao.updateThesis(thesis.getThesisId(),thesis);
                Toast.makeText(ThesisRequestActivity.this, R.string.thesis_request_submitted, Toast.LENGTH_SHORT).show();
                ActivityStarter.startSuccessThesisSubmittedActivity(context,thesis);
                ThesisRequestActivity.this.finish();
            }
        });
    }

    private void createNewThesis() {
        firebaseStorageDao.uploadExpose(file, new Callback<Uri>() {
            @Override
            public void onCallback(Uri downloadUri) {
                Thesis thesis = new Thesis(
                        thesisTitleEditText.getText().toString(),
                        student.getUserId(),
                        supervisor.getUserId(),
                        "",
                        ThesisStateEnum.open.name(),
                        BillingStateEnum.open.name(),
                        exposeTitle,
                        downloadUri.toString());
                firebaseFirestoreDao.saveNewThesis(thesis);
                Toast.makeText(ThesisRequestActivity.this, R.string.thesis_request_submitted, Toast.LENGTH_SHORT).show();
                ActivityStarter.startSuccessThesisSubmittedActivity(context,thesis);
                ThesisRequestActivity.this.finish();
            }
        });
    }


    private void validateThesisData(){
        if(thesisTitleEditText.getText().toString().isEmpty()){
            Toast.makeText(ThesisRequestActivity.this, R.string.missing_thesis_title_message, Toast.LENGTH_SHORT).show();
            Toast.makeText(ThesisRequestActivity.this, R.string.missing_thesis_title_message, Toast.LENGTH_SHORT).show();
            return;
        }
        if(exposeFilePathTextView.getText().toString().equals(getString(R.string.expose_filepath_placeholder))){
            Toast.makeText(ThesisRequestActivity.this, R.string.missing_expose_message, Toast.LENGTH_SHORT).show();
            return;
        }
        isValidThesisData = true;
    }

    private void initializePdfPicker() {
        pdfPickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            file = data.getData();
                            exposeTitle = getFileName(file);
                            exposeFilePathTextView.setText(exposeTitle);
                        }
                    } else {
                        Toast.makeText(ThesisRequestActivity.this, R.string.no_pdf_selected, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openPdfPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        pdfPickerLauncher.launch(intent);
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
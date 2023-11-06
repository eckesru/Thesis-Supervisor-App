package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.BillingStateEnum;
import de.iu.iwmb02_iu_betreuer_app.data.StudyProgramEnum;
import de.iu.iwmb02_iu_betreuer_app.data.ThesisStateEnum;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseStorageDao;
import de.iu.iwmb02_iu_betreuer_app.model.Student;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.model.Thesis;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class ThesisDetailsActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private static final String TAG = "ThesisDetailsActivity";
    private final Context context = ThesisDetailsActivity.this;
    private FirebaseAuth auth;
    private FirebaseFirestoreDao firebaseFirestoreDao;
    private FirebaseStorageDao firebaseStorageDao;
    private Thesis thesis;
    private String exposeTitle;
    private String exposeDownloadUri;
    private TextView thesisDetailsStudentNameTextView;
    private TextView thesisDetailsStudentStudyProgramTextView;
    private TextView thesisDetailsStudentEmailTextView;
    private TextView thesisDetailsTopicTextView;
    private TextView thesisDetailsExposeTextView;
    private TextView thesisDetailsStateTextView;
    private TextView thesisDetailsBillingTextView;
    private TextView thesisDetailsPrimarySupervisorTextView;
    private TextView thesisDetailsSecondarySupervisorTextView;
    private ImageButton thesisTopicEditButton;
    private ImageButton exposeDownloadButton;
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
        exposeDownloadButton = findViewById(R.id.exposeDownloadButton);
        thesisTopicEditButton = findViewById(R.id.thesisTopicEditButton);
        toolbar = findViewById(R.id.materialToolbar);

        getThesisFromIntentExtra();
        getUsers();
        getExpose();

        setOnClickListeners();

        initializeSelectThesisStateButton();
        initializeSelectThesisBillingButton();
    }

    private void getThesisFromIntentExtra() {
        Intent intent = getIntent();
        if (intent.hasExtra("thesis")) {
            thesis = (Thesis) intent.getSerializableExtra("thesis");

            thesisDetailsTopicTextView.setText(getString(R.string.topic_string_placeholder, thesis.getTitle()));
            thesisDetailsStateTextView.setText(getString(R.string.thesis_state_string_placeholder, ThesisStateEnum.getLocalizedString(context, thesis.getThesisState())));
            thesisDetailsBillingTextView.setText(getString(R.string.thesis_billing_string_placeholder, BillingStateEnum.getLocalizedString(context, thesis.getBillingState())));
        }
    }

    private void getUsers() {
        if (!thesis.getStudentId().isEmpty()) {
            firebaseFirestoreDao.getStudent(thesis.getStudentId(), new Callback<Student>() {
                @Override
                public void onCallback(Student student) {

                    thesisDetailsStudentNameTextView.setText(getString(R.string.student_name_string_placeholder, student.getFullName()));
                    thesisDetailsStudentStudyProgramTextView.setText(getString(R.string.student_study_program_string_placeholder, StudyProgramEnum.getLocalizedString(context, student.getStudyProgram()) + " " + student.getStudyLevel()));
                    thesisDetailsStudentEmailTextView.setText(getString(R.string.email_string_placeholder, student.getEmail()));
                }
            });
        } else {
            thesisDetailsStudentNameTextView.setText(getString(R.string.student_name_string_placeholder, getString(R.string.empty)));
            thesisDetailsStudentStudyProgramTextView.setText(getString(R.string.student_study_program_string_placeholder, getString(R.string.empty)));
            thesisDetailsStudentEmailTextView.setText(getString(R.string.email_string_placeholder, getString(R.string.empty)));
        }

        if (!thesis.getPrimarySupervisorId().isEmpty()) {
            firebaseFirestoreDao.getSupervisor(thesis.getPrimarySupervisorId(), new Callback<Supervisor>() {
                @Override
                public void onCallback(Supervisor primarySupervisor) {

                    thesisDetailsPrimarySupervisorTextView.setText(getString(R.string.primary_supervisor_name_string_placeholder, primarySupervisor.getFullName()));
                }
            });
        }
        if (!thesis.getSecondarySupervisorId().isEmpty()) {
            firebaseFirestoreDao.getSupervisor(thesis.getSecondarySupervisorId(), new Callback<Supervisor>() {
                @Override
                public void onCallback(Supervisor secondarySupervisor) {

                    thesisDetailsSecondarySupervisorTextView.setText(getString(R.string.secondary_supervisor_name_string_placeholder, secondarySupervisor.getFullName()));
                }
            });
        } else {
            thesisDetailsSecondarySupervisorTextView.setText(getString(R.string.secondary_supervisor_name_string_placeholder, getString(R.string.empty)));
        }
    }

    private void getExpose() {
        exposeDownloadUri = thesis.getExposeDownloadUri();
        exposeTitle = thesis.getExposeTitle();
        if (exposeDownloadUri.isEmpty()) {
            thesisDetailsExposeTextView.setText(getString(R.string.expose_string_placeholder, getString(R.string.empty)));
        } else {
            exposeTitle = thesis.getExposeTitle();
            thesisDetailsExposeTextView.setText(getString(R.string.expose_string_placeholder, exposeTitle));
        }
    }

    private void setOnClickListeners() {
        exposeDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(exposeDownloadUri.isEmpty()){
                    Toast.makeText(context,R.string.no_expose_saved,Toast.LENGTH_SHORT).show();
                    return;
                }
                downloadExpose();
            }
        });

        thesisTopicEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                final EditText input = new EditText(context);
                input.setText(thesis.getTitle());
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        thesis.setTitle(input.getText().toString());
                        firebaseFirestoreDao.updateThesis(thesis.getThesisId(), thesis);
                        thesisDetailsTopicTextView.setText(getString(R.string.topic_string_placeholder, thesis.getTitle()));
                    }
                });

                alert.setNegativeButton("Cancel", null);
                alert.show();
            }
        });

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

    private void downloadExpose() {
        firebaseStorageDao.downloadExpose(exposeDownloadUri, new Callback<byte[]>() {
            @Override
            public void onCallback(byte[] bytes) {
                File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
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

    public void setSecondarySupervisorOnClick(View view) {
        ActivityStarter.startSupervisorBoardActivity(context,thesis,"SELECT_SECONDARY_SUPERVISOR");
    }


    private void initializeSelectThesisStateButton() {

        ImageButton setThesisStateButton = findViewById(R.id.thesisStateEditButton);

        LinkedHashMap<String, String> enumMap = new LinkedHashMap<>();

        for(ThesisStateEnum state : ThesisStateEnum.values()) {
            enumMap.put(ThesisStateEnum.getLocalizedString(context, state.name()), state.name());
        }
        setThesisStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, setThesisStateButton);
                Menu menu = popupMenu.getMenu();

                for(String key : enumMap.keySet()) {
                    menu.add(key);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String itemTitle = item.getTitle().toString();
                        String enumString = enumMap.get(itemTitle);
                        thesis.setThesisState(enumString);
                        firebaseFirestoreDao.updateThesis(thesis.getThesisId(), thesis);
                        thesisDetailsStateTextView.setText(getString(R.string.thesis_state_string_placeholder, ThesisStateEnum.getLocalizedString(context, thesis.getThesisState())));
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }


    private void initializeSelectThesisBillingButton() {

        ImageButton setThesisBillingButton = findViewById(R.id.thesisBillingEditButton);

        LinkedHashMap<String, String> enumMap = new LinkedHashMap<>();

        for(BillingStateEnum state : BillingStateEnum.values()) {
            enumMap.put(BillingStateEnum.getLocalizedString(context, state.name()), state.name());
        }
        setThesisBillingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, setThesisBillingButton);
                Menu menu = popupMenu.getMenu();

                for(String key : enumMap.keySet()) {
                    menu.add(key);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String itemTitle = item.getTitle().toString();
                        String enumString = enumMap.get(itemTitle);
                        thesis.setBillingState(enumString);
                        firebaseFirestoreDao.updateThesis(thesis.getThesisId(), thesis);
                        thesisDetailsBillingTextView.setText(getString(R.string.thesis_billing_string_placeholder, BillingStateEnum.getLocalizedString(context, thesis.getBillingState())));
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }
}
package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.BillingStateEnum;
import de.iu.iwmb02_iu_betreuer_app.data.ThesisStateEnum;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.model.Thesis;

public class TopicCreateActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{
    private static final String TAG = "TopicCreateActivity";
    private final Context context = TopicCreateActivity.this;
    private FirebaseAuth auth;
    private FirebaseFirestoreDao firebaseFirestoreDao;
    private Supervisor primarySupervisor;
    private EditText topicCreateTopicEditText;
    private Button tenderTopicButton;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_create);

        auth = FirebaseAuth.getInstance();
        firebaseFirestoreDao = FirebaseFirestoreDao.getInstance();

        topicCreateTopicEditText = findViewById(R.id.topicCreateTopicEditText);
        tenderTopicButton = findViewById(R.id.tenderTopicButton);
        toolbar = findViewById(R.id.materialToolbar);

        getSupervisorFromIntentExtra();
        setOnClickListeners();
    }
    private void getSupervisorFromIntentExtra() {
        Intent intent = getIntent();
        if(intent.hasExtra("user")){
            primarySupervisor = (Supervisor) intent.getSerializableExtra("user");
        }
    }

    private void setOnClickListeners() {
        tenderTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateTitle()){
                    saveThesisTopic();
                    Toast.makeText(context, R.string.topic_created_message, Toast.LENGTH_SHORT).show();
                    ActivityStarter.startThesisOverviewActivity(context, primarySupervisor);
                    TopicCreateActivity.this.finish();
                    return;
                }
                Toast.makeText(context, R.string.missing_topic_title_message, Toast.LENGTH_SHORT).show();

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

    private boolean validateTitle() {
        return !topicCreateTopicEditText.getText().toString().isEmpty();
    }

    private void saveThesisTopic() {
        Thesis thesis = new Thesis(
                topicCreateTopicEditText.getText().toString(),
                "",primarySupervisor.getUserId(),
                "",
                ThesisStateEnum.open.name(),
                BillingStateEnum.open.name(),
                "");
        firebaseFirestoreDao.saveNewThesis(thesis);
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
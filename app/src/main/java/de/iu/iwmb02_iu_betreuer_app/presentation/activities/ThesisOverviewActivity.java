package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.model.Thesis;
import de.iu.iwmb02_iu_betreuer_app.model.User;
import de.iu.iwmb02_iu_betreuer_app.presentation.adapters.ThesisRecyclerAdapter;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class ThesisOverviewActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{
    private static final String TAG = "ThesisOverviewActivity";
    private final Context context = ThesisOverviewActivity.this;
    private FirebaseAuth auth;
    private FirebaseFirestoreDao firebaseFirestoreDao;
    private RecyclerView thesisOverviewRecyclerView;
    private TextView txtHiUser;
    private User user;
    private MaterialToolbar toolbar;
    private Button topicTenderButton;
    private ConstraintLayout chipGroups;
    Chip primaryChip;
    Chip secondaryChip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thesis_overview);

        auth = FirebaseAuth.getInstance();
        firebaseFirestoreDao = FirebaseFirestoreDao.getInstance();

        thesisOverviewRecyclerView = findViewById(R.id.thesisOverviewRecyclerView);
        txtHiUser = findViewById(R.id.hiUserNameTextView);
        toolbar = findViewById(R.id.materialToolbar);
        topicTenderButton = findViewById(R.id.topicTenderButton);

        initializeChips();

        chipGroups = findViewById(R.id.chipFilterConstraintLayout);
        setDefaultFilters();

        toolbar = findViewById(R.id.materialToolbar);

        handleUserGreeting();

        setOnClickListeners();
        setRecyclerAdapter();
    }

    private void setRecyclerAdapter() {
        getThesisRecyclerAdapter(getThesisQueries(), new Callback<ThesisRecyclerAdapter>() {
            @Override
            public void onCallback(ThesisRecyclerAdapter thesisRecyclerAdapter) {
                thesisOverviewRecyclerView.setAdapter(thesisRecyclerAdapter);
                thesisOverviewRecyclerView.setItemAnimator(null);
            }
        });
    }


    private void initializeChips() {
        primaryChip = findViewById(R.id.chipPrimarySupervisor);
        secondaryChip = findViewById(R.id.chipSecondarySupervisor);
        // TODO: Initialize all chips
    }

    private void setDefaultFilters() {
        primaryChip.setChecked(true);
    // TODO: Set all default filters
    }

    public void setOnClickListeners() {
        topicTenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityStarter.startCreateTopicActivity(context, user);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menuItem_filter) {
                    if (chipGroups.getVisibility() == View.VISIBLE) {
                        final Animation slideOutRight = AnimationUtils.loadAnimation(ThesisOverviewActivity.this, android.R.anim.slide_out_right);
                        slideOutRight.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            public void onAnimationEnd(Animation animation) {
                                chipGroups.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }

                        });
                        chipGroups.startAnimation(slideOutRight);
                    } else {
                        chipGroups.startAnimation(AnimationUtils.loadAnimation(ThesisOverviewActivity.this, android.R.anim.slide_in_left));
                        chipGroups.setVisibility(View.VISIBLE);
                    }
                    return true;
                } else if (id == R.id.menuItem_logout) {
                    logOutUser();
                    return true;
                }
                return false;
            }
        });
    }

    private void handleUserGreeting() {
        Intent intent = getIntent();
        if(intent.hasExtra("user")){
            user = (User) intent.getSerializableExtra("user");
            txtHiUser.setText(user.getNameFirst());
            return;
        }
        txtHiUser.setText("User");
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

    public void getThesisRecyclerAdapter(ArrayList<Query> queries, Callback<ThesisRecyclerAdapter> callback) {
        ArrayList<Task<QuerySnapshot>> tasks = new ArrayList<>();

        for (Query q : queries) {
            tasks.add(q.get());
        }

        Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {
                ArrayList<Thesis> theses = new ArrayList<>();

                for (Object obj : objects) {
                    if (obj instanceof QuerySnapshot) {
                        QuerySnapshot querySnapshot = (QuerySnapshot) obj;
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            if (document.exists()) {
                                Thesis thesis = document.toObject(Thesis.class);
                                theses.add(thesis);
                            }
                        }
                    }
                }

                callback.onCallback(new ThesisRecyclerAdapter(theses));
            }
        });
    }

    private ArrayList<Query> getThesisQueries(){
        String userId = user.getUserId();
        System.out.println(userId);
        ArrayList<Query> queries = new ArrayList<>();
        queries.add(firebaseFirestoreDao.getThesesCollectionRef().whereEqualTo("primarySupervisorId", userId)) ;
        queries.add(firebaseFirestoreDao.getThesesCollectionRef().whereEqualTo("secondarySupervisorId", userId)) ;
        return queries;
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
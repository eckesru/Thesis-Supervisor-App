package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.StudyFieldEnum;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.model.User;
import de.iu.iwmb02_iu_betreuer_app.presentation.adapters.SupervisorRecyclerAdapter;

public class SupervisorBoardActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{
    private static final String TAG = "SupervisorBoardActivity";
    private final Context context = SupervisorBoardActivity.this;
    private FirebaseAuth auth;
    private FirebaseFirestoreDao firebaseFirestoreDao;
    private RecyclerView supervisorBoardRecyclerView;
    private SupervisorRecyclerAdapter supervisorRecyclerAdapter;
    private TextView txtHiUser;
    private User user;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_board);

        auth = FirebaseAuth.getInstance();
        firebaseFirestoreDao = FirebaseFirestoreDao.getInstance();

        supervisorBoardRecyclerView = findViewById(R.id.supervisorBoardRecyclerView);
        txtHiUser = findViewById(R.id.hiUserNameTextView);
        toolbar = findViewById(R.id.materialToolbar);

        supervisorRecyclerAdapter = getSupervisorRecyclerAdapter("");
        supervisorBoardRecyclerView.setAdapter(supervisorRecyclerAdapter);
        supervisorBoardRecyclerView.setItemAnimator(null);

        fillSupervisorStudyFieldFilterOptions();
        setOnClickListeners();

        handleUserGreeting();
    }

    private void fillSupervisorStudyFieldFilterOptions(){
        Menu submenu = toolbar.getMenu().findItem(R.id.menuItem_filter).getSubMenu();

        submenu.add(0,R.string.show_all,0,getString(R.string.show_all)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                connectNewRecyclerAdapter("");
                return true;
            }
        });

        for (StudyFieldEnum studyFieldEnum : StudyFieldEnum.values()) {
            String enumName = studyFieldEnum.name();
            int stringResId = studyFieldEnum.getStringResId();

            submenu.add(
                    0,
                    stringResId,
                    0,
                    StudyFieldEnum.getLocalizedString(context, enumName)
            ).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                    handleSubMenuItemClick(enumName);
                    return true;
                }
            });
        }
    }

    private void handleSubMenuItemClick(String filterOption) {
        Log.d(TAG, "Clicked: " + filterOption);
        connectNewRecyclerAdapter(filterOption);
    }

    private void connectNewRecyclerAdapter(String filterOption) {
        supervisorRecyclerAdapter = getSupervisorRecyclerAdapter(filterOption);
        supervisorBoardRecyclerView.setAdapter(supervisorRecyclerAdapter);
        supervisorRecyclerAdapter.startListening();
    }

    public void setOnClickListeners() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menuItem_logout) {
                    logOutUser();
                    return true;
                } else if (id == R.id.menuItem_filter) {
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
        supervisorRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        supervisorRecyclerAdapter.stopListening();
        auth.removeAuthStateListener(this);
    }

    private SupervisorRecyclerAdapter getSupervisorRecyclerAdapter(String filterOption){
        FirestoreRecyclerOptions<Supervisor> options = getFirestoreRecyclerOptions(getSupervisorQuery(filterOption));
        return new SupervisorRecyclerAdapter(options);
    }

    private FirestoreRecyclerOptions<Supervisor> getFirestoreRecyclerOptions(Query query){
        return new FirestoreRecyclerOptions.Builder<Supervisor>()
                .setQuery(query, Supervisor.class)
                .build();
    }
    
    private Query getSupervisorQuery(String filterOption){
        Query query = firebaseFirestoreDao.getSupervisorsCollectionRef();

        if (filterOption != null && !filterOption.isEmpty()) {
            return query.whereArrayContains("studyFields", filterOption);
        } else {
            return query.orderBy("nameLast", Query.Direction.ASCENDING);
        }
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
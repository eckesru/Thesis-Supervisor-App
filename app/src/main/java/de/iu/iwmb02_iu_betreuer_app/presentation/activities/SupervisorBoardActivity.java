package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

import de.iu.iwmb02_iu_betreuer_app.R;
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
    private ImageView menuItem_logout;
    private TextView txtHiUser;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_board);
        auth = FirebaseAuth.getInstance();

        firebaseFirestoreDao = FirebaseFirestoreDao.getInstance();

        supervisorBoardRecyclerView = findViewById(R.id.supervisorBoardRecyclerView);
        supervisorRecyclerAdapter = getSupervisorRecyclerAdapter("");
        supervisorBoardRecyclerView.setAdapter(supervisorRecyclerAdapter);
        supervisorBoardRecyclerView.setItemAnimator(null);

        menuItem_logout = findViewById(R.id.menuItem_logout);
        txtHiUser = findViewById(R.id.hiUserNameTextView);

        handleUserGreeting();
        setOnClickListeners();
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

    public void setOnClickListeners(){
        menuItem_logout.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                    logOutUser();
            }
        });
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

    private SupervisorRecyclerAdapter getSupervisorRecyclerAdapter(String sortingOption){
        FirestoreRecyclerOptions<Supervisor> options = getFirestoreRecyclerOptions(getSupervisorQuery(sortingOption));
        return new SupervisorRecyclerAdapter(options);
    }

    private FirestoreRecyclerOptions<Supervisor> getFirestoreRecyclerOptions(Query query){
        return new FirestoreRecyclerOptions.Builder<Supervisor>()
                .setQuery(query, Supervisor.class)
                .build();
    }

    private Query getSupervisorQuery(String orderOption){
        Query query;
        switch (orderOption){
            // TODO: implement other sorting options and set up selection via buttons
            default:
                query = firebaseFirestoreDao.getSupervisorsCollectionRef()
                        .orderBy("nameLast", Query.Direction.ASCENDING);
        }
        return query;
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
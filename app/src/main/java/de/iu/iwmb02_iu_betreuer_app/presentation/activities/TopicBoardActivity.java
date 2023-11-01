package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.model.Thesis;
import de.iu.iwmb02_iu_betreuer_app.model.User;
import de.iu.iwmb02_iu_betreuer_app.presentation.adapters.TopicRecyclerAdapter;

public class TopicBoardActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{
    private static final String TAG = "TopicBoardActivity";
    private final Context context = TopicBoardActivity.this;
    private FirebaseAuth auth;
    private FirebaseFirestoreDao firebaseFirestoreDao;
    private RecyclerView topicBoardRecyclerView;
    private TopicRecyclerAdapter topicRecyclerAdapter;
    private User user;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_board);

        auth = FirebaseAuth.getInstance();
        firebaseFirestoreDao = FirebaseFirestoreDao.getInstance();

        topicBoardRecyclerView = findViewById(R.id.topicBoardRecyclerView);
        toolbar = findViewById(R.id.materialToolbar);


        topicRecyclerAdapter = getTopicRecyclerAdapter();
        topicBoardRecyclerView.setAdapter(topicRecyclerAdapter);
        topicBoardRecyclerView.setItemAnimator(null);

        setOnClickListeners();
    }


    private TopicRecyclerAdapter getTopicRecyclerAdapter(){
        FirestoreRecyclerOptions<Thesis> options = getFirestoreRecyclerOptions(getTopicQuery());
        return new TopicRecyclerAdapter(options);
    }

    private FirestoreRecyclerOptions<Thesis> getFirestoreRecyclerOptions(Query query){
        return new FirestoreRecyclerOptions.Builder<Thesis>()
                .setQuery(query, Thesis.class)
                .build();
    }

    private Query getTopicQuery(){
        Query query = firebaseFirestoreDao.getThesesCollectionRef();
        return query.whereEqualTo("studentId","");
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
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(this);
        topicRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        topicRecyclerAdapter.stopListening();
        auth.removeAuthStateListener(this);
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
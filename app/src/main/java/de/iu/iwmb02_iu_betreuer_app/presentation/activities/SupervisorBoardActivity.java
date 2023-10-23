package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.presentation.adapters.SupervisorRecyclerAdapter;

public class SupervisorBoardActivity extends AppCompatActivity {
    private static final String TAG = "SupervisorBoardActivity";
    private final Context context = SupervisorBoardActivity.this;
    private FirebaseFirestoreDao firebaseFirestoreDao;
    private RecyclerView supervisorBoardRecyclerView;
    private SupervisorRecyclerAdapter supervisorRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_board);

        firebaseFirestoreDao = FirebaseFirestoreDao.getInstance();

        supervisorBoardRecyclerView = findViewById(R.id.supervisorBoardRecyclerView);
        supervisorRecyclerAdapter = getSupervisorRecyclerAdapter("");
        supervisorBoardRecyclerView.setAdapter(supervisorRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        supervisorRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        supervisorRecyclerAdapter.stopListening();
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

}
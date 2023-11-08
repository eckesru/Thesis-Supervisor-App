package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.data.dao.UserDao;
import de.iu.iwmb02_iu_betreuer_app.model.Student;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.model.Thesis;
import de.iu.iwmb02_iu_betreuer_app.model.User;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{

    private static final String TAG = "MainActivity";
    private final Context context = MainActivity.this;
    private FirebaseAuth auth;
    private FirebaseFirestoreDao firebaseFirestoreDao;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        firebaseFirestoreDao = FirebaseFirestoreDao.getInstance();

        //TODO: Delete - Just for development
        //FirestoreTools tools = new FirestoreTools();
        //tools.deleteAllDBs();
        //tools.deleteUserDB();
        //tools.deleteSupervisorDB();
        //tools.deleteStudentsDB();
        //tools.populateDatabasesWithSampleData();

        toolbar = findViewById(R.id.materialToolbar);
        toolbar.inflateMenu(R.menu.logout_menu);
        setItemClickListener();

        UserDao userDao = FirebaseFirestoreDao.getInstance();
        if(auth.getCurrentUser() != null) {
            userDao.getUser(auth.getCurrentUser().getUid(), new Callback<User>() {
                @Override
                public void onCallback(User user) {
                    if (user instanceof Student) {
                        Log.d(TAG, "Logged in User is a student");
                        chooseActivity((Student) user);
                    } else if (user instanceof Supervisor) {
                        Log.d(TAG, "Logged in user is a supervisor");
                        ActivityStarter.startThesisOverviewActivity(context, user);
                        MainActivity.this.finish();
                    } else {
                        Log.d(TAG, "Unknown usertype");
                    }
                }
            });
        }
    }

    private void chooseActivity(Student student) {
        firebaseFirestoreDao.checkIfOpenThesisExistsForStudentId(student.getUserId(), new Callback<Thesis>() {
            @Override
            public void onCallback(Thesis thesis) {
                if(thesis != null){
                    ActivityStarter.startSuccessThesisSubmittedActivity(context, thesis);
                    MainActivity.this.finish();
                    return;
                }
                ActivityStarter.startSupervisorBoardActivity(context, student);
                MainActivity.this.finish();
            }
        });
    }

    private void setItemClickListener() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menuItem_logout) {
                    logOutUser();
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            Log.d(TAG, "onAuthStateChanged: No user signed in. Starting LoginActivity");
            ActivityStarter.startLoginActivity(context);
            this.finish();
            return;
        }
        Toast.makeText(context, getString(R.string.logged_in_as) +" "+ firebaseAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
    }

    private void logOutUser(){
        Log.d(TAG, "logOutUser: Logging out");
        AuthUI.getInstance().signOut(this);
        Toast.makeText(context, getString(R.string.logged_out), Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
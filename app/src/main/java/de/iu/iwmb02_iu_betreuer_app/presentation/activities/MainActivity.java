package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.data.dao.UserDao;
import de.iu.iwmb02_iu_betreuer_app.development.FirestoreTools;
import de.iu.iwmb02_iu_betreuer_app.model.Student;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.model.User;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{

    private static final String TAG = "MainActivity";
    private final Context context = MainActivity.this;
    private FirebaseAuth auth;
    private ImageView logoutImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        //TODO: Delete - Just for development
        FirestoreTools tools = new FirestoreTools();
        //tools.deleteAllDBs();
        //tools.populateDatabasesWithSampleData();

        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);
        toolbar.inflateMenu(R.menu.empty_menu);

        UserDao userDao = FirebaseFirestoreDao.getInstance();
        if(auth.getCurrentUser() != null) {
            userDao.getUser(auth.getCurrentUser().getUid(), new Callback<User>() {
                @Override
                public void onCallback(User user) {
                    if (user instanceof Student) {
                        Log.d(TAG, "Logged in User is a student");
                        ActivityStarter.startSupervisorBoardActivity(context, user);
                        MainActivity.this.finish();
                        //TODO: check if thesis object exists --> goto successpage
                    } else if (user instanceof Supervisor) {
                        Log.d(TAG, "Logged in user is a supervisor");
                        //TODO: implement thesis overview activity
                    } else {
                        Log.d(TAG, "Unknown usertype");
                    }
                }
            });
        }
    }

    private void setItemClickListener() {
        logoutImageView.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.menuItem_logout) {
                    logOutUser();
                }
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
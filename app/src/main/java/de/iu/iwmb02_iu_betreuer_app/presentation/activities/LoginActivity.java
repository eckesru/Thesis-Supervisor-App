package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import de.iu.iwmb02_iu_betreuer_app.R;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private Context context;
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Log.d(TAG, "onCreate: User already signed in. Starting MainActivity");
            ActivityStarter.startMainActivity(context);
            this.finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = LoginActivity.this;

        ImageView toolbarImageView = findViewById(R.id.menuItem_logout);
        if (toolbarImageView != null) {
            toolbarImageView.setVisibility(View.GONE);
        } else {
            Log.d("Debug", "ImageView nicht gefunden");
        }
    }

    public void onClick(View view){
        int id = view.getId();
        if (id == R.id.btnLogin) {
            createSignInIntent();
        }
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result){
        IdpResponse response = result.getIdpResponse();
        if(result.getResultCode() == RESULT_OK){
            Log.d(TAG, "onSignInResult: Sign in Success");
            ActivityStarter.startMainActivity(context);
        } else{
            if(response == null){
                Log.d(TAG, "onSignInResult: User canceled sign in request");
            } else {
                Log.e(TAG, "onSignInResult: ", response.getError());
            }
        }
    }

    public void createSignInIntent(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        //TODO: Set app-logo
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.LoginStyle)
                .setIsSmartLockEnabled(false)
                .setLogo(R.drawable.iu_logo)
                .build();
        signInLauncher.launch(signInIntent);
    }
}
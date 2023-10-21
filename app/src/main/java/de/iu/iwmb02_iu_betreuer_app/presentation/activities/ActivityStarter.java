package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import android.content.Context;
import android.content.Intent;

public class ActivityStarter {

    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void startSupervisorBoardActivity(Context context){
        Intent intent = new Intent(context, SupervisorBoardActivity.class);
        context.startActivity(intent);
    }
}

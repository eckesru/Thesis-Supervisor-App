package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import android.content.Context;
import android.content.Intent;

import de.iu.iwmb02_iu_betreuer_app.model.Thesis;
import de.iu.iwmb02_iu_betreuer_app.model.User;

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

    public static void startSupervisorBoardActivity(Context context, User user){
        Intent intent = new Intent(context, SupervisorBoardActivity.class);
        intent.putExtra("user", user);
        context.startActivity(intent);
    }

    public static void startSupervisorDetailsActivity(Context context, User user){
        Intent intent = new Intent(context, SupervisorDetailsActivity.class);
        intent.putExtra("user", user);
        context.startActivity(intent);
    }

    public static void startThesisRequestActivity(Context context, User user){
        Intent intent = new Intent(context, ThesisRequestActivity.class);
        intent.putExtra("user", user);
        context.startActivity(intent);
    }

    public static void startThesisOverviewActivity(Context context, User user) {
        Intent intent = new Intent(context, ThesisOverviewActivity.class);
        intent.putExtra("user", user);
        context.startActivity(intent);
    }

    public static void startThesisDetailsActivity(Context context, Thesis thesis) {
        Intent intent = new Intent(context, ThesisDetailsActivity.class);
        intent.putExtra("thesis", thesis);
        context.startActivity(intent);
    }

    public static void startCreateTopicActivity(Context context, User user) {
        Intent intent = new Intent(context, TopicCreateActivity.class);
        intent.putExtra("user", user);
        context.startActivity(intent);
    }

}

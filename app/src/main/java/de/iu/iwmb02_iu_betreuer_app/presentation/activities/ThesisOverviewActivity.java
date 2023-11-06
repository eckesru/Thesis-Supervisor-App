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
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.BillingStateEnum;
import de.iu.iwmb02_iu_betreuer_app.data.ThesisStateEnum;
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
    private ArrayList<Thesis> theses;
    private ArrayList<Chip> allChips;
    private Chip chipPrimarySupervisor;
    private Chip chipSecondarySupervisor;
    private Chip chipStateOpen;
    private Chip chipStateConsultation;
    private Chip chipStateRegistered;
    private Chip chipStateColloquium;
    private Chip chipStateCompleted;
    private Chip chipBillingOpen;
    private Chip chipBillingBilled;
    private Chip chipBillingSettled;
    private ArrayList<String> checkedChips;

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
        chipGroups = findViewById(R.id.chipFilterConstraintLayout);

        getUserFromIntentExtra();
        initializeChips();
        getAllChips();
        setDefaultFilters();

        setupRecyclerView();

        setOnClickListeners();
    }

    private void setupRecyclerView() {
        getAllThesesForSupervisor(new Callback<ArrayList<Thesis>>() {
            @Override
            public void onCallback(ArrayList<Thesis> theses) {
                ThesisOverviewActivity.this.theses = theses;
                updateRecyclerAdapter();
            }
        });
    }

    private void getUserFromIntentExtra() {
        Intent intent = getIntent();
        if(intent.hasExtra("user")){
            user = (User) intent.getSerializableExtra("user");
            txtHiUser.setText(user.getNameFirst());
            return;
        }
        txtHiUser.setText("User");
    }

    private void getAllThesesForSupervisor(Callback<ArrayList<Thesis>> callback) {
        ArrayList<Thesis> theses = new ArrayList<>();
        String userId = user.getUserId();

        ArrayList<Query> queries = new ArrayList<>();
        queries.add(firebaseFirestoreDao.getThesesCollectionRef().whereEqualTo("primarySupervisorId", userId)) ;
        queries.add(firebaseFirestoreDao.getThesesCollectionRef().whereEqualTo("secondarySupervisorId", userId)) ;

        ArrayList<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (Query q : queries) {
            tasks.add(q.get());
        }
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> objects) {


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
                callback.onCallback(theses);
            }
        });
    }

    private void updateRecyclerAdapter() {
        ArrayList<Thesis> filteredThesis = getFilteredTheses();
        thesisOverviewRecyclerView.setAdapter(new ThesisRecyclerAdapter(filteredThesis));
        thesisOverviewRecyclerView.setItemAnimator(null);
    }

    private void getAllChips(){
        allChips = new ArrayList<>();

        ChipGroup supervisionChipGroup = findViewById(R.id.supervisionChipGroup);
        ChipGroup stateChipGroup = findViewById(R.id.stateChipGroup);
        ChipGroup billingChipGroup = findViewById(R.id.billingChipGroup);

        allChips.addAll(getChipsInChipGroup(supervisionChipGroup));
        allChips.addAll(getChipsInChipGroup(stateChipGroup));
        allChips.addAll(getChipsInChipGroup(billingChipGroup));

    }

    private List<Chip> getChipsInChipGroup(ChipGroup chipGroup) {
        List<Chip> chips = new ArrayList<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            View view = chipGroup.getChildAt(i);
            if (view instanceof Chip) {
                chips.add((Chip) view);
            }
        }
        return chips;
    }

    private ArrayList<Chip> getCheckedChips(){
        ArrayList<Chip> checkedChips = new ArrayList<>();

        for (Chip chip: allChips){
            if(chip.isChecked()){
                checkedChips.add(chip);
            }
        }
        return checkedChips;
    }


    private void initializeChips() {
        chipPrimarySupervisor = findViewById(R.id.chipPrimarySupervisor);
        chipSecondarySupervisor = findViewById(R.id.chipSecondarySupervisor);
        chipStateOpen = findViewById(R.id.chipStateOpen);
        chipStateConsultation = findViewById(R.id.chipStateConsultation);
        chipStateRegistered = findViewById(R.id.chipStateRegistered);
        chipStateColloquium = findViewById(R.id.chipStateColloquium);
        chipStateCompleted = findViewById(R.id.chipStateCompleted);
        chipBillingOpen = findViewById(R.id.chipBillingOpen);
        chipBillingBilled = findViewById(R.id.chipBillingBilled);
        chipBillingSettled = findViewById(R.id.chipBillingSettled);
    }

    private void setDefaultFilters() {
        chipPrimarySupervisor.setChecked(true);
        chipSecondarySupervisor.setChecked(true);
        chipStateOpen.setChecked(true);
        chipStateConsultation.setChecked(true);
        chipStateRegistered.setChecked(true);
        chipBillingOpen.setChecked(true);
    }

    public void setOnClickListeners() {
        topicTenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityStarter.startCreateTopicActivity(context, user);
            }
        });

        for(Chip chip: allChips){
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateRecyclerAdapter();
                }
            });
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menuItem_filter) {
                    toggleChipGroupsVisibility();
                    return true;
                } else if (id == R.id.menuItem_logout) {
                    logOutUser();
                    return true;
                }
                return false;
            }
        });
    }

    private void toggleChipGroupsVisibility() {
        if (chipGroups.getVisibility() == View.VISIBLE) {
            hideChipGroups();
        } else {
            showChipGroups();
        }
    }

    private void showChipGroups() {
        chipGroups.startAnimation(AnimationUtils.loadAnimation(ThesisOverviewActivity.this, android.R.anim.slide_in_left));
        chipGroups.setVisibility(View.VISIBLE);
    }

    private void hideChipGroups() {
        final Animation slideOutRight = AnimationUtils.loadAnimation(ThesisOverviewActivity.this, android.R.anim.slide_out_right);
        slideOutRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            public void onAnimationEnd(Animation animation) {
                chipGroups.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}

        });
        chipGroups.startAnimation(slideOutRight);
    }

    private ArrayList<Thesis> getPrefilteredTheses() {
        ArrayList<Thesis> prefilteredTheses = new ArrayList<>();

        if(chipPrimarySupervisor.isChecked()){
            for(Thesis thesis : theses){
                if(thesis.getPrimarySupervisorId().equals(user.getUserId())){
                    prefilteredTheses.add(thesis);
                }
            }
        }
        if(chipSecondarySupervisor.isChecked()){
            for(Thesis thesis : theses){
                if(thesis.getSecondarySupervisorId().equals(user.getUserId())){
                    prefilteredTheses.add(thesis);
                }
            }
        }
        return prefilteredTheses;
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(this);
        setupRecyclerView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(this);
    }

    private ArrayList<Thesis> getFilteredTheses() {
        ArrayList<Thesis> filteredTheses = new ArrayList<>();
        ArrayList<Thesis> remainingTheses = getPrefilteredTheses();

        for (Chip chip : getCheckedChips()) {
            String chipText = chip.getText().toString();
            ChipGroup parentChipGroup = (ChipGroup) chip.getParent();
            ThesisStateEnum thesisState = null;
            BillingStateEnum billingState = null;

            if (getString(R.string.thesis_state_open).equals(chipText) && parentChipGroup.getId() == R.id.stateChipGroup ) {
                thesisState = ThesisStateEnum.open;
            } else if (getString(R.string.thesis_state_consultation).equals(chipText)) {
                thesisState = ThesisStateEnum.consultation;
            } else if (getString(R.string.thesis_state_registered).equals(chipText)) {
                thesisState = ThesisStateEnum.registered;
            } else if (getString(R.string.thesis_state_colloquium).equals(chipText)) {
                thesisState = ThesisStateEnum.colloquium;
            } else if (getString(R.string.thesis_state_completed).equals(chipText)) {
                thesisState = ThesisStateEnum.completed;
            } else if (getString(R.string.billing_state_open).equals(chipText)&& parentChipGroup.getId() == R.id.billingChipGroup) {
                billingState = BillingStateEnum.open;
            } else if (getString(R.string.billing_state_billed).equals(chipText)) {
                billingState = BillingStateEnum.billed;
            } else if (getString(R.string.billing_state_settled).equals(chipText)) {
                billingState = BillingStateEnum.settled;
            }

            if (thesisState != null) {
                filteredTheses.addAll(filterByThesisState(remainingTheses, thesisState));
            } else if (billingState != null) {
                filteredTheses.addAll(filterByBillingState(remainingTheses, billingState));
            }
        }

        return filteredTheses;
    }

    private ArrayList<Thesis> filterByThesisState(ArrayList<Thesis> theses, ThesisStateEnum state) {
        ArrayList<Thesis> filteredTheses = new ArrayList<>();
        Iterator<Thesis> iterator = theses.iterator();

        while (iterator.hasNext()) {
            Thesis thesis = iterator.next();
            if (thesis.getThesisState().equals(state.name())) {
                filteredTheses.add(thesis);
                iterator.remove();
            }
        }

        return filteredTheses;
    }

    private ArrayList<Thesis> filterByBillingState(ArrayList<Thesis> theses, BillingStateEnum state) {
        ArrayList<Thesis> filteredTheses = new ArrayList<>();
        Iterator<Thesis> iterator = theses.iterator();

        while (iterator.hasNext()) {
            Thesis thesis = iterator.next();
            if (thesis.getBillingState().equals(state.name())) {
                filteredTheses.add(thesis);
                iterator.remove();
            }
        }

        return filteredTheses;
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
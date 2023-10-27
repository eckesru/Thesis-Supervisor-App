package de.iu.iwmb02_iu_betreuer_app.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;

import de.iu.iwmb02_iu_betreuer_app.R;

public class ThesisOverviewActivity extends AppCompatActivity {

    private ConstraintLayout chipGroups;
    Chip primaryChip;
    Chip secondaryChip;


    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thesis_overview);

        initializeChips();

        chipGroups = findViewById(R.id.chipFilterConstraintLayout);
        setDefaultFilters();

        toolbar = findViewById(R.id.materialToolbar);
        setOnClickListeners(toolbar);


    }


    private void initializeChips() {
        primaryChip = findViewById(R.id.chipPrimarySupervisor);
        secondaryChip = findViewById(R.id.chipSecondarySupervisor);
        //TODO: Initialize all chips
    }

    private void setDefaultFilters() {
        primaryChip.setChecked(true);
// TODO: Set all default filters
    }

    public void setOnClickListeners(MaterialToolbar toolbar) {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menuItem_filter) {
                    if (chipGroups.getVisibility() == View.VISIBLE) {
                        final Animation slideOutRight = AnimationUtils.loadAnimation(ThesisOverviewActivity.this, android.R.anim.slide_out_right);
                        slideOutRight.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            public void onAnimationEnd(Animation animation) {
                                chipGroups.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }

                        });
                        chipGroups.startAnimation(slideOutRight);
                    } else {
                        chipGroups.startAnimation(AnimationUtils.loadAnimation(ThesisOverviewActivity.this, android.R.anim.slide_in_left));
                        chipGroups.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                return false;
            }
        });
    }


}
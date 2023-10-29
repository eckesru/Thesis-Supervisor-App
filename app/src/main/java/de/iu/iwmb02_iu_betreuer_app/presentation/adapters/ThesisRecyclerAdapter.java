package de.iu.iwmb02_iu_betreuer_app.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.data.BillingStateEnum;
import de.iu.iwmb02_iu_betreuer_app.data.ThesisStateEnum;
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.model.Student;
import de.iu.iwmb02_iu_betreuer_app.model.Thesis;
import de.iu.iwmb02_iu_betreuer_app.presentation.activities.ActivityStarter;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class ThesisRecyclerAdapter extends FirestoreRecyclerAdapter<Thesis, ThesisRecyclerAdapter.SupervisorViewHolder> {

    private  final FirebaseFirestoreDao firebaseFirestoreDao;
    public ThesisRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Thesis> options) {
        super(options);
        firebaseFirestoreDao = FirebaseFirestoreDao.getInstance();
    }

    @NonNull
    @Override
    public SupervisorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thesis,parent,false);
        return new SupervisorViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull SupervisorViewHolder holder, int position, @NonNull Thesis thesis) {
        Context holderContext = holder.itemView.getContext();
        holder.thesisTitleTextView.setText(holderContext.getString(R.string.topic_string_placeholder,thesis.getTitle()));

        if(thesis.getStudentId().isEmpty()){
            holder.thesisStudentNameTextView.setText(holderContext.getString(R.string.student_name_string_placeholder, holderContext.getString(R.string.empty)));
        }else {
            firebaseFirestoreDao.getStudent(thesis.getStudentId(), new Callback<Student>() {
                @Override
                public void onCallback(Student student) {
                    holder.thesisStudentNameTextView.setText(holderContext.getString(R.string.student_name_string_placeholder,student.getFullName()));
                }
            });
        }


        holder.thesisStateTextView.setText(holderContext.getString(R.string.thesis_state_string_placeholder,ThesisStateEnum.getLocalizedString(holderContext,thesis.getThesisState())));
        holder.thesisBillingTextView.setText(holderContext.getString(R.string.thesis_billing_string_placeholder,BillingStateEnum.getLocalizedString(holderContext,thesis.getBillingState())));




        holder.itemThesisConstraintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityStarter.startThesisDetailsActivity(holderContext,thesis);
            }
        });


    }

    public class SupervisorViewHolder extends RecyclerView.ViewHolder{
        private final View itemThesisConstraintView;
        private final TextView thesisTitleTextView;
        private final TextView thesisStudentNameTextView;
        private final TextView thesisStateTextView;
        private final TextView thesisBillingTextView;


        public SupervisorViewHolder(@NonNull View itemView) {
            super(itemView);
            itemThesisConstraintView = itemView.findViewById(R.id.itemThesisConstraintView);
            thesisTitleTextView = itemView.findViewById(R.id.thesisTitleTextView);
            thesisStudentNameTextView = itemView.findViewById(R.id.thesisStudentNameTextView);
            thesisStateTextView = itemView.findViewById(R.id.thesisStateTextView);
            thesisBillingTextView = itemView.findViewById(R.id.thesisBillingTextView);
        }
    }
}

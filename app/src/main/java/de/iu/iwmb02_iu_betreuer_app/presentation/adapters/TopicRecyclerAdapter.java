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
import de.iu.iwmb02_iu_betreuer_app.data.dao.FirebaseFirestoreDao;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;
import de.iu.iwmb02_iu_betreuer_app.model.Thesis;
import de.iu.iwmb02_iu_betreuer_app.presentation.activities.ActivityStarter;
import de.iu.iwmb02_iu_betreuer_app.util.Callback;

public class TopicRecyclerAdapter extends FirestoreRecyclerAdapter<Thesis, TopicRecyclerAdapter.TopicViewHolder> {
    private  final FirebaseFirestoreDao firebaseFirestoreDao;
    private String mode;
    public TopicRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Thesis> options) {
        super(options);
        firebaseFirestoreDao = FirebaseFirestoreDao.getInstance();
    }

    @NonNull
    @Override
    public TopicRecyclerAdapter.TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic,parent,false);
        return new TopicRecyclerAdapter.TopicViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull TopicRecyclerAdapter.TopicViewHolder holder, int position, @NonNull Thesis thesis) {
        Context holderContext = holder.itemView.getContext();
        holder.topicBoardThesisTitleTextView.setText(holderContext.getString(R.string.topic_string_placeholder,thesis.getTitle()));

        firebaseFirestoreDao.getSupervisor(thesis.getPrimarySupervisorId(), new Callback<Supervisor>() {
            @Override
            public void onCallback(Supervisor supervisor) {
                holder.supervisorNameTextView.setText(holderContext.getString(R.string.supervisor_name_string_placeholder, supervisor.getFullName()));
            }
        });


        holder.itemTopicConstraintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityStarter.startThesisDetailsActivity(holderContext,thesis, mode);
            }
        });


    }

    public class TopicViewHolder extends RecyclerView.ViewHolder{
        private final View itemTopicConstraintView;
        private final TextView topicBoardThesisTitleTextView;
        private final TextView supervisorNameTextView;



        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTopicConstraintView = itemView.findViewById(R.id.itemTopicConstraintView);
            topicBoardThesisTitleTextView = itemView.findViewById(R.id.topicBoardThesisTitleTextView);
            supervisorNameTextView = itemView.findViewById(R.id.supervisorNameTextView);
        }
    }
}

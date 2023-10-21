package de.iu.iwmb02_iu_betreuer_app.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.iu.iwmb02_iu_betreuer_app.R;
import de.iu.iwmb02_iu_betreuer_app.model.Supervisor;

public class SupervisorRecyclerAdapter extends FirestoreRecyclerAdapter<Supervisor, SupervisorRecyclerAdapter.SupervisorViewHolder> {

    public SupervisorRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Supervisor> options) {
        super(options);
    }

    @NonNull
    @Override
    public SupervisorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_supervisor,parent,false);
        return new SupervisorViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull SupervisorViewHolder holder, int position, @NonNull Supervisor supervisor) {
        holder.txtSupervisorName.setText(supervisor.getFullName());
    }

    public class SupervisorViewHolder extends RecyclerView.ViewHolder{
        private final TextView txtSupervisorName;

        public SupervisorViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSupervisorName = itemView.findViewById(R.id.supervisorNameTextView);
        }
    }
}

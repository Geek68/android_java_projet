package com.example.android_java_projet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TopEnseignantAdapter extends RecyclerView.Adapter<TopEnseignantAdapter.ViewHolder> {
    private final Context context;
    private final List<Enseignant> enseignants;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView matriculeTextView, nomTextView, prestationTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            matriculeTextView = itemView.findViewById(R.id.matriculeTextView);
            nomTextView = itemView.findViewById(R.id.nomTextView);
            prestationTextView = itemView.findViewById(R.id.prestationTextView);
        }
    }

    public TopEnseignantAdapter(Context context, List<Enseignant> enseignants) {
        this.context = context;
        this.enseignants = enseignants;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.top_enseignant_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Enseignant enseignant = enseignants.get(position);
        holder.matriculeTextView.setText(enseignant.getMatricule());
        holder.nomTextView.setText(enseignant.getNom());
        holder.prestationTextView.setText(String.format("%.2f€", enseignant.getPrestation()));
    }

    @Override
    public int getItemCount() {
        return enseignants.size();
    }
}
package com.example.android_java_projet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EnseignantAdapter extends RecyclerView.Adapter<EnseignantAdapter.ViewHolder> {
    private final Context context;
    private final List<Enseignant> enseignants;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Enseignant enseignant);
        void onDeleteClick(Enseignant enseignant);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView matriculeTextView, nomTextView, prestationTextView;
        public Button editButton, deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            matriculeTextView = itemView.findViewById(R.id.matriculeTextView);
            nomTextView = itemView.findViewById(R.id.nomTextView);
            prestationTextView = itemView.findViewById(R.id.prestationTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public EnseignantAdapter(Context context, List<Enseignant> enseignants, OnItemClickListener listener) {
        this.context = context;
        this.enseignants = enseignants;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.enseignant_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Enseignant enseignant = enseignants.get(position);

        holder.matriculeTextView.setText(enseignant.getMatricule());
        holder.nomTextView.setText(enseignant.getNom());
        holder.prestationTextView.setText(String.format("%.2f€ (%d h × %.2f€/h)",
                enseignant.getPrestation(),
                enseignant.getNombreHeures(),
                enseignant.getTauxhoraire()));

        holder.editButton.setOnClickListener(v -> listener.onEditClick(enseignant));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(enseignant));
    }

    @Override
    public int getItemCount() {
        return enseignants.size();
    }
}
package com.example.android_java_projet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

public class AddEnseignantActivity extends AppCompatActivity {
    private EditText matriculeEditText, nomEditText, tauxhoraireEditText, nombreHeuresEditText;
    private Button saveButton;
    private Enseignant enseignantToEdit;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_enseignant);

        matriculeEditText = findViewById(R.id.matriculeEditText);
        nomEditText = findViewById(R.id.nomEditText);
        tauxhoraireEditText = findViewById(R.id.tauxHoraireEditText); // Correction du nom
        nombreHeuresEditText = findViewById(R.id.nombreHeuresEditText);
        saveButton = findViewById(R.id.saveButton);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Vérifier si on est en mode édition
        enseignantToEdit = (Enseignant) getIntent().getSerializableExtra("enseignant");
        if (enseignantToEdit != null) {
            // Pré-remplir les champs avec les données de l'enseignant
            matriculeEditText.setText(enseignantToEdit.getMatricule());
            nomEditText.setText(enseignantToEdit.getNom());
            tauxhoraireEditText.setText(String.valueOf(enseignantToEdit.getTauxhoraire()));
            nombreHeuresEditText.setText(String.valueOf(enseignantToEdit.getNombreHeures()));
            // Désactiver la modification du matricule en mode édition
            matriculeEditText.setEnabled(false);
        }

        saveButton.setOnClickListener(v -> saveEnseignant());
    }

    private void saveEnseignant() {
        String matricule = matriculeEditText.getText().toString().trim();
        String nom = nomEditText.getText().toString().trim();
        String tauxhoraireStr = tauxhoraireEditText.getText().toString().trim();
        String nombreHeuresStr = nombreHeuresEditText.getText().toString().trim();

        if (matricule.isEmpty() || nom.isEmpty() || tauxhoraireStr.isEmpty() || nombreHeuresStr.isEmpty()) {
            Toast.makeText(this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
            return;
        }

        double tauxhoraire;
        int nombreHeures;
        try {
            tauxhoraire = Double.parseDouble(tauxhoraireStr);
            nombreHeures = Integer.parseInt(nombreHeuresStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Taux horaire et nombre d'heures doivent être des nombres valides", Toast.LENGTH_SHORT).show();
            return;
        }

        double prestation = tauxhoraire * nombreHeures;

        Enseignant enseignant = new Enseignant(matricule, nom, tauxhoraire, nombreHeures, prestation);

        Call<Enseignant> call;
        if (enseignantToEdit != null) {
            if (enseignantToEdit.getId() == 0) {
                Toast.makeText(this, "ID invalide pour la mise à jour", Toast.LENGTH_SHORT).show();
                return;
            }
            call = apiService.updateEnseignant(enseignantToEdit.getId(), enseignant);
        } else {
            call = apiService.createEnseignant(enseignant);
        }

        call.enqueue(new Callback<Enseignant>() {
            @Override
            public void onResponse(Call<Enseignant> call, Response<Enseignant> response) {
                if (response.isSuccessful()) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    String errorMessage = "Erreur: " + response.code() + " - " + response.message();
                    Toast.makeText(AddEnseignantActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    try {
                        Log.e("API Response", "Erreur: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("API Response", "Impossible de lire le corps d'erreur", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Enseignant> call, Throwable t) {
                Toast.makeText(AddEnseignantActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Network Error", t.getMessage(), t);
            }
        });
    }
}
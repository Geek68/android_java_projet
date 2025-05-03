package com.example.android_java_projet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        tauxhoraireEditText = findViewById(R.id.tauxHoraireEditText);
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
            nombreHeuresEditText.setText(String.valueOf(enseignantToEdit.getNombreHeures())); // Correction ici
            // Calculer et afficher la prestation (optionnel)
        }

        saveButton.setOnClickListener(v -> saveEnseignant());
    }

    private void saveEnseignant() {
        String matricule = matriculeEditText.getText().toString().trim();
        String nom = nomEditText.getText().toString().trim();
        double tauxhoraire = Double.parseDouble(tauxhoraireEditText.getText().toString().trim());
        int nombreHeures = Integer.parseInt(nombreHeuresEditText.getText().toString().trim());
        double prestation = tauxhoraire * nombreHeures;

        Enseignant enseignant = new Enseignant(matricule, nom, tauxhoraire, nombreHeures, prestation);

        Call<Enseignant> call;
        if (enseignantToEdit != null) {
            call = apiService.updateEnseignant(enseignantToEdit.getId().intValue(), enseignant);
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
                    Toast.makeText(AddEnseignantActivity.this, "Erreur: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Enseignant> call, Throwable t) {
                Toast.makeText(AddEnseignantActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
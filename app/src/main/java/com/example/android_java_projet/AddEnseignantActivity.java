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
    private EditText matriculeEditText, nomEditText, tauxHoraireEditText, nombreHeuresEditText;
    private Button saveButton;
    private ApiService apiService;
    private Enseignant enseignantToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_enseignant);

        matriculeEditText = findViewById(R.id.matriculeEditText);
        nomEditText = findViewById(R.id.nomEditText);
        tauxHoraireEditText = findViewById(R.id.tauxHoraireEditText);
        nombreHeuresEditText = findViewById(R.id.nombreHeuresEditText);
        saveButton = findViewById(R.id.saveButton);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        enseignantToEdit = (Enseignant) getIntent().getSerializableExtra("enseignant");
        if (enseignantToEdit != null) {
            matriculeEditText.setText(enseignantToEdit.getMatricule());
            nomEditText.setText(enseignantToEdit.getNom());
            tauxHoraireEditText.setText(String.valueOf(enseignantToEdit.getTauxhoraire()));
            nombreHeuresEditText.setText(String.valueOf(enseignantToEdit.getNombre_d_heures()));
        }

        saveButton.setOnClickListener(v -> {
            String matricule = matriculeEditText.getText().toString();
            String nom = nomEditText.getText().toString();
            String tauxHoraireStr = tauxHoraireEditText.getText().toString();
            String nombreHeuresStr = nombreHeuresEditText.getText().toString();

            if (matricule.isEmpty() || nom.isEmpty() || tauxHoraireStr.isEmpty() || nombreHeuresStr.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            double tauxHoraire = Double.parseDouble(tauxHoraireStr);
            int nombreHeures = Integer.parseInt(nombreHeuresStr);
            double prestation = tauxHoraire * nombreHeures;

            Enseignant enseignant = new Enseignant(matricule, nom, tauxHoraire, nombreHeures, prestation);

            if (enseignantToEdit == null) {
                apiService.createEnseignant(enseignant).enqueue(new Callback<Enseignant>() {
                    @Override
                    public void onResponse(Call<Enseignant> call, Response<Enseignant> response) {
                        if (response.isSuccessful()) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Enseignant> call, Throwable t) {
                        Toast.makeText(AddEnseignantActivity.this, "Erreur: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                apiService.updateEnseignant(enseignantToEdit.getId(), enseignant).enqueue(new Callback<Enseignant>() {
                    @Override
                    public void onResponse(Call<Enseignant> call, Response<Enseignant> response) {
                        if (response.isSuccessful()) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Enseignant> call, Throwable t) {
                        Toast.makeText(AddEnseignantActivity.this, "Erreur: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
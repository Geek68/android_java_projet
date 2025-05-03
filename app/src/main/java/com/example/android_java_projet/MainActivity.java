package com.example.android_java_projet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements EnseignantAdapter.OnItemClickListener {
    private RecyclerView enseignantRecyclerView;
    private TextView statsTextView;
    private List<Enseignant> enseignantList;
    private EnseignantAdapter adapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enseignantRecyclerView = findViewById(R.id.enseignantRecyclerView);
        statsTextView = findViewById(R.id.statsTextView);
        Button addButton = findViewById(R.id.addButton);
        Button statsButton = findViewById(R.id.statsButton);

        enseignantList = new ArrayList<>();
        adapter = new EnseignantAdapter(this, enseignantList, this);

        enseignantRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        enseignantRecyclerView.setAdapter(adapter);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEnseignantActivity.class);
            startActivityForResult(intent, 1);
        });

        statsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatsActivity.class);
            startActivity(intent);
        });

        loadEnseignants();
        loadStats();
    }

    private void loadEnseignants() {
        apiService.getEnseignants().enqueue(new Callback<List<Enseignant>>() {
            @Override
            public void onResponse(Call<List<Enseignant>> call, Response<List<Enseignant>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    enseignantList.clear();
                    enseignantList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    // Log pour déboguer
                    Log.d("MainActivity", "Nombre d'enseignants: " + enseignantList.size());
                    for (Enseignant e : enseignantList) {
                        Log.d("MainActivity", "Enseignant: " + e.getMatricule() + ", Nom: " + e.getNom());
                    }

                    findViewById(R.id.emptyView).setVisibility(
                            enseignantList.isEmpty() ? View.VISIBLE : View.GONE);
                } else {
                    Log.e("MainActivity", "Erreur API: " + response.code());
                    Toast.makeText(MainActivity.this, "Erreur: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Enseignant>> call, Throwable t) {
                Log.e("MainActivity", "Erreur réseau", t);
                Toast.makeText(MainActivity.this, "Erreur: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadStats() {
        apiService.getStats().enqueue(new Callback<Stats>() {
            @Override
            public void onResponse(Call<Stats> call, Response<Stats> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Stats stats = response.body();
                    String statsText = String.format("Total: %f\nMin: %.2f\nMax: %.2f",
                            stats.getTotal(), stats.getMin(), stats.getMax());
                    statsTextView.setText(statsText);
                }
            }

            @Override
            public void onFailure(Call<Stats> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onEditClick(Enseignant enseignant) {
        Intent intent = new Intent(this, AddEnseignantActivity.class);
        intent.putExtra("enseignant", enseignant);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onDeleteClick(Enseignant enseignant) {
        apiService.deleteEnseignant(enseignant.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loadEnseignants();
                    loadStats();
                    Toast.makeText(MainActivity.this, "Enseignant supprimé", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadEnseignants();
            loadStats();
        }
    }
}
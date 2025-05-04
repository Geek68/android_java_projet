package com.example.android_java_projet;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class StatsActivity extends AppCompatActivity {
    private BarChart barChart;
    private TextView totalEnseignantsTextView, totalHeuresTextView, budgetTotalTextView;
    private RecyclerView topEnseignantsRecyclerView;
    private List<Enseignant> topEnseignantsList;
    private TopEnseignantAdapter topEnseignantAdapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Initialisation des vues
        barChart = findViewById(R.id.barChart);
        totalEnseignantsTextView = findViewById(R.id.totalEnseignantsTextView);
        totalHeuresTextView = findViewById(R.id.totalHeuresTextView);
        budgetTotalTextView = findViewById(R.id.budgetTotalTextView);
        topEnseignantsRecyclerView = findViewById(R.id.topEnseignantsRecyclerView);

        // Configuration du RecyclerView
        topEnseignantsList = new ArrayList<>();
        topEnseignantAdapter = new TopEnseignantAdapter(this, topEnseignantsList);
        topEnseignantsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        topEnseignantsRecyclerView.setAdapter(topEnseignantAdapter);

        // Initialisation de Retrofit
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Charger les données
        loadStats();
        loadTopEnseignants();
    }

    private void loadStats() {
        apiService.getStats().enqueue(new Callback<Stats>() {
            @Override
            public void onResponse(Call<Stats> call, Response<Stats> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Stats stats = response.body();

                    // Mettre à jour les TextView avec les valeurs dynamiques
                    totalEnseignantsTextView.setText(String.valueOf(stats.getTotalEnseignant()));
                    totalHeuresTextView.setText(stats.getTotalHeures() + "h");
                    budgetTotalTextView.setText(String.format("%.2f€", stats.getBudgetTotal()));

                    // Mettre à jour le BarChart (total, min, max)
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    entries.add(new BarEntry(1, (float) stats.getTotal()));
                    entries.add(new BarEntry(2, (float) stats.getMin()));
                    entries.add(new BarEntry(3, (float) stats.getMax()));

                    BarDataSet dataSet = new BarDataSet(entries, "Prestations");
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                    BarData barData = new BarData(dataSet);
                    barChart.setData(barData);
                    barChart.invalidate();
                } else {
                    Toast.makeText(StatsActivity.this, "Erreur: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("Stats Error", "Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Stats> call, Throwable t) {
                Toast.makeText(StatsActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Network Error", t.getMessage(), t);
            }
        });
    }

    private void loadTopEnseignants() {
        apiService.getTopEnseignants().enqueue(new Callback<List<Enseignant>>() {
            @Override
            public void onResponse(Call<List<Enseignant>> call, Response<List<Enseignant>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    topEnseignantsList.clear();
                    topEnseignantsList.addAll(response.body());
                    topEnseignantAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(StatsActivity.this, "Erreur: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("Top Enseignants Error", "Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Enseignant>> call, Throwable t) {
                Toast.makeText(StatsActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Network Error", t.getMessage(), t);
            }
        });
    }
}
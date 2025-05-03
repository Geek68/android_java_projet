package com.example.android_java_projet;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {
    private BarChart barChart;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        barChart = findViewById(R.id.barChart);
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        apiService.getStats().enqueue(new Callback<Stats>() {
            @Override
            public void onResponse(Call<Stats> call, Response<Stats> response) {
                if (response.isSuccessful()) {
                    Stats stats = response.body();
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    entries.add(new BarEntry(1, (float) stats.getTotal()));
                    entries.add(new BarEntry(2, (float) stats.getMin()));
                    entries.add(new BarEntry(3, (float) stats.getMax()));

                    BarDataSet dataSet = new BarDataSet(entries, "Prestations");
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                    BarData barData = new BarData(dataSet);
                    barChart.setData(barData);
                    barChart.invalidate();
                }
            }

            @Override
            public void onFailure(Call<Stats> call, Throwable t) {
                // Gérer l'erreur
            }
        });
    }
}
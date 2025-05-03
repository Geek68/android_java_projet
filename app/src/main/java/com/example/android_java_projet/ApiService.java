package com.example.android_java_projet;

import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface ApiService {
    @GET("enseignants")
    Call<List<Enseignant>> getEnseignants();

    @POST("enseignants")
    Call<Enseignant> createEnseignant(@Body Enseignant enseignant);

    @PUT("enseignants/{id}")
    Call<Enseignant> updateEnseignant(@Path("id") long id, @Body Enseignant enseignant);

    @DELETE("enseignants/{id}")
    Call<Void> deleteEnseignant(@Path("id") long id);

    @GET("enseignants/stats")
    Call<Stats> getStats();
}
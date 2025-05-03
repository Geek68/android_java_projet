package com.example.android_java_projet;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Enseignant implements Serializable {
    @SerializedName("id")
    private Long id;

    @SerializedName("matricule")
    private String matricule;

    @SerializedName("nom")
    private String nom;

    @SerializedName("tauxhoraire")
    private double tauxhoraire;

    @SerializedName("nombre_d_heures")
    private int nombreHeures;

    @SerializedName("prestation")
    private double prestation;

    public Enseignant() {}

    public Enseignant(String matricule, String nom, double tauxhoraire, int nombreHeures, double prestation) {
        this.matricule = matricule;
        this.nom = nom;
        this.tauxhoraire = tauxhoraire;
        this.nombreHeures = nombreHeures;
        this.prestation = prestation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getTauxhoraire() {
        return tauxhoraire;
    }

    public void setTauxhoraire(double tauxhoraire) {
        this.tauxhoraire = tauxhoraire;
    }

    public int getNombreHeures() {
        return nombreHeures;
    }

    public void setNombreHeures(int nombreHeures) {
        this.nombreHeures = nombreHeures;
    }

    public double getPrestation() {
        return prestation;
    }

    public void setPrestation(double prestation) {
        this.prestation = prestation;
    }
}
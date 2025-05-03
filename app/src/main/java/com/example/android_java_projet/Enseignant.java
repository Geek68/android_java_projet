package com.example.android_java_projet;

import java.io.Serializable;

public class Enseignant implements Serializable {
    private Long id;
    private String matricule;
    private String nom;
    private double tauxhoraire;
    private int nombre_d_heures;
    private double prestation;

    public Enseignant() {}

    public Enseignant(String matricule, String nom, double tauxhoraire, int nombre_d_heures, double prestation) {
        this.matricule = matricule;
        this.nom = nom;
        this.tauxhoraire = tauxhoraire;
        this.nombre_d_heures = nombre_d_heures;
        this.prestation = prestation;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public double getTauxhoraire() { return tauxhoraire; }
    public void setTauxhoraire(double tauxhoraire) { this.tauxhoraire = tauxhoraire; }
    public int getNombre_d_heures() { return nombre_d_heures; }
    public void setNombre_d_heures(int nombre_d_heures) { this.nombre_d_heures = nombre_d_heures; }
    public double getPrestation() { return prestation; }
    public void setPrestation(double prestation) { this.prestation = prestation; }
}
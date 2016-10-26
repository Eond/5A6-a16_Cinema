package ca.qc.cstj.cinecheck.cinecheck.models;

import com.google.gson.JsonObject;

/**
 * Created by 0784957 on 2016-10-26.
 */
public class Cinema {
    private String nom;
    private String adresse;

    public Cinema(JsonObject object) {
        this.nom = object.getAsJsonPrimitive("nom").getAsString();
        this.adresse = object.getAsJsonPrimitive("adresse").getAsString();
    }

    public String getNom(){
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }
}

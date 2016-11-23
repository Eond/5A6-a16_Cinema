package ca.qc.cstj.cinecheck.cinecheck.models;

import com.google.gson.JsonObject;

/**
 * Created by 0784957 on 2016-11-21.
 */
public class Commentaire {
    private String auteur;
    private String message;

    public Commentaire(JsonObject comm) {
        this.auteur = comm.getAsJsonPrimitive("auteur").getAsString();
        this.message = comm.getAsJsonPrimitive("texte").getAsString();
    }

    public String getAuteur() { return auteur; }
    public String getMessage() { return message; }
}

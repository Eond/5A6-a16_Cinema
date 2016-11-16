package ca.qc.cstj.cinecheck.cinecheck.models;

import android.util.Log;

import com.google.gson.JsonObject;

/**
 * Created by 0784957 on 2016-10-26.
 */
public class Horaire {
    private String cinemaURL;
    private String filmURL;
    private String cinema;
    private String film;
    private int filmDuree;
    private String urlCinema;
    private String urlFilm;
    private String filmImgUrl;
    private String dateHeure;

    public Horaire(JsonObject object) {
        Log.d("Fragment Horaire", object.toString());
        this.cinemaURL = object.getAsJsonPrimitive("cinema").getAsJsonObject().getAsJsonPrimitive("url").getAsString();
        this.cinema = object.getAsJsonPrimitive("cinema").getAsJsonObject().getAsJsonPrimitive("nom").getAsString();
        this.filmURL = object.getAsJsonPrimitive("film").getAsJsonObject().getAsJsonPrimitive("url").getAsString();
        this.film = object.getAsJsonPrimitive("film").getAsJsonObject().getAsJsonPrimitive("titre").getAsString();
        this.filmDuree = object.getAsJsonPrimitive("film").getAsJsonObject().getAsJsonPrimitive("duree").getAsInt();
        this.filmImgUrl = object.getAsJsonPrimitive("film").getAsJsonObject().getAsJsonPrimitive("imageUrl").getAsString();
        this.urlCinema = object.getAsJsonPrimitive("urlc").getAsString();
        this.urlFilm = object.getAsJsonPrimitive("urlf").getAsString();
        this.dateHeure = object.getAsJsonPrimitive("dateHeure").getAsString();
    }

    public String getCinemaUrl(){
        return cinemaURL;
    }

    public String getFilmUrl() {
        return filmURL;
    }

    public String getCinema(){
        return cinema;
    }

    public String getFilm() {
        return film;
    }

    public int getDuree() {
        return filmDuree;
    }

    public String getUrlCinema(){
        return urlCinema;
    }

    public String getUrlFilm() {
        return urlFilm;
    }

    public String getDateHeure() { return dateHeure; }

    public String getFilmImgUrl() { return filmImgUrl;}
}

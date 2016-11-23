package ca.qc.cstj.cinecheck.cinecheck.models;

import android.util.Log;

import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        this.cinemaURL = object.get("cinema").getAsJsonObject().getAsJsonPrimitive("url").getAsString();
        this.cinema = object.get("cinema").getAsJsonObject().getAsJsonPrimitive("nom").getAsString();
        this.filmURL = object.get("film").getAsJsonObject().getAsJsonPrimitive("url").getAsString();
        this.film = object.get("film").getAsJsonObject().getAsJsonPrimitive("titre").getAsString();
        this.filmDuree = object.get("film").getAsJsonObject().getAsJsonPrimitive("duree").getAsInt();
        this.filmImgUrl = object.get("film").getAsJsonObject().getAsJsonPrimitive("imageUrl").getAsString();
        this.urlCinema = object.getAsJsonPrimitive("urlc").getAsString();
        this.urlFilm = object.getAsJsonPrimitive("urlf").getAsString();
        this.dateHeure = object.getAsJsonPrimitive("dateHeure").getAsString();
        this.dateHeure = this.dateHeure.replace("Z", "");
        this.dateHeure = this.dateHeure.replace("T", " ");
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.CANADA);
        try {
            this.dateHeure = df.format(df.parse(this.dateHeure));
        } catch (ParseException e) {

        }
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

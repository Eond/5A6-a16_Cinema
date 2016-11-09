package ca.qc.cstj.cinecheck.cinecheck.models;

import com.google.gson.JsonObject;

/**
 * Created by 0784957 on 2016-10-26.
 */
public class Film {
    private String imgUrl;
    private String titre;
    private String url;

    public Film(JsonObject object) {
        this.url = object.getAsJsonPrimitive("url").getAsString();
        this.imgUrl = object.getAsJsonPrimitive("imageUrl").getAsString();
        this.titre = object.getAsJsonPrimitive("titre").getAsString();
    }

    public String getImgUrl(){
        return imgUrl;
    }

    public String getUrl(){
        return url;
    }

    public String getTitre() {
        return titre;
    }
}

package ca.qc.cstj.cinecheck.cinecheck.helpers;

import java.util.ResourceBundle;

import ca.qc.cstj.cinecheck.cinecheck.R;

/**
 * Created by 0784957 on 2016-10-26.
 */
public enum FragmentTags {
    CINEMAS("cinemas", 0),
    FILMS("films", 1);

    public final String tag;
    public final int index;

    FragmentTags(String tag, int index) {
        this.tag = tag;
        this.index = index;
    }

    @Override
    public String toString() {
        return this.tag;
    }
}

package com.example.android.popuvie.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Asus X550ZE on 12/8/2016.
 */

public class GenresResponse {

    @SerializedName("genres")
    private List<Genre> genres;

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

}

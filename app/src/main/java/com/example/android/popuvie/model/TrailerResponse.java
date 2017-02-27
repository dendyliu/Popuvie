package com.example.android.popuvie.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Asus X550ZE on 12/7/2016.
 */

public class TrailerResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<Trailer> results;

    public int getId() {
        return id;
    }

    public void setId(int page) {
        this.id = page;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}

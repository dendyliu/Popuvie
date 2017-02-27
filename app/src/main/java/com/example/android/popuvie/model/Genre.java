package com.example.android.popuvie.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus X550ZE on 12/8/2016.
 */

public class Genre {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public Genre(int id,String name){
        this.id = id;
        this.name= name;
    }

    public int getId(){return this.id;}

    public void setId(int id){this.id= id;}

    public String getName(){ return this.name;}

    public void setName(String name){ this.name = name;}

}

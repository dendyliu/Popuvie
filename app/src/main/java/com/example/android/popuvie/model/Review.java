package com.example.android.popuvie.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Asus X550ZE on 12/8/2016.
 */

public class Review {

    @SerializedName("id")
    private String id;
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("url")
    private String url;

    public Review(String id, String author,String content,String url){
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }


    public String getid(){ return this.id;}

    public void setId(String id){ this.id=id;}

    public String getAuthor(){return this.author;}

    public void setAuthor(String author){this.author=author;}

    public String getContent(){return this.content;}

    public void setContent(String content){this.content = content;}

    public String getUrl(){return this.url;}

    public void setUrl(String url){this.url = url;}


}

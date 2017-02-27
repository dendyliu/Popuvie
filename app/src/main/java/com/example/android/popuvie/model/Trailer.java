package com.example.android.popuvie.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus X550ZE on 12/7/2016.
 */

public class Trailer {
    @SerializedName("id")
    private String id;
    @SerializedName("iso_639_1")
    private String iso_639_1;
    @SerializedName("iso_3166_1")
    private String iso_3166_1;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;
    @SerializedName("site")
    private String site;
    @SerializedName("size")
    private Integer size;
    @SerializedName("type")
    private String type;

    public Trailer(String id, String iso_639_1, String iso_3166_1, String key, String name, String site, Integer size, String type){
        this.id = id;
        this.iso_639_1 = iso_639_1;
        this.iso_3166_1 = iso_3166_1;
        this.key = key;
        this.name=  name;
        this.site= site;
        this.size = size;
        this.type = type;
    }

    public String getId(){ return this.id;}
    public void setId(String id){ this.id= id;}

    public String getIso_639_1(){ return this.iso_639_1;}
    public void setIso_639_1(String iso_639_1){ this.iso_639_1= iso_639_1;}

    public String getIso_3166_1(){ return this.iso_3166_1;}
    public void setIso_3166_1(String iso_3166_1){ this.iso_3166_1= iso_3166_1;}

    public String getKey(){ return this.key;}
    public void setKey(String key){ this.key= key;}

    public String getName(){ return this.name;}
    public void setName(String name){ this.name= name;}

    public String getSite(){ return this.site;}
    public void setSite(String site){ this.site= site;}

    public Integer getSize(){ return this.size;}
    public void setSize(Integer name){ this.size= size;}

    public String getType(){ return this.type;}
    public void setType(String type){ this.type= type;}
}

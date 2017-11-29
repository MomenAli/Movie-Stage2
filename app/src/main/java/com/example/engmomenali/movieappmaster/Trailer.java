package com.example.engmomenali.movieappmaster;

/**
 * Created by Momen Ali on 11/27/2017.
 */

public class Trailer {
    /*"id":"5963e0ea92514122510dac6c",
    "iso_639_1":
    "en","iso_3166_1":
    "US","key":"Qz8cjvKJLuw",
    "name":"Official Teaser",
    "site":"YouTube",
    "size":1080,
    "type":"Teaser"*/

    public Trailer(String id, String key, String size) {
        this.id = id;
        this.key = key;
        this.size = size;
    }

    public static final int Id_Tag = 50;
    String id;
    String key;
    String size;


    public Trailer() {
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", size='" + size + '\'' +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

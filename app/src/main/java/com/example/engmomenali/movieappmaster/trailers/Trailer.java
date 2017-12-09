package com.example.engmomenali.movieappmaster.trailers;

/**
 * Created by Momen Ali on 11/27/2017.
 */

public class Trailer  {


    public Trailer(String id, String key, String size) {
        this.id = id;
        this.key = key;
        this.size = size;
    }

    /* holds the id of the trailer*/
    String id;
    /* holds the key of the trailer which used in fetching the trailer
     * from youtube  */
    String key;
    /* represent the resolution of the movie*/
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

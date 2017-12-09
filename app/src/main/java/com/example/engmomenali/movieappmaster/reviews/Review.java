package com.example.engmomenali.movieappmaster.reviews;

/**
 * Created by Momen Ali on 11/28/2017.
 */

public class Review {
    /* holds the id of the review */
    String id;
    /* holds the author name of the review */
    String author;
    /* holds the content of the review */
    String content;
    /* holds the url which can open the review in the site*/
    String Url;

    public Review() {
    }

    public Review(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        content = content;
        Url = url;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", Content='" + content + '\'' +
                ", Url='" + Url + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}

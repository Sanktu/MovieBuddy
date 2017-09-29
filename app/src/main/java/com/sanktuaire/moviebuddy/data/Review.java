package com.sanktuaire.moviebuddy.data;

/**
 * Created by Sanktuaire on 28/09/2017.
 */

public class Review {
    private String id;
    private String author;
    private String content;
    private String url;
    private String excerpt;

    public Review(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
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
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExcerpt() {
        if (content.length() > 125)
            return content.substring(0, 120) + " ...";
        else
            return content;
    }
}

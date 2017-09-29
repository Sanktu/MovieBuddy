package com.sanktuaire.moviebuddy.data;

/**
 * Created by Sanktuaire on 27/09/2017.
 */

public class Trailer {
    private String name;
    private String size;
    private String source;
    private String type;

    public Trailer(String name, String size, String link, String type) {
        this.name = name;
        this.size = size;
        this.source = link;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHd() {
        if (size.equals("HD"))
            return true;
        return false;
    }

    public void source(String source) {
        this.source = source;
    }

    public String getSource() {
        return "https://www.youtube.com/watch?v=" + source;
    }

    public void setSource(String link) {
        this.source = link;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

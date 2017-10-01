package com.sanktuaire.moviebuddy.data.trailer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sanktuaire on 27/09/2017.
 */

public class Trailer implements Parcelable {
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

    protected Trailer(Parcel in) {
        name = in.readString();
        size = in.readString();
        source = in.readString();
        type = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(size);
        dest.writeString(source);
        dest.writeString(type);
    }
}

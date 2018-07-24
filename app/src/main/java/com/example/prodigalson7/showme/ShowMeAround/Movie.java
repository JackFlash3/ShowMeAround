package com.example.prodigalson7.showme.ShowMeAround;

import java.util.List;

/**
 * Created by ProdigaLsON7 on 25/12/2017.
 */

public class Movie {
    private int id;
    private String title;
    private String image;
    private double rating;
    private int realseYear;
    private List<String> genre;

    public Movie(int id, String title, String image, double rating, int realseYear, List<String> genre) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.rating = rating;
        this.realseYear = realseYear;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRealseYear() {
        return realseYear;
    }

    public void setRealseYear(int realseYear) {
        this.realseYear = realseYear;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }
}

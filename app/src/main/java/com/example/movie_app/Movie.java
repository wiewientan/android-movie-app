package com.example.movie_app;

public class Movie {
    private String id; // Tambahkan ini
    private String movie_name;
    private String genre;
    private String date_release;
    private String producer;

    // Constructor harus menerima ID sekarang
    public Movie(String id, String movie_name, String genre, String date_release, String producer) {
        this.id = id;
        this.movie_name = movie_name;
        this.genre = genre;
        this.date_release = date_release;
        this.producer = producer;
    }

    // GETTER untuk ID (Ini yang dicari oleh Adapter/UpdateActivity)
    public String getId() {
        return id;
    }

    public String getMovieName() {
        return movie_name;
    }

    public String getGenre() {
        return genre;
    }

    public String getDateRelease() {
        return date_release;
    }

    public String getProducer() {
        return producer;
    }
}
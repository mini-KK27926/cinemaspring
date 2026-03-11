package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateFilmDto {
    private String film_id;
    @NotBlank(message = "Title is required")
    private String title;
    private String genre;
    private String duration;
    private String age_rating;

    public CreateFilmDto() {}

    public String getFilm_id() { return film_id; }
    public void setFilm_id(String film_id) { this.film_id = film_id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getAge_rating() { return age_rating; }
    public void setAge_rating(String age_rating) { this.age_rating = age_rating; }
}
package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "films")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Long filmId;

    @NotBlank(message = "Название обязательно")
    @Size(max = 200, message = "Название не должно превышать 200 символов")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Жанр обязателен")
    @Size(max = 100, message = "Жанр не должен превышать 100 символов")
    @Column(name = "genre", nullable = false)
    private String genre;

    @NotNull(message = "Длительность обязательна")
    @Min(value = 1, message = "Длительность должна быть положительной")
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @NotBlank(message = "Возрастной рейтинг обязателен")
    @Pattern(regexp = "0\\+|6\\+|12\\+|16\\+|18\\+",
            message = "Возрастной рейтинг должен быть: 0+, 6+, 12+, 16+, 18+")
    @Column(name = "age_rating", nullable = false)
    private String ageRating;

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL)
    private List<Session> sessions;

    // Конструкторы
    public Film() {}

    public Film(String title, String genre, Integer duration, String ageRating) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.ageRating = ageRating;
    }

    // Геттеры и сеттеры
    public Long getFilmId() { return filmId; }
    public void setFilmId(Long filmId) { this.filmId = filmId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public String getAgeRating() { return ageRating; }
    public void setAgeRating(String ageRating) { this.ageRating = ageRating; }

    public List<Session> getSessions() { return sessions; }
    public void setSessions(List<Session> sessions) { this.sessions = sessions; }

    @Override
    public String toString() {
        return title + " (" + genre + ", " + duration + " мин, " + ageRating + ")";
    }
}
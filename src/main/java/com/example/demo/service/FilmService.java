package com.example.demo.service;

import com.example.demo.dto.CreateFilmDto;
import com.example.demo.entity.Film;
import com.example.demo.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FilmService {

    @Autowired
    private FilmRepository filmRepository;

    public List<Film> findAllFilm() {
        return filmRepository.findAll();
    }

    public List<Film> findFilmsByTitle(String title) {
        return filmRepository.findByTitleContainingIgnoreCase(title);
    }

    public Film findFilmById(Long id) {
        return filmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Фильм не найден"));
    }

    @Transactional
    public void saveFilm(CreateFilmDto dto) {
        if (filmRepository.existsByTitleIgnoreCase(dto.getTitle())) {
            throw new RuntimeException("Фильм с названием '" + dto.getTitle() + "' уже есть в базе!");
        }
        Film film = new Film();
        mapDtoToEntity(dto, film);
        filmRepository.save(film);
    }
    public boolean exists(String title) {
        return filmRepository.existsByTitleIgnoreCase(title);
    }

    @Transactional
    public void updateFilm(Long id, CreateFilmDto dto) {
        Film film = findFilmById(id);
        mapDtoToEntity(dto, film);
        filmRepository.save(film);
    }

    public void deleteFilm(Long id) {
        filmRepository.deleteById(id);
    }

    private void mapDtoToEntity(CreateFilmDto dto, Film film) {
        film.setTitle(dto.getTitle());
        film.setGenre(dto.getGenre());
        if (dto.getDuration() != null && !dto.getDuration().isEmpty()) {
            film.setDuration(Integer.parseInt(dto.getDuration()));
        }
        film.setAgeRating(dto.getAge_rating());
    }
}
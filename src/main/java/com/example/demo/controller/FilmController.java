package com.example.demo.controller;

import com.example.demo.dto.CreateFilmDto;
import com.example.demo.entity.Film;
import com.example.demo.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/films")
public class FilmController {
    @Autowired
    private FilmService filmService;

    @GetMapping
    public String listFilms(Model model, @RequestParam(value = "title", required = false) String title) {
        if (title != null && !title.isEmpty()) {
            model.addAttribute("films", filmService.findFilmsByTitle(title));
        } else {
            model.addAttribute("films", filmService.findAllFilm());
        }
        return "films/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("film", new CreateFilmDto());
        return "films/create";
    }

    @PostMapping
    public String saveFilm(@ModelAttribute("film") @Validated CreateFilmDto filmDto, BindingResult bindingResult) {
        if (filmService.exists(filmDto.getTitle())) {
            bindingResult.rejectValue("title", "error.film", "Фильм с таким названием уже существует!");
        }

        if (bindingResult.hasErrors()) {
            return "films/create";
        }

        filmService.saveFilm(filmDto);
        return "redirect:/films";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Film film = filmService.findFilmById(id);
        CreateFilmDto dto = new CreateFilmDto();

        dto.setFilm_id(String.valueOf(film.getFilmId()));
        dto.setTitle(film.getTitle());
        dto.setGenre(film.getGenre());
        dto.setDuration(film.getDuration() != null ? String.valueOf(film.getDuration()) : "");
        dto.setAge_rating(film.getAgeRating());

        model.addAttribute("film", dto);
        model.addAttribute("id", id);
        return "films/edit";
    }

    @PostMapping("/{id}")
    public String updateFilm(@PathVariable Long id, @ModelAttribute("film") CreateFilmDto filmDto) {
        filmService.updateFilm(id, filmDto);
        return "redirect:/films";
    }

    @PostMapping("/{id}/delete")
    public String deleteFilm(@PathVariable Long id) {
        filmService.deleteFilm(id);
        return "redirect:/films";
    }
    @GetMapping("/{id}/delete")
    public String confirmDelete(@PathVariable Long id, Model model) {
        Film film = filmService.findFilmById(id);
        model.addAttribute("film", film);
        return "films/delete";
    }
}
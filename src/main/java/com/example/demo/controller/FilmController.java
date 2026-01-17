package com.example.demo.controller;

import com.example.demo.entity.Film;
import com.example.demo.repository.FilmRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/films") // Все маршруты начинаются с /films
public class FilmController {

    @Autowired
    private FilmRepository filmRepository; // Репозиторий для работы с фильмами

    /**
     * Отображает список всех фильмов с возможностью фильтрации
     * @param model Модель для передачи данных
     * @param genre Жанр для фильтрации (необязательный параметр)
     * @param search Поисковый запрос (необязательный параметр)
     * @return имя шаблона списка фильмов
     */
    @GetMapping
    public String listFilms(Model model,
                            @RequestParam(required = false) String genre,
                            @RequestParam(required = false) String search) {
        List<Film> films;

        // Фильтрация по поисковому запросу
        if (search != null && !search.isEmpty()) {
            films = filmRepository.findByTitleContainingIgnoreCase(search);
            model.addAttribute("searchQuery", search);
        }
        // Фильтрация по жанру
        else if (genre != null && !genre.isEmpty()) {
            films = filmRepository.findByGenre(genre);
            model.addAttribute("selectedGenre", genre);
        }
        // Без фильтрации - все фильмы отсортированные
        else {
            films = filmRepository.findAllByOrderByTitleAsc();
        }

        // Получаем все уникальные жанры для выпадающего списка фильтрации
        List<String> genres = filmRepository.findAll().stream()
                .map(Film::getGenre)
                .distinct()
                .toList();

        // Передаем данные в модель
        model.addAttribute("films", films);
        model.addAttribute("genres", genres);
        model.addAttribute("ageRatings", List.of("0+", "6+", "12+", "16+", "18+"));
        return "films/list";
    }

    /**
     * Отображает форму для добавления нового фильма
     * @param model Модель для передачи формы
     * @return имя шаблона формы создания
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("film", new Film()); // Пустой объект для формы
        model.addAttribute("ageRatings", List.of("0+", "6+", "12+", "16+", "18+"));
        return "films/create";
    }

    /**
     * Обрабатывает создание нового фильма
     * @param film Данные фильма из формы
     * @param result Результаты валидации
     * @param model Модель для передачи данных
     * @return перенаправление на список фильмов или возврат формы при ошибках
     */
    @PostMapping
    public String createFilm(@Valid @ModelAttribute Film film,
                             BindingResult result, Model model) {
        // Проверяем ошибки валидации
        if (result.hasErrors()) {
            model.addAttribute("ageRatings", List.of("0+", "6+", "12+", "16+", "18+"));
            return "films/create"; // Возвращаем форму с ошибками
        }
        filmRepository.save(film); // Сохраняем фильм в БД
        return "redirect:/films"; // Перенаправляем на список
    }

    /**
     * Отображает форму для редактирования существующего фильма
     * @param id ID фильма для редактирования
     * @param model Модель для передачи данных
     * @return имя шаблона формы редактирования
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        // Находим фильм по ID или выбрасываем исключение
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Фильм с ID " + id + " не найден"));

        model.addAttribute("film", film);
        model.addAttribute("ageRatings", List.of("0+", "6+", "12+", "16+", "18+"));
        return "films/edit";
    }

    /**
     * Обрабатывает обновление фильма
     * @param id ID фильма для обновления
     * @param film Обновленные данные фильма
     * @param result Результаты валидации
     * @param model Модель для передачи данных
     * @return перенаправление на список фильмов
     */
    @PostMapping("/update/{id}")
    public String updateFilm(@PathVariable("id") Long id,
                             @Valid @ModelAttribute Film film,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("ageRatings", List.of("0+", "6+", "12+", "16+", "18+"));
            return "films/edit";
        }

        film.setFilmId(id); // Устанавливаем ID из URL
        filmRepository.save(film); // Сохраняем изменения
        return "redirect:/films";
    }

    /**
     * Удаляет фильм по ID
     * @param id ID фильма для удаления
     * @return перенаправление на список фильмов
     */
    @GetMapping("/delete/{id}")
    public String deleteFilm(@PathVariable("id") Long id) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Фильм с ID " + id + " не найден"));

        // Проверяем, есть ли связанные сеансы
        if (film.getSessions() != null && !film.getSessions().isEmpty()) {
            throw new IllegalStateException("Нельзя удалить фильм, у которого есть сеансы.");
        }

        filmRepository.delete(film);
        return "redirect:/films";
    }

    /**
     * Отображает детальную информацию о фильме
     * @param id ID фильма
     * @param model Модель для передачи данных
     * @return имя шаблона просмотра фильма
     */
    @GetMapping("/{id}")
    public String viewFilm(@PathVariable("id") Long id, Model model) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Фильм с ID " + id + " не найден"));

        model.addAttribute("film", film);
        return "films/view";
    }
}

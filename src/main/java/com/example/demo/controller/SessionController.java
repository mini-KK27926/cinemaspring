package com.example.demo.controller;

import com.example.demo.entity.Session;
import com.example.demo.entity.Film;
import com.example.demo.entity.Hall;
import com.example.demo.repository.SessionRepository;
import com.example.demo.repository.FilmRepository;
import com.example.demo.repository.HallRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/sessions") // Все маршруты начинаются с /sessions
public class SessionController {

    @Autowired
    private SessionRepository sessionRepository; // Репозиторий для работы с сеансами
    @Autowired
    private FilmRepository filmRepository;       // Репозиторий для работы с фильмами
    @Autowired
    private HallRepository hallRepository;       // Репозиторий для работы с залами

    /**
     * Отображает список всех сеансов с возможностью фильтрации по дате
     * @param model Модель для передачи данных
     * @param date Дата для фильтрации (необязательный параметр)
     * @return имя шаблона списка сеансов
     */
    @GetMapping
    public String listSessions(Model model,
                               @RequestParam(required = false) String date) {
        List<Session> sessions;

        // Фильтрация по дате
        if (date != null && !date.isEmpty()) {
            sessions = sessionRepository.findBySessionDate(LocalDate.parse(date));
        } else {
            // Без фильтрации - все сеансы отсортированные
            sessions = sessionRepository.findAllByOrderBySessionDateAscStartTimeAsc();
        }

        model.addAttribute("sessions", sessions);
        model.addAttribute("today", LocalDate.now()); // Текущая дата для фильтрации
        return "sessions/list";
    }

    /**
     * Отображает форму для создания нового сеанса
     * @param model Модель для передачи данных
     * @return имя шаблона формы создания
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("session", new Session()); // Пустой объект для формы

        // Получаем списки фильмов и залов для выпадающих списков
        List<Film> films = filmRepository.findAllByOrderByTitleAsc();
        List<Hall> halls = hallRepository.findAllByOrderByHallNumberAsc();

        model.addAttribute("films", films);
        model.addAttribute("halls", halls);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("minTime", LocalTime.of(9, 0)); // Минимальное время сеанса (9:00)

        return "sessions/create";
    }

    /**
     * Обрабатывает создание нового сеанса
     * @param session Данные сеанса из формы
     * @param result Результаты валидации
     * @param filmId ID выбранного фильма
     * @param hallId ID выбранного зала
     * @param model Модель для передачи данных
     * @return перенаправление на список сеансов
     */
    @PostMapping
    public String createSession(@Valid @ModelAttribute Session session,
                                BindingResult result,
                                @RequestParam("filmId") Long filmId,
                                @RequestParam("hallId") Long hallId,
                                Model model) {

        if (result.hasErrors()) {
            // При ошибках возвращаем списки фильмов и залов
            model.addAttribute("films", filmRepository.findAllByOrderByTitleAsc());
            model.addAttribute("halls", hallRepository.findAllByOrderByHallNumberAsc());
            model.addAttribute("today", LocalDate.now());
            return "sessions/create";
        }

        // Находим фильм и зал по ID
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new IllegalArgumentException("Фильм не найден"));
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new IllegalArgumentException("Зал не найден"));

        // Устанавливаем связи
        session.setFilm(film);
        session.setHall(hall);

        sessionRepository.save(session); // Сохраняем сеанс
        return "redirect:/sessions";
    }

    /**
     * Отображает форму для редактирования существующего сеанса
     * @param id ID сеанса для редактирования
     * @param model Модель для передачи данных
     * @return имя шаблона формы редактирования
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Сеанс не найден"));

        model.addAttribute("session", session);
        model.addAttribute("films", filmRepository.findAllByOrderByTitleAsc());
        model.addAttribute("halls", hallRepository.findAllByOrderByHallNumberAsc());
        model.addAttribute("today", LocalDate.now());

        return "sessions/edit";
    }

    /**
     * Обрабатывает обновление сеанса
     * @param id ID сеанса для обновления
     * @param session Обновленные данные сеанса
     * @param result Результаты валидации
     * @param filmId ID выбранного фильма
     * @param hallId ID выбранного зала
     * @param model Модель для передачи данных
     * @return перенаправление на список сеансов
     */
    @PostMapping("/update/{id}")
    public String updateSession(@PathVariable("id") Long id,
                                @Valid @ModelAttribute Session session,
                                BindingResult result,
                                @RequestParam("filmId") Long filmId,
                                @RequestParam("hallId") Long hallId,
                                Model model) {

        if (result.hasErrors()) {
            model.addAttribute("films", filmRepository.findAllByOrderByTitleAsc());
            model.addAttribute("halls", hallRepository.findAllByOrderByHallNumberAsc());
            return "sessions/edit";
        }

        session.setSessionId(id); // Устанавливаем ID из URL

        // Находим фильм и зал по ID
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new IllegalArgumentException("Фильм не найден"));
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new IllegalArgumentException("Зал не найден"));

        // Устанавливаем связи
        session.setFilm(film);
        session.setHall(hall);

        sessionRepository.save(session); // Сохраняем изменения
        return "redirect:/sessions";
    }

    /**
     * Удаляет сеанс по ID
     * @param id ID сеанса для удаления
     * @return перенаправление на список сеансов
     */
    @GetMapping("/delete/{id}")
    public String deleteSession(@PathVariable("id") Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Сеанс не найден"));

        // Проверяем, есть ли связанные билеты
        if (session.getTickets() != null && !session.getTickets().isEmpty()) {
            throw new IllegalStateException("Нельзя удалить сеанс, на который проданы билеты");
        }

        sessionRepository.delete(session);
        return "redirect:/sessions";
    }
}
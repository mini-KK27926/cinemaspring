package com.example.demo.controller;

import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

@Controller
public class MainController {

    // Репозитории для доступа к данным
    @Autowired
    private FilmRepository filmRepository;      // Для работы с фильмами
    @Autowired
    private HallRepository hallRepository;      // Для работы с залами
    @Autowired
    private SessionRepository sessionRepository; // Для работы с сеансами
    @Autowired
    private TicketRepository ticketRepository;   // Для работы с билетами
    @Autowired
    private CashierRepository cashierRepository; // Для работы с кассирами
    @Autowired
    private SaleRepository saleRepository;       // Для работы с продажами

    /**
     * Отображает главную страницу с общей статистикой
     * @param model Модель для передачи данных в представление
     * @return имя шаблона главной страницы
     */
    @GetMapping("/")
    public String index(Model model) {
        // Добавляем статистику по всем сущностям
        model.addAttribute("filmsCount", filmRepository.count());      // Количество фильмов
        model.addAttribute("hallsCount", hallRepository.count());      // Количество залов
        model.addAttribute("sessionsCount", sessionRepository.count()); // Количество сеансов
        model.addAttribute("ticketsCount", ticketRepository.count());   // Количество билетов
        model.addAttribute("cashiersCount", cashierRepository.count()); // Количество кассиров
        model.addAttribute("salesCount", saleRepository.count());       // Количество продаж

        // Сеансы на сегодняшний день
        model.addAttribute("todaySessions",
                sessionRepository.findBySessionDate(LocalDate.now()).size());

        return "index"; // Возвращаем шаблон index.html
    }
}
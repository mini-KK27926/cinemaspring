package com.example.demo.controller;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.Session;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.SessionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/tickets") // Все маршруты начинаются с /tickets
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository; // Репозиторий для работы с билетами
    @Autowired
    private SessionRepository sessionRepository; // Репозиторий для работы с сеансами

    /**
     * Отображает список всех билетов с возможностью фильтрации
     * @param model Модель для передачи данных
     * @param sessionId ID сеанса для фильтрации (необязательный параметр)
     * @param status Статус билета для фильтрации (необязательный параметр)
     * @return имя шаблона списка билетов
     */
    @GetMapping
    public String listTickets(Model model,
                              @RequestParam(required = false) Long sessionId,
                              @RequestParam(required = false) String status) {
        List<Ticket> tickets;

        // Фильтрация по сеансу
        if (sessionId != null) {
            tickets = ticketRepository.findBySessionSessionId(sessionId);
            model.addAttribute("selectedSessionId", sessionId);
        }
        // Фильтрация по статусу
        else if (status != null && !status.isEmpty()) {
            tickets = ticketRepository.findByStatus(status);
            model.addAttribute("selectedStatus", status);
        }
        // Без фильтрации - все билеты
        else {
            tickets = ticketRepository.findAll();
        }

        // Получаем список всех сеансов для выпадающего списка фильтрации
        List<Session> sessions = sessionRepository.findAllByOrderBySessionDateAscStartTimeAsc();

        model.addAttribute("tickets", tickets);
        model.addAttribute("sessions", sessions);
        model.addAttribute("statuses", List.of("свободен", "продан"));
        return "tickets/list";
    }

    /**
     * Отображает форму для создания нового билета
     * @param model Модель для передачи данных
     * @return имя шаблона формы создания
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ticket", new Ticket()); // Пустой объект для формы

        // Получаем список всех сеансов для выпадающего списка
        List<Session> sessions = sessionRepository.findAllByOrderBySessionDateAscStartTimeAsc();

        model.addAttribute("sessions", sessions);
        model.addAttribute("statuses", List.of("свободен", "продан"));
        return "tickets/create";
    }

    /**
     * Обрабатывает создание нового билета
     * @param ticket Данные билета из формы
     * @param result Результаты валидации
     * @param sessionId ID выбранного сеанса
     * @param model Модель для передачи данных
     * @return перенаправление на список билетов
     */
    @PostMapping
    public String createTicket(@Valid @ModelAttribute Ticket ticket,
                               BindingResult result,
                               @RequestParam("sessionId") Long sessionId,
                               Model model) {

        if (result.hasErrors()) {
            model.addAttribute("sessions", sessionRepository.findAllByOrderBySessionDateAscStartTimeAsc());
            model.addAttribute("statuses", List.of("свободен", "продан"));
            return "tickets/create";
        }

        // Находим сеанс по ID
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Сеанс не найден"));

        ticket.setSession(session); // Устанавливаем связь с сеансом

        ticketRepository.save(ticket); // Сохраняем билет
        return "redirect:/tickets";
    }

    /**
     * Отображает форму для редактирования существующего билета
     * @param id ID билета для редактирования
     * @param model Модель для передачи данных
     * @return имя шаблона формы редактирования
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Билет не найден"));

        List<Session> sessions = sessionRepository.findAllByOrderBySessionDateAscStartTimeAsc();

        model.addAttribute("ticket", ticket);
        model.addAttribute("sessions", sessions);
        model.addAttribute("statuses", List.of("свободен", "продан"));
        return "tickets/edit";
    }

    /**
     * Обрабатывает обновление билета
     * @param id ID билета для обновления
     * @param ticket Обновленные данные билета
     * @param result Результаты валидации
     * @param sessionId ID выбранного сеанса
     * @param model Модель для передачи данных
     * @return перенаправление на список билетов
     */
    @PostMapping("/update/{id}")
    public String updateTicket(@PathVariable("id") Long id,
                               @Valid @ModelAttribute Ticket ticket,
                               BindingResult result,
                               @RequestParam("sessionId") Long sessionId,
                               Model model) {

        if (result.hasErrors()) {
            model.addAttribute("sessions", sessionRepository.findAllByOrderBySessionDateAscStartTimeAsc());
            model.addAttribute("statuses", List.of("свободен", "продан"));
            return "tickets/edit";
        }

        ticket.setTicketId(id); // Устанавливаем ID из URL

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Сеанс не найден"));

        ticket.setSession(session); // Устанавливаем связь с сеансом

        ticketRepository.save(ticket); // Сохраняем изменения
        return "redirect:/tickets";
    }

    /**
     * Удаляет билет по ID
     * @param id ID билета для удаления
     * @return перенаправление на список билетов
     */
    @GetMapping("/delete/{id}")
    public String deleteTicket(@PathVariable("id") Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Билет не найден"));

        // Проверяем, не продан ли билет
        if (ticket.getSale() != null) {
            throw new IllegalStateException("Нельзя удалить билет, который уже продан");
        }

        ticketRepository.delete(ticket);
        return "redirect:/tickets";
    }
}
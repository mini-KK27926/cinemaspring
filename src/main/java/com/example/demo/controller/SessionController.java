package com.example.demo.controller;

import com.example.demo.dto.CreateSessionDto;
import com.example.demo.entity.Session;
import com.example.demo.repository.FilmRepository;
import com.example.demo.repository.HallRepository;
import com.example.demo.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private SessionService sessionService;

    @GetMapping
    public String listSessions(Model model) {
        model.addAttribute("sessions", sessionService.findAll());
        return "sessions/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("sess", new CreateSessionDto());
        model.addAttribute("films", filmRepository.findAll());
        model.addAttribute("halls", hallRepository.findAll());
        return "sessions/create";
    }

    @PostMapping
    public String saveSession(@ModelAttribute("sess") CreateSessionDto dto) {
        sessionService.saveSession(dto, null);
        return "redirect:/sessions";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Session session = sessionService.findById(id);

        CreateSessionDto dto = new CreateSessionDto();
        dto.setFilmId(session.getFilm().getFilmId());
        dto.setHallId(session.getHall().getHallId());
        dto.setSessionDate(session.getSessionDate());
        dto.setStartTime(session.getStartTime());
        dto.setTicketPrice(session.getTicketPrice());

        model.addAttribute("sess", dto);
        model.addAttribute("sessionId", id);
        model.addAttribute("films", filmRepository.findAll());
        model.addAttribute("halls", hallRepository.findAll());
        model.addAttribute("movieName", session.getFilm().getTitle());

        return "sessions/edit";
    }

    @PostMapping("/{id}")
    public String updateSession(@PathVariable Long id, @ModelAttribute("sess") CreateSessionDto dto) {
        sessionService.saveSession(dto, id);
        return "redirect:/sessions";
    }

    @GetMapping("/{id}/delete")
    public String confirmDelete(@PathVariable Long id, Model model) {
        Session session = sessionService.findById(id);
        model.addAttribute("sess", session);
        return "sessions/delete";
    }

    @PostMapping("/{id}/delete")
    public String deleteSession(@PathVariable Long id) {
        sessionService.delete(id);
        return "redirect:/sessions";
    }
}
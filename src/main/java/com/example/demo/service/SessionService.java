package com.example.demo.service;

import com.example.demo.dto.CreateSessionDto;
import com.example.demo.entity.Film;
import com.example.demo.entity.Hall;
import com.example.demo.entity.Session;
import com.example.demo.repository.FilmRepository;
import com.example.demo.repository.HallRepository;
import com.example.demo.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;

    public SessionService(SessionRepository sessionRepository,
                          FilmRepository filmRepository,
                          HallRepository hallRepository) {
        this.sessionRepository = sessionRepository;
        this.filmRepository = filmRepository;
        this.hallRepository = hallRepository;
    }

    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    public Session findById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Сеанс не найден"));
    }

    @Transactional
    public void saveSession(CreateSessionDto dto, Long id) {
        Session session = (id != null)
                ? sessionRepository.findById(id).orElse(new Session())
                : new Session();

        Film film = filmRepository.findById(dto.getFilmId())
                .orElseThrow(() -> new RuntimeException("Фильм не найден"));

        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new RuntimeException("Зал не найден"));

        session.setFilm(film);
        session.setHall(hall);
        session.setSessionDate(dto.getSessionDate());
        session.setStartTime(dto.getStartTime());
        session.setTicketPrice(dto.getTicketPrice());

        sessionRepository.save(session);
    }

    @Transactional
    public void delete(Long id) {
        sessionRepository.deleteSalesBySessionId(id);
        sessionRepository.deleteTicketsBySessionId(id);
        sessionRepository.deleteById(id);
    }
}
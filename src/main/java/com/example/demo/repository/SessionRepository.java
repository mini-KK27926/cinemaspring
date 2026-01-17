package com.example.demo.repository;

import com.example.demo.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    // ✅ Сеансы на конкретную дату
    List<Session> findBySessionDate(LocalDate date);

    // ✅ Сеансы фильма
    List<Session> findByFilmFilmId(Long filmId);

    // ✅ Сеансы в зале
    List<Session> findByHallHallId(Long hallId);

    // ✅ Сеансы между датами
    List<Session> findBySessionDateBetween(LocalDate startDate, LocalDate endDate);

    // ✅ Сортировка по дате и времени
    List<Session> findAllByOrderBySessionDateAscStartTimeAsc();

    // ✅ Будущие сеансы
    List<Session> findBySessionDateGreaterThanEqual(LocalDate date);

    // ✅ Сеансы на конкретную дату и время
    List<Session> findBySessionDateAndStartTime(LocalDate date, LocalTime time);

    // ✅ Сеансы фильма на конкретную дату
    List<Session> findByFilmFilmIdAndSessionDate(Long filmId, LocalDate date);
}
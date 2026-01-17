package com.example.demo.repository;

import com.example.demo.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // ✅ Билеты на сеанс
    List<Ticket> findBySessionSessionId(Long sessionId);

    // ✅ Билеты по статусу
    List<Ticket> findByStatus(String status);

    // ✅ Свободные билеты на сеанс
    List<Ticket> findBySessionSessionIdAndStatus(Long sessionId, String status);

    // ✅ Конкретное место на сеансе
    List<Ticket> findBySessionSessionIdAndSeatNumber(Long sessionId, Integer seatNumber);

    // ✅ Сортировка по номеру места
    List<Ticket> findBySessionSessionIdOrderBySeatNumberAsc(Long sessionId);

    // ✅ Проверка занятости места
    boolean existsBySessionSessionIdAndSeatNumberAndStatus(Long sessionId, Integer seatNumber, String status);

    // ✅ Количество свободных мест на сеанс
    long countBySessionSessionIdAndStatus(Long sessionId, String status);
}
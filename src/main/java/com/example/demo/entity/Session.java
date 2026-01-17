package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    @NotNull(message = "Фильм обязателен")
    private Film film;

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    @NotNull(message = "Зал обязателен")
    private Hall hall;

    @NotNull(message = "Дата сеанса обязательна")
    @FutureOrPresent(message = "Дата сеанса не может быть в прошлом")
    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate;

    @NotNull(message = "Время начала обязательно")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull(message = "Цена билета обязательна")
    @DecimalMin(value = "0.01", message = "Цена билета должна быть больше 0")
    @Column(name = "ticket_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal ticketPrice;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    // Конструкторы
    public Session() {}

    public Session(Film film, Hall hall, LocalDate sessionDate, LocalTime startTime, BigDecimal ticketPrice) {
        this.film = film;
        this.hall = hall;
        this.sessionDate = sessionDate;
        this.startTime = startTime;
        this.ticketPrice = ticketPrice;
    }

    // Геттеры и сеттеры
    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public Film getFilm() { return film; }
    public void setFilm(Film film) { this.film = film; }

    public Hall getHall() { return hall; }
    public void setHall(Hall hall) { this.hall = hall; }

    public LocalDate getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public BigDecimal getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(BigDecimal ticketPrice) { this.ticketPrice = ticketPrice; }

    public List<Ticket> getTickets() { return tickets; }
    public void setTickets(List<Ticket> tickets) { this.tickets = tickets; }

    // Дополнительные методы
    public LocalTime getEndTime() {
        if (film != null && film.getDuration() != null && startTime != null) {
            return startTime.plusMinutes(film.getDuration());
        }
        return null;
    }

    @Override
    public String toString() {
        return film.getTitle() + " - " + sessionDate + " " + startTime + " (Зал " + hall.getHallNumber() + ")";
    }
}
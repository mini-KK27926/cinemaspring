package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class CreateSessionDto {
    @NotNull(message = "Нужно выбрать фильм")
    private Long filmId;

    @NotNull(message = "Нужно выбрать зал")
    private Long hallId;

    @NotNull(message = "Дата обязательна")
    private LocalDate sessionDate;

    @NotNull(message = "Время обязательно")
    private LocalTime startTime;

    @Positive(message = "Цена должна быть больше нуля")
    private BigDecimal ticketPrice;

    public CreateSessionDto() {
    }

    public Long getFilmId() { return filmId; }
    public void setFilmId(Long filmId) { this.filmId = filmId; }

    public Long getHallId() { return hallId; }
    public void setHallId(Long hallId) { this.hallId = hallId; }

    public LocalDate getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public BigDecimal getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(BigDecimal ticketPrice) { this.ticketPrice = ticketPrice; }
}
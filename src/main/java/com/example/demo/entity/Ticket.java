package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    @NotNull(message = "Сеанс обязателен")
    private Session session;

    @NotNull(message = "Номер места обязателен")
    @Min(value = 1, message = "Номер места должен быть положительным")
    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @NotBlank(message = "Статус обязателен")
    @Pattern(regexp = "свободен|продан", message = "Статус должен быть 'свободен' или 'продан'")
    @Column(name = "status", nullable = false)
    private String status = "свободен";

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Sale sale;

    // Конструкторы
    public Ticket() {}

    public Ticket(Session session, Integer seatNumber, String status) {
        this.session = session;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public Ticket(Session session, Integer seatNumber) {
        this.session = session;
        this.seatNumber = seatNumber;
        this.status = "свободен";
    }

    // Геттеры и сеттеры
    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }

    public Integer getSeatNumber() { return seatNumber; }
    public void setSeatNumber(Integer seatNumber) { this.seatNumber = seatNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Sale getSale() { return sale; }
    public void setSale(Sale sale) { this.sale = sale; }

    // Полезные методы
    public String getSeatDisplay() {
        return "Ряд " + ((seatNumber - 1) / 10 + 1) + ", Место " + seatNumber;
    }

    public BigDecimal getPrice() {
        return session != null ? session.getTicketPrice() : BigDecimal.ZERO;
    }

    public boolean isAvailable() {
        return "свободен".equals(status);
    }

    public void sell() {
        if (!isAvailable()) {
            throw new IllegalStateException("Билет уже продан");
        }
        this.status = "продан";
    }

    public void release() {
        this.status = "свободен";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(ticketId, ticket.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId);
    }

    @Override
    public String toString() {
        return "Билет #" + ticketId +
                " (Место: " + seatNumber +
                ", Статус: " + status +
                ", Сеанс: " + (session != null ? session.getSessionId() : "N/A") + ")";
    }
}
package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long saleId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false, unique = true)
    @NotNull(message = "Билет обязателен")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashier_id", nullable = false)
    @NotNull(message = "Кассир обязателен")
    private Cashier cashier;

    @Column(name = "sale_datetime", nullable = false)
    private LocalDateTime saleDatetime = LocalDateTime.now();

    @NotNull(message = "Сумма оплаты обязательна")
    @DecimalMin(value = "0.01", message = "Сумма оплаты должна быть больше 0")
    @Column(name = "payment_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "payment_method")
    private String paymentMethod = "наличные"; // наличные, карта, онлайн

    @Column(name = "notes", length = 500)
    @Size(max = 500, message = "Примечание не должно превышать 500 символов")
    private String notes;

    // Конструкторы
    public Sale() {}

    public Sale(Ticket ticket, Cashier cashier, BigDecimal paymentAmount) {
        this.ticket = ticket;
        this.cashier = cashier;
        this.paymentAmount = paymentAmount;
    }

    public Sale(Ticket ticket, Cashier cashier, BigDecimal paymentAmount, String paymentMethod) {
        this.ticket = ticket;
        this.cashier = cashier;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
    }

    // Геттеры и сеттеры
    public Long getSaleId() { return saleId; }
    public void setSaleId(Long saleId) { this.saleId = saleId; }

    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    public Cashier getCashier() { return cashier; }
    public void setCashier(Cashier cashier) { this.cashier = cashier; }

    public LocalDateTime getSaleDatetime() { return saleDatetime; }
    public void setSaleDatetime(LocalDateTime saleDatetime) { this.saleDatetime = saleDatetime; }

    public BigDecimal getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(BigDecimal paymentAmount) { this.paymentAmount = paymentAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    // Полезные методы
    public String getFormattedDatetime() {
        return saleDatetime != null ?
                saleDatetime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) :
                "";
    }

    public BigDecimal getChangeAmount() {
        if (ticket != null && paymentAmount != null) {
            BigDecimal ticketPrice = ticket.getPrice();
            return paymentAmount.subtract(ticketPrice).max(BigDecimal.ZERO);
        }
        return BigDecimal.ZERO;
    }

    public boolean isFullPayment() {
        if (ticket != null && paymentAmount != null) {
            return paymentAmount.compareTo(ticket.getPrice()) >= 0;
        }
        return false;
    }

    public String getSaleSummary() {
        if (ticket != null && ticket.getSession() != null && ticket.getSession().getFilm() != null) {
            Film film = ticket.getSession().getFilm();
            return film.getTitle() + " - Место " + ticket.getSeatNumber();
        }
        return "Продажа #" + saleId;
    }

    @PrePersist
    @PreUpdate
    private void validateSale() {
        if (ticket != null && !ticket.isAvailable()) {
            throw new IllegalStateException("Нельзя продать уже проданный билет");
        }
        if (ticket != null) {
            ticket.sell();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(saleId, sale.saleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(saleId);
    }

    @Override
    public String toString() {
        return "Продажа #" + saleId +
                " (" + getFormattedDatetime() +
                ", Сумма: " + paymentAmount +
                ", Кассир: " + (cashier != null ? cashier.getShortName() : "N/A") + ")";
    }
}
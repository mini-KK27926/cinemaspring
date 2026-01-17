package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cashiers")
public class Cashier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cashier_id")
    private Long cashierId;

    @NotBlank(message = "ФИО обязательно")
    @Size(min = 2, max = 150, message = "ФИО должно быть от 2 до 150 символов")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotBlank(message = "Логин обязателен")
    @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "cashier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sale> sales = new ArrayList<>();

    // Конструкторы
    public Cashier() {}

    public Cashier(String fullName, String login) {
        this.fullName = fullName;
        this.login = login;
    }

    // Геттеры и сеттеры
    public Long getCashierId() { return cashierId; }
    public void setCashierId(Long cashierId) { this.cashierId = cashierId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public List<Sale> getSales() { return sales; }
    public void setSales(List<Sale> sales) { this.sales = sales; }

    // Полезные методы
    public String getShortName() {
        if (fullName == null) return "";
        String[] parts = fullName.split(" ");
        if (parts.length >= 2) {
            return parts[0] + " " + parts[1].charAt(0) + ".";
        }
        return fullName;
    }

    public Long getTotalSalesCount() {
        return sales != null ? (long) sales.size() : 0L;
    }

    public BigDecimal getTotalRevenue() {
        if (sales == null || sales.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return sales.stream()
                .map(Sale::getPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cashier cashier = (Cashier) o;
        return Objects.equals(cashierId, cashier.cashierId) &&
                Objects.equals(login, cashier.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cashierId, login);
    }

    @Override
    public String toString() {
        return fullName + " (" + login + ")" +
                (isActive ? "" : " [НЕ АКТИВЕН]");
    }
}
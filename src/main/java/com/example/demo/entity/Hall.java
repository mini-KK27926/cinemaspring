package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "halls")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hall_id")
    private Long hallId;

    @NotNull(message = "Номер зала обязателен")
    @Column(name = "hall_number", unique = true, nullable = false)
    private Integer hallNumber;

    @NotNull(message = "Вместимость обязательна")
    @Min(value = 1, message = "Вместимость должна быть положительной")
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL)
    private List<Session> sessions;

    // Конструкторы
    public Hall() {}

    public Hall(Integer hallNumber, Integer capacity) {
        this.hallNumber = hallNumber;
        this.capacity = capacity;
    }

    // Геттеры и сеттеры
    public Long getHallId() { return hallId; }
    public void setHallId(Long hallId) { this.hallId = hallId; }

    public Integer getHallNumber() { return hallNumber; }
    public void setHallNumber(Integer hallNumber) { this.hallNumber = hallNumber; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public List<Session> getSessions() { return sessions; }
    public void setSessions(List<Session> sessions) { this.sessions = sessions; }

    @Override
    public String toString() {
        return "Зал №" + hallNumber + " (" + capacity + " мест)";
    }
}
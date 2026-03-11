package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateSaleDto {
    @NotNull(message = "Сеанс не выбран")
    private Long sessionId;

    @Min(value = 1, message = "Минимум 1 билет")
    private Integer ticketsCount;

    public CreateSaleDto() {
    }

    public CreateSaleDto(Long sessionId, Integer ticketsCount) {
        this.sessionId = sessionId;
        this.ticketsCount = ticketsCount;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getTicketsCount() {
        return ticketsCount;
    }

    public void setTicketsCount(Integer ticketsCount) {
        this.ticketsCount = ticketsCount;
    }
}
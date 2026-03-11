package com.example.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long saleId;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @Column(name = "tickets_count")
    private Integer ticketsCount;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "sale_datetime")
    private LocalDateTime saleDatetime;

    public Sale() {}

    public Long getSaleId() { return saleId; }
    public void setSaleId(Long saleId) { this.saleId = saleId; }

    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
    public Integer getTicketsCount() { return ticketsCount; }
    public void setTicketsCount(Integer ticketsCount) { this.ticketsCount = ticketsCount; }

    public BigDecimal getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(BigDecimal paymentAmount) { this.paymentAmount = paymentAmount; }

    public LocalDateTime getSaleDatetime() { return saleDatetime; }
    public void setSaleDatetime(LocalDateTime saleDatetime) { this.saleDatetime = saleDatetime; }
}
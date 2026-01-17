package com.example.demo.repository;

import com.example.demo.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    // ✅ Продажи кассира
    List<Sale> findByCashierCashierId(Long cashierId);

    // ✅ Продажи за период
    List<Sale> findBySaleDatetimeBetween(LocalDateTime start, LocalDateTime end);

    // ✅ Сортировка по дате продажи (новые сначала)
    List<Sale> findAllByOrderBySaleDatetimeDesc();

    // ✅ Продажи по методу оплаты
    List<Sale> findByPaymentMethod(String paymentMethod);

    // ✅ Продажи за сегодня
    List<Sale> findBySaleDatetimeAfter(LocalDateTime startOfDay);

    // ✅ Продажи кассира за период
    List<Sale> findByCashierCashierIdAndSaleDatetimeBetween(Long cashierId, LocalDateTime start, LocalDateTime end);

    // ✅ Продажи по билету (должна быть одна)
    List<Sale> findByTicketTicketId(Long ticketId);
}
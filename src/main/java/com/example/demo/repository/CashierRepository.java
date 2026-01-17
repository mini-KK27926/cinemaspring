package com.example.demo.repository;

import com.example.demo.entity.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CashierRepository extends JpaRepository<Cashier, Long> {

    // ✅ Поиск по логину
    Optional<Cashier> findByLogin(String login);

    // ✅ Проверка существования логина
    boolean existsByLogin(String login);

    // ✅ Сортировка по ФИО
    List<Cashier> findAllByOrderByFullNameAsc();

    // ✅ Активные кассиры
    List<Cashier> findByIsActiveTrue();

    // ✅ Поиск по ФИО (частичное совпадение)
    List<Cashier> findByFullNameContainingIgnoreCase(String name);

    // ✅ Поиск по логину (частичное совпадение)
    List<Cashier> findByLoginContainingIgnoreCase(String login);
}
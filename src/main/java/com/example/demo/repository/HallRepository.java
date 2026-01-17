package com.example.demo.repository;

import com.example.demo.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {

    // ✅ Поиск по номеру зала
    Optional<Hall> findByHallNumber(Integer hallNumber);

    // ✅ Проверка существования номера зала
    boolean existsByHallNumber(Integer hallNumber);

    // ✅ Залы с минимальной вместимостью
    List<Hall> findByCapacityGreaterThanEqual(Integer minCapacity);

    // ✅ Сортировка по номеру зала
    List<Hall> findAllByOrderByHallNumberAsc();
}
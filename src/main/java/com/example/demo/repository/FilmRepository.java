package com.example.demo.repository;

import com.example.demo.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    // ✅ Метод поиска по жанру
    List<Film> findByGenre(String genre);

    // ✅ Поиск по названию (без учета регистра)
    List<Film> findByTitleContainingIgnoreCase(String title);

    // ✅ Поиск по возрастному рейтингу
    List<Film> findByAgeRating(String ageRating);

    // ✅ Сортировка по названию
    List<Film> findAllByOrderByTitleAsc();

    // ✅ Проверка существования по названию
    boolean existsByTitle(String title);

    // ✅ Поиск по названию (точное совпадение)
    Optional<Film> findByTitle(String title);
}
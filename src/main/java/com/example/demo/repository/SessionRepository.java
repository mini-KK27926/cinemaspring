package com.example.demo.repository;

import com.example.demo.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sales WHERE session_id = :sessionId", nativeQuery = true)
    void deleteSalesBySessionId(@Param("sessionId") Long sessionId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM tickets WHERE session_id = :sessionId", nativeQuery = true)
    void deleteTicketsBySessionId(@Param("sessionId") Long sessionId);
}
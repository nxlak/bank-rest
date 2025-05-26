package com.java.bank_rest.repository;

import com.java.bank_rest.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepo extends JpaRepository<Card, Long> {
    Page<Card> findByCardUserId(Long userId, Pageable pageable);
}

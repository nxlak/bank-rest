package com.java.bank_rest.service;

import com.java.bank_rest.dto.card.CardRequest;
import com.java.bank_rest.dto.card.CardResponse;
import com.java.bank_rest.dto.transaction.TransactionRequest;
import com.java.bank_rest.dto.transaction.TransactionResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface CardService {
    CardResponse createCard(CardRequest cardRequest);
    CardResponse updateStatus(Long id, String status);
    CardResponse blockCard(Long id);
    void deleteCard(Long id);
    Page<CardResponse> getAllCards(Pageable pageable);
    Page<CardResponse> getCards(Long userId, Pageable pageable);
    BigDecimal showBalance(Long userId, Long cardId);
    TransactionResponse makeTransaction(Long userId, TransactionRequest transactionRequest);
}

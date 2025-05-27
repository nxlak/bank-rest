package com.java.bank_rest.controller;

import com.java.bank_rest.dto.card.CardRequest;
import com.java.bank_rest.dto.card.CardResponse;
import com.java.bank_rest.dto.transaction.TransactionRequest;
import com.java.bank_rest.dto.transaction.TransactionResponse;
import com.java.bank_rest.service.CardService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public CardResponse createCard(@RequestBody CardRequest cardRequest) {
        return cardService.createCard(cardRequest);
    }

    @PatchMapping("/update/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public CardResponse updateStatus(@PathVariable Long id, @RequestParam String status) {
        return cardService.updateStatus(id, status);
    }

    @PatchMapping("/block/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CardResponse blockCard(@PathVariable Long id) {
        return cardService.blockCard(id);
    }

    @DeleteMapping("/delete/{userId}/{cardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCard(@PathVariable Long userId, @PathVariable Long cardId) {
        try {
            cardService.deleteCard(userId, cardId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error"));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<CardResponse> getAllCards(Pageable pageable) {
        return cardService.getAllCards(pageable);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public Page<CardResponse> getCards(@PathVariable Long userId, Pageable pageable) {
        return cardService.getCards(userId, pageable);
    }

    @GetMapping("/balance/{userId}/{cardId}")
    @PreAuthorize("hasRole('USER')")
    public BigDecimal showBalance(@PathVariable Long userId, @PathVariable Long cardId) {
        return cardService.showBalance(userId, cardId);
    }

    @PostMapping("/transfer/{userId}")
    @PreAuthorize("hasRole('USER')")
    public TransactionResponse makeTransaction(@PathVariable Long userId, @RequestBody TransactionRequest transactionRequest) {
        return cardService.makeTransaction(userId, transactionRequest);
    }

    @PatchMapping("/request/{userId}/{cardId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> requestBlock(@PathVariable Long userId, @PathVariable Long cardId) {
        try {
            cardService.requestBlock(userId, cardId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error"));
        }
    }
}

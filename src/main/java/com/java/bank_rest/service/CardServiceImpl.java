package com.java.bank_rest.service;

import com.java.bank_rest.dto.card.CardRequest;
import com.java.bank_rest.dto.card.CardResponse;
import com.java.bank_rest.dto.transaction.TransactionRequest;
import com.java.bank_rest.dto.transaction.TransactionResponse;
import com.java.bank_rest.entity.Card;
import com.java.bank_rest.entity.Transaction;
import com.java.bank_rest.entity.User;
import com.java.bank_rest.repository.CardRepo;
import com.java.bank_rest.repository.TransactionRepo;
import com.java.bank_rest.repository.UserRepo;
import com.java.bank_rest.util.CardStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final UserRepo userRepo;
    private final CardRepo cardRepo;
    private final TransactionRepo transactionRepo;

    @Override
    public CardResponse createCard(CardRequest cardRequest) {
        User user = userRepo.findById(cardRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Card card = Card.builder()
                .cardUser(user)
                .cardNumber(cardRequest.getCardNumber())
                .expiry(cardRequest.getExpiry())
                .status(CardStatus.ACTIVE)
                .balance(cardRequest.getBalance())
                .build();
        cardRepo.save(card);

        return convertIntoDto(card);
    }

    @Override
    public CardResponse updateStatus(Long id, String status) {
        Card card = cardRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));
        card.setStatus(CardStatus.valueOf(status.toUpperCase()));
        cardRepo.save(card);

        return convertIntoDto(card);

    }

    @Override
    public CardResponse blockCard(Long id) {
        Card card = cardRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));
        card.setStatus(CardStatus.BLOCKED);
        cardRepo.save(card);

        return convertIntoDto(card);
    }

    @Override
    public void deleteCard(Long userId, Long cardId) {
        Card card = loadCardForUser(userId, cardId);

        cardRepo.delete(card);
    }

    @Override
    public Page<CardResponse> getAllCards(Pageable pageable) {
        return cardRepo.findAll(pageable)
                .map(this::convertIntoDto);
    }

    @Override
    public Page<CardResponse> getCards(Long userId, Pageable pageable) {
        userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return cardRepo.findByCardUserId(userId, pageable)
                .map(this::convertIntoDto);
    }

    @Override
    public BigDecimal showBalance(Long userId, Long cardId) {
        Card card = loadCardForUser(userId, cardId);
        return card.getBalance();
    }

    @Override
    public TransactionResponse makeTransaction(Long userId, TransactionRequest transactionRequest) {
        Card from = loadCardForUser(userId, transactionRequest.getFromId());
        Card to = cardRepo.findById(transactionRequest.getToId()).orElseThrow(() -> new EntityNotFoundException("Card not found"));

        BigDecimal amount = transactionRequest.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new TransactionResponse("FAILED", "Некорректная сумма перевода");
        }

        if (from.getBalance().compareTo(amount) < 0) {
            return new TransactionResponse("FAILED", "Недостаточно средств на карте-отправителе");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        cardRepo.save(from);
        cardRepo.save(to);

        Transaction transaction = Transaction.builder()
                .fromCard(from)
                .toCard(to)
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build();
        transactionRepo.save(transaction);

        return new TransactionResponse("SUCCESS", "Перевод выполнен успешно");
    }

    @Override
    public void requestBlock(Long userId, Long cardId) {
        Card card = loadCardForUser(userId, cardId);
        card.setStatus(CardStatus.PENDING_BLOCK);

        cardRepo.save(card);
    }

    private Card loadCardForUser(Long userId, Long cardId) {
        return cardRepo.findByIdAndCardUserId(cardId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Карта не найдена или доступ запрещён"));
    }


    private CardResponse convertIntoDto(Card card) {
        return CardResponse.builder()
                .cardId(card.getId())
                .cardNumber(maskCardNumber(card.getCardNumber()))
                .expiry(card.getExpiry())
                .status(card.getStatus().toString())
                .balance(card.getBalance()).build();
    }

    private static String maskCardNumber(String fullNumber) {
        String last4 = fullNumber.substring(fullNumber.length() - 4);
        return "**** **** **** " + last4;
    }

}
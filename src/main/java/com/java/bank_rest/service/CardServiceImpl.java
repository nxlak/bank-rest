package com.java.bank_rest.service;

import com.java.bank_rest.dto.card.CardRequest;
import com.java.bank_rest.dto.card.CardResponse;
import com.java.bank_rest.entity.Card;
import com.java.bank_rest.entity.User;
import com.java.bank_rest.repository.CardRepo;
import com.java.bank_rest.repository.UserRepo;
import com.java.bank_rest.util.CardStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final UserRepo userRepo;
    private final CardRepo cardRepo;

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
    public void deleteCard(Long id) {
        Card card = cardRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));

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

    public CardResponse convertIntoDto(Card card) {
        return CardResponse.builder()
                .cardId(card.getId())
                .cardNumber(maskCardNumber(card.getCardNumber()))
                .expiry(card.getExpiry())
                .status(card.getStatus().toString())
                .balance(card.getBalance()).build();
    }

    public static String maskCardNumber(String fullNumber) {
        String last4 = fullNumber.substring(fullNumber.length() - 4);
        return "**** **** **** " + last4;
    }

}
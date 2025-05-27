package com.java.bank_rest.service;

import com.java.bank_rest.dto.transaction.TransactionRequest;
import com.java.bank_rest.dto.transaction.TransactionResponse;
import com.java.bank_rest.entity.Card;
import com.java.bank_rest.entity.Transaction;
import com.java.bank_rest.repository.CardRepo;
import com.java.bank_rest.repository.TransactionRepo;
import com.java.bank_rest.util.CardStatus;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepo cardRepo;

    @Mock
    private TransactionRepo transactionRepo;

    @InjectMocks
    private CardServiceImpl cardService;

    private Card cardFrom;
    private Card cardTo;

    @BeforeEach
    void setUp() {
        cardFrom = new Card();
        cardFrom.setId(1L);
        cardFrom.setCardNumber("1111222233334444");
        cardFrom.setStatus(CardStatus.ACTIVE);
        cardFrom.setBalance(new BigDecimal("100.00"));

        cardTo = new Card();
        cardTo.setId(2L);
        cardTo.setCardNumber("5555666677778888");
        cardTo.setStatus(CardStatus.ACTIVE);
        cardTo.setBalance(new BigDecimal("50.00"));
    }

    @Test
    void makeTransaction_Success() {
        TransactionRequest req = new TransactionRequest();
        req.setFromId(1L);
        req.setToId(2L);
        req.setAmount(new BigDecimal("30.00"));

        when(cardRepo.findByIdAndCardUserId(1L, 1L)).thenReturn(Optional.of(cardFrom));
        when(cardRepo.findById(2L)).thenReturn(Optional.of(cardTo));

        TransactionResponse resp = cardService.makeTransaction(1L, req);

        assertThat(resp.getStatus()).isEqualTo("SUCCESS");
        assertThat(cardFrom.getBalance()).isEqualByComparingTo("70.00");
        assertThat(cardTo.getBalance()).isEqualByComparingTo("80.00");
        verify(transactionRepo).save(any(Transaction.class));
    }

    @Test
    void makeTransaction_InvalidAmount() {
        TransactionRequest req = new TransactionRequest();
        req.setFromId(1L);
        req.setToId(2L);
        req.setAmount(BigDecimal.ZERO);

        when(cardRepo.findByIdAndCardUserId(anyLong(), anyLong())).thenReturn(Optional.of(cardFrom));
        when(cardRepo.findById(anyLong())).thenReturn(Optional.of(cardTo));

        TransactionResponse resp = cardService.makeTransaction(1L, req);

        assertThat(resp.getStatus()).isEqualTo("FAILED");
        assertThat(resp.getMessage()).contains("Некорректная сумма перевода");
        verify(transactionRepo, never()).save(any(Transaction.class));
    }

    @Test
    void makeTransaction_InsufficientFunds() {
        TransactionRequest req = new TransactionRequest();
        req.setFromId(1L);
        req.setToId(2L);
        req.setAmount(new BigDecimal("200.00"));

        when(cardRepo.findByIdAndCardUserId(1L, 1L)).thenReturn(Optional.of(cardFrom));
        when(cardRepo.findById(2L)).thenReturn(Optional.of(cardTo));

        TransactionResponse resp = cardService.makeTransaction(1L, req);

        assertThat(resp.getStatus()).isEqualTo("FAILED");
        assertThat(resp.getMessage()).contains("Недостаточно средств на карте-отправителе");
        verify(transactionRepo, never()).save(any(Transaction.class));
    }

    @Test
    void makeTransaction_CardNotFound_ShouldThrow() {
        TransactionRequest req = new TransactionRequest();
        req.setFromId(1L);
        req.setToId(2L);
        req.setAmount(new BigDecimal("10.00"));

        when(cardRepo.findByIdAndCardUserId(1L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardService.makeTransaction(1L, req))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Карта не найдена или доступ запрещён");
    }

    @Test
    void maskCardNumber_ShouldHideAllButLast4() throws Exception {
        var method = CardServiceImpl.class
                .getDeclaredMethod("maskCardNumber", String.class);
        method.setAccessible(true);
        String masked = (String) method.invoke(null, "1234567812345678");
        assertThat(masked).isEqualTo("**** **** **** 5678");
    }

    @Test
    void getAllCards_ShouldReturnPagedDto() {
        when(cardRepo.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(cardFrom)));

        var page = cardService.getAllCards(PageRequest.of(0, 1));

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getCardId()).isEqualTo(1L);
    }
}

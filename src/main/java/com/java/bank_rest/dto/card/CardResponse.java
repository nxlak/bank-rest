package com.java.bank_rest.dto.card;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.java.bank_rest.util.CardStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.YearMonth;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {

    private Long cardId;

    private String cardNumber;

    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth expiry;

    private String status;

    private BigDecimal balance;
}

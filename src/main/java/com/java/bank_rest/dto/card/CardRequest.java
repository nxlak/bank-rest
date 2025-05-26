package com.java.bank_rest.dto.card;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {

    private Long userId;

    private String cardNumber;

    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth expiry;

    private BigDecimal balance;

}
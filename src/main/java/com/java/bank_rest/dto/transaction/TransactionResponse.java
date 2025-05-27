package com.java.bank_rest.dto.transaction;

import com.java.bank_rest.dto.card.CardResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String status;
    private String message;
}

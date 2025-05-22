package com.java.bank_rest.service;

import com.java.bank_rest.repository.CardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    private final CardRepo cardRepo;

    @Autowired
    public CardService(CardRepo cardRepo) {
        this.cardRepo = cardRepo;
    }

}

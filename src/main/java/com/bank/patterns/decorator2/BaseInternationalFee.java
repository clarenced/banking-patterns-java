package com.bank.patterns.decorator2;

import com.bank.legacy.old.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;

class BaseInternationalFee implements FeeCalculator {
    @Override
    public BigDecimal fee(Transaction tx) {
        System.out.println("[INTERNATIONAL] Applying international fee");
        BigDecimal base = new BigDecimal("2.00") // frais fixe
                .add(new BigDecimal(tx.getAmount()).multiply(new BigDecimal("0.001"))); // +0.1% du montant
        return base.setScale(2, RoundingMode.HALF_UP);
    }
}


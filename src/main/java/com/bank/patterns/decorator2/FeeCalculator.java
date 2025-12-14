package com.bank.patterns.decorator2;

import com.bank.legacy.Transaction;

import java.math.BigDecimal;

public interface FeeCalculator {
    enum Currency { USD, EUR, GBP }

    BigDecimal fee(Transaction tx);
}

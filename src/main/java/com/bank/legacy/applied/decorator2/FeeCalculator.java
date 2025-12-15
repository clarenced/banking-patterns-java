package com.bank.legacy.applied.decorator2;

import com.bank.legacy.old.Transaction;

import java.math.BigDecimal;

public interface FeeCalculator {
    enum Currency { USD, EUR, GBP }

    BigDecimal fee(Transaction tx);
}

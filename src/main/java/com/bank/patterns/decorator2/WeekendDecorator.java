package com.bank.patterns.decorator2;

import com.bank.legacy.Transaction;

import java.math.BigDecimal;

class WeekendDecorator extends FeeDecorator {
    public WeekendDecorator(FeeCalculator delegate) { super(delegate); }

    @Override
    public BigDecimal fee(Transaction tx) {
        System.out.println("[DECORATOR] Applying weekend fee");
        BigDecimal f = delegate.fee(tx);
        if (tx.weekend()) {
            f = f.multiply(new BigDecimal("1.10")); // +10%
        }
        return scale(f);
    }
}


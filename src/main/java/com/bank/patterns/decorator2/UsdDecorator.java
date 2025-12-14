package com.bank.patterns.decorator2;

import com.bank.legacy.Transaction;

import java.math.BigDecimal;

public class UsdDecorator extends FeeDecorator{

    public UsdDecorator(FeeCalculator delegate) {
        super(delegate);
    }

    @Override
    public BigDecimal fee(Transaction tx) {
        System.out.println("[DECORATOR] Applying USD fee");
        BigDecimal f = delegate.fee(tx);
        if (tx.getCurrency() == Currency.USD) {
            f = f.add(new BigDecimal("0.50"));
        }
        return scale(f);
    }
}

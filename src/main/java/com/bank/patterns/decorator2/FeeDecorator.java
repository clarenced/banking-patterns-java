package com.bank.patterns.decorator2;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class FeeDecorator implements FeeCalculator {
    protected final FeeCalculator delegate;

    public FeeDecorator(FeeCalculator delegate) {
        this.delegate = delegate;
    }

    protected static BigDecimal scale(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP);
    }
}

package com.bank.patterns.decorator2;

import com.bank.legacy.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransferFeeService {

    public BigDecimal calculateFee(Transaction tx) {
        // 1️⃣ Base fee
        BigDecimal fee = new BigDecimal("2.00")
                .add(new BigDecimal(tx.getAmount()).multiply(new BigDecimal("0.001"))); // 0.1%

        // 2️⃣ Surcharge USD
        if (tx.getCurrency() == FeeCalculator.Currency.USD) {
            fee = fee.add(new BigDecimal("0.50"));
        }

        // 3️⃣ Surcharge week-end
        if (tx.weekend()) {
            fee = fee.multiply(new BigDecimal("1.10"));
        }

        return fee.setScale(2, RoundingMode.HALF_UP);
    }
}

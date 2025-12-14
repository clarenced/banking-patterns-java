package com.bank.patterns.decorator2;

import com.bank.legacy.old.Transaction;

public class Demo {

    public static void main(String[] args) {
        FeeCalculator calculator =
                new WeekendDecorator(
                        new UsdDecorator(
                                new BaseInternationalFee()
                        )
                );

        Transaction tx = new Transaction("123", "VIREMENT_INTERNATIONAL",
                1000.0, "123ABC","456DEF", FeeCalculator.Currency.EUR);

        System.out.println("USD weekend transfer: " + calculator.fee(tx) + " €");
    }
}

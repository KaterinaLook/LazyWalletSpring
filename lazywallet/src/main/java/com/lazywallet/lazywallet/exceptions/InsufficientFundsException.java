package com.lazywallet.lazywallet.exceptions;

import java.math.BigDecimal;

/**
 * Исключение при недостатке средств на счете
 */
public class InsufficientFundsException extends RuntimeException {
    private final BigDecimal currentBalance;
    private final BigDecimal requestedAmount;
    private final String currency;

    public InsufficientFundsException(BigDecimal currentBalance,
                                      BigDecimal requestedAmount,
                                      String currency) {
        super(String.format(
                "Not enough funds. Current balance: %.2f %s, Requested: %.2f %s",
                currentBalance, currency,
                requestedAmount, currency
        ));
        this.currentBalance = currentBalance;
        this.requestedAmount = requestedAmount;
        this.currency = currency;
    }

    // Геттеры
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public String getCurrency() {
        return currency;
    }
}
package com.example.bank.model;

/**
 * An enumeration representing the types of bank transactions.
 */
public enum TransactionType {
    /**
     * Represents a deposit transaction where money is added to an account.
     */
    DEPOSIT,

    /**
     * Represents a withdrawal transaction where money is taken out from an account.
     */
    WITHDRAWAL,

    /**
     * Represents a transfer transaction where money is moved from one account to another.
     */
    TRANSFER
}

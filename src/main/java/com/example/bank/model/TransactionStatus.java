package com.example.bank.model;

/**
 * An enumeration representing the possible statuses of a bank transaction.
 */
public enum TransactionStatus {
    /**
     * Indicates that the transaction is pending and has not yet been processed.
     */
    PENDING,

    /**
     * Indicates that the transaction has been successfully completed.
     */
    COMPLETED,

    /**
     * Indicates that the transaction has failed due to an error or issue.
     */
    FAILED
}

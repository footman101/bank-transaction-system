package com.example.bank.service;

import com.example.bank.exception.InvalidTransactionException;
import com.example.bank.exception.TransactionNotFoundException;
import com.example.bank.model.Transaction;
import com.example.bank.model.TransactionType;
import com.example.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;

    @CacheEvict(allEntries = true, value = "transactions")
    public Transaction createTransaction(Transaction transaction) {
        validateTransaction(transaction);
        transaction.setTimestamp(LocalDateTime.now());
        return repository.save(transaction);
    }

    @Cacheable("transactions")
    public Page<Transaction> getAllTransactions(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @CacheEvict(allEntries = true, value = "transactions")
    public void deleteTransaction(Long id) {
        if (!repository.existsById(id)) {
            throw new TransactionNotFoundException("Transaction not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @CacheEvict(allEntries = true, value = "transactions")
    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        validateTransaction(updatedTransaction);
        return repository.findById(id)
                .map(transaction -> {
                    transaction.setType(updatedTransaction.getType());
                    transaction.setAmount(updatedTransaction.getAmount());
                    transaction.setSourceAccount(updatedTransaction.getSourceAccount());
                    transaction.setTargetAccount(updatedTransaction.getTargetAccount());
                    transaction.setStatus(updatedTransaction.getStatus());
                    return repository.save(transaction);
                })
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));
    }

    private void validateTransaction(Transaction transaction) {
        if (transaction.getType() == TransactionType.TRANSFER &&
                (transaction.getSourceAccount() == null || transaction.getTargetAccount() == null)) {
            throw new InvalidTransactionException("Transfer requires both source and target accounts");
        }
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Amount must be greater than 0");
        }
    }
}
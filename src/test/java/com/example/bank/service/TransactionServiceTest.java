package com.example.bank.service;

import com.example.bank.exception.InvalidTransactionException;
import com.example.bank.exception.TransactionNotFoundException;
import com.example.bank.model.Transaction;
import com.example.bank.model.TransactionStatus;
import com.example.bank.model.TransactionType;
import com.example.bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionService service;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transaction = new Transaction();
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(BigDecimal.valueOf(100.0));
        transaction.setSourceAccount("123456789");
        transaction.setTargetAccount("987654321");
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.PENDING);
    }

    // 测试创建交易
    @Test
    void testCreateTransaction_Success() {
        when(repository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = service.createTransaction(transaction);

        assertNotNull(result);
        assertEquals(TransactionType.DEPOSIT, result.getType());
        verify(repository, times(1)).save(transaction);
    }

    @Test
    void testCreateTransaction_InvalidTransfer() {
        transaction.setType(TransactionType.TRANSFER);
        transaction.setTargetAccount(null);

        assertThrows(InvalidTransactionException.class, () -> {
            service.createTransaction(transaction);
        });
    }

    @Test
    void testCreateTransaction_InvalidAmount() {
        transaction.setType(TransactionType.TRANSFER);
        transaction.setAmount(null);

        assertThrows(InvalidTransactionException.class, () -> {
            service.createTransaction(transaction);
        });
    }

    // 测试删除交易
    @Test
    void testDeleteTransaction_Success() {
        when(repository.existsById(1L)).thenReturn(true);
        service.deleteTransaction(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTransaction_NotFound() {
        when(repository.existsById(999L)).thenReturn(false);
        assertThrows(TransactionNotFoundException.class, () -> {
            service.deleteTransaction(999L);
        });
    }

    // 测试修改交易
    @Test
    void testUpdateTransaction_Success() {
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setType(TransactionType.WITHDRAWAL);
        updatedTransaction.setAmount(BigDecimal.valueOf(50.0));

        when(repository.findById(1L)).thenReturn(Optional.of(transaction));
        when(repository.save(any(Transaction.class))).thenReturn(updatedTransaction);

        Transaction result = service.updateTransaction(1L, updatedTransaction);

        assertEquals(TransactionType.WITHDRAWAL, result.getType());
        assertEquals(BigDecimal.valueOf(50.0), result.getAmount());
    }

    @Test
    void testUpdateTransaction_NotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> {
            service.updateTransaction(999L, transaction);
        });
    }

    // 测试查询交易
    @Test
    void testGetAllTransactions() {
        Page<Transaction> page = new PageImpl<>(Collections.singletonList(transaction));
        when(repository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Transaction> result = service.getAllTransactions(0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals(TransactionType.DEPOSIT, result.getContent().get(0).getType());
    }
}
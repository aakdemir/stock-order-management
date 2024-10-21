package com.brokage.stock_order;

import com.brokage.stock_order.constant.TransactionType;
import com.brokage.stock_order.entity.Asset;
import com.brokage.stock_order.entity.Transaction;
import com.brokage.stock_order.repository.AssetRepository;
import com.brokage.stock_order.repository.TransactionRepository;
import com.brokage.stock_order.service.AssetServiceImpl;
import com.brokage.stock_order.service.TransactionService;
import com.brokage.stock_order.service.TransactionServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    @InjectMocks
    TransactionServiceImpl transactionServiceImpl;

    @Mock
    TransactionRepository transactionRepository;

    Transaction transaction;

    List<Transaction> transactionList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        transaction = Transaction.builder().createDate(new Date()).transactionType(TransactionType.DEPOSIT)
                .id(1L).customerId(1L).amount(BigDecimal.TEN).build();

        transactionList = new ArrayList<>();

        transactionList.add(transaction);

        transaction.setTransactionType(TransactionType.WITHDRAW);

        transactionList.add(transaction);
    }

    @Test
    public void testListAllTransactions_EmptyList(){
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> transactionServiceImpl.listAllTransactions());

        assertEquals("There isn't any transaction for now!", exception.getMessage());
    }


    @Test
    public void testListAllTransactions_FullList(){
        when(transactionRepository.findAll()).thenReturn(transactionList);
        List<Transaction> transactionServiceList = transactionServiceImpl.listAllTransactions();
        assertFalse(transactionServiceList.isEmpty());
        assertNotNull(transactionServiceList);
        assertEquals(2, transactionServiceList.size());
    }
}

package com.brokage.stock_order.service;

import com.brokage.stock_order.entity.Asset;
import com.brokage.stock_order.entity.Transaction;
import com.brokage.stock_order.repository.AssetRepository;
import com.brokage.stock_order.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    TransactionRepository transactionRepository;

    /**
     * @return Transaction
     */
    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    /**
     * @return List<Transaction>
     */
    @Override
    public List<Transaction> listAllTransactions() {
        List<Transaction> transactionList = transactionRepository.findAll();
        if(transactionList.isEmpty())
            throw new EntityNotFoundException("There isn't any transaction for now!");
        return transactionList;
        }
}

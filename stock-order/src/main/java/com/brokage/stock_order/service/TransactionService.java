package com.brokage.stock_order.service;

import com.brokage.stock_order.entity.Order;
import com.brokage.stock_order.entity.Transaction;
import com.brokage.stock_order.model.OrderRequest;
import com.brokage.stock_order.model.TransactionRequest;

import java.util.Date;
import java.util.List;

public interface TransactionService {
    Transaction save(Transaction transactionRequest);
    List<Transaction> listAllTransactions();
}

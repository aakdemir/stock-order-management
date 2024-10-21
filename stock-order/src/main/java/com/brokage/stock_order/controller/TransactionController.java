package com.brokage.stock_order.controller;

import com.brokage.stock_order.entity.Transaction;
import com.brokage.stock_order.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("/listAllTransactions")
    public ResponseEntity<List<Transaction>> listAllTransactions() {
        return new ResponseEntity<>(transactionService.listAllTransactions(), HttpStatus.OK);
    }
}

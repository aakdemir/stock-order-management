package com.brokage.stock_order.entity;

import com.brokage.stock_order.constant.Side;
import com.brokage.stock_order.constant.Status;
import com.brokage.stock_order.constant.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@ToString
@RequiredArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Builder
@Table(name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "createDate")
    private Date createDate;

}

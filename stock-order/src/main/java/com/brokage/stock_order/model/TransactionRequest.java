package com.brokage.stock_order.model;

import com.brokage.stock_order.constant.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionRequest {
    private Long customerId;

    private String assetName;

    private TransactionType transactionType;

    private BigDecimal amount;
}



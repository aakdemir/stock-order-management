package com.brokage.stock_order.model;

import com.brokage.stock_order.constant.Side;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;

@ToString
@AllArgsConstructor
@Data
public class OrderRequest {
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "assetName cannot be null")
    private String assetName;

    @NotNull(message = "orderSide cannot be null")
    private String orderSide;

    @NotNull(message = "size cannot be null")
    private Integer size;

    @NotNull(message = "price cannot be null")
    private BigDecimal price;
}



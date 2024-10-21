package com.brokage.stock_order.entity;

import com.brokage.stock_order.constant.Side;
import com.brokage.stock_order.constant.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "order_side")
    private Side orderSide;

    @Column(name = "size")
    private Integer size;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "status")
    private Status status;

    @Column(name = "createDate")
    private Date createDate;

    public Order(Long customerId, String assetName, Side orderSide,
                 Integer size, BigDecimal price, Status status, Date createDate) {
        this.customerId = customerId;
        this.assetName = assetName;
        this.orderSide = orderSide;
        this.size = size;
        this.price = price;
        this.status = status;
        this.createDate = createDate;
    }
}
